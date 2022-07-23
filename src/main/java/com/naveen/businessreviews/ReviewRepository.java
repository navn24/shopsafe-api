package com.naveen.businessreviews;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends CrudRepository<ReviewTable, Integer> {
	@Query(value = "select * from review_table where company_id =:company_id", nativeQuery = true)
	List<ReviewTable> returnObjectsWithCompanyId(@Param("company_id") int company_id);

	@Query(value = "select * from review_table where user_id =:user_id and company_id =:company_id", nativeQuery = true)
	Optional<ReviewTable> getRatingForUser(@Param("user_id") Integer user_id, @Param("company_id") Integer company_id);

	@Transactional
	@Modifying
	@Query(value = "update review_table set user_name =:user_name, category1_rating =:category1_rating, category2_rating =:category2_rating, category3_rating =:category3_rating, user_average_rating =:user_average_rating where company_id =:company_id and user_id =:user_id", nativeQuery = true)
	void updateReview(@Param("company_id") Integer company_id, @Param("user_id") Integer user_id,
			@Param("user_name") String user_name, @Param("category1_rating") float category1_rating,
			@Param("category2_rating") float category2_rating, @Param("category3_rating") float category3_rating,
			@Param("user_average_rating") float user_average_review);

}
