package com.iesebre.DAM2.ProjecteSintesi;

import com.example.androidhive.R;
import com.pushbots.push.Pushbots;

import android.app.Application;


public class Notificacio extends Application {
	@Override
	public void onCreate() {
        super.onCreate();
     
        Pushbots.init(this, "820445240982","538380281d0ab1d1048b45a7");
       
	}
}
