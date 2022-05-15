package com.my.app.glassapp.api.model;

import com.google.gson.annotations.SerializedName;

public class Message{

	@SerializedName("mobile_no")
	private String mobileNo;

	@SerializedName("email")
	private String email;

	public void setMobileNo(String mobileNo){
		this.mobileNo = mobileNo;
	}

	public String getMobileNo(){
		return mobileNo;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}
}