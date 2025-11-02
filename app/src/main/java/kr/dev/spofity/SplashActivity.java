package kr.dev.spofity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private  SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                Class orientation;

                if(sharedPreferences.getBoolean("isLoggedIn", false)){

                    orientation = PlayerActivity2.class;
                }
                else{
                    orientation = MainActivity.class;
                }

                startActivity(new Intent(SplashActivity.this, orientation));
                finish();

            }
        },2000);
    }
}