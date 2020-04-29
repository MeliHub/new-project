package com.reidius.lawrenceafriyie.overwatchmap.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crash.FirebaseCrash;
import com.reidius.lawrenceafriyie.overwatchmap.R;
import com.reidius.lawrenceafriyie.overwatchmap.activity.MainActivity;

public class Splash extends AppCompatActivity {

    private TextView textView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        textView = (TextView) findViewById(R.id.reidius_text);
        imageView = (ImageView) findViewById(R.id.reidius);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.transition);
        textView.startAnimation(anim);
        imageView.startAnimation(anim);
        final Intent intent = new Intent(this, SigninActivity.class);
        Thread timer = new Thread() {
            public void run() {
                try{
                    sleep(5000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                } finally {
                    startActivity(intent);
                    finish();
                }
            }
        };
        timer.start();
    }
}
