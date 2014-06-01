package com.iesebre.DAM2.ProjecteSintesi;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.example.androidhive.R;
import com.pushbots.push.Pushbots;

public class AndroidTabAndListView extends TabActivity {
	// TabSpec Names
	private static final String INBOX_SPEC = "Assajos";
	private static final String OUTBOX_SPEC = "Concerts";
	private static final String PROFILE_SPEC = "Partitures";
	private static final String VIDEO_SPEC = "Videos";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        TabHost tabHost = getTabHost();
        
        
     // Videos Tab
        TabSpec videoSpec = tabHost.newTabSpec(VIDEO_SPEC);
        // Tab Icon
        videoSpec.setIndicator(VIDEO_SPEC, getResources().getDrawable(R.drawable.videos));
        Intent videosIntent = new Intent(this, VideosActivity.class);
        videoSpec.setContent(videosIntent);
        
        // Inbox Tab
        TabSpec inboxSpec = tabHost.newTabSpec(INBOX_SPEC);
        // Tab Icon
        inboxSpec.setIndicator(INBOX_SPEC, getResources().getDrawable(R.drawable.assajs));
        Intent inboxIntent = new Intent(this, AssajosActivity.class);
        // Tab Content
        inboxSpec.setContent(inboxIntent);
        
        // Outbox Tab
        TabSpec outboxSpec = tabHost.newTabSpec(OUTBOX_SPEC);
        outboxSpec.setIndicator(OUTBOX_SPEC, getResources().getDrawable(R.drawable.concert));
        Intent outboxIntent = new Intent(this, ConcertsActivity.class);
        outboxSpec.setContent(outboxIntent);
        
        // Profile Tab
        TabSpec profileSpec = tabHost.newTabSpec(PROFILE_SPEC);
        profileSpec.setIndicator(PROFILE_SPEC, getResources().getDrawable(R.drawable.partitures));
        Intent profileIntent = new Intent(this, PartituresActivity.class);
        profileSpec.setContent(profileIntent);
        
        // Adding all TabSpec to TabHost
        tabHost.addTab(inboxSpec); // Adding Inbox tab
        tabHost.addTab(outboxSpec); // Adding Outbox tab
        tabHost.addTab(profileSpec); // Adding Profile tab
        tabHost.addTab(videoSpec);  // Adding Videos tab
    }
}