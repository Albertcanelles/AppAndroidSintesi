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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import com.example.androidhive.R;

public class ConcertsActivity extends ListActivity {
	// Progress Dialog
	private ProgressDialog pDialog;

	// Creating JSON Parser object
	JSONParser jsonParser = new JSONParser();

	ArrayList<HashMap<String, String>> outboxList;

	// products JSONArray
	JSONArray outbox = null;

	// Outbox JSON url
	private static final String OUTBOX_URL = "http://albertcanelles.esy.es/admin/index.php/welcome/json_concerts";
	
	// ALL JSON node names
	private static final String TAG_MESSAGES = "Concert";
	private static final String TAG_ID = "Concert";
	private static final String TAG_TO = "Roba";
	private static final String TAG_SUBJECT = "Lloc";
	private static final String TAG_DATE = "DiaHora";
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.outbox_list);
		
		// Hashmap for ListView
        outboxList = new ArrayList<HashMap<String, String>>();
 
        // Loading OUTBOX in Background Thread
        new LoadOutbox().execute();
	}

	/**
	 * Background Async Task to Load all OUTBOX messages by making HTTP Request
	 * */
	class LoadOutbox extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ConcertsActivity.this);
			pDialog.setMessage("Carregan Concerts ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting Outbox JSON
		 * */
		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			
			// getting JSON string from URL
			JSONObject json = jsonParser.makeHttpRequest(OUTBOX_URL, "GET",
					params);

			// Check your log cat for JSON reponse
			Log.d("Outbox JSON: ", json.toString());

			try {
				outbox = json.getJSONArray(TAG_MESSAGES);
				// looping through All messages
				for (int i = 0; i < outbox.length(); i++) {
					JSONObject c = outbox.getJSONObject(i);

					// Storing each json item in variable
					String id =c.getString(TAG_ID);
					String to ="Roba: "+ c.getString(TAG_TO);
					String subject ="Lloc: "+c.getString(TAG_SUBJECT);
					String date = c.getString(TAG_DATE);
					
					// subject taking only first 23 chars
					// to fit into screen
					if(subject.length() > 23){
						subject = subject.substring(0, 22) + "..";
					}

					// creating new HashMap
					HashMap<String, String> map = new HashMap<String, String>();

					// adding each child node to HashMap key => value
					map.put(TAG_ID, id);
					map.put(TAG_TO, to);
					map.put(TAG_SUBJECT, subject);
					map.put(TAG_DATE, date);

					// adding HashList to ArrayList
					outboxList.add(map);
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
							ConcertsActivity.this, outboxList,
							R.layout.outbox_list_item, new String[] {TAG_ID, TAG_SUBJECT, TAG_TO, TAG_DATE },
							new int[] { R.id.subject,R.id.diahora, R.id.to, R.id.date });
					// updating listview
					setListAdapter(adapter);
				}
			});

		}

	}
}