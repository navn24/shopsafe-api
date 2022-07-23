package com.naveen.businessreviews;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface AverageReviewRepository extends CrudRepository<AverageReviewTable, Integer> {
	@Transactional
	@Modifying
	@Query(value = "UPDATE average_review_table set average_review_1 =:average_review1, average_review_2 =:average_review2, "
			+ "average_review_3 =:average_review3, company_average_review =:company_average_review "
			+ " where company_id = :company_id", nativeQuery = true)

	void updateAverageReview(@Param("average_review1") float average_review1,
			@Param("average_review2") float average_review2, @Param("average_review3") float average_review3,
			@Param("company_id") Integer company_id, @Param("company_average_review") float company_average_review);

	@Transactional
	@Modifying
	@Query(value = "UPDATE average_review_table set company_average_review =:company_average_review "
			+ " where company_id = :company_id", nativeQuery = true)

	void updateStonksReview(@Param("company_id") Integer company_id,
			@Param("company_average_review") float company_average_review);

}