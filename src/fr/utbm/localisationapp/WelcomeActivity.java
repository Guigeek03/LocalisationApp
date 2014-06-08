package fr.utbm.localisationapp;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import fr.utbm.localisationapp.R;
import fr.utbm.localisationapp.utils.NetworkUtils;

public class WelcomeActivity extends Activity {
	private SharedPreferences sp;
	private Button butPrefs;
	private Button butLocalise;
	private ImageView logo;
	private TextView textcopyrights;
	private TextView welcometext;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome); 
        
        sp = PreferenceManager.getDefaultSharedPreferences(this);
       
        logo = (ImageView) findViewById(R.id.logo);
        butPrefs = (Button) findViewById(R.id.settings_button);
        butLocalise = (Button) findViewById(R.id.localisation_button);
        textcopyrights = (TextView) findViewById(R.id.copyrights);
        welcometext = (TextView) findViewById(R.id.welcometext);
    
        Typeface typeFace=Typeface.createFromAsset(getAssets(),"calibril.ttf");
        butLocalise.setTypeface(typeFace);
        textcopyrights.setTypeface(typeFace);
        butPrefs.setTypeface(typeFace);
        welcometext.setTypeface(typeFace);
        
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
				new LocaliseTask().execute();
			}
		});
    }
    
    private void launchLocalisationActivity(Float x, Float y) {
        Intent i = new Intent("fr.utbm.localisationapp.LOCALISATION");
        Bundle bundle = new Bundle();
        bundle.putFloat("x", x);
        bundle.putFloat("y", y);
        i.putExtras(bundle);
        startActivity(i);
    }
    
	private class LocaliseTask extends AsyncTask<Void, Void, String> {
		
		@Override
		protected String doInBackground(Void... params) {
			try {
				return NetworkUtils.sendRequest("http://" + sp.getString("serverAddress", "192.168.1.1") + ":" + sp.getString("serverPort", "80") + "/server/locateMe");
			} catch (IOException e) {
				return "Unable to retrieve web page. URL may be invalid.";
			}
		}

		@Override
		protected void onPostExecute(String result) {
			try {
				Log.d("HTTP_REQUEST", result);
				JSONObject jsonResult = new JSONObject(result);
				
				if(jsonResult.getBoolean("success")) {
					launchLocalisationActivity((float) jsonResult.getDouble("x"), (float) jsonResult.getDouble("y"));
				} else {
					Toast.makeText(getParent(), "Unable to retrieve location", Toast.LENGTH_LONG).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}
