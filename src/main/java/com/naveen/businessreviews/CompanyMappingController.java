package com.naveen.businessreviews;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "/company")
public class CompanyMappingController {
	@Autowired
	private CompanyMappingRepository companyMappingRepository;
	@Autowired
	private AverageReviewRepository averageReviewRepository;
	private static final Logger logger = LoggerFactory.getLogger(CompanyMappingController.class);

	@GetMapping(path = "/getCompanyId")
	public @ResponseBody GetCompanyIdObject getCompanyId(@RequestParam String company_name,
			@RequestParam String company_address) {
		logger.info("Getting company ID");
		GetCompanyIdObject getCompanyIdObject = new GetCompanyIdObject();
		Integer companyId = companyMappingRepository.returnCompanyId(company_name, company_address);
		// This executes if there was no company found with the given name and address
		// in the database
		if (companyId == null) {
			CompanyMappingTable companyMappingTable = new CompanyMappingTable();
			companyMappingTable.setCompany_name(company_name);
			companyMappingTable.setCompany_address(company_address);
			companyMappingRepository.save(companyMappingTable);
			companyId = companyMappingRepository.returnCompanyId(company_name, company_address);
			getCompanyIdObject.setCompany_id(companyId);
			getCompanyIdObject.setNewCompany(true);
		} else {
			// If company was found in database, this code executes

			// Make sure to not set newCompany=true just because there is a companyId record
			// in the database.
			// If no reviews have been left for the company, treat the company as "new"
			AverageReviewTable optional = averageReviewRepository.findById(companyId).get();
			if (optional.getAverage_review_1() != 0 || optional.getAverage_review_2() != 0
					|| optional.getAverage_review_3() != 0) {
				// If at least one of the columns are not zero
				getCompanyIdObject.setNewCompany(false);
			} else {
				// If all of the columns are zero
				getCompanyIdObject.setNewCompany(true);
			}
			getCompanyIdObject.setCompany_id(companyId);
		}
		logger.info("Successfully got company ID");

		return getCompanyIdObject;
	}

}
