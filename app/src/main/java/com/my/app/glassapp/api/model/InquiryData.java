package com.my.app.glassapp.api.model;

import com.google.gson.annotations.SerializedName;

public class InquiryData{

	@SerializedName("msg")
	private String msg;

	@SerializedName("error")
	private boolean error;

	@SerializedName("status")
	private int status;

	public String getMsg(){
		return msg;
	}

	public boolean isError(){
		return error;
	}

	public int getStatus(){
		return status;
	}
}