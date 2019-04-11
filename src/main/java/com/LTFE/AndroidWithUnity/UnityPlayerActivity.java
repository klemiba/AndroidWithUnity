package com.LTFE.AndroidWithUnity;

import android.app.ActionBar;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import com.unity3d.player.UnityPlayer;

public class UnityPlayerActivity extends AppCompatActivity
{
    protected UnityPlayer mUnityPlayer; // don't change the name of this variable; referenced from native code
    String TAG = "UnityPlayerActivity";

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getBaseContext(), MainActivity.class);
        startActivity(i);
    }

    // Setup activity layout
    @Override protected void onCreate(Bundle savedInstanceState)
    {
        //git test
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        mUnityPlayer = new UnityPlayerWrapper((ContextWrapper) getApplicationContext());
        setContentView(R.layout.unity_activity_layout);
        FrameLayout frameLayout = (FrameLayout)findViewById(R.id.unity_activity_player);
        frameLayout.addView(mUnityPlayer.getView());
        mUnityPlayer.requestFocus();


        getWindow().takeSurface(null);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final ActionBar actionBar = getActionBar();

        if(actionBar != null) {
            actionBar.show();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        Button cube = findViewById(R.id.cube);
        Button sphere = findViewById(R.id.sphere);
        Button capsule = findViewById(R.id.capsule);

        cube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnityPlayer.UnitySendMessage("Main Camera", "switchObject", "cube");
            }
        });
        sphere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnityPlayer.UnitySendMessage("Main Camera", "switchObject", "sphere");
            }
        });
        capsule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UnityPlayer.UnitySendMessage("Main Camera", "switchObject", "capsule");

            }
        });
    }


    @Override protected void onNewIntent(Intent intent)
    {
        // To support deep linking, we need to make sure that the client can get access to
        // the last sent intent. The clients access this through a JNI api that allows them
        // to get the intent set on launch. To update that after launch we have to manually
        // replace the intent with the one caught here.
        setIntent(intent);
    }

    // Quit Unity
    @Override protected void onDestroy ()
    {
        Log.d(TAG, "onDestroy: Hello");
        //mUnityPlayer.quit();
        mUnityPlayer.pause();
        //mUnityPlayer.quit();
        super.onDestroy();
    }

    // Pause Unity
    @Override protected void onPause()
    {
        super.onPause();
        mUnityPlayer.pause();
    }

    // Resume Unity
    @Override protected void onResume()
    {
        Log.d(TAG, "onResume: Resuming");
        super.onResume();
        mUnityPlayer.resume();
    }

    @Override protected void onStart()
    {
        Log.d(TAG, "onStart: Trigering");
        if( mUnityPlayer.isActivated()){
            onResume();
            Log.d(TAG, "onStart: Resuming instead of starting");
        }else{
            Log.d(TAG, "onStart: Starting");
            super.onStart();
            mUnityPlayer.start();
        }
    }

    @Override protected void onStop()
    {
        super.onStop();
        mUnityPlayer.stop();
    }

    // Low Memory Unity
    @Override public void onLowMemory()
    {
        super.onLowMemory();
        mUnityPlayer.lowMemory();
    }

    // Trim Memory Unity
    @Override public void onTrimMemory(int level)
    {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_RUNNING_CRITICAL)
        {
            mUnityPlayer.lowMemory();
        }
    }

    // This ensures the layout will be correct.
    @Override public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        mUnityPlayer.configurationChanged(newConfig);
    }

    // Notify Unity of the focus change.
    @Override public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        mUnityPlayer.windowFocusChanged(hasFocus);
    }

    // For some reason the multiple keyevent type is not supported by the ndk.
    // Force event injection by overriding dispatchKeyEvent().
    @Override public boolean dispatchKeyEvent(KeyEvent event)
    {
        if (event.getAction() == KeyEvent.ACTION_MULTIPLE)
            return mUnityPlayer.injectEvent(event);
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Pass any events not handled by (unfocused) views straight to UnityPlayer
    @Override public boolean onKeyUp(int keyCode, KeyEvent event)     {
        Log.d(TAG, "Code: "+keyCode);
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            Intent i = new Intent(getBaseContext(), MainActivity.class);
            startActivity(i);
            return true; //handled
        }
        return mUnityPlayer.injectEvent(event);
    }
    @Override public boolean onKeyDown(int keyCode, KeyEvent event)   {
        return mUnityPlayer.injectEvent(event); 
    }
    @Override public boolean onTouchEvent(MotionEvent event){
        return mUnityPlayer.injectEvent(event); }
    /*API12*/ public boolean onGenericMotionEvent(MotionEvent event)  { return mUnityPlayer.injectEvent(event); }

}
