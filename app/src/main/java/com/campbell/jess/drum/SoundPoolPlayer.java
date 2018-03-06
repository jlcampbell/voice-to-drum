package com.campbell.jess.drum;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Jess on 2/16/18.
 */

public class SoundPoolPlayer extends MainActivity {

    private final int MAX_STREAM = 10;
    private boolean soundLoaded = false;

    private int kickSoundId;
    private int snareSoundId;

    private SoundPool mSoundPool;
    private static final String TAG = "Soundpool player";

    public void StartSoundPoolPlayer(Context context){
        Log.v(TAG, "onCreate Soundpool player");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mSoundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            new SoundPool.Builder().setAudioAttributes(attributes).setMaxStreams(10).build();
        } else {

            mSoundPool = new android.media.SoundPool(MAX_STREAM, AudioManager.STREAM_MUSIC, 0);
        }
        Log.v(TAG, mSoundPool.toString());
        kickSoundId = mSoundPool.load(context, R.raw.kick2, 1);
        snareSoundId = mSoundPool.load(context, R.raw.snare, 2);

        mSoundPool.setOnLoadCompleteListener(new android.media.SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(android.media.SoundPool soundPool, int soundId, int status) {
                soundLoaded = true;

            }
        });

    }

    public void playSound(String soundName, float playbackRate) {
        if(soundLoaded){
            Log.v(TAG, "sound loaded");
            int soundId = 0;
            switch (soundName){
                case "kick":
                    Log.v(TAG, "kick");
                    soundId = kickSoundId;
                    break;
                case "snare":
                    Log.v(TAG, "snare");
                    soundId = snareSoundId;
                    break;
            }

            float leftVolume = 1;
            float rightVolume = 1;
            //loop forever
            int loop = 0;
            int streamId = mSoundPool.play(soundId, leftVolume, rightVolume, 1, loop, playbackRate);
        }
        else {
            Log.v(TAG, "sound didn't load");
        }
    }
    public void pause(){
        mSoundPool.autoPause();
    }
    public void resumePlay(){
        mSoundPool.autoResume();
    }
}
