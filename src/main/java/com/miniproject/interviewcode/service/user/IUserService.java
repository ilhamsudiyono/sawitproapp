package com.miniproject.interviewcode.service.user;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.miniproject.interviewcode.model.user.User;


public interface IUserService {
	
	

	List<User> getListByIdUser(Long idUser);
	
	List<User> getListByNoTelp(String nama);

	User findByNama(String nama);
	
	User findByNoTelp(String noTlp);

	List<User> findAllUsers();

	User saveUser(User user);

	List<String> findUsers(List<Long> idUser);
	
	int updateUserLoginByNama(String nama);
	
	int updateUserLoginByNoTelp(String notelp);
	
	List<User> getNoTelpOrNama(String notelp, String nama);
	
	User getListByNama(String nama);
	
	ResponseEntity<?> getAllByNoTelp(String uniqid, String token);
	
	

}
