package com.bahertech.firstgame.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
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
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.tapadoo.alerter.Alerter;

import java.util.Locale;

import me.anwarshahriar.calligrapher.Calligrapher;

public class MainMenuActivity extends AppCompatActivity implements RewardedVideoAdListener {

    private Button challengeYourself;
    private Button gameLevelsEasy;
    private Button gameLevelsMedium;
    private Button gameLevelsHard;
    private Button settings;
    private Button logout;
    private SoundPlayer soundPlayer;
    private AppSharedPreferences  appSharedPreferences;
    //Change Font
    private Calligrapher calligrapher;
    private Boolean appSound;
    // Rewarded Video Ads
    private RewardedVideoAd mRewardedVideoAd;

    private boolean start_reward = false;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseListener;

    @Override
    protected void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        //Start Sound Service
        startService(new Intent(MainMenuActivity.this, SoundService.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


        // Initialize Calsses
        // Change Font
        calligrapher = new Calligrapher(this);

        soundPlayer = new SoundPlayer(MainMenuActivity.this);
        appSharedPreferences = new AppSharedPreferences(MainMenuActivity.this);

        //Create Firebase Token
        FirebaseMessaging.getInstance().subscribeToTopic(Constants.DB_Firebase_Key);
        FirebaseInstanceId.getInstance().getToken();

        String FirebaseToken = FirebaseInstanceId.getInstance().getToken();

        appSharedPreferences.writeString(Constants.Firebase_Token, FirebaseToken);

        Log.d(Constants.log+"token", "Instance Id Token : "+FirebaseToken);

        // Initialize Calss Mobile Ads
//        MobileAds.initialize(this, Constants.ID_App);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        // Use an activity context to get the rewarded video instance.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(MainMenuActivity.this);
        mRewardedVideoAd.setRewardedVideoAdListener(MainMenuActivity.this);
        loadRewardedVideoAd();
        // View Ads
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(Constants.MainManu_b1_1);
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
//                onAdFailedToLoadErrorsCodes(MainMenuActivity.this, adError.getCode());
            }
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Log.v(Constants.log+"ads", "onAdClosed");
            }
        });

        if (Locale.getDefault().getLanguage().equals("ar")) {
            calligrapher.setFont(this, "homaarabic.ttf", true);//
        }else{
            calligrapher.setFont(this, "comicscarton.ttf", true);
        }

        challengeYourself = (Button) findViewById(R.id.challenge_yourself);
        gameLevelsEasy = (Button) findViewById(R.id.gamelevelseasy);
        gameLevelsMedium = (Button) findViewById(R.id.gamelevelsmedium);
        gameLevelsHard = (Button) findViewById(R.id.gamelevelshard);
        settings = (Button) findViewById(R.id.settings);
        logout = (Button) findViewById(R.id.logout);

        challengeYourself.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appSharedPreferences.writeString(Constants.gameKey, Constants.challengeYourselfValue);
                appSound = appSharedPreferences.readBoolean(Constants.appSound);
                if (appSound) {
                    soundPlayer.playClickSound();
                }
                startActivity(new Intent(MainMenuActivity.this, Start.class)
                        .putExtra(Constants.gameKey, Constants.challengeYourselfValue)
                        .putExtra(Constants.levelValue, ""));
            }
        });

        gameLevelsEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appSharedPreferences.writeString(Constants.gameKey, Constants.gameLevelEasyValue);
                appSound = appSharedPreferences.readBoolean(Constants.appSound);
                if (appSound) {
                    soundPlayer.playClickSound();
                }
                startActivity(new Intent(MainMenuActivity.this, Levels.class)
                        .putExtra(Constants.activitykey, Constants.pinkoin));
            }
        });

        gameLevelsMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appSharedPreferences.writeString(Constants.gameKey, Constants.gameLevelMediumValue);
                appSound = appSharedPreferences.readBoolean(Constants.appSound);
                if (appSound) {
                    soundPlayer.playClickSound();
                }
            }
        });

        gameLevelsHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appSharedPreferences.writeString(Constants.gameKey, Constants.gameLevelHardValue);
                appSound = appSharedPreferences.readBoolean(Constants.appSound);
                if (appSound) {
                    soundPlayer.playClickSound();
                }
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appSound = appSharedPreferences.readBoolean(Constants.appSound);
                if (appSound) {
                    soundPlayer.playClickSound();
                }
                startActivity(new Intent(MainMenuActivity.this, Settings.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appSound = appSharedPreferences.readBoolean(Constants.appSound);
                if (appSound) {
                    soundPlayer.playClickSound();
                }
                //Stop Sound Service
                stopService(new Intent(MainMenuActivity.this, SoundService.class));

                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
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
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem item = menu.findItem(R.id.settingsItem3);
        SpannableString s = new SpannableString(getString(R.string.settings));
        s.setSpan(new ForegroundColorSpan(Color.parseColor("#f1b902")), 0, s.length(), 0);
        item.setTitle(s);

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
                    Toast.makeText(MainMenuActivity.this,getString(R.string.installmessenger), Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.ratingItem2 :
                rate_dialog(MainMenuActivity.this);
                break;
            case R.id.settingsItem3 :
                startActivity(new Intent(this, Settings.class));
                break;
            case R.id.aboutItem4 :
                about_dialog(MainMenuActivity.this);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //Stop Sound Service
        stopService(new Intent(MainMenuActivity.this, SoundService.class));

        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    public void rate_dialog(Context context) {

        Typeface typeface;

        final Dialog dialog2 = new Dialog(context);
        dialog2.show();
        dialog2.setContentView(R.layout.rate_dialog);

        // with out background
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

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
        adView.setAdUnitId(Constants.Rate_b1_6);
        adView = dialog2.findViewById(R.id.adView1);
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
//                onAdFailedToLoadErrorsCodes(MainMenuActivity.this, adError.getCode());
            }
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Log.v(Constants.log+"ads", "onAdClosed");
            }
        });

        Button ok = dialog2.findViewById(R.id.ok);
        Button close = dialog2.findViewById(R.id.cancle);
        TextView rateMessage = dialog2.findViewById(R.id.rate_message);

        if (Locale.getDefault().getLanguage().equals("ar")) {
            typeface = Typeface.createFromAsset(context.getAssets(), "homaarabic.ttf");
        }else{
            typeface = Typeface.createFromAsset(context.getAssets(), "comicscarton.ttf");
        }

        ok.setTypeface(typeface);
        close.setTypeface(typeface);
        rateMessage.setTypeface(typeface);

        dialog2.setCancelable(false);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("market://details?id="+getPackageName());//shop.nicenews.displaynews
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(Constants.URLGooglePlay)));
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
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
//                onAdFailedToLoadErrorsCodes(MainMenuActivity.this, adError.getCode());
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
            alerter(getString(R.string.fantastic), getString(R.string.about_alerter_message), MainMenuActivity.this, 1);
        }else{
            alerter(getString(R.string.good), getString(R.string.about_not_complete_alerter_message), MainMenuActivity.this, 0);
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
        onAdFailedToLoadErrorsCodes(MainMenuActivity.this, i);
    }

    @Override
    public void onRewardedVideoCompleted() {

    }

    private void alerter(String title, String message, Context context, int soundPlay) {
        if (soundPlay == 1) {
            soundPlayer.playLevelUpSound();
        }
        Resources resources = getResources();
        Alerter.create((Activity) context)
                .setTitle(title)
                .setTitleAppearance(R.style.AlertTextAppearance_Title)
                .setTextAppearance(R.style.AlertTextAppearance_Text)
                .setText(message)
                .setDuration(8000)
                .enableSwipeToDismiss()
                .setBackgroundColorInt(resources.getColor(R.color.pink))
                .setIcon(resources.getDrawable(R.drawable.alerter_ic_notifications))
                .setIconColorFilter(resources.getColor(R.color.white))
                .enableProgress(true)
                .setProgressColorRes(R.color.blue)
                .show();
    }

    public void onAdFailedToLoadErrorsCodes(Context context, int errorCode){
        Typeface typeface;
        if (Locale.getDefault().getLanguage().equals("ar")) {
            typeface = Typeface.createFromAsset(context.getAssets(), "homaarabic.ttf");
        }else{
            typeface = Typeface.createFromAsset(context.getAssets(), "comicscarton.ttf");
        }
        switch (errorCode){
            case 0 :
                Toast toast = Toast.makeText(context, context.getString(R.string.error_try_again), Toast.LENGTH_SHORT);
                View view = toast.getView();
                view.setBackgroundResource(R.drawable.shaptextviewtoasterror1);
                TextView text = view.findViewById(android.R.id.message);
                text.setShadowLayer(0,0,0,0);
                text.setTextColor(Color.WHITE);
                text.setPadding(15, 0, 15, 0);
                text.setGravity(Gravity.CENTER);
                text.setTypeface(typeface);
                toast.show();
                break;
            case 1 :
                Toast toast1 = Toast.makeText(context, context.getString(R.string.error_try_again_later), Toast.LENGTH_SHORT);
                View view1 = toast1.getView();
                view1.setBackgroundResource(R.drawable.shaptextviewtoasterror1);
                TextView text1 = view1.findViewById(android.R.id.message);
                text1.setShadowLayer(0,0,0,0);
                text1.setTextColor(Color.WHITE);
                text1.setPadding(15, 0, 15, 0);
                text1.setGravity(Gravity.CENTER);
                text1.setTypeface(typeface);
                toast1.show();
                break;
            case 2 :
                Toast toast2 = Toast.makeText(context, context.getString(R.string.error_internet), Toast.LENGTH_SHORT);
                View view2 = toast2.getView();
                view2.setBackgroundResource(R.drawable.shaptextviewtoasterror1);
                TextView text2 = view2.findViewById(android.R.id.message);
                text2.setShadowLayer(0,0,0,0);
                text2.setTextColor(Color.WHITE);
                text2.setPadding(15, 0, 15, 0);
                text2.setGravity(Gravity.CENTER);
                text2.setTypeface(typeface);
                toast2.show();
                break;
            case 3 :
                Toast toast3 = Toast.makeText(context, context.getString(R.string.no_ad_inventory), Toast.LENGTH_SHORT);
                View view3 = toast3.getView();
                view3.setBackgroundResource(R.drawable.shaptextviewtoasterror1);
                TextView text3 = view3.findViewById(android.R.id.message);
                text3.setShadowLayer(0,0,0,0);
                text3.setTextColor(Color.WHITE);
                text3.setPadding(15, 0, 15, 0);
                text3.setGravity(Gravity.CENTER);
                text3.setTypeface(typeface);
                toast3.show();
                break;
            case 4 :
                Toast toast4 = Toast.makeText(context, context.getString(R.string.finalLevel), Toast.LENGTH_LONG);
                View view4 = toast4.getView();
                view4.setBackgroundResource(R.drawable.shaptextviewtoasterror1);
                TextView text4 = view4.findViewById(android.R.id.message);
                text4.setShadowLayer(0,0,0,0);
                text4.setTextColor(Color.WHITE);
                text4.setPadding(15, 0, 15, 0);
                text4.setGravity(Gravity.CENTER);
                text4.setTypeface(typeface);
                toast4.show();
                break;
            case 5 :
                Toast toast5 = Toast.makeText(context, context.getString(R.string.highScoreUp), Toast.LENGTH_LONG);
                View view5 = toast5.getView();
                view5.setBackgroundResource(R.drawable.shaptextviewtoasterror1);
                TextView text5 = view5.findViewById(android.R.id.message);
                text5.setShadowLayer(0,0,0,0);
                text5.setTextColor(Color.WHITE);
                text5.setPadding(15, 0, 15, 0);
                text5.setGravity(Gravity.CENTER);
                text5.setTypeface(typeface);
                toast5.show();
                break;
        }
    }
}
