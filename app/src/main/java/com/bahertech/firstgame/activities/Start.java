package com.bahertech.firstgame.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bahertech.firstgame.R;
import com.bahertech.firstgame.classes.SoundPlayer;
import com.bahertech.firstgame.interfaces.Constants;
import com.bahertech.firstgame.services.SoundService;
import com.bahertech.firstgame.utils.AppSharedPreferences;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
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

public class Start extends AppCompatActivity implements RewardedVideoAdListener {

    private Button startBtn;
    private TextView challenge_yourself_note;
    private String levelValue;
    private String challengeYourself;
    private SoundPlayer soundPlayer;
    private AppSharedPreferences appSharedPreferences;
    // Rewarded Video Ads
    private RewardedVideoAd mRewardedVideoAd;

    //Change Font
    private Calligrapher calligrapher;

    private Boolean appSound;
    private boolean start_reward = false;

    private InterstitialAd mInterstitialAd;

    private ImageView loadingSpinner;
    // Runnable
    private Runnable runnable;
    private Handler handler;

    private boolean killMe = true;

    @Override
    protected void onStart() {
        super.onStart();

        //Start Sound Service
        startService(new Intent(Start.this, SoundService.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // Change Font
        // Initialize Calss
        calligrapher = new Calligrapher(this);

        challenge_yourself_note = (TextView) findViewById(R.id.challenge_yourself_note);
        startBtn = (Button) findViewById(R.id.startbtn);
        loadingSpinner = (ImageView) findViewById(R.id.loading_spinner);

        // Initialize Calss Mobile Ads
//        MobileAds.initialize(this, Constants.ID_App);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        // Use an activity context to get the rewarded video instance.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(Start.this);
        mRewardedVideoAd.setRewardedVideoAdListener(Start.this);
        loadRewardedVideoAd();

        // Interstitial Ads
        mInterstitialAd = new InterstitialAd(Start.this);
        mInterstitialAd.setAdUnitId(Constants.StartGame_b2_1);
        mInterstitialAd.loadAd(new AdRequest.Builder()
                .build());

        //TODO //////////////loading /////////////////////////

        startBtn.setText("");
        startBtn.setEnabled(false);
        loadingSpinner.setVisibility(View.VISIBLE);

        handler = new Handler();

        runnable = new Runnable() {

            public void run() {
                if (killMe) {

                    if (mInterstitialAd.isLoaded()) {
                        killMe = false;
                        startBtn.setText(getString(R.string.start));
                        startBtn.setEnabled(true);
                        loadingSpinner.setVisibility(View.GONE);

                        loadAdView();
                    }
                    handler.postDelayed(this, 500);
                } else {
                    handler.removeCallbacks(runnable);
                }

                Log.v(Constants.log + "211", "Runnable is Running");
            }
        };
        handler.postDelayed(runnable, 500);

        if (Locale.getDefault().getLanguage().equals("ar")) {
            calligrapher.setFont(this, "homaarabic.ttf", true);
        } else {
            calligrapher.setFont(this, "comicscarton.ttf", true);
        }

        //Initialize Calsses
        soundPlayer = new SoundPlayer(Start.this);
        appSharedPreferences = new AppSharedPreferences(Start.this);

        appSound = appSharedPreferences.readBoolean(Constants.appSound);

        Intent intent = getIntent();
        levelValue = intent.getStringExtra(Constants.levelValue);
        challengeYourself = intent.getStringExtra(Constants.gameKey);

        if (!levelValue.equals("")) {

            // Change Title
            switch (Integer.parseInt(levelValue)) {
                case 1:
                    getSupportActionBar().setTitle(R.string.levelOne);
                    break;
                case 2:
                    getSupportActionBar().setTitle(R.string.levelTwo);
                    break;
                case 3:
                    getSupportActionBar().setTitle(R.string.levelThree);
                    break;
                case 4:
                    getSupportActionBar().setTitle(R.string.levelFour);
                    break;
                case 5:
                    getSupportActionBar().setTitle(R.string.levelFive);
                    break;
                case 6:
                    getSupportActionBar().setTitle(R.string.levelSix);
                    break;
                case 7:
                    getSupportActionBar().setTitle(R.string.levelSeven);
                    break;
                case 8:
                    getSupportActionBar().setTitle(R.string.levelEight);
                    break;
                case 9:
                    getSupportActionBar().setTitle(R.string.levelNine);
                    break;
                case 10:
                    getSupportActionBar().setTitle(R.string.levelTen);
                    break;
                case 11:
                    getSupportActionBar().setTitle(R.string.levelEleven);
                    break;
                case 12:
                    getSupportActionBar().setTitle(R.string.levelTwelve);
                    break;
            }

            challenge_yourself_note.setVisibility(View.GONE);

            startBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    appSharedPreferences.writeInteger(Constants.levelValue, Integer.parseInt(levelValue));
                    if (appSound) {
                        soundPlayer.playClickSound();
                    }

                    // Show Interstitial Ads
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                        Log.d(Constants.log + "131", "mInterstitialAd.show.");
                    } else {
                        Log.d(Constants.log + "131", "The interstitial wasn't loaded yet.");
                    }

                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdLoaded() {
                            super.onAdLoaded();
                            Log.d(Constants.log + "131", "onAdLoaded");
                        }

                        @Override
                        public void onAdFailedToLoad(LoadAdError adError) {
                            super.onAdFailedToLoad(adError);
                            Log.d(Constants.log + "131", "onAdFailedToLoad = "+adError.getCode());
                            new MainMenuActivity().onAdFailedToLoadErrorsCodes(Start.this, adError.getCode());
                        }

                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
                            Log.d(Constants.log + "131", "onAdClosed");
                            startActivity(new Intent(Start.this, MainActivity.class)
                                    .putExtra(Constants.levelValue, levelValue)
                                    .putExtra(Constants.activitykey, Constants.activitykey));
                        }
                    });
                }
            });

        } else if (challengeYourself.equals(Constants.challengeYourselfValue)) {

            challenge_yourself_note.setVisibility(View.VISIBLE);

            startBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (appSound) {
                        soundPlayer.playClickSound();
                    }

                    // Show Interstitial Ads
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                        Log.d(Constants.log + "131", "mInterstitialAd.show.");
                    } else {
                        Log.d(Constants.log + "131", "The interstitial wasn't loaded yet.");
                    }

                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdLoaded() {
                            super.onAdLoaded();
                            Log.d(Constants.log + "131", "onAdLoaded");
                        }

                        @Override
                        public void onAdFailedToLoad(LoadAdError adError) {
                            super.onAdFailedToLoad(adError);
                            Log.d(Constants.log + "131", "onAdFailedToLoad = "+adError.getCode());
                            new MainMenuActivity().onAdFailedToLoadErrorsCodes(Start.this, adError.getCode());
                        }

                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
                            Log.d(Constants.log + "131", "onAdClosed");
                            startActivity(new Intent(Start.this, MainActivity.class)
                                    .putExtra(Constants.activitykey, Constants.activitykey));
                        }
                    });
                }
            });
        }

    }

    private void loadAdView() {
        // View Ads
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(Constants.Start_b1_3);
        adView = (AdView) findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.v(Constants.log + "ads", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                super.onAdFailedToLoad(adError);
                Log.v(Constants.log + "ads", "onAdFailedToLoad = " + adError.getCode());
//                new MainMenuActivity().onAdFailedToLoadErrorsCodes(Start.this, adError.getCode());
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Log.v(Constants.log + "ads", "onAdClosed");
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
//                new MainMenuActivity().onAdFailedToLoadErrorsCodes(Start.this, adError.getCode());
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
        switch (item.getItemId()) {
            case R.id.shareItem1:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        getString(R.string.sharemessage) + " " + "\n\n: "+Constants.URLGooglePlay);
                sendIntent.setType("text/plain");
//                sendIntent.setPackage("com.facebook.orca");
                try {
                    startActivity(sendIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(Start.this, getString(R.string.installmessenger), Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.ratingItem2:
                new MainMenuActivity().rate_dialog(Start.this);

                break;
            case R.id.aboutItem4:
                about_dialog(Start.this);
                break;
        }

        return super.onOptionsItemSelected(item);
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
        handler.removeCallbacks(runnable);
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
            alerter(getString(R.string.fantastic), getString(R.string.about_alerter_message), Start.this, 1);
        }else{
            alerter(getString(R.string.good), getString(R.string.about_not_complete_alerter_message), Start.this, 0);
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
        new MainMenuActivity().onAdFailedToLoadErrorsCodes(Start.this, i);
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

    // Stop back button
    /*@Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_BACK:
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }*/
}
