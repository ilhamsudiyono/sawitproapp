package com.miniproject.interviewcode.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.miniproject.interviewcode.auth.sercurity.JwtTokenProvider;
import com.miniproject.interviewcode.auth.sercurity.UserPrincipal;
import com.miniproject.interviewcode.model.auth.LoginRequest;
import com.miniproject.interviewcode.model.auth.SignUpRequest;
import com.miniproject.interviewcode.model.payload.ApiResponse;
import com.miniproject.interviewcode.model.payload.JwtAuthenticationResponse;
import com.miniproject.interviewcode.model.role.Role;
import com.miniproject.interviewcode.model.role.RoleName;
import com.miniproject.interviewcode.model.user.User;
import com.miniproject.interviewcode.repository.IRoleRepository;
import com.miniproject.interviewcode.repository.IUserRepository;
import com.miniproject.interviewcode.service.user.impl.UserServiceImpl;

import io.swagger.annotations.ApiOperation;

import javax.validation.Valid;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    IUserRepository userRepository;
    
    @Autowired
    UserServiceImpl userServiceImpl;

    @Autowired
    IRoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;
    
    @Autowired
    private MessageSource messages;
   
    @Autowired
    ApplicationEventPublisher eventPublisher;
    
    @PostMapping("/signin")
    @ApiOperation(value = "Get generate token", notes = "${AuthController.authenticateUser.notes}")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getNoTelp(),
                        loginRequest.getPassword()
                )
        );
      System.out.println("authentication>>>>>>>>>>>>>>>>>>" + authentication.getCredentials());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String noTelp = loginRequest.getNoTelp();
        
        if (isValid(noTelp)) {
        	System.out.println("NO_TELP>>>>>>" + noTelp);
        	System.out.println("PASS>>>>>>" + loginRequest.getPassword());
        	userServiceImpl.updateUserLoginByNoTelp(noTelp);
		}
        String jwt = null;
		try {
			jwt = tokenProvider.generateToken(authentication);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());
		
        return ResponseEntity.ok(new JwtAuthenticationResponse(userDetails.getId(),userDetails.getUsername(), userDetails.getNama(),roles, jwt));
    }
    

    @PostMapping("/signup-phone-number")
    public ResponseEntity<?> registerUserEmail(@Valid @RequestBody SignUpRequest signUpRequest) {


        if(userRepository.existsByNoTelp(signUpRequest.getNoTelp())) {
            return new ResponseEntity(new ApiResponse(false, "Phone number already in use!"),
                    HttpStatus.BAD_REQUEST);
        }
        
        Instant instant = Instant.now();
       
        // Creating user's account
        User user = new User(signUpRequest.getNoTelp(),
                signUpRequest.getNama(), signUpRequest.getPassword(), true,  instant);
        
//        Role role = new Role(RoleName.ROLE_USER);
        
//        roleRepository.save(role);
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER);
        System.out.println("ROLES = " + userRole);
        user.setRoles(Collections.singleton(userRole));

        User result = userRepository.save(user);
 
        
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{nama}")
                .buildAndExpand(result.getNama()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }
    
   
    
    
    static boolean isValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
     }
    
    

    
}
