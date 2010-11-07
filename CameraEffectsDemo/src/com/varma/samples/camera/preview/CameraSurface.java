package com.varma.samples.camera.preview;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.GestureDetector.OnGestureListener;
import com.varma.samples.camera.callback.CameraCallback;

public class CameraSurface extends SurfaceView implements SurfaceHolder.Callback, OnGestureListener{	
	private Camera camera = null;
	private SurfaceHolder holder = null;
	private CameraCallback callback = null;
	private GestureDetector gesturedetector = null;
	private String[] supportedColorEffects = null;
	private String[] supportedWhiteBalances = null;
	private int currentColorEffect = 0;
	private int currentWhiteBalance = 0;
	private int currentZoom = 0;
	private boolean isZoomIn = true;
	private boolean isStarted = true;
	
	public CameraSurface(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		initialize(context);
	}

	public CameraSurface(Context context) {
		super(context);
		
		initialize(context);
	}

	public CameraSurface(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		initialize(context);
	}
	
	public void setCallback(CameraCallback callback){
		this.callback = callback;
	}
	
	public void startPreview(){
		camera.startPreview();
	}
	
	public void startTakePicture(){
		camera.autoFocus(new AutoFocusCallback() {
			@Override
			public void onAutoFocus(boolean success, Camera camera) {
				takePicture();
			}
		});
	}
	
	public int getCurrentColorEffect(){
		return currentColorEffect;
	}
	
	public int getCurrentWhiteBalance(){
		return currentWhiteBalance;
	}
	
	public String[] getSupportedColorEffects(){
		return supportedColorEffects;
	}
	
	public String[] getSupportedWhiteBalances(){
		return supportedWhiteBalances;
	}
	
	public void setColorEffect(int effect){
		Camera.Parameters parameters = camera.getParameters();
		
		parameters.setColorEffect(supportedColorEffects[effect]);
		
		camera.setParameters(parameters);
		
		currentColorEffect = effect;
	}
	
	public void setWhiteBalance(int effect){
		Camera.Parameters parameters = camera.getParameters();
		
		parameters.setWhiteBalance(supportedWhiteBalances[effect]);
		
		camera.setParameters(parameters);
		
		currentWhiteBalance = effect;
	}
	
	public void takePicture() {
		camera.takePicture(
				new ShutterCallback() {
					@Override
					public void onShutter(){
						if(null != callback) callback.onShutter();
					}
				},
				new PictureCallback() {
					@Override
					public void onPictureTaken(byte[] data, Camera camera){
						if(null != callback) callback.onRawPictureTaken(data, camera);
					}
				},
				new PictureCallback() {
					@Override
					public void onPictureTaken(byte[] data, Camera camera){
						if(null != callback) callback.onJpegPictureTaken(data, camera);
					}
				});
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
		if(null != camera && isStarted)
		{
			camera.startPreview();
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		camera = Camera.open();
		
		try {
			camera.setPreviewDisplay(holder);
			camera.setPreviewCallback(new Camera.PreviewCallback() {
				@Override
				public void onPreviewFrame(byte[] data, Camera camera) {
					if(null != callback) callback.onPreviewFrame(data, camera);
				}
			});
			
			final List<String> coloreffects = camera.getParameters().getSupportedColorEffects();
			final List<String> whiteBalances = camera.getParameters().getSupportedWhiteBalance();
			
			supportedColorEffects = new String[coloreffects.size()];
			supportedWhiteBalances = new String[whiteBalances.size()];
			
			coloreffects.toArray(supportedColorEffects);
			whiteBalances.toArray(supportedWhiteBalances);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		isStarted = false;
		
		camera.stopPreview();
		camera.setPreviewCallback(null);
		camera.release();
		
		camera = null;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return gesturedetector.onTouchEvent(event);
	}
	
	@Override
	public boolean onDown(MotionEvent e) {
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		Camera.Parameters parameters = camera.getParameters();
		
		if(isZoomIn)
			currentZoom += 1;
		else
			currentZoom -= 1;
		
		if(currentZoom > parameters.getMaxZoom())
		{
			currentZoom = parameters.getMaxZoom();
			isZoomIn = false;
		}
		else if(currentZoom <= 0)
		{
			currentZoom = 0;
			isZoomIn = true;
		}
		
		parameters.setZoom(currentZoom);
		
		camera.setParameters(parameters);
		
		Log.i("CameraEffectsDemo", "Current Zoom: " + currentZoom + ", Max Zoom: " + parameters.getMaxZoom());
		
		return false;
	}

	private void initialize(Context context) {
		holder = getHolder();
		
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		gesturedetector = new GestureDetector(this);
	}
}