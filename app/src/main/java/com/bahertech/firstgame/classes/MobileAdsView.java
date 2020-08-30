package com.bahertech.firstgame.classes;

import android.content.Context;
import android.util.Log;

import com.bahertech.firstgame.R;
import com.bahertech.firstgame.interfaces.Constants;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class MobileAdsView {
    AdView adView;
    AdRequest adRequest;

    public MobileAdsView(Context context){
        // Initialize Calss Mobile Ads
//        com.google.android.gms.ads.MobileAds.initialize(context, Constants.ID_App);
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        // View Ads
        adView = new AdView(context);
        adRequest = new AdRequest.Builder().build();
    }

    public void bannerAds(String b){
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(b);
        adView = adView.findViewById(R.id.adView1);
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
            }
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Log.v(Constants.log+"ads", "onAdClosed");
            }
        });
    }
}
