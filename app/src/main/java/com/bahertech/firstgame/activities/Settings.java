package com.bahertech.firstgame.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bahertech.firstgame.R;
import com.bahertech.firstgame.classes.SoundPlayer;
import com.bahertech.firstgame.interfaces.Constants;
import com.bahertech.firstgame.services.SoundService;
import com.bahertech.firstgame.utils.AppSharedPreferences;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.ads.rewarded.ServerSideVerificationOptions;
import com.tapadoo.alerter.Alerter;

import java.util.Locale;

import me.anwarshahriar.calligrapher.Calligrapher;

public class Settings extends AppCompatActivity {

    private RelativeLayout rlAppSound, rlBoxSound, rlSoundLevel, rlVibrate;
    private TextView appSoundText, boxSoundText, soundLevelText, vibrateText;
    private Switch switchAppSound, switchBoxSound, switchVibrate;
    private SeekBar sbSoundLevel;
    private ImageView ivSound;
    private AppSharedPreferences appSharedPreferences;
    private Boolean appSound;
    private Boolean boxSound;
    int progressV;
    float volume;
    private SoundPlayer soundPlayer;
    // Change Font
    private Calligrapher calligrapher;
    // Rewarded Video Ads
    private RewardedAd mRewardedAd;
    private final String TAG = "Settings";

    private boolean start_reward = false;

    int i = 0;

    @Override
    protected void onStart() {
        super.onStart();
        stopService(new Intent(Settings.this, SoundService.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Change Font
        // Initialize Calss
        calligrapher = new Calligrapher(this);
        soundPlayer = new SoundPlayer(this);

        // Initialize Calss Mobile Ads
//        MobileAds.initialize(this, Constants.ID_App);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        // Use an activity context to get the rewarded video instance.
        loadRewardedVideoAd();
        // View Ads
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(Constants.Settings_b1_5);
        adView = (AdView) findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.v(Constants.log + "ads", "onAdLoaded");
            }
            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                super.onAdFailedToLoad(adError);
                Log.v(Constants.log+"ads", "onAdFailedToLoad = "+adError.getCode());
//                new MainMenuActivity().onAdFailedToLoadErrorsCodes(Settings.this, adError.getCode());
            }
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Log.v(Constants.log+"ads", "onAdClosed");
            }
        });

        if (Locale.getDefault().getLanguage().equals("ar")) {
            calligrapher.setFont(this, "homaarabic.ttf", true);
        }else{
            calligrapher.setFont(this, "comicscarton.ttf", true);
        }

        rlAppSound = (RelativeLayout) findViewById(R.id.rl_app_sound);
        rlBoxSound = (RelativeLayout) findViewById(R.id.rl_box_sound);
        rlSoundLevel = (RelativeLayout) findViewById(R.id.rl_level);
        rlVibrate = (RelativeLayout) findViewById(R.id.rl_vibrate);
        //////////////////////////////////////////
        appSoundText = (TextView) findViewById(R.id.app_sound_text);
        boxSoundText = (TextView) findViewById(R.id.box_sound_text);
        soundLevelText = (TextView) findViewById(R.id.sound_level_text);
        vibrateText = (TextView) findViewById(R.id.vibrate_text);
        //////////////////////////////////////////////////////
        switchAppSound = (Switch) findViewById(R.id.switch_app_sound);
        switchBoxSound = (Switch) findViewById(R.id.switch_box_sound);
        switchVibrate = (Switch) findViewById(R.id.switch_vibrate);
        /////////////////////////////////////////////////////
        sbSoundLevel = (SeekBar) findViewById(R.id.sb_sound_level);
        ////////////////////////////////////////////////////
        ivSound = (ImageView) findViewById(R.id.iv_sound);

        // Inisialize Classes
        appSharedPreferences = new AppSharedPreferences(Settings.this);

        Boolean defaultSeekBar = appSharedPreferences.readBoolean(Constants.defaultSeekBar);
        if (defaultSeekBar){
            Log.v(Constants.log+"121", "defaultSeekBar = true");
            appSharedPreferences.writeInteger(Constants.soundLevel, 10);
            appSharedPreferences.writeBoolean(Constants.defaultSeekBar, false);
        }

        appSound = appSharedPreferences.readBoolean(Constants.appSound);
        boxSound = appSharedPreferences.readBoolean(Constants.boxSound);
        progressV = appSharedPreferences.readInteger(Constants.soundLevel);

        if (appSound){
            boxSoundText.setTextColor(Color.parseColor("#FFA80D"));
            soundLevelText.setTextColor(Color.parseColor("#FFA80D"));
            switchBoxSound.setEnabled(true);
            sbSoundLevel.setEnabled(true);
            ivSound.setColorFilter(ContextCompat.getColor(Settings.this,
                    R.color.colorAccent), PorterDuff.Mode.SRC_IN);

            switchBoxSound.setChecked(boxSound);
            if (progressV == 0){
                ivSound.setImageResource(R.drawable.ic_volume_off_black_24dp);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Log.v(Constants.log+"121", "sdk");
                    sbSoundLevel.setProgress(progressV, true);
                }else{
                    Log.v(Constants.log+"121", "notsdk");
                    sbSoundLevel.setProgress(progressV);
                }
            }else{
                ivSound.setImageResource(R.drawable.ic_volume_up_black_24dp);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Log.v(Constants.log+"121", "else sdk");
                    sbSoundLevel.setProgress(progressV, true);
                }else{
                    Log.v(Constants.log+"121", "else notsdk");
                    sbSoundLevel.setProgress(progressV);
                }
            }
        }else{
            boxSoundText.setTextColor(Color.parseColor("#939393"));
            soundLevelText.setTextColor(Color.parseColor("#939393"));
            switchBoxSound.setEnabled(false);
            sbSoundLevel.setEnabled(false);
            ivSound.setColorFilter(ContextCompat.getColor(Settings.this,
                    R.color.black_view), PorterDuff.Mode.SRC_IN);
        }
            switchAppSound.setChecked(appSound);
        switchAppSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v(Constants.log+"11", isChecked+"");
                appSharedPreferences.writeBoolean(Constants.appSound, isChecked);
                if (isChecked == true){
                    Log.v(Constants.log+"11", isChecked+"true");
                    boxSoundText.setTextColor(Color.parseColor("#FFA80D"));
                    soundLevelText.setTextColor(Color.parseColor("#FFA80D"));
                    switchBoxSound.setEnabled(true);
                    sbSoundLevel.setEnabled(true);
                    ivSound.setColorFilter(ContextCompat.getColor(Settings.this,
                            R.color.colorAccent), PorterDuff.Mode.SRC_IN);

                    switchBoxSound.setChecked(boxSound);
                    if (progressV == 0){
                        ivSound.setImageResource(R.drawable.ic_volume_off_black_24dp);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            Log.v(Constants.log+"121", "sdk");
                            sbSoundLevel.setProgress(progressV, true);
                        }else{
                            Log.v(Constants.log+"121", "notsdk");
                            sbSoundLevel.setProgress(progressV);
                        }
                    }else{
                        ivSound.setImageResource(R.drawable.ic_volume_up_black_24dp);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            Log.v(Constants.log+"121", "else sdk");
                            sbSoundLevel.setProgress(progressV, true);
                        }else{
                            Log.v(Constants.log+"121", "else notsdk");
                            sbSoundLevel.setProgress(progressV);
                        }
                    }
                }else{
                    Log.v(Constants.log+"11", isChecked+"false");
                    boxSoundText.setTextColor(Color.parseColor("#939393"));
                    soundLevelText.setTextColor(Color.parseColor("#939393"));
                    switchBoxSound.setEnabled(false);
                    sbSoundLevel.setEnabled(false);
                    ivSound.setColorFilter(ContextCompat.getColor(Settings.this,
                            R.color.black_view), PorterDuff.Mode.SRC_IN);
                }
            }
        });

        switchBoxSound.setChecked(boxSound);
        switchBoxSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                appSharedPreferences.writeBoolean(Constants.boxSound, isChecked);
            }
        });

        Log.v(Constants.log+"121", "Progress value = "+progressV);
        if (progressV == 0){
            ivSound.setImageResource(R.drawable.ic_volume_off_black_24dp);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Log.v(Constants.log+"121", "sdk");
                sbSoundLevel.setProgress(progressV, true);
            }else{
                Log.v(Constants.log+"121", "notsdk");
                sbSoundLevel.setProgress(progressV);
            }
        }else{
            ivSound.setImageResource(R.drawable.ic_volume_up_black_24dp);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Log.v(Constants.log+"121", "else sdk");
                sbSoundLevel.setProgress(progressV, true);
            }else{
                Log.v(Constants.log+"121", "else notsdk");
                sbSoundLevel.setProgress(progressV);
            }
        }
        sbSoundLevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                Log.v(Constants.log+"212", progress+"");
                switch (progress){
                    case 0 :
                        soundPlayer.volume(0.0f);
                        volume = 0.0f;
                        break;
                    case 1 :
                        soundPlayer.volume(0.1f);
                        volume = 0.1f;
                        break;
                    case 2 :
                        soundPlayer.volume(0.2f);
                        volume = 0.2f;
                        break;
                    case 3 :
                        soundPlayer.volume(0.3f);
                        volume = 0.3f;
                        break;
                    case 4 :
                        soundPlayer.volume(0.4f);
                        volume = 0.4f;
                        break;
                    case 5 :
                        soundPlayer.volume(0.5f);
                        volume = 0.5f;
                        break;
                    case 6 :
                        soundPlayer.volume(0.6f);
                        volume = 0.6f;
                        break;
                    case 7 :
                        soundPlayer.volume(0.7f);
                        volume = 0.7f;
                        break;
                    case 8 :
                        soundPlayer.volume(0.8f);
                        volume = 0.8f;
                        break;
                    case 9 :
                        soundPlayer.volume(0.9f);
                        volume = 0.9f;
                        break;
                    case 10 :
                        soundPlayer.volume(1.0f);
                        volume = 1.0f;
                        break;

                }
                progressV = progress;
                if (progressV > 0){
                    ivSound.setImageResource(R.drawable.ic_volume_up_black_24dp);
                }else{
                    ivSound.setImageResource(R.drawable.ic_volume_off_black_24dp);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                appSharedPreferences.writeInteger(Constants.soundLevel, progressV);
                appSharedPreferences.writeString(Constants.volumeLevel, volume+"");
                Log.v(Constants.log+"121", "Added Value = "+progressV);
                soundPlayer.playFireworksSound(2);
                soundPlayer.playJumpingSoundTest(volume);
            }
        });

        Boolean vibrate = appSharedPreferences.readBoolean(Constants.vibrate);
            switchVibrate.setChecked(vibrate);
        switchVibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                appSharedPreferences.writeBoolean(Constants.vibrate, isChecked);
            }
        });

        if(mRewardedAd != null)
            mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdClicked() {
                // Called when a click is recorded for an ad.
                Log.d(TAG, "Ad was clicked.");
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
                Log.d(TAG, "Ad dismissed fullscreen content.");
                mRewardedAd = null;
                if (start_reward) {
                    alerter(getString(R.string.fantastic), getString(R.string.about_alerter_message), Settings.this, 1);
                }else{
                    alerter(getString(R.string.good), getString(R.string.about_not_complete_alerter_message), Settings.this, 0);
                }
            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                // Called when ad fails to show.
                Log.e(TAG, "Ad failed to show fullscreen content.");
                mRewardedAd = null;
//                new MainMenuActivity().onAdFailedToLoadErrorsCodes(Settings.this, adError.getCode());
            }

            @Override
            public void onAdImpression() {
                // Called when an impression is recorded for an ad.
                Log.d(TAG, "Ad recorded an impression.");
                start_reward = true;
                loadRewardedVideoAd();
            }

            @Override
            public void onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d(TAG, "Ad showed fullscreen content.");
            }
        });

    }

    public void about_dialog(final Context context) {

        Typeface typeface, typeface1;
        final Dialog dialog1 = new Dialog(context);
        dialog1.show();
        dialog1.setContentView(R.layout.about_dialog);

        // with out background
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Initialize Calss Mobile Ads
//        MobileAds.initialize(context, Constants.ID_App);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        // View Ads
        AdView adView = new AdView(context);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(Constants.About_b1_9);
        adView = dialog1.findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.v(Constants.log + "ads", "onAdLoaded");
            }
            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                super.onAdFailedToLoad(adError);
                Log.v(Constants.log+"ads", "onAdFailedToLoad = "+adError.getCode());
//                new MainMenuActivity().onAdFailedToLoadErrorsCodes(Settings.this, adError.getCode());
            }
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Log.v(Constants.log+"ads", "onAdClosed");
            }
        });

        Button ok = dialog1.findViewById(R.id.okbtn);
        Button watch = dialog1.findViewById(R.id.watch);
        TextView aboutGame = dialog1.findViewById(R.id.about_game);
        TextView aboutContent = dialog1.findViewById(R.id.about_content);
        TextView viewRewardAd = dialog1.findViewById(R.id.view_reward_ad);
        View viewScroll = dialog1.findViewById(R.id.viewscroll);
        Animation topAnimViewScroll = AnimationUtils.loadAnimation(context, R.anim.slide_top_in);

        if (Locale.getDefault().getLanguage().equals("ar")) {
            typeface = Typeface.createFromAsset(context.getAssets(), "homaarabic.ttf");
        }else{
            typeface = Typeface.createFromAsset(context.getAssets(), "comicscarton.ttf");
        }

        ok.setTypeface(typeface);
        watch.setTypeface(typeface);
        aboutGame.setTypeface(typeface);
        viewRewardAd.setTypeface(typeface);

        typeface1 = Typeface.createFromAsset(context.getAssets(), "homaarabic.ttf");
        aboutContent.setTypeface(typeface1);

        AppSharedPreferences appSharedPreferences1 = new AppSharedPreferences(context);
        int hideViewScroll = appSharedPreferences1.readInteger(Constants.hideViewScroll);
        if (hideViewScroll == 3) {
            viewScroll.setVisibility(View.GONE);
        }else{
            viewScroll.setVisibility(View.VISIBLE);
            viewScroll.startAnimation(topAnimViewScroll);
            hideViewScroll++;
            appSharedPreferences1.writeInteger(Constants.hideViewScroll, hideViewScroll);
        }
        dialog1.setCancelable(false);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });

        watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRewardedAd != null) {
                    Activity activityContext = Settings.this;
                    mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            // Handle the reward.
                            Log.d(TAG, "The user earned the reward.");
                            Log.v(Constants.log + "232", "Main Menu Activity mRewardedVideoAd.isLoaded()");
                            int rewardAmount = rewardItem.getAmount();
                            String rewardType = rewardItem.getType();
                        }
                    });
                } else {
                    Log.d(TAG, "The rewarded ad wasn't ready yet.");
                }
            }
        });

    }

    // TODO //////////////////// loadRewardedVideoAd ////////////////
    private void loadRewardedVideoAd() {

        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, Constants.watch_Btn_RewardAd_b3_1,
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d(TAG, loadAdError.toString());

                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        ServerSideVerificationOptions options = new ServerSideVerificationOptions
                                .Builder()
                                .setCustomData("SAMPLE_CUSTOM_DATA_STRING")
                                .build();
                        mRewardedAd.setServerSideVerificationOptions(options);
                        Log.d(TAG, "Ad was loaded.");
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu2, menu);

        MenuItem item1 = menu.findItem(R.id.aboutItem4);
        SpannableString s1 = new SpannableString(getString(R.string.about_game));
        s1.setSpan(new ForegroundColorSpan(Color.parseColor("#f1b902")), 0, s1.length(), 0);
        item1.setTitle(s1);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.shareItem1:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        getString(R.string.sharemessage)+" "+"\n\n: "+Constants.URLGooglePlay);
                sendIntent.setType("text/plain");
//                sendIntent.setPackage("com.facebook.orca");
                try {
                    startActivity(sendIntent);
                }
                catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(Settings.this,getString(R.string.installmessenger), Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.ratingItem2 :
                new MainMenuActivity().rate_dialog(Settings.this);
                break;
            case R.id.aboutItem4 :
                about_dialog(Settings.this);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void alerter(String title, String message, Context context, int soundPlay) {
        if (soundPlay == 1) {
            soundPlayer.playLevelUpSound();
        }
        Alerter.create((Activity) context)
                .setTitle(title)
                .setTitleAppearance(R.style.AlertTextAppearance_Title)
                .setTextAppearance(R.style.AlertTextAppearance_Text)
                .setText(message)
                .setDuration(8000)
                .enableSwipeToDismiss()
                .setBackgroundColorInt(getResources().getColor(R.color.pink))
                .setIcon(getResources().getDrawable(R.drawable.alerter_ic_notifications))
                .setIconColorFilter(getResources().getColor(R.color.white))
                .enableProgress(true)
                .setProgressColorRes(R.color.blue)
                .show();
    }
}
