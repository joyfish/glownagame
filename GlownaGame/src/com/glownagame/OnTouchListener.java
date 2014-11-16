package com.glownagame;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class OnTouchListener implements IOnSceneTouchListener {

	public Engine engine;
	public ITextureRegion textureRegion;
	public PhysicsWorld physicsWorld;
	public Camera camera;
	
	float lastX = 0;
	float lastY = 0;
	
	public OnTouchListener(Engine engine, ITextureRegion textureRegion, PhysicsWorld physicsWorld, Camera camera)
	{
		this.engine = engine;
		this.textureRegion = textureRegion;
		this.physicsWorld = physicsWorld;
		this.camera = camera;
	}
	
	
	
    @Override
    public boolean onSceneTouchEvent(Scene pScene,TouchEvent pSceneTouchEvent) {
    	if(pSceneTouchEvent.isActionDown())
    	{
        	Sprite sPlayer = new Sprite(pSceneTouchEvent.getX(), pSceneTouchEvent.getY(), this.textureRegion, this.engine.getVertexBufferObjectManager());
    		
    		final FixtureDef PLAYER_FIX = PhysicsFactory.createFixtureDef(10.0f, 1.0f, 0.0f);
    		Body body = PhysicsFactory.createCircleBody(physicsWorld, sPlayer, BodyType.DynamicBody, PLAYER_FIX);
    		pScene.attachChild(sPlayer);
    		physicsWorld.registerPhysicsConnector(new PhysicsConnector(sPlayer, body, true, false));
    	}
    	
    	if(pSceneTouchEvent.isActionMove())
    	{
    		float x = pSceneTouchEvent.getX();
    		float y = pSceneTouchEvent.getY();
    		System.out.println(x + " - " + y);
    		this.camera.setCenter(this.camera.getCenterX() +1, this.camera.getCenterY() +1);
    	}
    	
    	return true;
    }
}
