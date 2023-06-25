package com.mendonca.menssagerchat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mendonca.menssagerchat.model.UserMessenger;


@Repository
public interface UserMessengerRepository extends JpaRepository<UserMessenger, String> {

	@Query("select um.userName from UserMessenger um ")
	List<String> findAllUsersById();
	
	
	
}
