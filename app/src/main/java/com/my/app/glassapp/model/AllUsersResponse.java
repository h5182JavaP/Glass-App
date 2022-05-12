package com.my.app.glassapp.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class AllUsersResponse{

	@SerializedName("users")
	private List<UsersItem> users;

	public List<UsersItem> getUsers(){
		return users;
	}
}