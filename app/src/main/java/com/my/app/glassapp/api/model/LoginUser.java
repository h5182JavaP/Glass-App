package com.my.app.glassapp.api.model;

import com.google.gson.annotations.SerializedName;

public class LoginUser{

	@SerializedName("data")
	private Data data;

	@SerializedName("error")
	private boolean error;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private int status;

	public Data getData(){
		return data;
	}

	public boolean isError(){
		return error;
	}

	public String getMessage(){
		return message;
	}

	public int getStatus(){
		return status;
	}
}