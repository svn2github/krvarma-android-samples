package com.varma.samples.multitouchsample;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
//import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;

public class MultitouchView extends View {
	private static final int STROKE_WIDTH = 1;
	private static final int CIRCLE_RADIUS = 20;
	
	private ArrayList<PointF> touchPoints = null;
	private Paint drawingPaint = null;
	private boolean isMultiTouch = false;
	private int pathEffectPhase = 0;

	public MultitouchView(Context context) {
		super(context);
		
		initialize(context);
	}

	public MultitouchView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		initialize(context);
	}

	public MultitouchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		initialize(context);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if(touchPoints.size() > 0)
		{
			DashPathEffect effect = new DashPathEffect(new float[] {7,7}, pathEffectPhase);
			PointF midpt = null;
			
			drawingPaint.setPathEffect(effect);
			
			for(int index=1; index<touchPoints.size(); ++index)
			{
				midpt = getMidPoint(
						touchPoints.get(index - 1).x,touchPoints.get(index - 1).y,
						touchPoints.get(index).x,touchPoints.get(index).y);
				
				canvas.drawCircle(
						touchPoints.get(index - 1).x,touchPoints.get(index - 1).y, 
						1, drawingPaint);
				canvas.drawCircle(
						touchPoints.get(index - 1).x,touchPoints.get(index - 1).y, 
						CIRCLE_RADIUS, drawingPaint);
				
				canvas.drawCircle(touchPoints.get(index).x,touchPoints.get(index).y, 
						1, drawingPaint);
				canvas.drawCircle(touchPoints.get(index).x,touchPoints.get(index).y, 
						CIRCLE_RADIUS, drawingPaint);
				
				canvas.drawLine(
						touchPoints.get(index - 1).x,touchPoints.get(index - 1).y,
						touchPoints.get(index).x,touchPoints.get(index).y,
						drawingPaint);
									
				canvas.drawCircle(midpt.x,midpt.y, 10, drawingPaint);
			}
			
			++pathEffectPhase;
					
			invalidate();
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		
		int action = event.getAction() & MotionEvent.ACTION_MASK;
		
		switch(action)
		{
			case MotionEvent.ACTION_DOWN:
			{
				invalidate();
				
				break;
			}
			case MotionEvent.ACTION_POINTER_DOWN:
			{
				isMultiTouch = true;
				
				setPoints(event);
				invalidate();
								
				break;
			}
			case MotionEvent.ACTION_POINTER_UP:
			{
				isMultiTouch = false;
				
				break;
			}
			case MotionEvent.ACTION_MOVE:
			{
				if(isMultiTouch)
				{
					setPoints(event);
					invalidate();
				}
				
				break;
			}
		}
		
		return true;
	}	
	
	private void initialize(Context context){
		drawingPaint = new Paint();

		drawingPaint.setColor(Color.RED);
		drawingPaint.setStrokeWidth(STROKE_WIDTH);
		drawingPaint.setStyle(Paint.Style.STROKE);
		drawingPaint.setAntiAlias(true);
		
		touchPoints = new ArrayList<PointF>();
	}
	
	public void setPoints(MotionEvent event){
		touchPoints.clear();
		
		int pointerIndex = 0;
		
		for(int index=0; index<event.getPointerCount(); ++index)
		{
			pointerIndex = event.getPointerId(index);
			
			touchPoints.add(new PointF(event.getX(pointerIndex),event.getY(pointerIndex)));
		}
	}

	private PointF getMidPoint(float x1,float y1, float x2, float y2) {
		PointF point = new PointF();
		
		float x = x1 + x2;
		float y = y1 + y2;
		
		point.set(x / 2, y / 2);
		
		return point;
	}
}