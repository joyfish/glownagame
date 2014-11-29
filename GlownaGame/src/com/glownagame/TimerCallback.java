package com.glownagame;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;

public class TimerCallback implements ITimerCallback {

	Point p;
	Camera camera;
	
	public TimerCallback(Camera camera, Point p)
	{
		this.camera = camera;
		this.p = p;
	}
	
	@Override
	public void onTimePassed(TimerHandler pTimerHandler) {
		this.camera.setCenter(p.x, p.y);
	}

}
