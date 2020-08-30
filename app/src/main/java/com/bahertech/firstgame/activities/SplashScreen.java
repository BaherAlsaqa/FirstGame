package com.bahertech.firstgame.activities;

import android.content.Intent;
import android.graphics.Typeface;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.bahertech.firstgame.R;

import java.util.Locale;

public class SplashScreen extends AppCompatActivity {

    private Typeface typeface;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        textView = (TextView)findViewById(R.id.app_name_value);

        if (Locale.getDefault().getLanguage().equals("ar")) {
            typeface = Typeface.createFromAsset(SplashScreen.this.getAssets(), "homaarabic.ttf");
        }else{
            typeface = Typeface.createFromAsset(SplashScreen.this.getAssets(), "comicscarton.ttf");
        }

        textView.setTypeface(typeface);

        Thread mythread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(4100);
                    startActivity(new Intent(getApplicationContext(),MainMenuActivity.class));
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        mythread.start();
    }
}
