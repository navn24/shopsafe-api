package com.naveen.businessreviews;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CommentsRepository extends CrudRepository<CommentsTable, Integer> {

	@Query(value = "select new com.naveen.businessreviews.GetUserReviewsObject(review_table.user_average_rating, comments_info.user_name, comments_info.comment_text, comments_info.comment_date) \r\n"
			+
			"from CommentsTable comments_info join ReviewTable review_table on comments_info.company_id=review_table.company_id \r\n"
			+
			"and comments_info.user_id=review_table.user_id\r\n" +
			"and comments_info.company_id =:company_id")
	List<GetUserReviewsObject> getComments(@Param("company_id") Integer company_id);

	@Query(value = "select * from comments_info where user_id =:user_id and company_id =:company_id", nativeQuery = true)
	Optional<CommentsTable> getCommentsForUser(@Param("user_id") Integer user_id,
			@Param("company_id") Integer company_id);

	@Transactional
	@Modifying
	@Query(value = "update comments_info set comment_text =:comment_text, user_name =:user_name, comment_date=:comment_date where company_id =:company_id and user_id =:user_id", nativeQuery = true)
	void updateComments(@Param("company_id") Integer company_id, @Param("user_id") Integer user_id,
			@Param("comment_text") String comment_text, @Param("user_name") String user_name,
			@Param("comment_date") Date comment_date);

	@Transactional
	@Modifying
	@Query(value = "update comments_info set user_name =:user_name, comment_date=:comment_date where company_id =:company_id and user_id =:user_id", nativeQuery = true)
	void updateNameAndDateInComments(@Param("company_id") Integer company_id, @Param("user_id") Integer user_id,
			@Param("user_name") String user_name, @Param("comment_date") Date comment_date);

}
