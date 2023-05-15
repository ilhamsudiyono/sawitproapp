package com.miniproject.interviewcode.model.DTO;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class RequestMinisterDTO {


	@NotNull
	@NotEmpty
	private String dob;

	@NotNull
	@NotEmpty
	private String identityAddress;

	@NotNull
	@NotEmpty
	private String lastEducation;

	@NotNull
	@NotEmpty
	private String name;

	@NotNull
	@NotEmpty
	private Integer nik;

	@NotNull
	@NotEmpty
	private String occupation;
	
	public RequestMinisterDTO() {
		
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getIdentityAddress() {
		return identityAddress;
	}

	public void setIdentityAddress(String identityAddress) {
		this.identityAddress = identityAddress;
	}

	public String getLastEducation() {
		return lastEducation;
	}

	public void setLastEducation(String lastEducation) {
		this.lastEducation = lastEducation;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getNik() {
		return nik;
	}

	public void setNik(Integer nik) {
		this.nik = nik;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	
	
}
