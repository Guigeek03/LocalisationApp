package fr.utbm.localisationapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import fr.utbm.localisationapp.R;

public class LocalisationActivity extends Activity {
	
	private RelativeLayout relativeLayout;
	private ImageView map;
	private ImageView marker;
	private Bundle bundle;
	private Float x;
	private Float y;
	private Rect bounds;
	private Matrix matrix = new Matrix();
	private float[] imageValues = new float[9];
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localisation);
        
        bundle = getIntent().getExtras();
        x = bundle.getFloat("x");
        y = bundle.getFloat("y");
        
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        map = (ImageView) findViewById(R.id.map);
        
        matrix = map.getImageMatrix();
        matrix.getValues(imageValues);
        
        marker = new ImageView(this);
        marker.setImageResource(R.drawable.marker);
        
        marker.setX(imageValues[0]*x+imageValues[2]);
        marker.setY(imageValues[4]*y+imageValues[5]);
        
        relativeLayout.addView(marker);
        
        Toast.makeText(this, "X = " + x + " | Y = " + y, Toast.LENGTH_LONG).show();
        
    }
    
    
    
}
