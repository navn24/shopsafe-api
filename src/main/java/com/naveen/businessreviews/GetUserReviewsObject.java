package com.naveen.businessreviews;

import java.util.Date;

public class GetUserReviewsObject {
	private float user_average_rating;
	private String user_name;
	private String comment_text;
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

	public float getUser_average_rating() {
		return user_average_rating;
	}

	public void setUser_average_review(float user_average_rating) {
		this.user_average_rating = user_average_rating;
	}

	public GetUserReviewsObject(float user_average_rating, String user_name, String comment_text, Date comment_date) {
		this.user_average_rating = user_average_rating;
		this.user_name = user_name;
		this.comment_text = comment_text;
		this.comment_date = comment_date;
	}

	public Date getComment_date() {
		return comment_date;
	}

	public void setComment_date(Date comment_date) {
		this.comment_date = comment_date;
	}

}
