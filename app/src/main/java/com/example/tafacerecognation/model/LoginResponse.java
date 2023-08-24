package com.example.tafacerecognation.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse{

	@SerializedName("id")
	private int id;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}
}