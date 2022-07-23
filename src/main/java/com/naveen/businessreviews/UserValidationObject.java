package com.naveen.businessreviews;

public class UserValidationObject {
	private boolean user_found;
	private boolean pass_matches;
	private String error = "No Errors";
	private Integer user_id;
	private String user_name;
	private String email;
	private Integer google_login;

	public boolean isUser_found() {
		return user_found;
	}

	public void setUser_found(boolean user_found) {
		this.user_found = user_found;
	}

	public boolean isPass_matches() {
		return pass_matches;
	}

	public void setPass_matches(boolean pass_matches) {
		this.pass_matches = pass_matches;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getGoogle_login() {
		return google_login;
	}

	public void setGoogle_login(Integer google_login) {
		this.google_login = google_login;
	}
}
