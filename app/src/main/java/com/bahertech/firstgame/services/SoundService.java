package com.bahertech.firstgame.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import com.bahertech.firstgame.R;
import com.bahertech.firstgame.interfaces.Constants;
import com.bahertech.firstgame.utils.AppSharedPreferences;

public class SoundService extends Service {
    MediaPlayer player;
    AppSharedPreferences appSharedPreferences;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        player = MediaPlayer.create(this, R.raw.monkey_island_band); //select music file
        player.setLooping(true); //set looping
        // Initialize Class
        appSharedPreferences = new AppSharedPreferences(this);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(Constants.log+"s", "start service");
        Boolean appSound = appSharedPreferences.readBoolean(Constants.appSound);
        String volume = appSharedPreferences.readString(Constants.volumeLevel);
        float volumeF = Float.parseFloat(volume);
        if (appSound) {
            player.setVolume(volumeF, volumeF);
            player.start();
        }
        String serviceKey = appSharedPreferences.readString(Constants.serviceKey);
        if (serviceKey.equals("pause")){
            if (appSound) {
                player.pause();
            }
            appSharedPreferences.writeString(Constants.serviceKey, "0");
        }else if (serviceKey.equals("start")){
            if (appSound) {
                player.setVolume(volumeF, volumeF);
                player.start();
            }
            appSharedPreferences.writeString(Constants.serviceKey, "0");
        }
        return Service.START_NOT_STICKY;
    }

    public void onDestroy() {
        Boolean appSound = appSharedPreferences.readBoolean(Constants.appSound);
        if (appSound) {
            player.stop();
            player.release();
        }
        stopSelf();
        super.onDestroy();
    }

}
