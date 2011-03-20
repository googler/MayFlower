package com.paladin.bean;

public class User {
	private int id;
	private String username;
	private String password;
	private String nickname;
	private String reg_date;
	private String lastlogin_date;
	private String email;
	private String first_ip;
	private String role;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getLastlogin_date() {
		return lastlogin_date;
	}

	public void setLastlogin_date(String lastloginDate) {
		lastlogin_date = lastloginDate;
	}

	public String getReg_date() {
		return reg_date;
	}

	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirst_ip() {
		return first_ip;
	}

	public void setFirst_ip(String first_ip) {
		this.first_ip = first_ip;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
