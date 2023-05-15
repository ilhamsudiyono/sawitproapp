package com.miniproject.interviewcode.model.user;

public class UserSummary {
    
	
	private Long id;
    private String noTelp;
    private String nama;
    
    
	public UserSummary(Long id, String noTelp, String nama) {
		super();
		this.id = id;
		this.noTelp = noTelp;
		this.nama = nama;
	}
	
    
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNoTelp() {
		return noTelp;
	}
	public void setNoTelp(String noTelp) {
		this.noTelp = noTelp;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}
	


}
