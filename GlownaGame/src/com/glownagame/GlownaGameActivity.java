package com.glownagame;


import org.andengine.engine.camera.Camera;
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
import com.badlogic.gdx.physics.box2d.FixtureDef;


public class GlownaGameActivity extends BaseGameActivity/* implements IScrollDetectorListener*/  {

	Scene scene;
	protected static final int CAMERA_WIDTH = 800;
	protected static final int CAMERA_HEIGHT = 480;
	BitmapTextureAtlas playerTexture;
	ITextureRegion playerTexureRegion;
	PhysicsWorld physicsWorld;
	Camera mCamera;

	@Override
	public EngineOptions onCreateEngineOptions() {
		// TODO Auto-generated method stub

		 mCamera = new Camera(300, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

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

		this.scene.setOnSceneTouchListener(new OnTouchListener(this.mEngine, this.playerTexureRegion, this.physicsWorld, this.mCamera));


		//this.scene.setTouchAreaBindingEnabled(true);
		
		
		pOnCreateSceneCallback.onCreateSceneFinished(this.scene);

	}

	private void createWalls() {
		// TODO Auto-generated method stub
		FixtureDef WALL_FIX = PhysicsFactory.createFixtureDef(0.0f, 0.0f, 0.0f);
		Rectangle ground = new Rectangle(0, CAMERA_HEIGHT - 15, CAMERA_WIDTH,
				15, this.mEngine.getVertexBufferObjectManager());
		ground.setColor(new Color(15, 50, 0));
		PhysicsFactory.createBoxBody(physicsWorld, ground, BodyType.StaticBody,
				WALL_FIX);
		this.scene.attachChild(ground);
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
		// TODO Auto-generated method stub

		Sprite sPlayer = new Sprite(CAMERA_WIDTH / 2, CAMERA_HEIGHT / 2,
				playerTexureRegion, this.mEngine.getVertexBufferObjectManager());
		sPlayer.setRotation(45.0f);
		final FixtureDef PLAYER_FIX = PhysicsFactory.createFixtureDef(10.0f,
				1.0f, 0.0f);
		Body body = PhysicsFactory.createCircleBody(physicsWorld, sPlayer,
				BodyType.DynamicBody, PLAYER_FIX);
		this.scene.attachChild(sPlayer);
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(sPlayer,
				body, true, false));

		pOnPopulateSceneCallback.onPopulateSceneFinished();
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