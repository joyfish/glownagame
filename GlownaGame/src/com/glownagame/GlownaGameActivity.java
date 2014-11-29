package com.glownagame;


import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.color.Color;

import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;


public class GlownaGameActivity extends BaseGameActivity implements IOnSceneTouchListener /* implements IScrollDetectorListener*/  {

	Scene scene;
	protected static final float CAMERA_WIDTH = 800;
	protected static final float CAMERA_HEIGHT = 480;
	BitmapTextureAtlas playerTexture;
	ITextureRegion playerTexureRegion;
	PhysicsWorld physicsWorld;
	Camera mCamera;
	Point p;
	Body body;
	Sprite sPlayer;
	Rectangle ground;
	Body bodyGround;
	boolean canJump = false; 
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		// TODO Auto-generated method stub

		// Camera movement speeds
		final float maxVelocityX = 200;
		final float maxVelocityY = 200;
		// Camera zoom speed
		final float maxZoomFactorChange = 30;
		    
		// Create smooth camera
		//mCamera = new SmoothCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT, maxVelocityX, maxVelocityY, maxZoomFactorChange);
		 mCamera = new Camera(300, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
//				  new BoundCamera(0.0f, 0.0f, CAMERA_WIDTH, CAMERA_HEIGHT){
//			        @Override
//			        public void setCenter(float pCenterX, float pCenterY) {
//			                super.setCenter(p.x,p.y);
//			        }      
//			};
			// new SmoothCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT, 0.1f, 0.1f, 0.1f);
			
		 p = new Point(this.mCamera.getCenterX(), this.mCamera.getCenterY());

		EngineOptions options = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(
						CAMERA_WIDTH, CAMERA_HEIGHT), mCamera);
		return options;
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws Exception {
		// TODO Auto-generated method stub
	

		 loadGfx();
		// resource
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	private void loadGfx() {
		// TODO Auto-generated method stub
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		// width and height power of 2^x
		playerTexture = new BitmapTextureAtlas(getTextureManager(), 64, 128);
		playerTexureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(playerTexture, this, "badge.png", 0, 0);
		playerTexture.load();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws Exception {
		this.scene = new Scene();
		this.scene.setBackground(new Background(0, 125, 58));
		physicsWorld = new PhysicsWorld(new Vector2(0,
				SensorManager.GRAVITY_EARTH), false);
		this.scene.registerUpdateHandler(physicsWorld);
		createWalls();
		
		this.scene.setOnSceneTouchListener(this);
		//this.scene.registerUpdateHandler(new TimerHandler(0.03f, true, new TimerCallback(mCamera, p)));
		
		physicsWorld.setContactListener(contactListener2(this));
		pOnCreateSceneCallback.onCreateSceneFinished(this.scene);
			
	}

	private ContactListener contactListener2(final GlownaGameActivity g)
	{
		ContactListener contactListener = new ContactListener()
		{

			@Override
			public void beginContact(Contact contact) 
			{
				// TODO Auto-generated method stub
				g.canJump = true;
			}

			@Override
			public void endContact(Contact contact) {

				g.canJump = false;
			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				// TODO Auto-generated method stub
				
			}
		};
		return contactListener;
	}
	
	private void createWalls() {
		// TODO Auto-generated method stub
		FixtureDef WALL_FIX = PhysicsFactory.createFixtureDef(0.0f, 0.0f, 0.0f);
		ground = new Rectangle(0, CAMERA_HEIGHT - 15, CAMERA_WIDTH,
				15, this.mEngine.getVertexBufferObjectManager());
		ground.setColor(new Color(15, 50, 0));
		bodyGround = PhysicsFactory.createBoxBody(physicsWorld, ground, BodyType.StaticBody,
				WALL_FIX);
		this.scene.attachChild(ground);
		
		Rectangle ground2 = new Rectangle(CAMERA_WIDTH -40, CAMERA_HEIGHT - 70, 20,
				150, this.mEngine.getVertexBufferObjectManager());
		ground2.setColor(new Color(88, 50, 0));
		PhysicsFactory.createBoxBody(physicsWorld, ground2, BodyType.StaticBody,
				WALL_FIX);
		this.scene.attachChild(ground2);
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
		// TODO Auto-generated method stub

		sPlayer = new Sprite(CAMERA_WIDTH / 2 - 100, CAMERA_HEIGHT / 2,
				playerTexureRegion, this.mEngine.getVertexBufferObjectManager());
		final FixtureDef PLAYER_FIX = PhysicsFactory.createFixtureDef(1.0f,
				0.0f, 0.0f);
		
		//body = PhysicsFactory.createCircleBody(physicsWorld, sPlayer, BodyType.DynamicBody, PLAYER_FIX);
		body = PhysicsFactory.createBoxBody(physicsWorld, 50, 30, 64, 128, BodyType.DynamicBody, PLAYER_FIX);
		this.scene.attachChild(sPlayer);
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(sPlayer,
				body, true, false));

		this.mCamera.setChaseEntity(sPlayer);
		
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
	
	@Override
	public boolean onSceneTouchEvent(Scene pScene, final TouchEvent pSceneTouchEvent)
	{
		
	    if (pSceneTouchEvent.isActionDown())
	    {
//	        Vector2 vel = this.body.getLinearVelocity();
//	    	vel.x += 5;//pSceneTouchEvent.getX() < CAMERA_WIDTH /2 ? -5 : 5;
//	    	this.body.setLinearVelocity(vel);

	    	if(this.canJump)
	    	{
	  	    	//jump
	  	    	float impulse = this.body.getMass() * 10;
	  	    	this.body.applyLinearImpulse(new Vector2(0, -impulse), this.body.getWorldCenter());	
	    	}
	    }
	    if (pSceneTouchEvent.isActionUp())
	    {
	        Vector2 vel = this.body.getLinearVelocity();
	    	vel.x = 0; //pSceneTouchEvent.getX() < CAMERA_WIDTH /2 ? -5 : 5;
	    	this.body.setLinearVelocity(vel);
	    }
	    return true;
	}
	
	
//	@Override
//	public void onScrollStarted(ScrollDetector pScollDetector, int pPointerID,
//			float pDistanceX, float pDistanceY) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void onScroll(ScrollDetector pScollDetector, int pPointerID,
//			float pDistanceX, float pDistanceY) {
//		// TODO Auto-generated method stub
//		mCamera.setCenter(mCamera.getCenterX() - pDistanceX, mCamera.getCenterY() - pDistanceY);
//	}
//
//	@Override
//	public void onScrollFinished(ScrollDetector pScollDetector, int pPointerID,
//			float pDistanceX, float pDistanceY) {
//		// TODO Auto-generated method stub
//		
//	}
}

/*
import java.io.IOException;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.IResolutionPolicy;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.bitmap.AssetBitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.color.Color;

import android.content.Context;
import android.view.KeyEvent;
import android.widget.Toast;

public class GlownaGameActivity extends BaseGameActivity {
	Scene scene;
	protected static final int CAMERA_WIDTH = 800;
	protected static final int CAMERA_HEIGHT = 480;
	BitmapTextureAtlas playerTexture;
	ITextureRegion playerTexureRegion;
	BitmapTextureAtlas mBitmapTextureAtlas;
	ITextureRegion mRockTex;
	Flingable mSprite;
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		// TODO Auto-generated method stub
		Camera mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		EngineOptions options = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(
						CAMERA_WIDTH, CAMERA_HEIGHT), mCamera);
		return options;
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws Exception {
		// TODO Auto-generated method stub
		loadGfx();
		// resource
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	private void loadGfx() {
		// TODO Auto-generated method stub
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		// width and height power of 2^x
		playerTexture = new BitmapTextureAtlas(getTextureManager(), 128, 128);
		playerTexureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(playerTexture, this, "badge.png", 0, 0);
		playerTexture.load();
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
	  mBitmapTextureAtlas = new BitmapTextureAtlas(getTextureManager(), 128, 128);
	  mRockTex = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, this, "badge.png", 0, 0);
	  mBitmapTextureAtlas.load();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws Exception {
		// TODO Auto-generated method stub
		
		
		this.scene = new Scene();
		this.scene.setBackground(new Background(0, 125, 58));
		
		mSprite = new Flingable(300, 300, mRockTex, getVertexBufferObjectManager());
		this.scene.attachChild(mSprite);
		this.scene.registerTouchArea(mSprite);
		
		pOnCreateSceneCallback.onCreateSceneFinished(this.scene);
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
		// TODO Auto-generated method stub

		Sprite sPlayer = new Sprite(CAMERA_WIDTH / 2, CAMERA_HEIGHT / 2,
				playerTexureRegion, this.mEngine.getVertexBufferObjectManager());
		//sPlayer.setRotation(45.0f);
		this.scene.attachChild(sPlayer);
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
}*/