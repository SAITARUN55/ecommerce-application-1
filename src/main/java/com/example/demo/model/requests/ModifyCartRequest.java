package com.example.demo.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class ModifyCartRequest {
	
	@JsonProperty
	@NotNull(message = "User name cannot be null")
	private String username;
	
	@JsonProperty
	@NotNull(message = "Item Id cannot be null")
	private long itemId;
	
	@JsonProperty
	@NotNull(message = "Quantity cannot be null")
	private int quantity;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getItemId() {
		return itemId;
	}

	public void setItemId(long itemId) {
		this.itemId = itemId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	

}
