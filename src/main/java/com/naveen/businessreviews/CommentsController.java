package com.naveen.businessreviews;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import javax.persistence.NonUniqueResultException;
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
@RequestMapping(path = "/comments")
public class CommentsController {
	final private String comment_default_value = "empty";
	@Autowired
	private CommentsRepository commentsRepository;
	@Autowired
	private ReviewRepository reviewRepository;

	private static final Logger logger = LoggerFactory.getLogger(CommentsController.class);

	@GetMapping(path = "/get")
	public @ResponseBody List<GetUserReviewsObject> getComments(@RequestParam Integer company_id) {
		List<GetUserReviewsObject> l = commentsRepository.getComments(company_id);
		return l;
	}

	@GetMapping(path = "/getForUser")
	public @ResponseBody GetCommentsForUserObject getCommentsForUser(@RequestParam Integer user_id,
			@RequestParam Integer company_id) {
		logger.info("Getting comments");

		GetCommentsForUserObject getCommentsForUserObject = new GetCommentsForUserObject();
		Optional<ReviewTable> userReviewTable = reviewRepository.getRatingForUser(user_id, company_id);

		Optional<CommentsTable> commentsTable = commentsRepository.getCommentsForUser(user_id, company_id);
		// If user has previously left a rating, return the average returned review
		if (userReviewTable.isPresent()) {
			float average_review = (userReviewTable.get().getCategory1_rating()
					+ userReviewTable.get().getCategory2_rating() + userReviewTable.get().getCategory3_rating()) / 3;
			getCommentsForUserObject.setUser_average_review(average_review);
		} else {
			getCommentsForUserObject.setUser_average_review(0);
		}
		// If user has previously left a comment, return the user name saved from that
		// comment and the text of that comment
		if (commentsTable.isPresent()) {
			getCommentsForUserObject.setUser_name(commentsTable.get().getUser_name());
			getCommentsForUserObject.setComment_text(commentsTable.get().getComment_text());
			getCommentsForUserObject.setComment_date(commentsTable.get().getComment_date());
		} else {
			// If user has never left a comment before, then return "" as user name and
			// comment_text
			getCommentsForUserObject.setUser_name("");
			getCommentsForUserObject.setComment_text("");
		}

		logger.info("Successfully got comments");

		return getCommentsForUserObject;
	}

	@PostMapping(path = "/update")
	public @ResponseBody String updateComments(@RequestParam Integer company_id, @RequestParam Integer user_id,
			@RequestParam String comment_text, @RequestParam String user_name, @RequestParam Date comment_date) {
		// If trying to update comment_text to value "", set it to comment_default_value
		// instead, to differentiate
		// between people who have left empty reviews and people who haven't left
		// reviews at all
		logger.info("Updating comment");

		if (comment_text.equals("")) {
			commentsRepository.updateComments(company_id, user_id, comment_default_value, user_name, comment_date);
		} else {
			commentsRepository.updateComments(company_id, user_id, comment_text, user_name, comment_date);
		}
		logger.info("Successfully updated comment");

		return "Saved Successfully";
	}

	@PostMapping(path = "/add")
	public @ResponseBody String addComments(@RequestParam Integer company_id, @RequestParam Integer user_id,
			@RequestParam String comment_text, @RequestParam String user_name, @RequestParam Date comment_date) {
		logger.info("Adding comment");
		try {
			Optional<CommentsTable> userReviewTable = commentsRepository.getCommentsForUser(user_id, company_id);
			if (userReviewTable.isPresent()) {
				logger.warn(
						"FOUND A COMMENT FOR THIS USER IN THE DATABASE ALREADY! - why is client trying to add again?");
				return "FOUND A COMMENT FOR THIS USER IN THE DATABASE ALREADY! - why is client trying to add again?";
			}
		} catch (NonUniqueResultException e) {
			e.printStackTrace();
			return e.getStackTrace().toString();
		}

		CommentsTable comments_table = new CommentsTable();
		comments_table.setCompany_id(company_id);
		comments_table.setUser_id(user_id);
		comments_table.setUser_name(user_name);
		comments_table.setComment_date(comment_date);
		// If trying to add comment_text with value "", set it to comment_default_value
		// instead, to differentiate
		// between people who have left empty reviews and people who haven't left
		// reviews at all
		if (comment_text.equals("")) {
			comments_table.setComment_text(comment_default_value);
		} else {
			comments_table.setComment_text(comment_text);
		}
		commentsRepository.save(comments_table);
		logger.info("Successfully added comment");
		return "Saved Successfully";
	}
}
