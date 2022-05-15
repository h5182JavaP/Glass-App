package com.my.app.glassapp.model;

import com.google.gson.annotations.SerializedName;

public class AllSalesPersonItem{

	@SerializedName("name")
	private String name;

	@SerializedName("mobile_no")
	private String mobileNo;

	@SerializedName("id")
	private String id;

	@SerializedName("email")
	private String email;

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setMobileNo(String mobileNo){
		this.mobileNo = mobileNo;
	}

	public String getMobileNo(){
		return mobileNo;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}
}