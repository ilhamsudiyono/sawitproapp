package com.miniproject.interviewcode.model.user;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


import com.miniproject.interviewcode.model.role.Role;




@Entity
@Table(name = "m_user", uniqueConstraints = { @UniqueConstraint(columnNames = { "no_telp" }),
		@UniqueConstraint(columnNames = { "nama" }) })
public class User extends DateAudit {
	    private static final long serialVersionUID = 1L;

		@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @NotBlank
	    @Size(max = 13, min = 10)
	    @Column(name="no_telp")
	    private String noTelp;

	    
	    @NotBlank
	    @Size(max = 60)
	    private String nama;

	    @NotBlank
	    @Size(max = 16, min = 6)
	    private String password;
	 
		
		@Column(name="is_login")
		private Boolean isLogin;
	
		
		@Column(name="created_at")
		private Instant createdAt;

	    @ManyToMany(fetch = FetchType.LAZY)
	    @JoinTable(name = "r_user_role",
	            joinColumns = @JoinColumn(name = "user_id"),
	            inverseJoinColumns = @JoinColumn(name = "role_id"))
	    private Set<Role> roles = new HashSet<>();

	    public User() {
	    	
	    }

	    public User(String noTelp,String password, Boolean isLogin,  Instant createdAt) {
	        this.noTelp = noTelp;
	        this.password = password;
	        this.isLogin = isLogin;
	        this.createdAt = createdAt;
	        
	    }
	    
	    public User(String noTelp, String nama, String password, Boolean isLogin,  Instant createdAt) {
	        this.noTelp = noTelp;
	        this.nama = nama;
	        this.password = password;
	        this.isLogin = isLogin;
	        this.createdAt = createdAt;
	        
	    }

	    public Long getId() {
	        return id;
	    }

	    public void setId(Long id) {
	        this.id = id;
	    }

		public String getNama() {
			return nama;
		}

		public void setNama(String nama) {
			this.nama = nama;
		}

		public String getNoTelp() {
			return noTelp;
		}

		public void setNoTelp(String noTelp) {
			this.noTelp = noTelp;
		}
		
		
		public Boolean getIsLogin() {
			return isLogin;
		}

		public void setIsLogin(Boolean isLogin) {
			this.isLogin = isLogin;
		}

		public String getPassword() {
	        return password;
	    }

	    public void setPassword(String password) {
	        this.password = password;
	    }

	    public Set<Role> getRoles() {
	        return roles;
	    }

	    public void setRoles(Set<Role> roles) {
	        this.roles = roles;
	    }
	    
	    
	
	
}