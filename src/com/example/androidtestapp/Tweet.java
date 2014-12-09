package com.example.androidtestapp;

import java.util.Date;

public class Tweet {
	private String tweet;
	private Date data;
	
	public String getTweet() {
		return tweet;
	}
	
	public void setTweet(String tweet) {
		this.tweet = tweet;
	}
	
	public Date getData() {
		return data;
	}
	
	public void setData(Date data) {
		this.data = data;
	}
	
	public void setData(Long data){
		this.data = new Date(data);
	}
	
	public void setData(String data){
		
	}
	
	public String getDataString(){
		return null;
	}
	
	public Long getDataLong(){
		return this.data.getTime();
	}

	
	
}
