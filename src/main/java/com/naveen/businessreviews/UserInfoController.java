package com.naveen.businessreviews;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
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
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

@Controller
@RequestMapping(path = "/user")
public class UserInfoController {

	private static final Logger logger = LoggerFactory.getLogger(UserInfoController.class);

	@Autowired
	public UserInfoRepository userInfoRepository;

	@GetMapping(path = "/getInfoWithEmail")
	public @ResponseBody Optional<UserInfoTable> getInfoWithEmail(String email) {
		return userInfoRepository.returnObjectsWithEmail(email);
	}

	@GetMapping(path = "/getInfoWithName")
	public @ResponseBody Optional<UserInfoTable> getInfoWithName(String name) {
		logger.info("Vuaw So Cool Much Dank");
		return userInfoRepository.returnObjectsWithName(name);
	}

	@PostMapping(path = "/add")
	public @ResponseBody String AddUser(@RequestParam String user_name, @RequestParam String email,
			@RequestParam String password) {
		logger.info("Adding user");

		UserInfoTable userInfoTable = new UserInfoTable();
		userInfoTable.setName(user_name);
		userInfoTable.setEmail(email);
		userInfoTable.setPassword(password);
		userInfoRepository.save(userInfoTable);
		logger.info("Successfully added user: " + user_name);

		return "Saved Successfully";
	}

	@PostMapping(path = "/updateName")
	public @ResponseBody String UpdateName(@RequestParam Integer user_id, @RequestParam String user_name) {
		logger.info("Updating name with user_id:" + user_id + "to " + user_name);

		userInfoRepository.updateUserName(user_id, user_name);
		logger.info("Successfully updated name with user_id:" + user_id + "to " + user_name);

		return "Updated Successfully";
	}

	@PostMapping(path = "/updatePassword")
	public @ResponseBody String UpdatePassword(@RequestParam Integer user_id, @RequestParam String password) {
		logger.info("Updating password for user_id:" + user_id);

		userInfoRepository.updatePassword(user_id, password);

		logger.info("Successfully updated password");

		return "Updated Successfully";
	}

	@GetMapping(path = "/googleSignIn")
	public @ResponseBody GoogleValidationObject GoogleSignIn(@RequestParam String idTokenString) {

		logger.info("Signing user in with google");

		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(),
				JacksonFactory.getDefaultInstance())
				// Specify the CLIENT_ID of the app that accesses the backend:
				.setAudience(Collections
						.singletonList("***********"))

				.build();
		GoogleIdToken verifiedIdToken;
		boolean emailVerified = false;
		String email = "DEFAULT EMAIL";
		String name = "DEFAULT NAME";
		try {
			verifiedIdToken = verifier.verify(idTokenString);

			if (verifiedIdToken != null) {
				Payload payload = verifiedIdToken.getPayload();

				// Print user identifier
				String userId = payload.getSubject();
				logger.info("User ID: " + userId);

				// Get profile information from payload
				email = payload.getEmail();
				emailVerified = Boolean.valueOf(payload.getEmailVerified());
				name = (String) payload.get("given_name");
				// Get only first name, and if first name has spaces, replace the spaces with
				// dashes
				name = name.replaceAll(" ", "-");
				// name.split(" ")[0];
				String pictureUrl = (String) payload.get("picture");
				logger.info(pictureUrl);
				String locale = (String) payload.get("locale");
				String familyName = (String) payload.get("family_name");
				String givenName = (String) payload.get("given_name");
				logger.info("Name: " + name + "Email: " + email);
			} else {
				logger.info("Invalid ID token.");
				// Return error message that id token is invalid, and set all other values to
				// default - check in the client what the error message is before operations

				String error_message = "Invalid ID Token";
				return new GoogleValidationObject(0, "", "", error_message, null);
			}
		} catch (GeneralSecurityException | IOException e) {
			e.printStackTrace();
		}
		// Use or store profile information
		GoogleValidationObject googleValObject;
		Optional<UserInfoTable> userInfoTable;
		if (emailVerified == true) {

			// Set an Optional to the value returned by the "returnObjectsWithEmail
			// Function"
			userInfoTable = userInfoRepository.returnObjectsWithEmail(email);
			// Check if email is in database - and if not, add a new account
			if (!userInfoTable.isPresent()) {
				UserInfoTable n = new UserInfoTable();
				n.setName(name);
				n.setEmail(email);
				n.setGoogle_login(1);
				userInfoRepository.save(n);

				// Get the newly added account and set it to userInfoTable
				userInfoTable = userInfoRepository.returnObjectsWithEmail(email);

				String error_message = "";
				Boolean new_account = true;
				googleValObject = new GoogleValidationObject(userInfoTable.get().getUser_id(),
						userInfoTable.get().getName(), userInfoTable.get().getEmail(), error_message, new_account);
				// Set error message to none
				return googleValObject;
			} else {
				// If email was found with previous google login, that means user has previously
				// used google to login
				if (userInfoTable.get().getGoogle_login() == 1) {
					String error_message = "";
					Boolean new_account = false;
					googleValObject = new GoogleValidationObject(userInfoTable.get().getUser_id(),
							userInfoTable.get().getName(), userInfoTable.get().getEmail(), error_message, new_account);

					return googleValObject;
					// Set error message to none
				} else {
					// If email is in database, ask them if they would like to link accounts
					String error_message = "Would you like to link accounts";
					Boolean new_account = false;
					googleValObject = new GoogleValidationObject(userInfoTable.get().getUser_id(),
							userInfoTable.get().getName(), userInfoTable.get().getEmail(), error_message, new_account);
					return googleValObject;

					// Set error message to "would you like to link accounts?"
				}
			}
		} else {
			// Return error message that email couldn't be verified, and set all other
			// values to default - check in the client what the error message is before
			// operations
			String error_message = "Email couldn't be verified";
			googleValObject = new GoogleValidationObject(0, "", "", error_message, null);
			return googleValObject;
		}
	}

	@PostMapping(path = "/linkAccount")
	public @ResponseBody String LinkAccounts(@RequestParam String idTokenString) {
		/*
		 * Verify incoming id token, and if verified, check if there is an account with
		 * the email
		 * retrieved from the idToken, and if there is, add set google_login == true;
		 */
		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(),
				JacksonFactory.getDefaultInstance())
				// Specify the CLIENT_ID of the app that accesses the backend:
				.setAudience(Collections
						.singletonList("***********"))
				.build();
		GoogleIdToken verifiedIdToken;
		boolean emailVerified = false;
		String email = "DEFAULT EMAIL";
		String name = "DEFAULT NAME";
		try {
			verifiedIdToken = verifier.verify(idTokenString);

			if (verifiedIdToken != null) {
				Payload payload = verifiedIdToken.getPayload();

				// Print user identifier
				String userId = payload.getSubject();
				logger.info("User ID: " + userId);

				// Get profile information from payload
				email = payload.getEmail();
				emailVerified = Boolean.valueOf(payload.getEmailVerified());
				name = (String) payload.get("name");
				String pictureUrl = (String) payload.get("picture");
				String locale = (String) payload.get("locale");
				String familyName = (String) payload.get("family_name");
				String givenName = (String) payload.get("given_name");
				logger.info("Name: " + name + "Email: " + email);
				Optional<UserInfoTable> userInfoTable;

				if (emailVerified) {
					userInfoTable = userInfoRepository.returnObjectsWithEmail(email);
					if (userInfoTable.isPresent()) {
						// Link the accounts (find the account and set google_login=1) and return linked
						// successfully
						Integer googleLoginVal = 1;
						userInfoRepository.updateGoogleLogin(userInfoTable.get().getUser_id(), googleLoginVal);
						return "Accounts Linked Successfully";
					} else {
						return "There were no accounts found with the given email";
					}
				} else {
					return "Email couldn't be verified";
				}
			} else {
				logger.info("Invalid ID token.");
				return "Invalid ID Token";
			}
		} catch (GeneralSecurityException | IOException e) {
			e.printStackTrace();
		}
		logger.info("Successfully signed user in with google");
		return ""; // This line is here only to appease the editor to stop the red line from
					// showing - it should never get called
	}

	@GetMapping(path = "/emailFound")
	public @ResponseBody Boolean emailFound(@RequestParam String email) {
		if (userInfoRepository.returnObjectsWithEmail(email).isPresent()) {
			logger.info("Email Found");
			return true;
		} else {
			logger.info("Email Not Found");
			return false;
		}
	}

	@GetMapping(path = "/validateByName")
	public @ResponseBody UserValidationObject ValidateByName(String name, String password) {
		// Get Objects in user_info_table with name
		UserValidationObject userValidationObject = new UserValidationObject();
		Optional<UserInfoTable> userInfoTable = userInfoRepository.returnObjectsWithName(name);

		if (userInfoTable.isPresent()) {
			// Check if provided name and password match with database
			String database_name = userInfoTable.get().getName();
			logger.info(database_name);

			String database_password = userInfoTable.get().getPassword();
			logger.info(database_password);

			Integer user_id = userInfoTable.get().getUser_id();
			userValidationObject.setUser_id(user_id);

			String user_name = userInfoTable.get().getName();
			userValidationObject.setUser_name(user_name);

			String email = userInfoTable.get().getEmail();
			userValidationObject.setEmail(email);

			Integer google_login = userInfoTable.get().getGoogle_login();
			userValidationObject.setGoogle_login(google_login);

			if (name.equals(database_name)) {
				userValidationObject.setUser_found(true);
			} else {
				userValidationObject.setUser_found(false);
			}
			if (password.equals(database_password)) {
				userValidationObject.setPass_matches(true);
			} else {
				userValidationObject.setPass_matches(false);
			}
		} else {
			// Return Error
			userValidationObject.setError("No user was found with the given name");
		}

		return userValidationObject;
	}

	@GetMapping(path = "/validateByEmail")
	public @ResponseBody UserValidationObject ValidateByEmail(String email, String password) {
		logger.info("Validating by email");

		// Get Objects in user_info_table with name
		Optional<UserInfoTable> userInfoTable = userInfoRepository.returnObjectsWithEmail(email);
		UserValidationObject userValidationObject = new UserValidationObject();

		if (userInfoTable.isPresent()) {
			// Check if provided name and password match with database
			String database_email = userInfoTable.get().getEmail();
			logger.info(database_email);

			String database_password = userInfoTable.get().getPassword();
			logger.info(database_password);

			Integer user_id = userInfoTable.get().getUser_id();
			userValidationObject.setUser_id(user_id);

			String user_name = userInfoTable.get().getName();
			userValidationObject.setUser_name(user_name);

			Integer google_login = userInfoTable.get().getGoogle_login();
			userValidationObject.setGoogle_login(google_login);

			if (email.equals(database_email)) {
				userValidationObject.setUser_found(true);
			} else {
				userValidationObject.setUser_found(false);
			}
			if (password.equals(database_password)) {
				userValidationObject.setPass_matches(true);
			} else {
				userValidationObject.setPass_matches(false);
			}

		} else {
			userValidationObject.setError("No user was found with the given email");
		}
		logger.info("Successfully validated by email");

		return userValidationObject;
	}

}
