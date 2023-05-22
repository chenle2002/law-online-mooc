package com.chenle.commonutils;

import lombok.Data;

import java.util.List;

/**
 * com.example.shirojwtdemo.util
 *
 * @author chenle
 * 2022/11/28
 * 15:38
 */
@Data
public class JwtEntity {
	private String userId;
	private String username;
	private List<String> roles;

	private String nickname;


	public JwtEntity() {
	}
}
