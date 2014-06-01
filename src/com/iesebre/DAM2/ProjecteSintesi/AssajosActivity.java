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

public class AssajosActivity extends ListActivity {
	// Progress Dialog
	private ProgressDialog pDialog;

	// Creating JSON Parser object
	JSONParser jsonParser = new JSONParser();

	ArrayList<HashMap<String, String>> inboxList;

	// products JSONArray
	JSONArray inbox = null;

	// Inbox JSON url
	private static final String INBOX_URL = "http://albertcanelles.esy.es/admin/index.php/welcome/json_assajos";
	
	// ALL JSON node names
	private static final String TAG_MESSAGES = "Assajs";
	private static final String TAG_ID = "id_assajs";
	private static final String TAG_FROM = "Assajs";
	private static final String TAG_EMAIL = "ProxActuacio";
	private static final String TAG_SUBJECT = "Lloc";
	private static final String TAG_DATE = "DiaHora";
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inbox_list);
		
		// Hashmap for ListView
        inboxList = new ArrayList<HashMap<String, String>>();
 
        // Loading INBOX in Background Thread
        new LoadInbox().execute();
	}

	/**
	 * Background Async Task to Load all INBOX messages by making HTTP Request
	 * */
	class LoadInbox extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(AssajosActivity.this);
			pDialog.setMessage("Carregant Assajos ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting Inbox JSON
		 * */
		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			
			// getting JSON string from URL
			JSONObject json = jsonParser.makeHttpRequest(INBOX_URL, "GET",
					params);

			// Check your log cat for JSON reponse
			Log.d("Inbox JSON: ", json.toString());

			try {
				inbox = json.getJSONArray(TAG_MESSAGES);
				// looping through All messages
				for (int i = 0; i < inbox.length(); i++) {
					JSONObject c = inbox.getJSONObject(i);

					// Storing each json item in variable
					String id = c.getString(TAG_ID);
					String from  = c.getString(TAG_FROM);
					String email = "Prox. Act.: "+c.getString(TAG_EMAIL);
					String subject = "Lloc: "+c.getString(TAG_SUBJECT);
					String date = c.getString(TAG_DATE);

					// creating new HashMap
					HashMap<String, String> map = new HashMap<String, String>();

					// adding each child node to HashMap key => value
					map.put(TAG_ID, id);
					map.put(TAG_FROM, from);
					map.put(TAG_EMAIL, email);
					map.put(TAG_SUBJECT, subject);
					map.put(TAG_DATE, date);

					// adding HashList to ArrayList
					inboxList.add(map);
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
							AssajosActivity.this, inboxList,
							R.layout.inbox_list_item, new String[] { TAG_FROM, TAG_EMAIL, TAG_SUBJECT, TAG_DATE },
							new int[] { R.id.from, R.id.email, R.id.subject, R.id.date });
					// updating listview
					setListAdapter(adapter);
				}
			});

		}

	}
}
