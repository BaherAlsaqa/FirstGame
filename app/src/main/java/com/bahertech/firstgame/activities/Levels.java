package com.bahertech.firstgame.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bahertech.firstgame.R;
import com.bahertech.firstgame.classes.SoundPlayer;
import com.bahertech.firstgame.interfaces.Constants;
import com.bahertech.firstgame.services.SoundService;
import com.bahertech.firstgame.utils.AppSharedPreferences;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.tapadoo.alerter.Alerter;

import java.util.Locale;

import me.anwarshahriar.calligrapher.Calligrapher;

public class Levels extends AppCompatActivity implements RewardedVideoAdListener {

    //Sound Player
    private SoundPlayer soundPlayer;
    private Button[] btnlevels;
    private AppSharedPreferences appSharedPreferences;
    // Change Font
    private Calligrapher calligrapher;

    private Boolean appSound;
    // Rewarded Video Ads
    private RewardedVideoAd mRewardedVideoAd;

    private boolean start_reward = false;

    int i = 0;

    @Override
    protected void onStart() {
        super.onStart();

        //Start Sound Service
        startService(new Intent(Levels.this, SoundService.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);

        // Change Font
        // Initialize Calss
        calligrapher = new Calligrapher(this);
        // Initialize Calss Mobile Ads
//        MobileAds.initialize(this, Constants.ID_App);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        // Use an activity context to get the rewarded video instance.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(Levels.this);
        mRewardedVideoAd.setRewardedVideoAdListener(Levels.this);
        loadRewardedVideoAd();
        // View Ads
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(Constants.Levels_b1_2);
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
                Log.v(Constants.log+"ads", "onAdFailedToLoad = "+adError.getMessage());
                new MainMenuActivity().onAdFailedToLoadErrorsCodes(Levels.this, adError.getCode());
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

        Intent intent = getIntent();
        String activityKey = intent.getStringExtra(Constants.activitykey);
        if (activityKey.equals(Constants.pinkoin)) {
            // Initialize Class Sound Player
            soundPlayer = new SoundPlayer(Levels.this);
            btnlevels = new Button[12];
            appSharedPreferences = new AppSharedPreferences(Levels.this);

            appSound = appSharedPreferences.readBoolean(Constants.appSound);


            int privateLevelValue = appSharedPreferences.readInteger(Constants.privateLevelValue);

            for (int i = 0; i < btnlevels.length; i++) {
                String btnLevelId = "level" + (i + 1);
                int resID = getResources().getIdentifier(btnLevelId, "id", getPackageName());
                btnlevels[i] = (Button) findViewById(resID);
            }

            switch (privateLevelValue){
                case 0 :
                    btnlevels[0].setEnabled(true);
                    btnlevels[1].setEnabled(false);
                    btnlevels[2].setEnabled(false);
                    btnlevels[3].setEnabled(false);
                    btnlevels[4].setEnabled(false);
                    btnlevels[5].setEnabled(false);
                    btnlevels[6].setEnabled(false);
                    btnlevels[7].setEnabled(false);
                    btnlevels[8].setEnabled(false);
                    btnlevels[9].setEnabled(false);
                    btnlevels[10].setEnabled(false);
                    btnlevels[11].setEnabled(false);
                    break;
                case 1 :
                    btnlevels[0].setEnabled(true);
                    btnlevels[1].setEnabled(true);
                    btnlevels[2].setEnabled(false);
                    btnlevels[3].setEnabled(false);
                    btnlevels[4].setEnabled(false);
                    btnlevels[5].setEnabled(false);
                    btnlevels[6].setEnabled(false);
                    btnlevels[7].setEnabled(false);
                    btnlevels[8].setEnabled(false);
                    btnlevels[9].setEnabled(false);
                    btnlevels[10].setEnabled(false);
                    btnlevels[11].setEnabled(false);
                    break;
                case 2 :
                    btnlevels[0].setEnabled(true);
                    btnlevels[1].setEnabled(true);
                    btnlevels[2].setEnabled(true);
                    btnlevels[3].setEnabled(false);
                    btnlevels[4].setEnabled(false);
                    btnlevels[5].setEnabled(false);
                    btnlevels[6].setEnabled(false);
                    btnlevels[7].setEnabled(false);
                    btnlevels[8].setEnabled(false);
                    btnlevels[9].setEnabled(false);
                    btnlevels[10].setEnabled(false);
                    btnlevels[11].setEnabled(false);
                    break;
                case 3 :
                    btnlevels[0].setEnabled(true);
                    btnlevels[1].setEnabled(true);
                    btnlevels[2].setEnabled(true);
                    btnlevels[3].setEnabled(true);
                    btnlevels[4].setEnabled(false);
                    btnlevels[5].setEnabled(false);
                    btnlevels[6].setEnabled(false);
                    btnlevels[7].setEnabled(false);
                    btnlevels[8].setEnabled(false);
                    btnlevels[9].setEnabled(false);
                    btnlevels[10].setEnabled(false);
                    btnlevels[11].setEnabled(false);
                    break;
                case 4 :
                    btnlevels[0].setEnabled(true);
                    btnlevels[1].setEnabled(true);
                    btnlevels[2].setEnabled(true);
                    btnlevels[3].setEnabled(true);
                    btnlevels[4].setEnabled(true);
                    btnlevels[5].setEnabled(false);
                    btnlevels[6].setEnabled(false);
                    btnlevels[7].setEnabled(false);
                    btnlevels[8].setEnabled(false);
                    btnlevels[9].setEnabled(false);
                    btnlevels[10].setEnabled(false);
                    btnlevels[11].setEnabled(false);
                    break;
                case 5 :
                    btnlevels[0].setEnabled(true);
                    btnlevels[1].setEnabled(true);
                    btnlevels[2].setEnabled(true);
                    btnlevels[3].setEnabled(true);
                    btnlevels[4].setEnabled(true);
                    btnlevels[5].setEnabled(true);
                    btnlevels[6].setEnabled(false);
                    btnlevels[7].setEnabled(false);
                    btnlevels[8].setEnabled(false);
                    btnlevels[9].setEnabled(false);
                    btnlevels[10].setEnabled(false);
                    btnlevels[11].setEnabled(false);
                    break;
                case 6 :
                    btnlevels[0].setEnabled(true);
                    btnlevels[1].setEnabled(true);
                    btnlevels[2].setEnabled(true);
                    btnlevels[3].setEnabled(true);
                    btnlevels[4].setEnabled(true);
                    btnlevels[5].setEnabled(true);
                    btnlevels[6].setEnabled(true);
                    btnlevels[7].setEnabled(false);
                    btnlevels[8].setEnabled(false);
                    btnlevels[9].setEnabled(false);
                    btnlevels[10].setEnabled(false);
                    btnlevels[11].setEnabled(false);
                    break;
                case 7 :
                    btnlevels[0].setEnabled(true);
                    btnlevels[1].setEnabled(true);
                    btnlevels[2].setEnabled(true);
                    btnlevels[3].setEnabled(true);
                    btnlevels[4].setEnabled(true);
                    btnlevels[5].setEnabled(true);
                    btnlevels[6].setEnabled(true);
                    btnlevels[7].setEnabled(true);
                    btnlevels[8].setEnabled(false);
                    btnlevels[9].setEnabled(false);
                    btnlevels[10].setEnabled(false);
                    btnlevels[11].setEnabled(false);
                    break;
                case 8 :
                    btnlevels[0].setEnabled(true);
                    btnlevels[1].setEnabled(true);
                    btnlevels[2].setEnabled(true);
                    btnlevels[3].setEnabled(true);
                    btnlevels[4].setEnabled(true);
                    btnlevels[5].setEnabled(true);
                    btnlevels[6].setEnabled(true);
                    btnlevels[7].setEnabled(true);
                    btnlevels[8].setEnabled(true);
                    btnlevels[9].setEnabled(false);
                    btnlevels[10].setEnabled(false);
                    btnlevels[11].setEnabled(false);
                    break;
                case 9 :
                    btnlevels[0].setEnabled(true);
                    btnlevels[1].setEnabled(true);
                    btnlevels[2].setEnabled(true);
                    btnlevels[3].setEnabled(true);
                    btnlevels[4].setEnabled(true);
                    btnlevels[5].setEnabled(true);
                    btnlevels[6].setEnabled(true);
                    btnlevels[7].setEnabled(true);
                    btnlevels[8].setEnabled(true);
                    btnlevels[9].setEnabled(true);
                    btnlevels[10].setEnabled(false);
                    btnlevels[11].setEnabled(false);
                    break;
                case 10 :
                    btnlevels[0].setEnabled(true);
                    btnlevels[1].setEnabled(true);
                    btnlevels[2].setEnabled(true);
                    btnlevels[3].setEnabled(true);
                    btnlevels[4].setEnabled(true);
                    btnlevels[5].setEnabled(true);
                    btnlevels[6].setEnabled(true);
                    btnlevels[7].setEnabled(true);
                    btnlevels[8].setEnabled(true);
                    btnlevels[9].setEnabled(true);
                    btnlevels[10].setEnabled(true);
                    btnlevels[11].setEnabled(false);
                    break;
                case 11 :
                    btnlevels[0].setEnabled(true);
                    btnlevels[1].setEnabled(true);
                    btnlevels[2].setEnabled(true);
                    btnlevels[3].setEnabled(true);
                    btnlevels[4].setEnabled(true);
                    btnlevels[5].setEnabled(true);
                    btnlevels[6].setEnabled(true);
                    btnlevels[7].setEnabled(true);
                    btnlevels[8].setEnabled(true);
                    btnlevels[9].setEnabled(true);
                    btnlevels[10].setEnabled(true);
                    btnlevels[11].setEnabled(true);
                    break;
                case 12 :
                    btnlevels[0].setEnabled(true);
                    btnlevels[1].setEnabled(true);
                    btnlevels[2].setEnabled(true);
                    btnlevels[3].setEnabled(true);
                    btnlevels[4].setEnabled(true);
                    btnlevels[5].setEnabled(true);
                    btnlevels[6].setEnabled(true);
                    btnlevels[7].setEnabled(true);
                    btnlevels[8].setEnabled(true);
                    btnlevels[9].setEnabled(true);
                    btnlevels[10].setEnabled(true);
                    btnlevels[11].setEnabled(true);
                    break;
            }

            for (int x = 0; x < btnlevels.length; x++) {
                final int finalX = x;
                btnlevels[finalX].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.v(Constants.log+"3", "clicked");
                        if (appSound) {
                            soundPlayer.playClickSound();
                        }
                        String levelValue = btnlevels[finalX].getText().toString();
                        startActivity(new Intent(Levels.this, Start.class).putExtra(Constants.levelValue, levelValue));
                    }
                });
            }

        }

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
//                Log.v(Constants.log+"ads", "onAdFailedToLoad = "+i);
//                new MainMenuActivity().onAdFailedToLoadErrorsCodes(Levels.this, adError.getCode());
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
                if (mRewardedVideoAd.isLoaded()) {
                    Log.v(Constants.log + "232", "Main Menu Activity mRewardedVideoAd.isLoaded()");
                    mRewardedVideoAd.show();
                }
            }
        });

    }

    // TODO //////////////////// loadRewardedVideoAd ////////////////
    private void loadRewardedVideoAd() {

        mRewardedVideoAd.loadAd(Constants.watch_Btn_RewardAd_b3_1,
                new AdRequest.Builder()
                        .build());
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
                    Toast.makeText(Levels.this,getString(R.string.installmessenger), Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.ratingItem2 :
                new MainMenuActivity().rate_dialog(Levels.this);
                break;
            case R.id.aboutItem4 :
                about_dialog(Levels.this);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Levels.this, MainMenuActivity.class));
    }

    @Override
    public void onResume() {
        mRewardedVideoAd.resume(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        mRewardedVideoAd.pause(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mRewardedVideoAd.destroy(this);
        super.onDestroy();
    }

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        loadRewardedVideoAd();
        if (start_reward == true) {
            alerter(getString(R.string.fantastic), getString(R.string.about_alerter_message), Levels.this, 1);
        }else{
            alerter(getString(R.string.good), getString(R.string.about_not_complete_alerter_message), Levels.this, 0);
        }
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        start_reward = true;
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

        new MainMenuActivity().onAdFailedToLoadErrorsCodes(Levels.this, i);

    }

    @Override
    public void onRewardedVideoCompleted() {

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
