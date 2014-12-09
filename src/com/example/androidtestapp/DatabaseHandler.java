package com.example.androidtestapp;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper implements IDatabaseHandler{
	private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "tweetsList.db";
    private static final String TABLE_TWEET = "tweet";
    private static final String KEY_ID = "id";
    private static final String KEY_TWEET = "tweet";
    private static final String KEY_DATA = "data";
    
    private Tweet tweet;
	
	public DatabaseHandler(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_TWEETS_TABLE = "CREATE TABLE " + TABLE_TWEET + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TWEET + " TEXT,"
                + KEY_DATA + " LONG" + ");";
        db.execSQL(CREATE_TWEETS_TABLE);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TWEET);
        onCreate(db);
		
	}
	
	@Override
	public void addTweet(Tweet tweet) {
		SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TWEET, tweet.getTweet());
        values.put(KEY_DATA, tweet.getDataLong());
        db.insert(TABLE_TWEET, null, values);
        db.close();
	}
	
	@Override
	public void addTweets(List<Tweet> tweetList) {
		SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        for(Tweet tw: tweetList){
        	values.put(KEY_TWEET, tw.getTweet());
            values.put(KEY_DATA, tw.getDataLong());
            db.insert(TABLE_TWEET, null, values);
        }
        db.close();
	}

	@Override
	public List<Tweet> getAllTweets() {
		List<Tweet> tweetList = new ArrayList<Tweet>();
        String selectQuery = "SELECT  * FROM " + TABLE_TWEET;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null); 
        if (cursor.moveToFirst()) {
            do {
                tweet = new Tweet();
                tweet.setTweet(cursor.getString(1));
                tweet.setData(cursor.getLong(2));
                tweetList.add(tweet);
            } while (cursor.moveToNext());
        }
        db.close();
		return tweetList;
	}

	@Override
	public Tweet getTweet(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAllTweets() {
		SQLiteDatabase db = this.getWritableDatabase();
		String DEL_ALL_TWEET="DELETE FROM "+TABLE_TWEET;
		db.execSQL(DEL_ALL_TWEET);
		db.close();
	}

	@Override
	public void deleteTweetMinDate() {
		SQLiteDatabase db = this.getWritableDatabase();
		//db.delete(TABLE_TWEET, KEY_DATA + "=?", );
		db.close();
		
	}	
}
