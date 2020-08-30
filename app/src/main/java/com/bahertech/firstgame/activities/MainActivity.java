package com.bahertech.firstgame.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bahertech.firstgame.R;
import com.bahertech.firstgame.classes.SoundPlayer;
import com.bahertech.firstgame.classes.ToastGenerate;
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
import java.util.Timer;
import java.util.TimerTask;

import me.anwarshahriar.calligrapher.Calligrapher;

public class MainActivity extends AppCompatActivity implements RewardedVideoAdListener {

    private TextView scoreLabel;
    private TextView accessScore;
    private TextView startLabel;
    private TextView titlescoreLabel;
    private ImageView moneyBox;
    private ImageView bomb;
    private ImageView yellowCoin;
    private ImageView blueCoin;
    private ImageView yellowCoin1;
    private ImageView blueCoin1;
    private ImageView pinkCoin;
    private FrameLayout frame;
    private RelativeLayout levelUp;
    private ImageView levelUpImage;
    private CheckBox playpause;
    private Button menu;
    private Button rerun;
    private Button next;

    //Game Key
    String gameKey;

    //position
    private int boxY;
    private int yCoinX;
    private int yCoinY;
    private int bCoinX;
    private int bCoinY;
    private int yCoinX1;
    private int yCoinY1;
    private int bCoinX1;
    private int bCoinY1;
    private int pCoinX;
    private int pCoinY;
    private int bombX;
    private int bombY;

    //Classes
    private Handler handler;
    private Timer timer;
    private Runnable runnable;

    //Status check
    private boolean action_flg = false;
    private boolean start_flg = false;
    private boolean start_reward = false;
    private boolean not_complete_rewardAd = false;
    private boolean killMe = true;
    private boolean btn_watch_reward = false;
    private boolean between_two_rewardAd = false;

    //Speed
    private int yellowCoinSpeed;
    private int blueCoinSpees;
    private int yellowCoinSpeed1;
    private int blueCoinSpees1;
    private int pinkCoinSpeed;
    private int bombSpeed;
    private int boxSpeed;

    //Size
    private int frameHeight;
    private int boxSize;
    private int screenWidth;
    private int screenHeight;

    //Score
    private int score = 0;

    //Sound Player
    private SoundPlayer soundPlayer;

    // the Level
    private String levelValue;
    private String levelValueSameActivity;
    private int hundred = 50;

    //Dialog Reward
    Dialog dialogReward;

    //Shared Preferences
    private AppSharedPreferences appSharedPreferences;

    // Speed game
    int boxSF = 48;
    int yellowSF = 190;
    int blueSF = 130;
    int yellowSF1 = 170;
    int blueSF1 = 150;
    int pinkSF = 120;
    int bombSF = 95;

    // Change Font
    private Calligrapher calligrapher;

    // Variables
    int x = 0;
    int i = 0;
    int u = 3;

    private Boolean appSound;
    private Boolean boxSound;
    private Boolean vibrate;

    // Interstitial Ads
    private InterstitialAd mInterstitialAd;
    private InterstitialAd mInterstitialAd1;

    // Rewarded Video Ads
    private RewardedVideoAd mRewardedVideoAd, mRewardedVideoAd1;

    // Runnable
    private Runnable runnableWatch;
    private Handler handlerWatch;

    // Score Animation
    Animation scoreAnimationZoomIn, scoreAnimationZoomOut;

//    private Firebase firebase;

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        String activity_key = intent.getStringExtra(Constants.activitykey);
        if (activity_key.equals(Constants.activitykey)) {
            if (x == 0) {
                Log.v(Constants.log + "12", activity_key);
                stopService(new Intent(MainActivity.this, SoundService.class));
                x++;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO /////////////////// onCreate ////////////////////
        // Change Font
        // Initialize Calss
        calligrapher = new Calligrapher(this);
        ToastGenerate.getInstance(MainActivity.this);

        // firebase
        /*Firebase.setAndroidContext(this);
        firebase = new Firebase("https://pink-coin.firebaseio.com/" + Constants.DB_Firebase_Key);*/

        // Initialize Calss Mobile Ads
//        MobileAds.initialize(this, Constants.ID_App);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        // Interstitial Ads
        mInterstitialAd = new InterstitialAd(MainActivity.this);
        mInterstitialAd.setAdUnitId(Constants.Menu_Btn_b2_2);
        mInterstitialAd.loadAd(new AdRequest.Builder()
                .build());
        mInterstitialAd1 = new InterstitialAd(MainActivity.this);
        mInterstitialAd1.setAdUnitId(Constants.End_Level_b2_4);
        mInterstitialAd1.loadAd(new AdRequest.Builder()
                .build());

        // Use an activity context to get the rewarded video instance.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(MainActivity.this);
        mRewardedVideoAd.setRewardedVideoAdListener(MainActivity.this);
        loadRewardedVideoAd();
        mRewardedVideoAd1 = MobileAds.getRewardedVideoAdInstance(MainActivity.this);
        mRewardedVideoAd1.setRewardedVideoAdListener(MainActivity.this);
        loadRewardedVideoAd1();

        if (Locale.getDefault().getLanguage().equals("ar")) {
            calligrapher.setFont(this, "homaarabic.ttf", true);
        } else {
            calligrapher.setFont(this, "comicscarton.ttf", true);
        }

        Intent intent = getIntent();
        levelValue = intent.getStringExtra(Constants.levelValue);
        appSharedPreferences = new AppSharedPreferences(MainActivity.this);
        gameKey = appSharedPreferences.readString(Constants.gameKey);

        //App Sound
        appSound = appSharedPreferences.readBoolean(Constants.appSound);
        boxSound = appSharedPreferences.readBoolean(Constants.boxSound);
        vibrate = appSharedPreferences.readBoolean(Constants.vibrate);

        levelValue = String.valueOf(appSharedPreferences.readInteger(Constants.levelValue));

        Log.v(Constants.log + "9", levelValue);

        scoreLabel = (TextView) findViewById(R.id.scoreLabel);
        accessScore = (TextView) findViewById(R.id.access_score);
        startLabel = (TextView) findViewById(R.id.startLabel);
        titlescoreLabel = (TextView) findViewById(R.id.titlescoreLabel);
        moneyBox = (ImageView) findViewById(R.id.moneyBox);
        bomb = (ImageView) findViewById(R.id.bomb);
        yellowCoin = (ImageView) findViewById(R.id.yellowcoin);
        blueCoin = (ImageView) findViewById(R.id.bluecoin);
        yellowCoin1 = (ImageView) findViewById(R.id.yellowcoin1);
        blueCoin1 = (ImageView) findViewById(R.id.bluecoin1);
        pinkCoin = (ImageView) findViewById(R.id.pinkcoin);
        frame = (FrameLayout) findViewById(R.id.frame);
        levelUp = (RelativeLayout) findViewById(R.id.levelup);
        levelUpImage = (ImageView) findViewById(R.id.levelupimage);
        playpause = (CheckBox) findViewById(R.id.playpause);
        menu = (Button) findViewById(R.id.menu);
        rerun = (Button) findViewById(R.id.rerun);
        next = (Button) findViewById(R.id.next);

        // Score Animation
        scoreAnimationZoomIn = AnimationUtils.loadAnimation(MainActivity.this, R.anim.zoom_in);
        scoreAnimationZoomOut = AnimationUtils.loadAnimation(MainActivity.this, R.anim.zoom_out);

        // new Sound Player
        soundPlayer = new SoundPlayer(MainActivity.this);

        //Initialize Class Timer and handler
        handler = new Handler();
        handlerWatch = new Handler();
        timer = new Timer();

        //Get size screen
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        screenWidth = size.x;
        screenHeight = size.y;

        /*Log.v("SPEED1", boxSpeed+"");
        Log.v("SPEED1", yellowCoinSpeed+"");
        Log.v("SPEED1", blueCoinSpees+"");
        Log.v("SPEED1", pinkCoinSpeed+"");
        Log.v("SPEED1", bombSpeed+"");*/

        // move to out of screen
        bomb.setX(-80);
        bomb.setY(-80);
        yellowCoin.setX(-80);
        yellowCoin.setY(-80);
        blueCoin.setX(-80);
        blueCoin.setY(-80);
        yellowCoin1.setX(-80);
        yellowCoin1.setY(-80);
        blueCoin1.setX(-80);
        blueCoin1.setY(-80);
        pinkCoin.setX(-80);
        pinkCoin.setY(-80);

        switch (gameKey) {
            case Constants.challengeYourselfValue:
                // Change Title
                getSupportActionBar().setTitle(R.string.challenge_yourself);

                titlescoreLabel.setText(getString(R.string.highscore));
                int highScore = appSharedPreferences.readInteger(Constants.score);
                accessScore.setText(highScore + "");
                scoreLabel.setText("0");
                ////////////////////////////////
                //Nexus4 width:768 height:1184
                boxSpeed = Math.round(screenHeight / boxSF);
                yellowCoinSpeed = Math.round(screenWidth / yellowSF);
                blueCoinSpees = Math.round(screenWidth / blueSF);
                yellowCoinSpeed1 = Math.round(screenWidth / yellowSF1);
                blueCoinSpees1 = Math.round(screenWidth / blueSF1);
                pinkCoinSpeed = Math.round(screenWidth / pinkSF);
                bombSpeed = Math.round(screenWidth / bombSF);
                ///////////////////////////////////////////////////////
                final int v = 5;
                final int w = 5;
                final int x = 5;
                final int y = 5;
                final int z = 5;
                    runnable = new Runnable() {
                        public void run() {

                            //Nexus4 width:768 height:1184
                            if (boxSF >= 20) {
                                Log.i(Constants.log + "i", "Box = " + boxSF + "");
                                boxSpeed = Math.round(screenHeight / boxSF);
                                boxSF -= v;
                            } else {
                                boxSF = 20;
                                Log.i(Constants.log + "i", "Box = " + boxSF + "");
                                boxSpeed = Math.round(screenHeight / boxSF);
                            }
                            yellowSF -= w;
                            if (yellowSF != 0)
                                yellowCoinSpeed = Math.round(screenWidth / yellowSF);
                            blueSF -= x;
                            if (blueSF != 0)
                                blueCoinSpees = Math.round(screenWidth / blueSF);

                            yellowSF1 -= w;
                            if (yellowSF1 != 0)
                                yellowCoinSpeed1 = Math.round(screenWidth / yellowSF1);
                            blueSF1 -= x;
                            if (blueSF1 != 0)
                                blueCoinSpees1 = Math.round(screenWidth / blueSF1);

                            pinkSF -= y;
                            if (pinkSF != 0)
                                pinkCoinSpeed = Math.round(screenWidth / pinkSF);
                            if (bombSF >= 35) {
                                Log.i(Constants.log + "i", "Bomb = " + bombSF + "");
                                bombSpeed = Math.round(screenWidth / bombSF);
                                bombSF -= z;
                            } else {
                                bombSF = 35;
                                Log.i(Constants.log + "i", "Bomb = " + bombSF + "");
                                bombSpeed = Math.round(screenWidth / bombSF);
                            }

                            handler.postDelayed(this, 20000);
                        }
                    };
                    handler.postDelayed(runnable, 20000);
                break;
            case Constants.gameLevelEasyValue:
                if (levelValue != null) {
                    scoreLabel.setText("0");
                    int level = Integer.parseInt(levelValue);
                    switch (levelValue) {
                        //Level One
                        case "1":
                            getSupportActionBar().setTitle(R.string.levelOne);
                            accessScore.setText(level * hundred + "");
                            //Nexus4 width:768 height:1184
                            boxSpeed = Math.round(screenHeight / boxSF);
                            yellowCoinSpeed = Math.round(screenWidth / yellowSF);
                            blueCoinSpees = Math.round(screenWidth / blueSF);
                            yellowCoinSpeed1 = Math.round(screenWidth / yellowSF1);
                            blueCoinSpees1 = Math.round(screenWidth / blueSF1);
                            pinkCoinSpeed = Math.round(screenWidth / pinkSF);
                            bombSpeed = Math.round(screenWidth / bombSF);
                            break;
                        //Level Two
                        case Constants.gameLevelMediumValue:
                            getSupportActionBar().setTitle(R.string.levelTwo);
                            accessScore.setText(level * hundred + "");
                            boxSpeed = Math.round(screenHeight / (boxSF - ((Integer.parseInt(levelValue) * 5) - 5)));
                            yellowCoinSpeed = Math.round(screenWidth / (yellowSF - ((Integer.parseInt(levelValue) * 10) - 10)));
                            blueCoinSpees = Math.round(screenWidth / (blueSF - ((Integer.parseInt(levelValue) * 10) - 10)));
                            yellowCoinSpeed1 = Math.round(screenWidth / (yellowSF1 - ((Integer.parseInt(levelValue) * 10) - 10)));
                            blueCoinSpees1 = Math.round(screenWidth / (blueSF1 - ((Integer.parseInt(levelValue) * 10) - 10)));
                            pinkCoinSpeed = Math.round(screenWidth / (pinkSF - ((Integer.parseInt(levelValue) * 10) - 10)));
                            bombSpeed = Math.round(screenWidth / (bombSF - ((Integer.parseInt(levelValue) * 10) - 10)));
                            break;
                        //Level Three
                        case Constants.gameLevelHardValue:
                            getSupportActionBar().setTitle(R.string.levelThree);
                            accessScore.setText(level * hundred + "");
                            boxSpeed = Math.round(screenHeight / (boxSF - ((Integer.parseInt(levelValue) * 5) - 5)));
                            yellowCoinSpeed = Math.round(screenWidth / (yellowSF - ((Integer.parseInt(levelValue) * 10) - 10)));
                            blueCoinSpees = Math.round(screenWidth / (blueSF - ((Integer.parseInt(levelValue) * 10) - 10)));
                            yellowCoinSpeed1 = Math.round(screenWidth / (yellowSF1 - ((Integer.parseInt(levelValue) * 10) - 10)));
                            blueCoinSpees1 = Math.round(screenWidth / (blueSF1 - ((Integer.parseInt(levelValue) * 10) - 10)));
                            pinkCoinSpeed = Math.round(screenWidth / (pinkSF - ((Integer.parseInt(levelValue) * 10) - 10)));
                            bombSpeed = Math.round(screenWidth / (bombSF - ((Integer.parseInt(levelValue) * 10) - 10)));
                            break;
                        //Level Four
                        case "4":
                            getSupportActionBar().setTitle(R.string.levelFour);
                            accessScore.setText(level * hundred + "");
                            boxSpeed = Math.round(screenHeight / (boxSF - ((Integer.parseInt(levelValue) * 5) - 5)));
                            yellowCoinSpeed = Math.round(screenWidth / (yellowSF - ((Integer.parseInt(levelValue) * 10) - 10)));
                            blueCoinSpees = Math.round(screenWidth / (blueSF - ((Integer.parseInt(levelValue) * 10) - 10)));
                            yellowCoinSpeed1 = Math.round(screenWidth / (yellowSF1 - ((Integer.parseInt(levelValue) * 10) - 10)));
                            blueCoinSpees1 = Math.round(screenWidth / (blueSF1 - ((Integer.parseInt(levelValue) * 10) - 10)));
                            pinkCoinSpeed = Math.round(screenWidth / (pinkSF - ((Integer.parseInt(levelValue) * 10) - 10)));
                            bombSpeed = Math.round(screenWidth / (bombSF - ((Integer.parseInt(levelValue) * 10) - 10)));
                            break;
                        //Level Five
                        case "5":
                            getSupportActionBar().setTitle(R.string.levelFive);
                            accessScore.setText(level * hundred + "");
                            boxSpeed = Math.round(screenHeight / (boxSF - ((Integer.parseInt(levelValue) * 5) - 5)));
                            yellowCoinSpeed = Math.round(screenWidth / (yellowSF - ((Integer.parseInt(levelValue) * 10) - 10)));
                            blueCoinSpees = Math.round(screenWidth / (blueSF - ((Integer.parseInt(levelValue) * 10) - 10)));
                            yellowCoinSpeed1 = Math.round(screenWidth / (yellowSF1 - ((Integer.parseInt(levelValue) * 10) - 10)));
                            blueCoinSpees1 = Math.round(screenWidth / (blueSF1 - ((Integer.parseInt(levelValue) * 10) - 10)));
                            pinkCoinSpeed = Math.round(screenWidth / (pinkSF - ((Integer.parseInt(levelValue) * 10) - 10)));
                            bombSpeed = Math.round(screenWidth / (bombSF - ((Integer.parseInt(levelValue) * 10) - 10)));
                            break;
                        case "6":
                            getSupportActionBar().setTitle(R.string.levelSix);
                            accessScore.setText(level * hundred + "");
                            boxSpeed = Math.round(screenHeight / (boxSF - ((Integer.parseInt(levelValue) * 5) - 5)));
                            yellowCoinSpeed = Math.round(screenWidth / (yellowSF - ((Integer.parseInt(levelValue) * 10) - 10)));
                            blueCoinSpees = Math.round(screenWidth / (blueSF - ((Integer.parseInt(levelValue) * 10) - 10)));
                            yellowCoinSpeed1 = Math.round(screenWidth / (yellowSF1 - ((Integer.parseInt(levelValue) * 10) - 10)));
                            blueCoinSpees1 = Math.round(screenWidth / (blueSF1 - ((Integer.parseInt(levelValue) * 10) - 10)));
                            pinkCoinSpeed = Math.round(screenWidth / (pinkSF - ((Integer.parseInt(levelValue) * 10) - 10)));
                            bombSpeed = Math.round(screenWidth / (bombSF - ((Integer.parseInt(levelValue) * 10) - 10)));
                            break;
                        case "7":
                            getSupportActionBar().setTitle(R.string.levelSeven);
                            accessScore.setText(level * hundred + "");
                            boxSpeed = Math.round(screenHeight / (boxSF - ((Integer.parseInt(levelValue) * 5) - 5)));
                            yellowCoinSpeed = Math.round(screenWidth / (yellowSF - ((Integer.parseInt(levelValue) * 10) - 10)));
                            blueCoinSpees = Math.round(screenWidth / (blueSF - ((Integer.parseInt(levelValue) * 10) - 10)));
                            yellowCoinSpeed1 = Math.round(screenWidth / (yellowSF1 - ((Integer.parseInt(levelValue) * 10) - 10)));
                            blueCoinSpees1 = Math.round(screenWidth / (blueSF1 - ((Integer.parseInt(levelValue) * 10) - 10)));
                            pinkCoinSpeed = Math.round(screenWidth / (pinkSF - ((Integer.parseInt(levelValue) * 10) - 10)));
                            bombSpeed = Math.round(screenWidth / (bombSF - ((Integer.parseInt(levelValue) * 10) - 10)));
                            break;
                        case "8":
                            getSupportActionBar().setTitle(R.string.levelEight);
                            accessScore.setText(level * hundred + "");
                            boxSpeed = Math.round(screenHeight / (boxSF - ((Integer.parseInt(levelValue) * 5) - 5)));
                            yellowCoinSpeed = Math.round(screenWidth / (yellowSF - ((Integer.parseInt(levelValue) * 10) - 10)));
                            blueCoinSpees = Math.round(screenWidth / (blueSF - ((Integer.parseInt(levelValue) * 10) - 10)));
                            yellowCoinSpeed1 = Math.round(screenWidth / (yellowSF1 - ((Integer.parseInt(levelValue) * 10) - 10)));
                            blueCoinSpees1 = Math.round(screenWidth / (blueSF1 - ((Integer.parseInt(levelValue) * 10) - 10)));
                            pinkCoinSpeed = Math.round(screenWidth / (pinkSF - ((Integer.parseInt(levelValue) * 10) - 10)));
                            bombSpeed = Math.round(screenWidth / (bombSF - ((7 * 10) - 10)));
                            break;
                        case "9":
                            getSupportActionBar().setTitle(R.string.levelNine);
                            accessScore.setText(level * hundred + "");
                            boxSpeed = Math.round(screenHeight / (boxSF - ((8 * 5) - 5)));
                            yellowCoinSpeed = Math.round(screenWidth / (yellowSF - ((Integer.parseInt(levelValue) * 10) - 10)));
                            blueCoinSpees = Math.round(screenWidth / (blueSF - ((Integer.parseInt(levelValue) * 10) - 10)));
                            yellowCoinSpeed1 = Math.round(screenWidth / (yellowSF1 - ((Integer.parseInt(levelValue) * 10) - 10)));
                            blueCoinSpees1 = Math.round(screenWidth / (blueSF1 - ((Integer.parseInt(levelValue) * 10) - 10)));
                            pinkCoinSpeed = Math.round(screenWidth / (pinkSF - ((Integer.parseInt(levelValue) * 10) - 10)));
                            bombSpeed = Math.round(screenWidth / (bombSF - ((7 * 10) - 10)));
                            break;
                        case "10":
                            getSupportActionBar().setTitle(R.string.levelTen);
                            accessScore.setText(level * hundred + "");
                            boxSpeed = Math.round(screenHeight / (boxSF - ((8 * 5) - 5)));
                            yellowCoinSpeed = Math.round(screenWidth / (yellowSF - ((Integer.parseInt(levelValue) * 10) - 10)));
                            blueCoinSpees = Math.round(screenWidth / (blueSF - ((Integer.parseInt(levelValue) * 10) - 10)));
                            yellowCoinSpeed1 = Math.round(screenWidth / (yellowSF1 - ((Integer.parseInt(levelValue) * 10) - 10)));
                            blueCoinSpees1 = Math.round(screenWidth / (blueSF1 - ((Integer.parseInt(levelValue) * 10) - 10)));
                            pinkCoinSpeed = Math.round(screenWidth / (pinkSF - ((Integer.parseInt(levelValue) * 10) - 10)));
                            bombSpeed = Math.round(screenWidth / (bombSF - ((7 * 10) - 10)));
                            break;
                        case "11":
                            getSupportActionBar().setTitle(R.string.levelEleven);
                            accessScore.setText(level * hundred + "");
                            boxSpeed = Math.round(screenHeight / (boxSF - ((8 * 5) - 5)));
                            yellowCoinSpeed = Math.round(screenWidth / (yellowSF - ((Integer.parseInt(levelValue) * 10) - 10)));
                            blueCoinSpees = Math.round(screenWidth / (blueSF - ((Integer.parseInt(levelValue) * 10) - 10)));
                            yellowCoinSpeed1 = Math.round(screenWidth / (yellowSF1 - ((Integer.parseInt(levelValue) * 10) - 10)));
                            blueCoinSpees1 = Math.round(screenWidth / (blueSF1 - ((Integer.parseInt(levelValue) * 10) - 10)));
                            pinkCoinSpeed = Math.round(screenWidth / (pinkSF - ((Integer.parseInt(levelValue) * 10) - 10)));
                            bombSpeed = Math.round(screenWidth / (bombSF - ((7 * 10) - 10)));
                            break;
                        case "12":
                            getSupportActionBar().setTitle(R.string.levelTwelve);
                            accessScore.setText(level * hundred + "");
                            boxSpeed = Math.round(screenHeight / (boxSF - ((8 * 5) - 5)));
                            yellowCoinSpeed = Math.round(screenWidth / (yellowSF - ((Integer.parseInt(levelValue) * 10) - 10)));
                            blueCoinSpees = Math.round(screenWidth / (blueSF - ((Integer.parseInt(levelValue) * 10) - 10)));
                            yellowCoinSpeed1 = Math.round(screenWidth / (yellowSF1 - ((Integer.parseInt(levelValue) * 10) - 10)));
                            blueCoinSpees1 = Math.round(screenWidth / (blueSF1 - ((Integer.parseInt(levelValue) * 10) - 10)));
                            pinkCoinSpeed = Math.round(screenWidth / (pinkSF - ((Integer.parseInt(levelValue) * 10) - 10)));
                            bombSpeed = Math.round(screenWidth / (bombSF - ((7 * 10) - 10)));
                            break;
                    }

                }
                break;
            case "2":
                // Here code to game levels medium
                break;
            case "3":
                // Here code to game levels hard
                break;
        }

        playpause.setEnabled(false);

        playpause.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                        if (appSound) {
                            soundPlayer.playClickSound();
                        }
                    }
                    viewButtons(0);
                    appSharedPreferences.writeString(Constants.serviceKey, "pause");
                    startService(new Intent(MainActivity.this, SoundService.class));
                } else {
                    if (timer == null) {
                        timer = new Timer();
                        timer.schedule(new MyTask(), 0, 20);
                        if (appSound) {
                            soundPlayer.playClickSound();
                        }
                    }
                    viewButtons(2);
                    appSharedPreferences.writeString(Constants.serviceKey, "start");
                    startService(new Intent(MainActivity.this, SoundService.class));
                    Log.v(Constants.log + "456", "start service 2");
                }
            }
        });

    }

    // TODO //////////////////// loadRewardedVideoAd ////////////////
    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd(Constants.resumeDialogRewardAd_b3_1,
                new AdRequest.Builder()
                        .build());
    }

    private void loadRewardedVideoAd1() {
        mRewardedVideoAd1.loadAd(Constants.watch_Btn_RewardAd_b3_1,
                new AdRequest.Builder()
                        .build());
    }

    public void viewButtons(final int play) {

        // Initialize Calss Mobile Ads
//        MobileAds.initialize(MainActivity.this, Constants.ID_App);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        // View Ads
        AdView adView = new AdView(MainActivity.this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(Constants.WinDialog_b1_8);
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
//                Log.v(Constants.log + "ads", "onAdFailedToLoad = " + adError.getCode());
//                new MainMenuActivity().onAdFailedToLoadErrorsCodes(MainActivity.this, adError.getCode());
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Log.v(Constants.log + "ads", "onAdClosed");
            }
        });

        menu.setVisibility(View.INVISIBLE);
        rerun.setVisibility(View.INVISIBLE);
        next.setVisibility(View.INVISIBLE);

        // number 1 = to next level, number 0 = from isChecked playpause btn, number 2 = from isnotChecked playpause btn;
        if (play == 1) {
            levelUp.setVisibility(View.VISIBLE);
            levelUpImage.setVisibility(View.VISIBLE);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            menu.setVisibility(View.VISIBLE);
                            rerun.setVisibility(View.VISIBLE);
                            next.setVisibility(View.VISIBLE);
                            next.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_nextgame1);
                        }
                    });
                }
            }, 2000);

        } else if (play == 0) {
            /*menu.setVisibility(View.VISIBLE);
            rerun.setVisibility(View.VISIBLE);
            next.setVisibility(View.VISIBLE);
            levelUp.setVisibility(View.VISIBLE);
            levelUpImage.setVisibility(View.GONE);*/
            playpause_dialog();
        } else {
            levelUp.setVisibility(View.GONE);
        }
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (appSound) {
                    soundPlayer.playFireworksSound(2);
                    soundPlayer.playClickSound();
                }

                switch (gameKey) {
                    case Constants.challengeYourselfValue:
                        // Show Interstitial Ads
                        if (mInterstitialAd1.isLoaded()) {
                            mInterstitialAd1.show();
                            Log.v(Constants.log + "132", "mInterstitialAd1.show.");
                        } else {
                            Log.v(Constants.log + "132", "The interstitial1 wasn't loaded yet.");
                        }
                        mInterstitialAd1.setAdListener(new AdListener() {
                            @Override
                            public void onAdLoaded() {
                                super.onAdLoaded();
                                Log.v(Constants.log + "132", "onAdLoaded");
                            }

                            @Override
                            public void onAdFailedToLoad(LoadAdError adError) {
                                super.onAdFailedToLoad(adError);
//                                Log.v(Constants.log + "132", "onAdFailedToLoad = " + i);
                                new MainMenuActivity().onAdFailedToLoadErrorsCodes(MainActivity.this, adError.getCode());
                                startActivity(new Intent(MainActivity.this, MainMenuActivity.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                            }

                            @Override
                            public void onAdClosed() {
                                super.onAdClosed();
                                Log.v(Constants.log + "132", "onAdClosed");
                                levelUp.setVisibility(View.GONE);
                                startActivity(new Intent(MainActivity.this, MainMenuActivity.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                            }
                        });
                        break;
                    case Constants.gameLevelEasyValue:
                        // Show Interstitial Ads
                        if (mInterstitialAd1.isLoaded()) {
                            mInterstitialAd1.show();
                            Log.v(Constants.log + "132", "mInterstitialAd1.show.");
                        } else {
                            Log.v(Constants.log + "132", "The interstitial1 wasn't loaded yet.");
                        }

                        mInterstitialAd1.setAdListener(new AdListener() {
                            @Override
                            public void onAdLoaded() {
                                super.onAdLoaded();
                                Log.v(Constants.log + "132", "onAdLoaded");
                            }

                            @Override
                            public void onAdFailedToLoad(LoadAdError adError) {
                                super.onAdFailedToLoad(adError);
//                                Log.v(Constants.log + "132", "onAdFailedToLoad = " + i);
                                new MainMenuActivity().onAdFailedToLoadErrorsCodes(MainActivity.this, adError.getCode());
                                startActivity(new Intent(MainActivity.this, Levels.class)
                                        .putExtra(Constants.activitykey, "pinkoin")
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                            }

                            @Override
                            public void onAdClosed() {
                                super.onAdClosed();
                                Log.v(Constants.log + "132", "onAdClosed");
                                levelUp.setVisibility(View.GONE);
                                startActivity(new Intent(MainActivity.this, Levels.class)
                                        .putExtra(Constants.activitykey, "pinkoin")
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                            }
                        });
                        break;
                }
            }
        });

        rerun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (appSound) {
                    soundPlayer.playFireworksSound(2);
                    soundPlayer.playClickSound();
                }
                levelUp.setVisibility(View.GONE);
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (play == 1) {
                    if (appSound) {
                        soundPlayer.playFireworksSound(2);
                        soundPlayer.playClickSound();
                    }
                    if (Integer.parseInt(levelValue) < 12) {
                        // Show Interstitial Ads
                        if (mInterstitialAd.isLoaded()) {
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
                            public void onAdFailedToLoad(LoadAdError adError) {
                                super.onAdFailedToLoad(adError);
//                                Log.v(Constants.log + "132", "onAdFailedToLoad = " + i);
                                new MainMenuActivity().onAdFailedToLoadErrorsCodes(MainActivity.this, adError.getCode());
                            }

                            @Override
                            public void onAdClosed() {
                                super.onAdClosed();
                                Log.v(Constants.log + "132", "onAdClosed");
                                levelUp.setVisibility(View.GONE);
                                int levelValue1 = Integer.parseInt(levelValue) + 1;
                                appSharedPreferences.writeInteger(Constants.levelValue, levelValue1);
                                Intent intent = getIntent();
                                finish();
                                Log.v(Constants.log + "6", Integer.parseInt(levelValue) + 1 + "");
                                startActivity(intent);
                            }
                        });
                    } else {
                        new MainMenuActivity().onAdFailedToLoadErrorsCodes(MainActivity.this, 4);
                    }
                } else {
                    if (appSound) {
                        soundPlayer.playClickSound();
                    }
                    if (timer == null) {
                        timer = new Timer();
                        timer.schedule(new MyTask(), 0, 20);
                    }
                    levelUp.setVisibility(View.GONE);
                    levelUpImage.setVisibility(View.VISIBLE);
                    playpause.setChecked(false);
                }
            }
        });
    }

    private void changePos() {

        hitCheck();

        //Yellow coin
        yCoinX -= yellowCoinSpeed;
        if (yCoinX < 0) {
            yCoinX = screenWidth + 50;
            yCoinY = (int) Math.floor(Math.random() * (frameHeight - yellowCoin.getHeight()));
        }
        yellowCoin.setX(yCoinX);
        yellowCoin.setY(yCoinY);

        //Blue coin
        bCoinX -= blueCoinSpees;
        if (bCoinX < 0) {
            bCoinX = screenWidth + 10;
            bCoinY = (int) Math.floor(Math.random() * (frameHeight - blueCoin.getHeight()));
        }
        blueCoin.setX(bCoinX);
        blueCoin.setY(bCoinY);

        //yellow coin 1
        yCoinX1 -= yellowCoinSpeed1;
        if (yCoinX1 < 0) {
            yCoinX1 = screenWidth + 400;
            yCoinY1 = (int) Math.floor(Math.random() * (frameHeight - yellowCoin1.getHeight()));
        }
        yellowCoin1.setX(yCoinX1);
        yellowCoin1.setY(yCoinY1);

        //Blue coin 1
        bCoinX1 -= blueCoinSpees1;
        if (bCoinX1 < 0) {
            bCoinX1 = screenWidth + 800;
            bCoinY1 = (int) Math.floor(Math.random() * (frameHeight - blueCoin1.getHeight()));
        }
        blueCoin1.setX(bCoinX1);
        blueCoin1.setY(bCoinY1);

        //Pink coin
        pCoinX -= pinkCoinSpeed;
        if (pCoinX < 0) {
            pCoinX = screenWidth + 3500;
            pCoinY = (int) Math.floor(Math.random() * (frameHeight - pinkCoin.getHeight()));
        }
        pinkCoin.setX(pCoinX);
        pinkCoin.setY(pCoinY);

        //Bomb
        bombX -= bombSpeed;
        if (bombX < 0) {
            bombX = screenWidth + 2500;
            bombY = (int) Math.floor(Math.random() * (frameHeight - bomb.getHeight()));
        }
        bomb.setX(bombX);
        bomb.setY(bombY);

        //Move box
        if (action_flg == true) {
            //Touching
            boxY -= boxSpeed;
        } else {
            //Releasing
            boxY += boxSpeed;
        }

        if (boxY < 0) boxY = 0;
        if (boxY > frameHeight - boxSize) boxY = frameHeight - boxSize;
        moneyBox.setY(boxY);

        scoreLabel.setText(score + "");
    }

    private void hitCheck() {

        //If the center of the coin is in the box, it counts as a hit.

        //Yellow coin
        int yellowCenterX = yCoinX + yellowCoin.getWidth() / 2;
        int yellowCenterY = yCoinY + yellowCoin.getHeight() / 2;

        // 0 <= yellowCenterX -`<= boxWidth
        // boxY <= yellowCenterY <= boxY + boxHeight
        if (0 <= yellowCenterX && yellowCenterX <= boxSize && boxY <= yellowCenterY && yellowCenterY <= boxY + boxSize) {

            if (appSound) {
                soundPlayer.playNormalCoinSound();
            }
            score += 5;
            yCoinX = -10;

            accessScore(0);

            startAnimationScoreZoomOut();

        }

        //Blue coin
        int blueCenterX = bCoinX + blueCoin.getWidth() / 2;
        int blueCenterY = bCoinY + blueCoin.getHeight() / 2;

        if (0 <= blueCenterX && blueCenterX <= boxSize && boxY <= blueCenterY && blueCenterY <= boxY + boxSize) {

            if (appSound) {
                soundPlayer.playNormal2CoinSound();
            }
            score += 5;
            bCoinX = -10;
            accessScore(0);

            startAnimationScoreZoomOut();

        }

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //Yellow coin
        int yellowCenterX1 = yCoinX1 + yellowCoin1.getWidth() / 2;
        int yellowCenterY1 = yCoinY1 + yellowCoin1.getHeight() / 2;

        // 0 <= yellowCenterX <= boxWidth
        // boxY <= yellowCenterY <= boxY + boxHeight
        if (0 <= yellowCenterX1 && yellowCenterX1 <= boxSize && boxY <= yellowCenterY1 && yellowCenterY1 <= boxY + boxSize) {

            if (appSound) {
                soundPlayer.playNormalCoinSound();
            }
            score += 5;
            yCoinX1 = -10;

            accessScore(0);

            startAnimationScoreZoomOut();

        }

        //Blue coin
        int blueCenterX1 = bCoinX1 + blueCoin1.getWidth() / 2;
        int blueCenterY1 = bCoinY1 + blueCoin1.getHeight() / 2;

        if (0 <= blueCenterX1 && blueCenterX1 <= boxSize && boxY <= blueCenterY1 && blueCenterY1 <= boxY + boxSize) {

            if (appSound) {
                soundPlayer.playNormal2CoinSound();
            }
            score += 5;
            bCoinX1 = -10;

            accessScore(0);

            startAnimationScoreZoomOut();

        }
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //Pink coin
        int pinkCenterX = pCoinX + pinkCoin.getWidth() / 2;
        int pinkCenterY = pCoinY + pinkCoin.getHeight() / 2;

        if (0 <= pinkCenterX && pinkCenterX <= boxSize && boxY <= pinkCenterY && pinkCenterY <= boxY + boxSize) {

            if (appSound) {
                soundPlayer.pinkCoinSound();
            }
            score += 20;
            pCoinX = -10;

            accessScore(1);
            Log.v(Constants.log + "pink", "accessScore/pinkCoin");

            startAnimationScoreZoomOut();
        }

        //Bomb
        int bombCenterX = bombX + bomb.getWidth() / 2;
        int bombCenterY = bombY + bomb.getHeight() / 2;

        if (0 <= bombCenterX && bombCenterX <= boxSize && boxY <= bombCenterY && bombCenterY <= boxY + boxSize) {

            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (vibrate) {
                    v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                }
            } else {
                if (vibrate) {
                    //deprecated in API 26
                    v.vibrate(500);
                }
            }
            if (appSound) {
                soundPlayer.BombSound();
            }
            // Stop Timer
            if (timer != null) {
                timer.cancel();
                timer = null;
                playpause.setEnabled(false);
            }

            bombX = -10;

            Log.v(Constants.log + "211", "note_loss_dialog");

            note_loss_dialog();

            handler.removeCallbacks(runnable);

        }

    }

    private void startAnimationScoreZoomOut() {
        scoreLabel.startAnimation(scoreAnimationZoomIn);
        handler = new Handler();

        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        scoreLabel.startAnimation(scoreAnimationZoomOut);
                    }
                });
            }
        }).start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (start_reward == true) {
            start_reward = false;
            startLabel.setVisibility(View.GONE);
            if (appSound) {
                soundPlayer.playClickSound();
            }
            if (timer == null) {
                timer = new Timer();
                timer.schedule(new MyTask(), 0, 20);
            }

        }
        if (start_flg == false) {
            start_flg = true;

            startService(new Intent(MainActivity.this, SoundService.class));
            Log.v(Constants.log + "456", "start service 3");

            frameHeight = frame.getHeight();
            boxY = (int) moneyBox.getY();
            boxSize = moneyBox.getHeight();

            startLabel.setVisibility(View.GONE);

            if (timer == null) {
                timer = new Timer();
                timer.schedule(new MyTask(), 0, 20);
            } else {
                timer.schedule(new MyTask(), 0, 20);
            }
            playpause.setEnabled(true);

        } else {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                action_flg = true;
                if (timer != null) {
                    if (appSound) {
                        if (boxSound) {
                            soundPlayer.playJumpingSound();
                        }
                    }
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                action_flg = false;
            }
        }

        return true;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_BACK:
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    public void accessScore(int pink) {
        if (gameKey.equals(Constants.challengeYourselfValue)) {

            int accessScore1 = Integer.parseInt(accessScore.getText().toString());
            if (score == accessScore1) {
                if (appSound) {
                    Log.v(Constants.log + "65", "(score == accessScore1)");
                    appSound = false;
                    soundPlayer.playHighScoreUpSound();
                    appSound = true;
                }
            }

        } else {
            if (pink == 1) {
                Log.v(Constants.log + "pink", "accessScore/pinkCoin 1111111");
                Log.v(Constants.log + "pink", "UP accessScore/pinkCoin scoreL = " + Integer.parseInt(scoreLabel.getText().toString()) + " >= accessS = " + Integer.parseInt(accessScore.getText().toString()));
            }
            int accessScore1 = Integer.parseInt(accessScore.getText().toString());
            if (score >= accessScore1) {
                Log.v(Constants.log + "1", "SCORE111");
                if (appSound) {
                    soundPlayer.playLevelUpSound();
                }
                viewButtons(1);
                if (appSound) {
                    soundPlayer.playFireworksSound(1);
                }
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                playpause.setEnabled(false);

                int newLevelValue = 0;

                int privateLevelValue = appSharedPreferences.readInteger(Constants.privateLevelValue);
                if (privateLevelValue > 0) {
                    // Here the level is added at the private level if the level is greater than the private level
                    if (Integer.parseInt(levelValue) > privateLevelValue) {
                        //TODO /////////firebaseToken///////////////
                        //add to database firebase
                        Log.v(Constants.log + "token", "if = " + levelValue);
                        String firebaseToken = appSharedPreferences.readString(Constants.Firebase_Token);
                        /*Firebase parentC = firebase.child(firebaseToken);
                        parentC.child("LevelValue").setValue(levelValue);*/
                        /////////////////////////////////////////////////
                        newLevelValue = Integer.parseInt(levelValue);
                        appSharedPreferences.writeInteger(Constants.privateLevelValue, newLevelValue);
                        Log.v(Constants.log + "2", "levelValue = " + newLevelValue + " / " + "privateLevelValue = " + privateLevelValue);
                    } else {
                        //add to database firebase
                        Log.v(Constants.log + "token", "if = " + levelValue);
                        String firebaseToken = appSharedPreferences.readString(Constants.Firebase_Token);
                        /*Firebase parentC = firebase.child(firebaseToken);
                        parentC.child("LevelValue").setValue(levelValue);*/
                        /////////////////////////////////////////////////
                        Log.v(Constants.log + "2", "Minimum levelValue = " + newLevelValue + " / " + "Same privateLevelValue = " + privateLevelValue);
                    }
                } else {
                    //add to database firebase
                    Log.v(Constants.log + "token", "else = " + levelValue);
                    String firebaseToken = appSharedPreferences.readString(Constants.Firebase_Token);
                    /*Firebase parentC = firebase.child(firebaseToken);
                    parentC.child("LevelValue").setValue(levelValue);*/
                    /////////////////////////////////////////////////
                    // Here the first level is added for the first time
                    newLevelValue = Integer.parseInt(levelValue);
                    appSharedPreferences.writeInteger(Constants.privateLevelValue, newLevelValue);
                    int privateLevelValue1 = appSharedPreferences.readInteger(Constants.privateLevelValue);
                    Log.v(Constants.log + "2", "levelValue first time = " + newLevelValue + " / " + "privateLevelValue first time = " + privateLevelValue1);
                }
            }
        }
    }

    private class MyTask extends TimerTask {
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    changePos();
                }
            });
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
//                new MainMenuActivity().onAdFailedToLoadErrorsCodes(MainActivity.this, adError.getCode());
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Log.v(Constants.log + "ads", "onAdClosed");
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
        } else {
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
        } else {
            viewScroll.setVisibility(View.VISIBLE);
            viewScroll.startAnimation(topAnimViewScroll);
            hideViewScroll++;
            appSharedPreferences1.writeInteger(Constants.hideViewScroll, hideViewScroll);
        }
        dialog1.setCancelable(false);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (start_flg == true) {
                    if (timer == null) {
                        timer = new Timer();
                        timer.schedule(new MyTask(), 0, 20);
                        if (appSound) {
                            soundPlayer.playClickSound();
                        }
                    }
                    appSharedPreferences.writeString(Constants.serviceKey, "start");
                    startService(new Intent(MainActivity.this, SoundService.class));
                }
                dialog1.dismiss();
            }
        });

        watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRewardedVideoAd.isLoaded()) {
                    Log.v(Constants.log + "232", "Main Menu Activity mRewardedVideoAd.isLoaded()");

                    mRewardedVideoAd.show();
                    between_two_rewardAd = true;
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu1, menu);

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
                        getString(R.string.sharemessage) + " " + "\n\n: " + Constants.URLGooglePlay);
                sendIntent.setType("text/plain");
//                sendIntent.setPackage("com.facebook.orca");
                try {
                    startActivity(sendIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(MainActivity.this, getString(R.string.installmessenger), Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.volumeItem2:
                invalidateOptionsMenu();
                Log.v(Constants.log + "122", "volumeItem2");

                break;
            case R.id.aboutItem4:
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                appSharedPreferences.writeString(Constants.serviceKey, "pause");
                startService(new Intent(MainActivity.this, SoundService.class));
                about_dialog(MainActivity.this);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (i % 2 == 0 && appSharedPreferences.readBoolean(Constants.appSound) == true) {
            Log.v(Constants.log + "122", "(x % 2 ==== 0) = " + i);
            i++;
            appSound = true;
            if (start_flg == true) {
                appSharedPreferences.writeString(Constants.serviceKey, "start");
                startService(new Intent(MainActivity.this, SoundService.class));
            }
            Log.v(Constants.log + "456", "start service 4");
            MenuItem settingsItem = menu.findItem(R.id.volumeItem2);
            settingsItem.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_volume_up));
        } else {
            i++;
            Log.v(Constants.log + "122", "(x % 2 !!!= 0) = " + i);
            appSound = false;
            appSharedPreferences.writeString(Constants.serviceKey, "pause");
            startService(new Intent(MainActivity.this, SoundService.class));
            Log.v(Constants.log + "456", "start service 5");
            MenuItem settingsItem = menu.findItem(R.id.volumeItem2);
            settingsItem.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_volume_off));
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void playpause_dialog() {

        final Dialog dialog1 = new Dialog(MainActivity.this);
        dialog1.show();
        dialog1.setContentView(R.layout.playpause_dialog);

        // Initialize Calss Mobile Ads
//        MobileAds.initialize(this, Constants.ID_App);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        // View Ads
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(Constants.PlayPause_b1_7);
        adView = dialog1.findViewById(R.id.adView1);
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
//                new MainMenuActivity().onAdFailedToLoadErrorsCodes(MainActivity.this, adError.getCode());
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Log.v(Constants.log + "ads", "onAdClosed");
            }
        });

        // with out background
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button menu = dialog1.findViewById(R.id.menu);
        Button rerun = dialog1.findViewById(R.id.rerun);
        Button playPause = dialog1.findViewById(R.id.playpause);

        dialog1.setCancelable(false);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (appSound) {
                    soundPlayer.playFireworksSound(2);
                    soundPlayer.playClickSound();
                }
                switch (gameKey) {
                    case Constants.challengeYourselfValue:
                        // Show Interstitial Ads
                        if (mInterstitialAd1.isLoaded()) {
                            mInterstitialAd1.show();
                            Log.v(Constants.log + "132", "mInterstitialAd1.show.");
                        } else {
                            Log.v(Constants.log + "132", "The interstitial1 wasn't loaded yet.");
                        }
                        mInterstitialAd1.setAdListener(new AdListener() {
                            @Override
                            public void onAdLoaded() {
                                super.onAdLoaded();
                                Log.v(Constants.log + "132", "onAdLoaded");
                            }

                            @Override
                            public void onAdFailedToLoad(LoadAdError adError) {
                                super.onAdFailedToLoad(adError);
                                Log.v(Constants.log + "132", "onAdFailedToLoad = " + adError.getCode());
                                new MainMenuActivity().onAdFailedToLoadErrorsCodes(MainActivity.this, adError.getCode());
                                startActivity(new Intent(MainActivity.this, MainMenuActivity.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                            }

                            @Override
                            public void onAdClosed() {
                                super.onAdClosed();
                                Log.v(Constants.log + "132", "onAdClosed");
                                dialog1.dismiss();
                                startActivity(new Intent(MainActivity.this, MainMenuActivity.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                            }
                        });
                        break;
                    case Constants.gameLevelEasyValue:
                        // Show Interstitial Ads
                        if (mInterstitialAd1.isLoaded()) {
                            mInterstitialAd1.show();
                            Log.v(Constants.log + "132", "mInterstitialAd1.show.");
                        } else {
                            Log.v(Constants.log + "132", "The interstitial1 wasn't loaded yet.");
                        }
                        mInterstitialAd1.setAdListener(new AdListener() {
                            @Override
                            public void onAdLoaded() {
                                super.onAdLoaded();
                                Log.v(Constants.log + "132", "onAdLoaded");
                            }

                            @Override
                            public void onAdFailedToLoad(LoadAdError adError) {
                                super.onAdFailedToLoad(adError);
                                Log.v(Constants.log + "132", "onAdFailedToLoad = " + adError.getCode());
//                                new MainMenuActivity().onAdFailedToLoadErrorsCodes(MainActivity.this, adError.getCode());
                                startActivity(new Intent(MainActivity.this, Levels.class)
                                        .putExtra(Constants.activitykey, "pinkoin")
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                            }

                            @Override
                            public void onAdClosed() {
                                super.onAdClosed();
                                Log.v(Constants.log + "132", "onAdClosed");
                                dialog1.dismiss();
                                startActivity(new Intent(MainActivity.this, Levels.class)
                                        .putExtra(Constants.activitykey, "pinkoin")
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                            }
                        });

                        break;
                }
            }
        });

        rerun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (appSound) {
                    soundPlayer.playFireworksSound(2);
                    soundPlayer.playClickSound();
                }
                Intent intent = getIntent();
                finish();
                startActivity(intent);

                dialog1.dismiss();
            }
        });

        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (appSound) {
                    soundPlayer.playClickSound();
                }
                if (timer == null) {
                    timer = new Timer();
                    timer.schedule(new MyTask(), 0, 20);
                }
                playpause.setChecked(false);

                dialog1.dismiss();
            }
        });

    }

    private void note_loss_dialog() {

        Typeface typeface;
        dialogReward = new Dialog(MainActivity.this);
        dialogReward.show();
        dialogReward.setContentView(R.layout.note_loss_dialog);

        // with out background
        dialogReward.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button watch = dialogReward.findViewById(R.id.watch);
        Button cancel = dialogReward.findViewById(R.id.cancel);
        final TextView watchTimer = dialogReward.findViewById(R.id.timer);
        TextView title_loss = dialogReward.findViewById(R.id.title_loss);
        TextView reward_content = dialogReward.findViewById(R.id.reward_content);

        if (Locale.getDefault().getLanguage().equals("ar")) {
            typeface = Typeface.createFromAsset(MainActivity.this.getAssets(), "homaarabic.ttf");
        } else {
            typeface = Typeface.createFromAsset(MainActivity.this.getAssets(), "comicscarton.ttf");
        }

        watch.setTypeface(typeface);
        cancel.setTypeface(typeface);
        watchTimer.setTypeface(typeface);
        title_loss.setTypeface(typeface);
        reward_content.setTypeface(typeface);

        dialogReward.setCancelable(false);

        runnableWatch = new Runnable() {

            public void run() {
                if (killMe) {
                    if (u >= 1) {
                        Log.v(Constants.log + "211", "run = " + u);
                        watchTimer.setText(u + "");
                        u--;
                    } else if (u == 0) {
                        // Here View Reward Ad
                        if (not_complete_rewardAd == true) {
                            not_complete_rewardAd = false;
                            between_two_rewardAd = false;
                            killMe = false;
                            dialogReward.dismiss();
                            startActivity(new Intent(MainActivity.this, Result.class)
                                    .putExtra(Constants.score, score)
                                    .putExtra(Constants.levelValue, levelValue));
                        } else {
                            appSharedPreferences.writeString(Constants.serviceKey, "pause");
                            startService(new Intent(MainActivity.this, SoundService.class));
                            Log.v(Constants.log + "456", "start service 6");
                            if (mRewardedVideoAd.isLoaded()) {
                                Log.v(Constants.log + "232", "mRewardedVideoAd.isLoaded()");
                                mRewardedVideoAd.show();
                            }
                            u--;
                            Log.v(Constants.log + "211", "(u !!!= 3) = " + u);
                        }
                    }

                    handler.postDelayed(this, 1000);
                } else {
                    handlerWatch.removeCallbacks(runnableWatch);
                }

                Log.v(Constants.log + "211", "Runnable is Running");
            }
        };
        handlerWatch.postDelayed(runnableWatch, 1500);
        //TODO ///////////watch////////////////
        watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                appSharedPreferences.writeString(Constants.serviceKey, "pause");
                startService(new Intent(MainActivity.this, SoundService.class));
                Log.v(Constants.log + "456", "start service 7");
                if (mRewardedVideoAd.isLoaded()) {
                    Log.v(Constants.log + "232", "mRewardedVideoAd.isLoaded()");

                    mRewardedVideoAd.show();
                    between_two_rewardAd = false;
                }
                handlerWatch.removeCallbacks(runnableWatch);
//                dialog1.dismiss();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                killMe = false;
                handlerWatch.removeCallbacks(runnableWatch);
                dialogReward.dismiss();
                // Show results
                startActivity(new Intent(MainActivity.this, Result.class)
                        .putExtra(Constants.score, score)
                        .putExtra(Constants.levelValue, levelValue));
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
        handlerWatch.removeCallbacks(runnableWatch);
        mRewardedVideoAd.destroy(this);
        super.onDestroy();
    }

    @Override
    public void onRewarded(RewardItem reward) {
        start_reward = true;
        btn_watch_reward = true;
        Log.v(Constants.log + "444", "btn_watch_reward true true");
        Log.v(Constants.log + "232", "onRewarded! currency: " + reward.getType() + "  amount: " +
                reward.getAmount());
        if (!between_two_rewardAd) {
            dialogReward.dismiss();
            startLabel.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        Log.v(Constants.log + "232", "onRewardedVideoAdLeftApplication");
    }

    @Override
    public void onRewardedVideoAdClosed() {

        loadRewardedVideoAd();
        loadRewardedVideoAd1();
        Log.v(Constants.log + "232", "onRewardedVideoAdClosed");

        appSharedPreferences.writeString(Constants.serviceKey, "start");
        startService(new Intent(MainActivity.this, SoundService.class));
        Log.v(Constants.log + "456", "start service 1");

        if (between_two_rewardAd) {
            if (btn_watch_reward == true) {
                btn_watch_reward = false;
                Log.v(Constants.log + "444", "btn_watch_reward true");
                alerter(getString(R.string.fantastic), getString(R.string.about_alerter_message), MainActivity.this, 1);
                //add to database firebase
                String firebaseToken = appSharedPreferences.readString(Constants.Firebase_Token);
                /*Firebase parentC = firebase.child(firebaseToken);
                int b = appSharedPreferences.readInteger(Constants.countWatchF);
                b++;
                parentC.child("RewardAdFantastic").setValue(b);
                appSharedPreferences.writeInteger(Constants.countWatchF, b);*/
                /////////////////////////////////////////////////
            } else if (btn_watch_reward == false) {
                Log.v(Constants.log + "444", "btn_watch_reward false");
                alerter(getString(R.string.good), getString(R.string.about_not_complete_alerter_message), MainActivity.this, 0);
                //add to database firebase
                String firebaseToken = appSharedPreferences.readString(Constants.Firebase_Token);
                /*Firebase parentC = firebase.child(firebaseToken);
                int y = appSharedPreferences.readInteger(Constants.countWatchG);
                y++;
                parentC.child("RewardAdGood").setValue(y);
                appSharedPreferences.writeInteger(Constants.countWatchG, y);*/
                /////////////////////////////////////////////////
            }
        } else {
            if (start_reward == true) {
                Log.v(Constants.log + "444", "start_reward true");
                alerter(getString(R.string.reward), getString(R.string.alerter_message), MainActivity.this, 1);
                //add to database firebase
                String firebaseToken = appSharedPreferences.readString(Constants.Firebase_Token);
                /*Firebase parentC = firebase.child(firebaseToken);
                int w = appSharedPreferences.readInteger(Constants.countWatchC);
                w++;
                parentC.child("RewardAdComplete").setValue(w);
                appSharedPreferences.writeInteger(Constants.countWatchC, w);*/
                /////////////////////////////////////////////////
            } else if (start_reward == false) {
                Log.v(Constants.log + "444", "start_reward false");
                killMe = true;
                u = 3;
                not_complete_rewardAd = true;
                alerter(getString(R.string.unfortunately), getString(R.string.alerter_message_not_complete_ad), MainActivity.this, 0);
                //add to database firebase
                String firebaseToken = appSharedPreferences.readString(Constants.Firebase_Token);
                /*Firebase parentC = firebase.child(firebaseToken);
                int z = appSharedPreferences.readInteger(Constants.countWatchNotC);
                z++;
                parentC.child("RewardAdNotComplete").setValue(z);
                appSharedPreferences.writeInteger(Constants.countWatchNotC, z);*/
                /////////////////////////////////////////////////
            }
        }
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
        Log.v(Constants.log + "232", "onRewardedVideoAdFailedToLoad = " + errorCode);
        new MainMenuActivity().onAdFailedToLoadErrorsCodes(MainActivity.this, errorCode);
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        Log.v(Constants.log + "232", "onRewardedVideoAdLoaded");
    }

    @Override
    public void onRewardedVideoAdOpened() {
        Log.v(Constants.log + "232", "onRewardedVideoAdOpened");
        appSharedPreferences.writeString(Constants.serviceKey, "pause");
        startService(new Intent(MainActivity.this, SoundService.class));
    }

    @Override
    public void onRewardedVideoStarted() {
        Log.v(Constants.log + "232", "onRewardedVideoStarted");
    }

    @Override
    public void onRewardedVideoCompleted() {
        Log.v(Constants.log + "232", "onRewardedVideoCompleted");
    }

    private void alerter(String title, String message, Context context, int playSound) {
        if (playSound == 1) {
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

