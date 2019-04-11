package com.LTFE.AndroidWithUnity;

import android.content.ContextWrapper;
import android.util.Log;

import com.unity3d.player.UnityPlayer;

import static android.content.ContentValues.TAG;

public class UnityPlayerWrapper extends UnityPlayer {

    public UnityPlayerWrapper(final ContextWrapper contextWrapper) {
        super(contextWrapper);
    }

    @Override
    protected void kill() {
        Log.d(TAG, "kill: killing it");
            //Do nothing
    }
    @Override
    public void stop(){
        Log.d(TAG, "Stopping it!");
    }

}