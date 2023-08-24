package com.example.tafacerecognation.model;

import com.google.gson.annotations.SerializedName;

public class ImageResponse{

	@SerializedName("image")
	private String image;

	@SerializedName("sound_how_to_read")
	private String soundHowToRead;

	@SerializedName("word_after_noon_sakin")
	private String wordAfterNoonSakin;

	@SerializedName("arabic_text")
	private String arabicText;

	@SerializedName("tajweed_law")
	private String tajweedLaw;

	public String getImage(){
		return image;
	}

	public String getSoundHowToRead(){
		return soundHowToRead;
	}

	public String getWordAfterNoonSakin(){
		return wordAfterNoonSakin;
	}

	public String getArabicText(){
		return arabicText;
	}

	public String getTajweedLaw(){
		return tajweedLaw;
	}
}