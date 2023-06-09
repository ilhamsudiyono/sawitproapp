package com.miniproject.interviewcode.service.user.impl;



import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.miniproject.interviewcode.model.auth.LoginRequest;
import com.miniproject.interviewcode.model.user.User;
import com.miniproject.interviewcode.repository.IRoleRepository;
import com.miniproject.interviewcode.repository.IUserRepository;
import com.miniproject.interviewcode.service.user.IUserService;


@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	private IUserRepository userRepository;
	
	@Autowired
	private IRoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public List<User> getListByIdUser(Long idUser) {
		// TODO Auto-generated method stub
		return userRepository.getListByIdUser(idUser);
	}

	@Override
	public User findByNama(String email) {
		// TODO Auto-generated method stub
		return userRepository.findByNama(email).orElse(null);
	}

	@Override
	public User findByNoTelp(String noTlp) {
		// TODO Auto-generated method stub
		return userRepository.findByNoTelp(noTlp).orElse(null);
	}

	@Override
	public List<User> findAllUsers() {
		// TODO Auto-generated method stub
		return userRepository.findAll();
	}

	@Override
	public User saveUser(User user) {
		// TODO Auto-generated method stub
		user.setNama(user.getNama());;
		user.setNoTelp(user.getNoTelp());
		user.setPassword(user.getPassword());
		user.setIsLogin(user.getIsLogin());
		return userRepository.save(user);
	}

	@Override
	public List<String> findUsers(List<Long> idUser) {
		// TODO Auto-generated method stub
		return userRepository.findNamaByIdList(idUser);
	}

	private boolean emailExists(final String email) {
		return userRepository.findByNama(email) != null;
	}

	@Override
	public int updateUserLoginByNama(String nama) {
		// TODO Auto-generated method stub
		return userRepository.updateUserLoginByNama(nama);
	}

	@Override
	public int updateUserLoginByNoTelp(String notelp) {
		// TODO Auto-generated method stub
		return userRepository.updateUserLoginByNoTelp(notelp);
	}

	@Override
	public List<User> getNoTelpOrNama(String notelp, String nama) {
		// TODO Auto-generated method stub
		return userRepository.getNoTelpOrNama(notelp, nama);
	}

	@Override
	public User getListByNama(String nama) {
		// TODO Auto-generated method stub
		return userRepository.getListByNama(nama);
	}

	
	

	@Override
	public ResponseEntity<?> getAllByNoTelp(String noTelp, String token) {
		// TODO Auto-generated method stub
		
		System.out.println(token);
		
		if (token != null) {
			List<User> response = userRepository.getAllByNoTelp(noTelp);

			return new ResponseEntity<List<User>>(response, SetCspHeader(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Token expired!", SetCspHeader(), HttpStatus.BAD_REQUEST);
		}
		
		
		
	}


	
	public HttpHeaders SetCspHeader() {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Content-Security-Policy", "base-uri 'self'; object-src 'none'; script-src 'self'");
		return responseHeaders;
	}

	@Override
	public List<User> getListByNoTelp(String nama) {
		return userRepository.getListByNoHp(nama);
	}
	
	
	 public List<User> getAllUser()
	    {
	        return this.userRepository.findAll();
	    }
	 
	 public UserServiceImpl(IUserRepository repo)
	    {
	        this.userRepository = repo;
	    }

}
