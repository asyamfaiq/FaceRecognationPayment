package com.example.tafacerecognation.model;

import com.google.gson.annotations.SerializedName;

public class UserResponse{

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	@SerializedName("saldo")
	private Double saldo;

	@SerializedName("email")
	private String email;

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setSaldo(double saldo){
		this.saldo = saldo;
	}

	public Double getSaldo(){
		return saldo;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}
}