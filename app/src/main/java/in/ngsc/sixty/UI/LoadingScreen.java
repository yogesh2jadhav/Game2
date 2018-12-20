package in.ngsc.sixty.UI;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;

import in.ngsc.sixty.MainActivity;
import in.ngsc.sixty.R;
import io.fabric.sdk.android.Fabric;

public class LoadingScreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_loading);


        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                ImageView rl = findViewById(R.id.MainLoadingImg);
                rl.setImageResource(R.drawable.splash1);

                Button startGame = findViewById(R.id.startGame);
                startGame.setVisibility(View.VISIBLE);

                // close this activity
                // finish();
            }
        }, SPLASH_TIME_OUT);

    }


    public void onSlpashStartGame(View v) {

        Intent i = new Intent(LoadingScreen.this, MainActivity.class);
        startActivity(i);
        // close this activity
        finish();
    }
}