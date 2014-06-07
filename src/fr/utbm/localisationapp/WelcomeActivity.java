package fr.utbm.localisationapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import fr.utbm.localisationapp.R;

public class WelcomeActivity extends Activity {
	Button butPrefs;
	Button butLocalise;
	ImageView logo;
	TextView textcopyrights;
	TextView welcometext;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome); 
       
        logo = (ImageView) findViewById(R.id.logo);
        butPrefs = (Button) findViewById(R.id.settings_button);
        butLocalise = (Button) findViewById(R.id.localisation_button);
        textcopyrights = (TextView) findViewById(R.id.copyrights);
        welcometext = (TextView) findViewById(R.id.welcometext);
        
        //Set new font
        Typeface typeFace=Typeface.createFromAsset(getAssets(),"calibril.ttf");
        butLocalise.setTypeface(typeFace);
        textcopyrights.setTypeface(typeFace);
        butPrefs.setTypeface(typeFace);
        welcometext.setTypeface(typeFace);
        
        //Font now set
        
        butPrefs.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
		        Intent i = new Intent("fr.utbm.localisationapp.PREFS");
		        startActivity(i);
			}
		});
        
        butLocalise.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
		        Intent i = new Intent("fr.utbm.localisationapp.LOCALISATION");
		        startActivity(i);
			}
		});
        
        /**File directory = new File(Environment.getExternalStorageDirectory(), "/calibrationApp/maps");
        Log.d("MAPS_MANAGEMENT", directory.getAbsolutePath());
        Log.d("MAPS_MANAGEMENT", directory.getPath());
        
        if (!directory.exists()) {
        	Log.d("MAPS_MANAGEMENT", "MAPS DIRECTORY CREATED");
        	directory.mkdir();
        }**/

    }
}
