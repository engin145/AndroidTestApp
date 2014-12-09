package com.example.androidtestapp;

import java.util.List;

public interface IDatabaseHandler {
	public void addTweet(Tweet tweet);
	public void addTweets(List<Tweet> tweetList);
	public List<Tweet> getAllTweets();
	public Tweet getTweet(Integer id);
	public void deleteAllTweets();
	public void deleteTweetMinDate();
}
