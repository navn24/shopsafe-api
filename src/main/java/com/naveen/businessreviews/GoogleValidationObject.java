package com.naveen.businessreviews;

public class GoogleValidationObject {
	private Integer user_id;
	private String user_name;
	private String email;
	private String error_message;
	private Boolean new_account;

	public GoogleValidationObject(Integer user_id, String user_name, String email, String error_message,
			Boolean new_account) {
		this.user_id = user_id;
		this.user_name = user_name;
		this.email = email;
		this.error_message = error_message;
		this.new_account = new_account;
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

	public String getError_message() {
		return error_message;
	}

	public void setError_message(String error_message) {
		this.error_message = error_message;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getNew_account() {
		return new_account;
	}

	public void setNew_account(Boolean new_account) {
		this.new_account = new_account;
	}

}
