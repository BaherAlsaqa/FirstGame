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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class Result extends AppCompatActivity{

    private TextView score, hightScore, highscoretitle;
    private Button again;
    private AppSharedPreferences appSharedPreferences;
    private String levelValue;
    private String gameKey;
    private SoundPlayer soundPlayer;
    private ImageView loadingSpinner;
    // Change Font
    private Calligrapher calligrapher;

    private Boolean appSound;
    private boolean start_reward = false;

    /*// Interstitial Ads
    private InterstitialAd mInterstitialAd;*/
    // Rewarded Video Ads
    private RewardedAd mRewardedAd;
    private final String TAG = "Result";

    // Firebase
//    private Firebase firebase;

    /*//Runnable and handler;
    private Runnable runnable;
    private Handler handler;
    private boolean killMe = true;*/

    @Override
    protected void onStart() {
        super.onStart();

        //Start Sound Service
        stopService(new Intent(Result.this, SoundService.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Change Font
        // Initialize Calss
        calligrapher = new Calligrapher(this);
        appSharedPreferences = new AppSharedPreferences(Result.this);
        soundPlayer = new SoundPlayer(Result.this);

        // firebase
        /*Firebase.setAndroidContext(this);
        firebase = new Firebase("https://pink-coin.firebaseio.com/"+Constants.DB_Firebase_Key);*/

        //get ui to java code
        score = (TextView) findViewById(R.id.score);
        hightScore = (TextView) findViewById(R.id.highscore);
        highscoretitle = (TextView) findViewById(R.id.highscoretitle);
        again = (Button) findViewById(R.id.tryagain);
        loadingSpinner = (ImageView) findViewById(R.id.loading_spinner);

        // Initialize Calss Mobile Ads
//        MobileAds.initialize(this, Constants.ID_App);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        // Use an activity context to get the rewarded video instance.
        loadAdView();

        /*// Interstitial Ads
        mInterstitialAd = new InterstitialAd(Result.this);
        mInterstitialAd.setAdUnitId(Constants.Menu_Btn_b2_2);
        mInterstitialAd.loadAd(new AdRequest.Builder()
                .build());

        again.setText("");
        again.setEnabled(false);
        loadingSpinner.setVisibility(View.VISIBLE);

        handler = new Handler();

        runnable = new Runnable() {

            public void run() {
                if (killMe) {

                    if (mInterstitialAd.isLoaded()) {
                        killMe = false;
                        again.setText(getString(R.string.again));
                        again.setEnabled(true);
                        loadingSpinner.setVisibility(View.GONE);
                    }
                    handler.postDelayed(this, 500);
                } else {
                    handler.removeCallbacks(runnable);
                }

                Log.v(Constants.log + "211", "Runnable is Running");
            }
        };
        handler.postDelayed(runnable, 500);*/

        appSound = appSharedPreferences.readBoolean(Constants.appSound);

        if (Locale.getDefault().getLanguage().equals("ar")) {
            calligrapher.setFont(this, "homaarabic.ttf", true);
        }else{
            calligrapher.setFont(this, "comicscarton.ttf", true);
        }

        Intent intent = getIntent();
        if (intent != null){

            gameKey = appSharedPreferences.readString(Constants.gameKey);

            levelValue = intent.getStringExtra(Constants.levelValue);
            int scoreNumber = intent.getIntExtra(Constants.score, 0);
            score.setText(scoreNumber+"");

            if (gameKey.equals(Constants.challengeYourselfValue)) {
                Log.v("2112", "if (gameKey == Constants.challengeYourselfValue)");
                hightScore.setVisibility(View.VISIBLE);
                highscoretitle.setVisibility(View.VISIBLE);
                int highScoreNumber = appSharedPreferences.readInteger(Constants.score);
                hightScore.setText(highScoreNumber + "");

                if (scoreNumber > highScoreNumber) {
                    Log.v("2112", "if (scoreNumber > highScoreNumber)");
                    alerter(getString(R.string.fantastic), getString(R.string.highScoreUp), Result.this, 1);
                    appSharedPreferences.writeInteger(Constants.score, scoreNumber);
                    //add to database firebase
                    Log.v(Constants.log+"token", "if scoreNumber = "+scoreNumber);
                    String firebaseToken = appSharedPreferences.readString(Constants.Firebase_Token);
                    /*Firebase parentC = firebase.child(firebaseToken);
                    parentC.child("HighScore").setValue(scoreNumber);*/
                    /////////////////////////////////////////////////
                }
            }else{
                Log.v("2112", "else (gameKey == Constants.challengeYourselfValue)");
                hightScore.setVisibility(View.INVISIBLE);
                highscoretitle.setVisibility(View.INVISIBLE);
            }
        }

        again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appSharedPreferences.writeInteger(Constants.levelValue, Integer.parseInt(levelValue));
                if (appSound) {
                    soundPlayer.playClickSound();
                }
                startActivity(new Intent(Result.this, MainActivity.class)
                        .putExtra(Constants.levelValue, levelValue)
                        .putExtra(Constants.activitykey, Constants.activitykey));
                // Show Interstitial Ads
                /*if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                    Log.v(Constants.log + "132", "mInterstitialAd.show.");
                } else {
                    Log.v(Constants.log + "132", "The interstitial wasn't loaded yet.");
                }
                mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        Log.v(Constants.log + "132", "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(int i) {
                        super.onAdFailedToLoad(i);
                        Log.v(Constants.log + "132", "onAdFailedToLoad = "+i);
                        new MainMenuActivity().onAdFailedToLoadErrorsCodes(Result.this, i);
                        startActivity(new Intent(Result.this, MainActivity.class)
                                .putExtra(Constants.levelValue, levelValue)
                                .putExtra(Constants.activitykey, Constants.activitykey));
                    }

                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        Log.v(Constants.log + "132", "onAdClosed");
                    }
                });*/
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
                    alerter(getString(R.string.fantastic), getString(R.string.about_alerter_message), Result.this, 1);
                }else{
                    alerter(getString(R.string.good), getString(R.string.about_not_complete_alerter_message), Result.this, 0);
                }
            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                // Called when ad fails to show.
                Log.e(TAG, "Ad failed to show fullscreen content.");
                mRewardedAd = null;
//                new MainMenuActivity().onAdFailedToLoadErrorsCodes(Result.this, adError.getCode());
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

    private void loadAdView() {
        // View Ads
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(Constants.Result_b1_4);
        adView = (AdView) findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.v(Constants.log + "ads", "onAdLoaded");
                loadRewardedVideoAd();
            }
            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                super.onAdFailedToLoad(adError);
                Log.v(Constants.log+"ads", "onAdFailedToLoad = "+adError.getCode());
//                new MainMenuActivity().onAdFailedToLoadErrorsCodes(Result.this, adError.getCode());
            }
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Log.v(Constants.log+"ads", "onAdClosed");
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
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });
        // View Ads
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(Constants.About_b1_9);
        adView = dialog1.findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.v(Constants.log + "ads", "onAdLoaded");
            }
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                super.onAdFailedToLoad(adError);
                Log.v(Constants.log+"ads", "onAdFailedToLoad = "+adError.getCode());
//                new MainMenuActivity().onAdFailedToLoadErrorsCodes(Result.this, adError.getCode());
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
                    Activity activityContext = Result.this;
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

        RewardedAd.load(Result.this, Constants.watch_Btn_RewardAd_b3_1,
                new AdRequest.Builder().build(),  new RewardedAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        Log.d(TAG, "Ad was loaded.");
                        mRewardedAd = ad;
                        ServerSideVerificationOptions options = new ServerSideVerificationOptions
                                .Builder()
                                .setCustomData("SAMPLE_CUSTOM_DATA_STRING")
                                .build();
                        mRewardedAd.setServerSideVerificationOptions(options);
                    }
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.d(TAG, loadAdError.toString());
                        mRewardedAd = null;
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
                    Toast.makeText(Result.this,getString(R.string.installmessenger), Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.ratingItem2 :
                new MainMenuActivity().rate_dialog(Result.this);
                break;
            case R.id.aboutItem4 :
                about_dialog(Result.this);
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN){
            switch (event.getKeyCode()){
                case KeyEvent.KEYCODE_BACK :
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        /*handler.removeCallbacks(runnable);*/
        super.onDestroy();
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
