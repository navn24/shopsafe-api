package com.naveen.businessreviews;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import org.hibernate.annotations.ColumnTransformer;

@Entity
@Table(name = "user_info")
public class UserInfoTable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer user_id;
	private String user_name;
	private String email;
	@Lob
	@ColumnTransformer(forColumn = "password", write = "AES_ENCRYPT(?, UNHEX(SHA2('Lago Lago Lago',512)))")
	@Column(name = "password", columnDefinition = "BLOB")
	private String password;
	private int google_login;

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return user_name;
	}

	public void setName(String name) {
		this.user_name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getGoogle_login() {
		return google_login;
	}

	public void setGoogle_login(int google_login) {
		this.google_login = google_login;
	}
}