package com.my.app.glassapp.api.model;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("access_token")
	private String accessToken;

	public String getAccessToken(){
		return accessToken;
	}
}