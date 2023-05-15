package com.miniproject.interviewcode.controller;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.miniproject.interviewcode.auth.sercurity.CurrentUser;
import com.miniproject.interviewcode.auth.sercurity.UserPrincipal;
import com.miniproject.interviewcode.model.auth.LoginRequest;
import com.miniproject.interviewcode.model.auth.SignUpRequest;
import com.miniproject.interviewcode.model.role.Role;
import com.miniproject.interviewcode.model.role.RoleName;
import com.miniproject.interviewcode.model.user.User;
import com.miniproject.interviewcode.model.user.UserSummary;
import com.miniproject.interviewcode.repository.IRoleRepository;
import com.miniproject.interviewcode.repository.IUserRepository;
import com.miniproject.interviewcode.service.user.IUserService;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api")
public class UserController {

	@Autowired
	private IUserRepository userRepository;
	
    @Autowired
    private IRoleRepository roleRepository;
    
	@Autowired
	private IUserService iUserService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	PasswordEncoder passwordEncoder = null;


	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	public HttpHeaders SetCspHeader() {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Content-Security-Policy", "base-uri 'self'; object-src 'none'; script-src 'self'");
		return responseHeaders;
	}

	
	@GetMapping("/users/me")
	@PreAuthorize("hasRole('USER')")
	@ApiOperation(value = "Get User by CurrentUser", notes = "Get User By Current User")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header") })
	public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
		UserSummary userSummary = new UserSummary(currentUser.getId(), currentUser.getUsername(),
				currentUser.getNama());
		return userSummary;
	}
	
	private static String decode(String encodedString) {
	    return new String(Base64.getUrlDecoder().decode(encodedString));
	}
	
	
	
	@PreAuthorize("hasRole('USER')")
	@PostMapping("/data-user-by-number")
	@ApiOperation(value = "Get User by CurrentUser", notes = "Get User By Current User")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header") })
	public ResponseEntity<?> getUserManagement(@RequestBody  LoginRequest loginRequest,
			HttpServletRequest request) throws Exception {
			
		String noPhone = loginRequest.getNoTelp();
		String token = request.getHeader("authorization").substring(7);
		String[] parts = token.split("\\.");
		JSONObject payload = new JSONObject(decode(parts[1]));
		boolean exp = payload.getLong("exp") > (System.currentTimeMillis() / 1000);
		
	
		if (payload.getString("sub").equals(loginRequest.getNoTelp())) {
			if (exp) {
			   return iUserService.getAllByNoTelp(noPhone, request.getHeader("authorization").substring(7));
			} else {
				return new ResponseEntity<>("Token Expired...!", SetCspHeader(), HttpStatus.BAD_REQUEST);
			}	
		} else {
			return new ResponseEntity<>("UNAUTHORIZED...!", SetCspHeader(), HttpStatus.UNAUTHORIZED); 
		}
	}
	
	
	@PreAuthorize("hasRole('USER')")
	@PostMapping("/update/dataUser")
	@ApiOperation(value = "Get User by CurrentUser", notes = "Get User By Current User")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header") })
	public ResponseEntity<?> updateUserManagement(@RequestBody SignUpRequest signUpRequest,
			HttpServletRequest request) throws Exception {
		
	    
	    
	    String token = request.getHeader("authorization").substring(7);
		String[] parts = token.split("\\.");
		JSONObject payload = new JSONObject(decode(parts[1]));
		boolean exp = payload.getLong("exp") > (System.currentTimeMillis() / 1000);
		
	
		if (payload.getString("sub").equals(signUpRequest.getNoTelp())) {
			if (exp) {
				
				 Instant instant = Instant.now();
				 

			    List<User> usr = iUserService.getNoTelpOrNama(signUpRequest.getNoTelp(), signUpRequest.getNama()) ;

			    System.out.println("=============");
				if (!userRepository.existsByNoTelp(signUpRequest.getNoTelp())) {
					User usr1 = new User();
					
					usr1.setNoTelp(signUpRequest.getNoTelp());
					usr1.setNama(signUpRequest.getNama());
					usr1.setPassword(signUpRequest.getPassword());
					usr1.setIsLogin(false);
					usr1.setRoles(signUpRequest.getRoleName());
					usr1.setCreatedAt(Instant.now());
					usr1.setUpdatedAt(null);
					userRepository.save(usr1);
				} else {
					userRepository.deleteByRuser(usr.get(0).getId());
					userRepository.deleteByNoTlp(signUpRequest.getNoTelp());
					 User usr1 = new User(signUpRequest.getNoTelp(),
				                signUpRequest.getNama(), signUpRequest.getPassword(), true,  instant);
					
					 usr1.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
				        
			        Role userRole = roleRepository.findByName(RoleName.ROLE_USER);
			        System.out.println("ROLES = " + userRole);
			        usr1.setRoles(Collections.singleton(userRole));

			        User result = userRepository.save(usr1);
			 
			        
			        URI location = ServletUriComponentsBuilder
			                .fromCurrentContextPath().path("/users/{nama}")
			                .buildAndExpand(result.getNama()).toUri();
				}
				return new ResponseEntity<>("Success", SetCspHeader(), HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Token Expired...!", SetCspHeader(), HttpStatus.BAD_REQUEST);
			}	
		} else {
			return new ResponseEntity<>("UNAUTHORIZED...!", SetCspHeader(), HttpStatus.UNAUTHORIZED); 
		}
	}
	
	
	

}
