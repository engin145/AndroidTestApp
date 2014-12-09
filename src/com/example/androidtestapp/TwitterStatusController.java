package com.example.androidtestapp;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Paging;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class TwitterStatusController extends AsyncTask<Integer, Void, List<String>>{
	private Twitter twitter;
	private List<twitter4j.Status> statusList;
	private List<Tweet> tweetList;
	private List<String> generateOutStr;
	private Tweet tweet;
	private String user = "@ladygaga";
	private Context context;
	private DatabaseHandler db;
	private boolean err;
	
	TwitterStatusController(Context context){
		err=false;
		this.context=context;
		tweetList = new ArrayList<Tweet>();
	}
	
	private void createConnectTwitter(){
		ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
	        .setOAuthConsumerKey(TwitterConstants.CONSUMER_KEY)
	        .setOAuthConsumerSecret(TwitterConstants.CONSUMER_SECRET)
	        .setOAuthAccessToken(TwitterConstants.ACCESS_TOKEN)
	        .setOAuthAccessTokenSecret(TwitterConstants.ACCESS_TOKEN_SECRET);
        TwitterFactory tw = new TwitterFactory(cb.build());
        twitter = tw.getInstance();
	}
	
	private void getTwitterStatusListFromServer(int page) throws TwitterException{
	
			statusList = twitter.getUserTimeline(user, new Paging(1,page));
		
	}
	
	public void setTweetList(){
		for (twitter4j.Status st : statusList) {
			Log.d("myLogs",st.getText());
			tweet = new Tweet();
			tweet.setTweet(st.getText());
			tweet.setData(st.getCreatedAt());
			tweetList.add(tweet);
		}
	}
	
	public void generateOutStr(){
		generateOutStr = new ArrayList<String>();
		Log.d("myLogs","Generate");
		for(Tweet tw: tweetList){
			Log.d("myLogs",tw.getTweet());
			generateOutStr.add("["+tw.getData()+"]"+"\n"+tw.getTweet());
		}
	}
	
	public void setTweetSQLite(){
		db = new DatabaseHandler(context);
		db.addTweets(tweetList);
	}
	
	public void getTweetSQLite(){
		db = new DatabaseHandler(context);
		tweetList.clear();
		tweetList.addAll(db.getAllTweets());
	}
	
	public void delAllTweetFromDB(){
		db = new DatabaseHandler(context);
		db.deleteAllTweets();
	}
	
	public static boolean internetConnection(final Context context) {
	    ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	    if (wifiInfo != null && wifiInfo.isConnected())
	    {
	    	Log.d("myLogs", "TYPE_WIFI");
	        return true;
	    }
	    wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	    if (wifiInfo != null && wifiInfo.isConnected())
	    {
	    	Log.d("myLogs", "TYPE_MOBILE");
	        return true;
	    }
	    wifiInfo = cm.getActiveNetworkInfo();
	    if (wifiInfo != null && wifiInfo.isConnected())
	    {
	    	Log.d("myLogs", "ActiveNetwork");
	        return true;
	    }
	    return false;
	}
	
	public void correctSript(int page){
		try{
			createConnectTwitter();
			getTwitterStatusListFromServer(page);
			setTweetList();
			delAllTweetFromDB();
			setTweetSQLite();
		}catch(Exception ex){
			fatalSript();
		}
		generateOutStr();
	}
	
	public void showToast() {
		
		Toast toast = Toast.makeText(context, 
			  "Sorry, no connection with server!\nThis information may be deprecated!",
			  Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
		
	}
	
	public void fatalSript(){
		err=true;
		getTweetSQLite();
		generateOutStr();
	}

	
	@Override
	protected List<String> doInBackground(Integer... params) {
		if(internetConnection(context)){
			Log.d("myLogs", "Connection internet");
			correctSript(params[0]);
		}else{
			Log.d("myLogs", "No connection internet");
			
			fatalSript();
		}
		return generateOutStr;
	}
	

	@Override
	protected void onPostExecute(List<String> result) {
		super.onPostExecute(result);
		if(err){
			showToast();
		}
	}
	
}
