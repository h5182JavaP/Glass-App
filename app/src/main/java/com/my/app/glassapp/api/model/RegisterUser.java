package com.my.app.glassapp.api.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class RegisterUser{

	@SerializedName("data")
	private List<Object> data;

	@SerializedName("error")
	private boolean error;

	@SerializedName("message")
	private Message message;

	@SerializedName("status")
	private int status;

	public void setData(List<Object> data){
		this.data = data;
	}

	public List<Object> getData(){
		return data;
	}

	public void setError(boolean error){
		this.error = error;
	}

	public boolean isError(){
		return error;
	}

	public void setMessage(Message message){
		this.message = message;
	}

	public Message getMessage(){
		return message;
	}

	public void setStatus(int status){
		this.status = status;
	}

	public int getStatus(){
		return status;
	}
}