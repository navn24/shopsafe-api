package com.naveen.businessreviews;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CompanyMappingRepository extends CrudRepository<CompanyMappingTable, Integer> {

	@Query(value = "select id from company_mapping_table where company_name =:company_name and company_address =:company_address", nativeQuery = true)
	Integer returnCompanyId(
			@Param("company_name") String company_name, @Param("company_address") String company_address);

}
