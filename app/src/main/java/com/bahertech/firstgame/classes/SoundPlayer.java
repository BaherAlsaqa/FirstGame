package com.bahertech.firstgame.classes;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

import com.bahertech.firstgame.R;
import com.bahertech.firstgame.interfaces.Constants;
import com.bahertech.firstgame.utils.AppSharedPreferences;

public class SoundPlayer {

    private AudioAttributes audioAttributes;
    private static final int SOUND_POOL_MAX = 2;
    private static SoundPool soundPool;
    private static int normal_coin_sound;
    private static int normal2_coin_sound;
    private static int pink_coin_sound;
    private static int bomb_sound;
    private static int click_sound;
    private static int jumping_sound;
    private static int levelup_sound;
    private static int highscoreup_sound;
    private static int fireworks_sound;
    private static int fireworks_sound1;
    private String volume;
    private float volumeF;
    AppSharedPreferences appSharedPreferences;

    public SoundPlayer(Context context){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(SOUND_POOL_MAX)
                    .build();
        }

        try {
            normal_coin_sound = soundPool.load(context, R.raw.normal_coin, 1);
            normal2_coin_sound = soundPool.load(context, R.raw.normal_coin2, 1);
            pink_coin_sound = soundPool.load(context, R.raw.pink_coin, 1);
            bomb_sound = soundPool.load(context, R.raw.bomb, 1);
            click_sound = soundPool.load(context, R.raw.click, 1);
            jumping_sound = soundPool.load(context, R.raw.jumping, 1);
            levelup_sound = soundPool.load(context, R.raw.level_up_one, 1);
            highscoreup_sound = soundPool.load(context, R.raw.high_score_up, 1);
            fireworks_sound = soundPool.load(context, R.raw.fireworks, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Initialize Classes
        appSharedPreferences = new AppSharedPreferences(context);

        Boolean defaultVolume = appSharedPreferences.readBoolean(Constants.defaultVolume);
        if (defaultVolume){
            Log.v(Constants.log+"121", "defaultVolume = true");
            appSharedPreferences.writeString(Constants.volumeLevel, "1.0");
            appSharedPreferences.writeBoolean(Constants.defaultVolume, false);
        }

        volume = appSharedPreferences.readString(Constants.volumeLevel);
        try {
            volumeF = Float.parseFloat(volume);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public void playNormalCoinSound(){
        //play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
        try {
            soundPool.play(normal_coin_sound, volumeF, volumeF, 1, 0, 1.0f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playNormal2CoinSound(){
        //play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
        try {
            soundPool.play(normal2_coin_sound, volumeF, volumeF, 1, 0, 1.0f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pinkCoinSound(){
        //play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
        try {
            soundPool.play(pink_coin_sound, volumeF, volumeF, 1, 0, 1.0f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void BombSound(){
        //play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
        try {
            soundPool.play(bomb_sound, volumeF, volumeF, 1, 0, 1.0f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playClickSound(){
        //play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
        try {
            soundPool.play(click_sound, volumeF, volumeF, 1, 0, 1.0f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playJumpingSound(){
        //play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
        try {
            soundPool.play(jumping_sound, volumeF, volumeF, 1, 0, 1.0f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playJumpingSoundTest(float volume){
        //play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
        try {
            soundPool.play(jumping_sound, volume, volume, 1, 0, 1.0f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playLevelUpSound(){
        //play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
        try {
            soundPool.play(levelup_sound, volumeF, volumeF, 1, 0, 1.0f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playHighScoreUpSound(){
        //play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
        try {
            soundPool.play(highscoreup_sound, volumeF, volumeF, 1, 0, 1.0f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playFireworksSound(int stop){
        //play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
        try {
            if (stop == 1) {
                soundPool.play(fireworks_sound, volumeF, volumeF, 1, 10, 1.0f);
            }else{
                soundPool.autoPause();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void volume(float volume){
        soundPool.setVolume(fireworks_sound, volume, volume);
    }

    /*public void stopFireworksSound(){
        Log.v(Constants.log+"5", "stopFireworksSound");
        //play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
        int streamID = soundPool.play(fireworks_sound1, 1.0f, 1.0f, 2, 0, 1f);
        soundPool.stop(streamID);
    }*/
}
