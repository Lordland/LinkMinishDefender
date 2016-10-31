package com.example.linkminishdefender;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.opengl.CCGLSurfaceView;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

	protected CCGLSurfaceView vista;
	private CCScene scene;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		vista = new CCGLSurfaceView(this);
		setContentView(vista);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onStart() {
		super.onStart();

		// Director
		CCDirector.sharedDirector().attachInView(vista);
		CCDirector.sharedDirector().setDeviceOrientation(
				CCDirector.kCCDeviceOrientationPortrait);

		scene = CapaMenu.scene();
		CCDirector.sharedDirector().runWithScene(scene);
	}

	@Override
	public void onPause() {
		super.onPause();
		CCDirector.sharedDirector().pause();
		CapaJuego.pausarMusica();
	}

	@Override
	public void onResume() {
		super.onResume();
		CCDirector.sharedDirector().resume();
		CapaJuego.iniciarMusica();
	}

	@Override
	public void onStop() {
		super.onStop();
		CCDirector.sharedDirector().end();
	}


}
