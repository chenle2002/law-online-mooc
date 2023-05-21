package com.chenle.educenter.controller;


import com.chenle.commonutils.JwtUtils;
import com.chenle.educenter.entity.UcenterMember;
import com.chenle.educenter.service.UcenterMemberService;
import com.chenle.educenter.utils.ConstantWxUtil;
import com.chenle.educenter.utils.HttpClientUtils;
import com.chenle.servicebase.exceptionhandler.GuliException;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

//@CrossOrigin
@Controller  //只是请求地址，不需要返回数据
@RequestMapping("/api/ucenter/wx")
public class WxApiController {

    @Autowired
    private UcenterMemberService memberService;

    @GetMapping("callback")
    public String callback(String code, String state, HttpSession session) {
        //获取到code
        //拿着code请求微信固定地址，得到access_token和openid
        //得到授权临时票据code
        System.out.println("code = " + code);
        System.out.println("state = " + state);

        try{
            String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                    "?appid=%s" +
                    "&secret=%s" +
                    "&code=%s" +
                    "&grant_type=authorization_code";

            String accessTokenUrl = String.format(baseAccessTokenUrl,
                    ConstantWxUtil.WX_OPEN_APP_ID,
                    ConstantWxUtil.WX_OPEN_APP_SECRET,
                    code);

            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);
            //获取accessToken和openid

            String result = null;

            Gson gson=new Gson();
            HashMap mapAccessToken = gson.fromJson(accessTokenInfo, HashMap.class);
            String access_token = (String) mapAccessToken.get("access_token");
            String openid = (String) mapAccessToken.get("openid");



            //将扫码人信息添加到数据库
            //判断数据库中是否有重复微信信息
            UcenterMember member=memberService.getOpenIdMember(openid);
            if (member==null){

                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";

                String userInfoUrl = String.format(
                        baseUserInfoUrl,
                        access_token,
                        openid
                );
                String userInfo = HttpClientUtils.get(userInfoUrl);
                HashMap userInfoMap = gson.fromJson(userInfo, HashMap.class);
                String nickname = (String) userInfoMap.get("nickname");
                String headimgurl = (String) userInfoMap.get("headimgurl");


                //添加
                member=new UcenterMember();
                member.setNickname(nickname);
                member.setAvatar(headimgurl);
                member.setOpenid(openid);
                memberService.save(member);

            }
            //使用jwt根据member对象生成token字符串
            String jwtToken = JwtUtils.getJwtToken(member.getId(), member.getNickname());
            return "redirect:http://localhost:3000?token="+jwtToken;

        }catch (Exception e){
            throw new GuliException(20001,"登录失败");
        }
    }



    @GetMapping("login")
    public String genQrConnect(HttpSession session) {

        // 微信开放平台授权baseUrl
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

        // 回调地址
        String redirectUrl = ConstantWxUtil.WX_OPEN_REDIRECT_URL;
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "UTF-8"); //url编码
        } catch (UnsupportedEncodingException e) {
            throw new GuliException(20001, e.getMessage());
        }
        String state = "chenle";
        System.out.println("state = " + state);


        //生成qrcodeUrl
        String qrcodeUrl = String.format(
                baseUrl,
                ConstantWxUtil.WX_OPEN_APP_ID,
                redirectUrl,
                state);

        return "redirect:" + qrcodeUrl;
    }

}
