package com.ritara.svustudent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ritara.svustudent.utils.SharedPreferences_SVU;


public class SplashScreenActivity extends Activity {

  ImageView imageViewSplash;
  TextView txtAppName ,university;
  RelativeLayout relativeLayout;
  Thread SplashThread;
  SharedPreferences_SVU sharedPreferences_svu;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash_screen);

    sharedPreferences_svu = SharedPreferences_SVU.getInstance(this);

    imageViewSplash = (ImageView) findViewById(R.id.imageViewSplash);
    txtAppName = (TextView) findViewById(R.id.txtAppName);
    university = (TextView) findViewById(R.id.university);
    relativeLayout = (RelativeLayout) findViewById(R.id.relative);

    startAnimations();
  }

  private void startAnimations() {

    Animation rotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
    Animation translate = AnimationUtils.loadAnimation(this, R.anim.translate);

    rotate.reset();
    translate.reset();
    relativeLayout.clearAnimation();

    imageViewSplash.startAnimation(rotate);
    university.startAnimation(translate);
    txtAppName.startAnimation(translate);
    SplashThread = new Thread(){
      @Override
      public void run() {
        super.run();
        int waited = 0;
        while (waited < 4500) {
          try {
            sleep(100);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          waited += 100;
        }
        SplashScreenActivity.this.finish();

        Intent intent;

        if(sharedPreferences_svu.getTrainingDone()){
          intent = new Intent(SplashScreenActivity.this, Dashboard.class);
        }else{
          intent = new Intent(SplashScreenActivity.this, MainActivity.class);
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
      }
    };
    SplashThread.start();
  }
}