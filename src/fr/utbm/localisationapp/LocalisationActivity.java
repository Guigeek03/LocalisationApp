package fr.utbm.localisationapp;

import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class LocalisationActivity extends Activity {

	private static final Float TRANSPARENT = 0f;
	private static final Float VISIBLE = 1f;

	private RelativeLayout relativeLayout;
	private ImageView map;
	private ImageView marker;
	private Bundle bundle;
	private Float x;
	private Float y;
	private Rect bounds;
	private Matrix matrix = new Matrix();
	private float[] imageValues = new float[9];
	private Boolean pointVisible = true;

	private Matrix savedMatrix = new Matrix();
	private int viewHeight;
	private int viewWidth;
	private Rect markerBounds;

	// Remember some things for zooming
	private PointF startPoint = new PointF();
	private PointF midPoint = new PointF();
	private float oldDist = 1f;
	private String savedItemClicked;

	private static final int NONE = 0;
	private static final int DRAG = 1;
	private static final int ZOOM = 2;
	private int mode = NONE;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_localisation);

		bundle = getIntent().getExtras();
		x = bundle.getFloat("x");
		y = bundle.getFloat("y");

		relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
		map = (ImageView) findViewById(R.id.map);
		bounds = map.getDrawable().getBounds();
		
		viewHeight = getResources().getDisplayMetrics().heightPixels;
		viewWidth = getResources().getDisplayMetrics().widthPixels;
		
		map.setOnTouchListener(passiveModeListener);

		matrix = map.getImageMatrix();
		matrix.getValues(imageValues);

		marker = new ImageView(this);
		marker.setImageResource(R.drawable.marker);
		markerBounds = marker.getDrawable().getBounds();

		marker.setX(imageValues[0] * x + imageValues[2] - markerBounds.width()/2);
		marker.setY(imageValues[4] * y + imageValues[5] - markerBounds.height());

		relativeLayout.addView(marker);

		Toast.makeText(this, "X = " + x + " | Y = " + y, Toast.LENGTH_LONG).show();
	}

	private OnTouchListener passiveModeListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			ImageView view = (ImageView) v;

			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
				if (pointVisible) {
					pointVisible = false;
					marker.setAlpha(TRANSPARENT);
				}
				startPoint.set(event.getX(), event.getY());
				savedMatrix.set(matrix);
				mode = DRAG;
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				if (pointVisible) {
					pointVisible = false;
					marker.setAlpha(TRANSPARENT);
				}
				oldDist = spacing(event);
				if (oldDist > 10f) {
					savedMatrix.set(matrix);
					midPoint(midPoint, event);
					mode = ZOOM;
				}
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				if (!pointVisible) {
					pointVisible = true;
					matrix.getValues(imageValues);
					marker.setX(imageValues[0] * x + imageValues[2] - markerBounds.width()/2);
					marker.setY(imageValues[4] * y + imageValues[5] - markerBounds.height());
					marker.setAlpha(VISIBLE);
				}
				mode = NONE;
				break;
			case MotionEvent.ACTION_MOVE:
				if (mode == DRAG) {
					matrix.set(savedMatrix);
					matrix.postTranslate(event.getX() - startPoint.x, event.getY() - startPoint.y);
				} else if (mode == ZOOM) {
					float newDist = spacing(event);
					if (newDist > 10f) {
						matrix.set(savedMatrix);
						float scale = newDist / oldDist;

						matrix.postScale(scale, scale, midPoint.x, midPoint.y);
						matrix.getValues(imageValues);

						/** ZOOM CONTROL **/
						if (imageValues[0] < viewWidth * 1. / bounds.width()) {
							matrix.setScale(viewWidth * 1.f / bounds.width(), viewWidth * 1.f / bounds.width(), midPoint.x, midPoint.y);
						}
						if (imageValues[0] > 2) {
							matrix.setScale(2, 2);
						}
					}
				}
				matrix.getValues(imageValues);
				/** PANNING CONTROL **/
				if (imageValues[2] > 0) {
					matrix.postTranslate(-imageValues[2], 0);
				}
				if (imageValues[5] > 0) {
					matrix.postTranslate(0, -imageValues[5]);
				}
				if (imageValues[2] < viewWidth - bounds.width() * imageValues[0]) {
					matrix.postTranslate(viewWidth - bounds.width() * imageValues[0] - imageValues[2], 0);
				}

				if (imageValues[5] < -bounds.height() * imageValues[0]) {
					matrix.postTranslate(0, -bounds.height() * imageValues[0] - imageValues[5]);
				}

				break;
			}

			view.setImageMatrix(matrix);

			return true;
		}

		/** Determine the space between the first two fingers */
		private float spacing(MotionEvent event) {
			float x = event.getX(0) - event.getX(1);
			float y = event.getY(0) - event.getY(1);
			return (float) Math.sqrt(x * x + y * y);
		}

		/** Calculate the mid point of the first two fingers */
		private void midPoint(PointF point, MotionEvent event) {
			float x = event.getX(0) + event.getX(1);
			float y = event.getY(0) + event.getY(1);
			point.set(x / 2, y / 2);
		}

	};

}
