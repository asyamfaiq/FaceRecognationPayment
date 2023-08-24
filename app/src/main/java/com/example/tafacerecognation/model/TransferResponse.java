package com.example.tafacerecognation.model;

import com.google.gson.annotations.SerializedName;

public class TransferResponse{

	@SerializedName("account_number")
	private String accountNumber;

	@SerializedName("amount")
	private String amount;

	@SerializedName("nama")
	private String nama;

	@SerializedName("status")
	private String status;

	public void setAccountNumber(String accountNumber){
		this.accountNumber = accountNumber;
	}

	public String getAccountNumber(){
		return accountNumber;
	}

	public void setAmount(String amount){
		this.amount = amount;
	}

	public String getAmount(){
		return amount;
	}

	public void setNama(String nama){
		this.nama = nama;
	}

	public String getNama(){
		return nama;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}
}