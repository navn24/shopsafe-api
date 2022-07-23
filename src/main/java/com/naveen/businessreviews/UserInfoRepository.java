package com.naveen.businessreviews;

import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserInfoRepository extends CrudRepository<UserInfoTable, Integer> {

	@Query(value = "select user_id, email, google_login, cast(aes_decrypt(password, UNHEX(SHA2('Lago Lago Lago',512))) as char) as password, user_name from user_info  where user_name =:name", nativeQuery = true)

	Optional<UserInfoTable> returnObjectsWithName(@Param("name") String name);

	@Query(value = "select user_id, email, google_login, cast(aes_decrypt(password, UNHEX(SHA2('Lago Lago Lago',512))) as char) as password, user_name from user_info  where email =:email", nativeQuery = true)
	Optional<UserInfoTable> returnObjectsWithEmail(@Param("email") String email);

	@Transactional
	@Modifying
	@Query(value = "update user_info set user_name =:user_name where user_id =:user_id", nativeQuery = true)
	void updateUserName(@Param("user_id") Integer user_id, @Param("user_name") String user_name);

	@Transactional
	@Modifying
	@Query(value = "update user_info set password =AES_ENCRYPT(:password, UNHEX(SHA2('Lago Lago Lago',512))) where user_id =:user_id", nativeQuery = true)
	void updatePassword(@Param("user_id") Integer user_id, @Param("password") String password);

	@Transactional
	@Modifying
	@Query(value = "update user_info set google_login =:google_login where user_id =:user_id", nativeQuery = true)
	void updateGoogleLogin(@Param("user_id") Integer user_id, @Param("google_login") Integer google_login);

}
