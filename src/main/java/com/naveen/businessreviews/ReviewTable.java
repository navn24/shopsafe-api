package com.naveen.businessreviews;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity // This tells Hibernate to make a table out of this class
@Table(name = "review_table")
public class ReviewTable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id_review_table;

  private String user_name;

  private Integer company_id;

  private Integer user_id;

  private float category1_rating;

  private float category2_rating;

  private float category3_rating;

  private float user_average_rating;

  public ReviewTable(float category1_rating, float category2_rating, float category3_rating) {
    this.category1_rating = category1_rating;
    this.category2_rating = category2_rating;
    this.category3_rating = category3_rating;
  }

  public ReviewTable() {

  }

  public Integer getId() {
    return id_review_table;
  }

  public void setId(Integer id) {
    this.id_review_table = id;
  }

  public String getName() {
    return user_name;
  }

  public void setName(String name) {
    this.user_name = name;
  }

  public Integer getCompanyId() {
    return company_id;
  }

  public void setCompanyId(Integer companyId) {
    this.company_id = companyId;
  }

  public float getCategory1_rating() {
    return category1_rating;
  }

  public void setCategory1_rating(float category1_rating2) {
    this.category1_rating = category1_rating2;
  }

  public float getCategory2_rating() {
    return category2_rating;
  }

  public void setCategory2_rating(float category2_rating) {
    this.category2_rating = category2_rating;
  }

  public float getCategory3_rating() {
    return category3_rating;
  }

  public void setCategory3_rating(float category3_rating) {
    this.category3_rating = category3_rating;
  }

  public Integer getUser_id() {
    return user_id;
  }

  public void setUser_id(Integer user_id) {
    this.user_id = user_id;
  }

  public float getUser_average_rating() {
    return user_average_rating;
  }

  public void setUser_average_rating(float user_average_rating) {
    this.user_average_rating = user_average_rating;
  }
}