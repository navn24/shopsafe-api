package com.naveen.businessreviews;

import java.util.List;
import java.sql.Date;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "/review")
public class ReviewController {
	@Autowired
	private ReviewRepository reviewRepository;
	@Autowired
	private CommentsRepository commentsRepository;
	@Autowired
	private AverageReviewRepository averageReviewRepository;
	private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);

	@PostMapping(path = "/add") // Map ONLY POST Requests
	public @ResponseBody String addNewReview(@RequestParam Integer user_id, @RequestParam String user_name,
			@RequestParam Integer companyId, @RequestParam Date comment_date, @RequestParam float category1_rating,
			@RequestParam float category2_rating, @RequestParam float category3_rating) {
		logger.info("Adding review");
		// Check if any ratings are zero, and if so, don't add that rating to the
		// database
		if (category1_rating == 0 || category2_rating == 0 || category3_rating == 0) {
			logger.info("Must enter a value for all categories!");
			return "Must enter a value for all categories!";
		}
		// Check if the user id has already left a rating for that company, and if so,
		// don't add that rating to the database
		if (/* The getRatingForUser.isPresent will always be true */getRatingForUser(user_id, companyId).isPresent()
				&& getRatingForUser(user_id, companyId).get().getUser_id() != null) {
			logger.info("This user has already left a review for this company!");
			return "This user has already left a review for this company!";
		}
		ReviewTable n = new ReviewTable();
		float user_average_rating = (category1_rating + category2_rating + category3_rating) / 3;

		n.setName(user_name);
		n.setCompanyId(companyId);
		n.setUser_id(user_id);
		n.setCategory1_rating(category1_rating);
		n.setCategory2_rating(category2_rating);
		n.setCategory3_rating(category3_rating);
		n.setUser_average_rating(user_average_rating);
		reviewRepository.save(n);
		// Make name change show in the main company screen - this is because I select
		// user name from the comments table using inner join!
		commentsRepository.updateNameAndDateInComments(companyId, user_id, user_name, comment_date);
		UpdateAverageReviewForACompany(companyId);
		logger.info("Successfully added review");
		return "Saved Successfully";
	}

	@PostMapping(path = "/update")
	public @ResponseBody String updateReview(@RequestParam Integer company_id, @RequestParam Integer user_id,
			@RequestParam String user_name, @RequestParam Date comment_date, @RequestParam float category1_rating,
			@RequestParam float category2_rating, @RequestParam float category3_rating) {
		logger.info("Updating Review for user_id: " + String.valueOf(user_id) + " company_id: "
				+ String.valueOf(company_id));
		float user_average_rating = (category1_rating + category2_rating + category3_rating) / 3;

		reviewRepository.updateReview(company_id, user_id, user_name, category1_rating, category2_rating,
				category3_rating, user_average_rating);
		// Make name change show in the main company screen - this is because I select
		// user name from the comments table using inner join!
		commentsRepository.updateNameAndDateInComments(company_id, user_id, user_name, comment_date);

		UpdateAverageReviewForACompany(company_id);
		logger.info("Successfully updated Review for user_id: " + String.valueOf(user_id) + " company_id: "
				+ String.valueOf(company_id));
		return "Saved Successfully";
	}

	@GetMapping(path = "/getForUser")
	public @ResponseBody Optional<ReviewTable> getRatingForUser(@RequestParam Integer user_id,
			@RequestParam Integer company_id) {
		logger.info("Getting Review for user_id: " + String.valueOf(user_id) + " company_id: "
				+ String.valueOf(company_id));
		if (reviewRepository.getRatingForUser(user_id, company_id).isPresent()) {
			logger.info("Found and successfully returned reviews");
			return reviewRepository.getRatingForUser(user_id, company_id);
		} else {
			logger.info("Returning zero values for reviews");
			return Optional.of(new ReviewTable(0, 0, 0));
		}
	}

	@GetMapping(path = "/all")
	public @ResponseBody Iterable<ReviewTable> getAllUsers() {
		logger.info("Getting All Users");
		// This returns a JSON with the users
		return reviewRepository.findAll();
	}

	@GetMapping(path = "/getaverage")
	public @ResponseBody Optional<AverageReviewTable> getAverageReviews(@RequestParam Integer companyId) {
		logger.info("Getting average reviews");
		Optional<AverageReviewTable> avgReviewTable = averageReviewRepository.findById(companyId);
		if (!avgReviewTable.isPresent()) {
			AverageReviewTable n = new AverageReviewTable();
			n.setAverage_review_1(0);
			n.setAverage_review_2(0);
			n.setAverage_review_3(0);
			n.setCompany_average_review(0);
			averageReviewRepository.save(n);
			avgReviewTable = averageReviewRepository.findById(companyId);
		}
		logger.info("Successfully returned average reviews");
		return avgReviewTable;
	}

	@GetMapping(path = "/test")
	public @ResponseBody String updateAverages() {
		for (int i = 1; i <= averageReviewRepository.count(); i++) {
			AverageReviewTable avgTable = averageReviewRepository.findById(i).get();
			averageReviewRepository.updateStonksReview(i,
					(avgTable.getAverage_review_1() + avgTable.getAverage_review_2() + avgTable.getAverage_review_3())
							/ 3);
			i++;
		}
		return "YUHHH";
	}

	private void UpdateAverageReviewForACompany(int companyId) {

		List<ReviewTable> companyReviewResultSet = reviewRepository.returnObjectsWithCompanyId(companyId);
		float cat1 = 0;
		float cat2 = 0;
		float cat3 = 0;

		for (ReviewTable t : companyReviewResultSet) {
			cat1 += t.getCategory1_rating();
			cat2 += t.getCategory2_rating();
			cat3 += t.getCategory3_rating();
		}
		int rowCount = companyReviewResultSet.size();

		cat1 = cat1 / rowCount;
		cat2 = cat2 / rowCount;
		cat3 = cat3 / rowCount;
		float company_average_review = (cat1 + cat2 + cat3) / 3;
		logger.info("company_average_review: " + Float.toString(cat3) + " for company_id: " + companyId);
		averageReviewRepository.updateAverageReview(cat1, cat2, cat3, companyId, company_average_review);
	}

}