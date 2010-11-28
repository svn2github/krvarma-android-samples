package com.varma.samples.patternwallpaper;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;


public class PatternWallpaper extends WallpaperService {
	public static final String PATTERN_WALLPAPER_PREF_NAME = "PatternWallpaperPref.20101128";
	
	private final Handler handler = new Handler();
	
	private Paint paint = null;
	private boolean isVisible = false;
	private SharedPreferences preference;
	
	public PatternWallpaper() {
		super();
	}

	@Override
	public Engine onCreateEngine() {
		return new PatternWallpaperEngine();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		preference = this.getSharedPreferences(PatternWallpaper.PATTERN_WALLPAPER_PREF_NAME, 0);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	private class PatternWallpaperEngine extends Engine{
		private static final int DRAW_DELAY = 5000;

		private final Runnable drawrunnable = new Runnable() {
			public void run() {
				draw();
			}
		};
		
		private int height = 0;
		private int width = 0;
		
		@Override
		public void onCreate(SurfaceHolder surfaceHolder) {
			super.onCreate(surfaceHolder);
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
			
			handler.removeCallbacks(drawrunnable);
		}

		@Override
		public void onOffsetsChanged(float xOffset, float yOffset,
									 float xOffsetStep, float yOffsetStep, int xPixelOffset,
									 int yPixelOffset) {
			
			super.onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep,xPixelOffset, yPixelOffset);
		}

		@Override
		public void onSurfaceChanged(SurfaceHolder holder, int format,int width, int height) {
			super.onSurfaceChanged(holder, format, width, height);
			
			this.width = width;
			this.height = height;
		}

		@Override
		public void onSurfaceCreated(SurfaceHolder holder) {
			super.onSurfaceCreated(holder);
			
			paint = new Paint();
			
			paint.setAntiAlias(true);
			paint.setColor(Color.RED);
		}

		@Override
		public void onSurfaceDestroyed(SurfaceHolder holder) {
			super.onSurfaceDestroyed(holder);
		}

		@Override
		public void onVisibilityChanged(boolean visible) {
			super.onVisibilityChanged(visible);
			
			isVisible = visible;
			
            if (visible) {
            	draw();
            } else {
                handler.removeCallbacks(drawrunnable);
            }

		}
		
		private void draw(){
			final SurfaceHolder holder = getSurfaceHolder();
			
			Canvas canvas = null;
			
			handler.removeCallbacks(drawrunnable);
			
			try
			{
				canvas = holder.lockCanvas();
				
				drawPattern(canvas);
			}
			finally
			{
				if (canvas != null) 
					holder.unlockCanvasAndPost(canvas);
			}
			
			if(isVisible)
			{
				handler.postDelayed(drawrunnable,DRAW_DELAY);
			}
		}
		
		private void drawPattern(Canvas canvas){
			String shapeString = preference.getString("pattern_shape", "1");
			int shape = Integer.parseInt(shapeString);
			
			switch(shape)
			{
				case 1:		drawSpirograph(canvas);		break;		// Siprograph
				case 2:		drawCirclePattern(canvas);	break;		// Circle pattern
				case 3:		drawSierpinski(canvas);		break;		// Sierpinski triangle
				case 4:		drawSpirals(canvas);		break;		// Sierpinski triangle
			}
		}

		private void drawSierpinski(Canvas canvas) {
			double rand = 0.0;
			double x = width/2;
			double y = height/2;
			float iter = 0;
			float increment = 0.2f;
			
			canvas.save();
			
			paint.setAntiAlias(true);
			paint.setColor(Color.rgb((int)(Math.random() * 256), 
					(int)(Math.random() * 256), 
					(int)(Math.random() * 256)));
			
			canvas.drawColor(Color.BLACK);
			
			int w = width/2;
			int h = height/2;
			
			while (iter < 10000.0f) {
				rand = Math.random();

				if (rand < 0.3333) {
					x = 0.5 * (x + 1);
					y = 0.5 * y;
				}
				if ((rand >= 0.3333) && (rand <= 0.6666)) {
					x = x * 0.5;
					y = y * 0.5;
				}
				
				if (rand > 0.6666) {
					x = 0.5 * (x + 0.5);
					y = 0.5 * (y + 1);
				}	

				canvas.drawPoint((int)(w * x) + width/4, h - (int)(h * y) + height/4,paint);
				
				iter += increment;
			}
			
			canvas.restore();
		}

		private void drawCirclePattern(Canvas canvas) {
			float z = 0;
			float g = (int) (Math.random() * 128);
			float b = (int) (Math.random() * 128);
			float increment = 1.0f;

			paint.setAntiAlias(true);
			
			for(float row=0; row<height; row += increment){
				for(float col=0; col<width; col += increment){
					z = ((row * row) + (col * col));
					
					paint.setColor(Color.rgb((int)(z % 256), (int)g, (int)b));
					
					canvas.drawPoint(col, row, paint);
				}
			}
		}

		private void drawSpirograph(Canvas canvas) {
			int offsetx = width/2;
			int offsety = height/2;
			
			double R = Math.random() * 50 + 1;
			double r = Math.random() * 80 + 1;
			double O = Math.random() * 100 + 1;
			double step = 0.004 * Math.sqrt(r);
			double exprResult = 0;
			double rDiff = R + r;
			int x = 0;
			int y = 0;
			
			double iterations = 120;
			
			paint.setColor(Color.rgb(
					(int)(Math.random() * 256), 
					(int)(Math.random() * 256), 
					(int)(Math.random() * 256)));
			
			canvas.drawColor(Color.BLACK);
			
			for(double iter=0.0; iter<iterations; iter+=step)
			{
				exprResult = (rDiff * iter) / (double) r;
				
				x = (int)((rDiff) * Math.cos(iter) + (O) * Math.cos(exprResult) + offsetx);
				y = (int)((rDiff) * Math.sin(iter) - (O) * Math.cos(exprResult) + offsety);
				
				canvas.drawPoint(x, y, paint);
			}
		}

		private void drawSpirals(Canvas canvas) {
			int offsetx = width/2;
			int offsety = height/2;
			
			float a = 1.0f;
			float t = 4.5f;
			float x = offsetx;
			float y = offsetx;
			float iter = 0.0f;
			float increment = 0.01f;
			float tincrement = 0.1f;
			float maxIter = 1024;
			
			paint.setColor(Color.rgb(
					(int)(Math.random() * 256), 
					(int)(Math.random() * 256), 
					(int)(Math.random() * 256)));
			paint.setStrokeWidth(2.0f);
			
			canvas.drawColor(Color.BLACK);
			
			while(iter < maxIter){
				x = (float) (a * t * Math.cos(t)) + offsetx;
				y = (float) (a * t * Math.sin(t)) + offsety;
				
				canvas.drawPoint(x, y, paint);
				
				iter += increment;
				t += tincrement;
			}
		}
	}
}
