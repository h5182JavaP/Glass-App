package com.my.app.glassapp.model;

import com.google.gson.annotations.SerializedName;

public class UsersItem{

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private String id;

	public String getName(){
		return name;
	}

	public String getId(){
		return id;
	}
}