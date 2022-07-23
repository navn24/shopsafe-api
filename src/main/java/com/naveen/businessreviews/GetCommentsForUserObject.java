package com.naveen.businessreviews;

import java.sql.Date;

public class GetCommentsForUserObject {

	private String user_name;
	private String comment_text;
	private float user_average_review;
	private Date comment_date;

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getComment_text() {
		return comment_text;
	}

	public void setComment_text(String comment_text) {
		this.comment_text = comment_text;
	}

	public float getUser_average_review() {
		return user_average_review;
	}

	public void setUser_average_review(float user_average_review) {
		this.user_average_review = user_average_review;
	}

	public Date getComment_date() {
		return comment_date;
	}

	public void setComment_date(Date comment_date) {
		this.comment_date = comment_date;
	}

}
