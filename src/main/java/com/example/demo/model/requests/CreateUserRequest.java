package com.example.demo.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class CreateUserRequest {

	@JsonProperty
	@NotNull(message = "User name cannot be null")
	private String username;

	// EMG - To implement JWT, there are two things that need to be added, the password and the
	// confirmPassword fields, together with the corresponding getters and setters.
	@JsonProperty
	@NotNull(message = "Password cannot be null")
	private String password;

	@JsonProperty
	@NotNull(message = "Password confirmation cannot be null")
	private String confirmPassword;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
}
