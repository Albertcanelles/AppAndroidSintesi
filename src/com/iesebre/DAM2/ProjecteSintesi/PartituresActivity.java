package com.iesebre.DAM2.ProjecteSintesi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.androidhive.R;

public class PartituresActivity extends ListActivity {

	// Progress Dialog
	private ProgressDialog pDialog;

	// Creating JSON Parser object
	JSONParser jsonParser = new JSONParser();

	ArrayList<HashMap<String, String>> ProfileList;

	// products JSONArray
	JSONArray profiles = null;
	
// Profile JSON url
private static final String PROFILE_URL = "http://albertcanelles.esy.es/admin/index.php/welcome/json_partitures";

// ALL JSON node names
private static final String TAG_PROFILE = "Partitures";
private static final String TAG_NAME = "Nom";
private static final String TAG_EMAIL = "Partitura";



@Override
public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.profile_list);
	
	   ProfileList = new ArrayList<HashMap<String, String>>();
	   
    // Loading Profile in Background Thread
    new LoadProfile().execute();

    ListAdapter adaptador = new ArrayAdapter<String>(
          this, android.R.layout.simple_list_item_1);
    
    //Asociamos el adaptador a la vista.
    ListView lv = (ListView) findViewById(android.R.id.list);
    lv.setAdapter(adaptador);

}

@Override
protected void onListItemClick(ListView l, View v, int position, long id) {
  super.onListItemClick(l, v, position, id);
  
  Uri uri = Uri.parse("http://www.albertcanelles.esy.es/admin/partitures/"+ProfileList.get(+position).get("Nom"));
 	Intent intent = new Intent(Intent.ACTION_VIEW, uri);
 	startActivity(intent);
}
	
/**
 * Background Async Task to Load profile by making HTTP Request
 * */
class LoadProfile extends AsyncTask<String, String, String> {

	/**
	 * Before starting background thread Show Progress Dialog
	 * */
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pDialog = new ProgressDialog(PartituresActivity.this);
		pDialog.setMessage("Carregant Partitures ...");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(false);
		pDialog.show();
	}

	/**
	 * getting Profile JSON
	 * */
	protected String doInBackground(String... args) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		// getting JSON string from URL
		JSONObject json = jsonParser.makeHttpRequest(PROFILE_URL, "GET",
				params);

		// Check your log cat for JSON reponse
		Log.d("Profile JSON: ", json.toString());

		try {
			profiles = json.getJSONArray(TAG_PROFILE);
			// looping through All messages
			for (int i = 0; i < profiles.length(); i++) {
				JSONObject c = profiles.getJSONObject(i);

				// Storing each json item in variable
				
				String name = c.getString(TAG_NAME);
				String email = c.getString(TAG_EMAIL);

				// creating new HashMap
				HashMap<String, String> map = new HashMap<String, String>();

				// adding each child node to HashMap key => value
			
				map.put(TAG_NAME, name);
				map.put(TAG_EMAIL, email);
				
				// adding HashList to ArrayList
				ProfileList.add(map);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}


	/**
	 * After completing background task Dismiss the progress dialog
	 * **/
	protected void onPostExecute(String file_url) {
		// dismiss the dialog after getting all products
		pDialog.dismiss();
		// updating UI from Background Thread
		runOnUiThread(new Runnable() {
			public void run() {
				/**
				 * Updating parsed JSON data into ListView
				 * */
				ListAdapter adapter = new SimpleAdapter(
						PartituresActivity.this, ProfileList,
						R.layout.profile_list_item, new String[] { TAG_NAME},
						new int[] { R.id.partitura});
				// updating listview
				setListAdapter(adapter);
				
								
			}
		
			
		});

	}

}
}