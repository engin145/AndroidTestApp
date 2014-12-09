package com.example.androidtestapp;

import java.util.ArrayList;
import java.util.List;

import com.example.androidtesta.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends Activity{
	private TwitterStatusController twitterController;
	private List<String> statusTwitterList;
	private ArrayAdapter<String> adapter;
	private ListView lvMain;
	
	public final static Integer paging = 10;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        lvMain = (ListView) findViewById(R.id.lvMain);
        statusTwitterList = new ArrayList<String>();
        try{
        	doTakeNewStatusList();
        }catch(Exception ex){
        	Log.d("myLogs","Error", ex);
        }
        
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, statusTwitterList);
        lvMain.setAdapter(adapter);
    }

	
	public void refresh(View v){
		doTakeNewStatusList();
		adapter.notifyDataSetChanged();
	}
		
	public void doTakeNewStatusList(){
		statusTwitterList.clear();
		twitterController = new TwitterStatusController(this);
    	try{  
    		twitterController.execute(paging);
    		statusTwitterList.addAll(twitterController.get());
        	
        }catch(Exception ex){
			Toast toast = Toast.makeText(getApplicationContext(), 
			   "Sorry, no connection with server!\nThis information may be deprecated!", Toast.LENGTH_LONG); 
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
        	Log.d("myLogs","Error", ex);
        }	
	}
}

