package in.ngsc.sixty;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.LevelEndEvent;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import in.ngsc.sixty.database.DbManager;
import in.ngsc.sixty.helpers.GameTimer;
import in.ngsc.sixty.helpers.Helper;
import in.ngsc.sixty.helpers.NumberManager;
import io.fabric.sdk.android.Fabric;

//https://developers.facebook.com/quickstarts/1597458496947906/?platform=android

public class MainActivity extends Helper {
    String msg = "MainActivity :: ";
    Map<String, Integer> variablesMap = new Hashtable();
    View myActivity;
    //SQLiteDatabase mydatabase;
    NumberManager numberManager;
    GameTimer myTimer;
    int helperStatus = 0;
    PopupWindow scorePopupWindow = null;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        createDatabase();
        Log.d(msg, "########### > The onCreate() event");
    }

    /**
     * Called when the activity is about to become visible.
     */
    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_main);
//        createDatabase();
        Cursor resultSet = getDataFromDatabase();
        resultSet.moveToFirst();
        textViewShowText(R.id.bestScore, "" + resultSet.getInt(0));
        if (resultSet.getInt(0) == 0) {
            helperStatus = 0;
            linearLayoutVisiblity(R.id.MainLinear0, View.VISIBLE);
            linearLayoutVisiblity(R.id.MainLinear1, View.GONE);
            linearLayoutVisiblity(R.id.MainLinear2, View.GONE);
            linearLayoutVisiblity(R.id.MainLinear3, View.GONE);
            linearLayoutVisiblity(R.id.MainLinear4, View.GONE);
            linearLayoutVisiblity(R.id.MainLinear5, View.GONE);
        }
        Log.d(msg, "########### > The onStart() event");
    }

    /**
     * Called when the activity has become visible.
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (scorePopupWindow != null && scorePopupWindow.isShowing()) {
            scorePopupWindow.dismiss();
        }
        Log.d(msg, "########### > The onResume() event");
    }

    /**
     * Called when another activity is taking focus.
     */
    @Override
    protected void onPause() {
        super.onPause();
        // gameOver();
        if (myTimer == null) {
            myTimer = new GameTimer(this, this);
        }
        myTimer.countDown = 0;
        Log.d(msg, "########### >The onPause() event");
    }

    /**
     * Called when the activity is no longer visible.
     */
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(msg, "########### >The onStop() event");
    }

    /**
     * Called just before the activity is destroyed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (scorePopupWindow != null && scorePopupWindow.isShowing()) {
            scorePopupWindow.dismiss();
        }
        Log.d(msg, "########### > The onDestroy() event");
    }


    public void showHelp(View v) {

        ImageView helpImage = findViewById(R.id.helpImage);
        String backgroundImageName = String.valueOf(helpImage.getTag());
        if (helperStatus == 0) {
            helpImage.setImageResource(R.drawable.help02);
            helperStatus = 1;
        } else {
            linearLayoutVisiblity(R.id.MainLinear0, View.GONE);
            linearLayoutVisiblity(R.id.MainLinear1, View.VISIBLE);
            linearLayoutVisiblity(R.id.MainLinear2, View.VISIBLE);
            linearLayoutVisiblity(R.id.MainLinear3, View.VISIBLE);
            linearLayoutVisiblity(R.id.MainLinear4, View.VISIBLE);
            linearLayoutVisiblity(R.id.MainLinear5, View.VISIBLE);
        }

    }


    public void startGame(View v) {
        myActivity = v;
        System.out.println(msg + "Started");
        // createDatabase();
        myTimer = new GameTimer(this, this);
        myTimer.timerInit();
        numberManager = new NumberManager(this, myTimer);
        numberManager.assignNumbers();

        //Cursor resultSet = getDataFromDatabase();
        //resultSet.moveToFirst();
        //textViewShowText(R.id.bestScore,""+resultSet.getInt(0));
        textViewVisiblity(R.id.correctAns, View.VISIBLE);
        textViewShowText(R.id.correctAns, "0");
        textViewVisiblity(R.id.wrongAns, View.VISIBLE);
        textViewShowText(R.id.wrongAns, "0");
    }


    public void restartGame(View v) {
        variablesMap = null;
        //SQLiteDatabase mydatabase;
        numberManager = null;
        startGame(v);

    }

    public void checkMyAnswer(View v) {
        if (numberManager != null)
            numberManager.checkMyAnswer(v);
    }

    public void gameOver() {

        Cursor resultSet = getDataFromDatabase();
        resultSet.moveToFirst();
        TextView correctAns = findViewById(R.id.correctAns);
        TextView wrongAns = findViewById(R.id.wrongAns);

        int bestScore = (resultSet.getInt(0) < Integer.parseInt(correctAns.getText().toString())) ? Integer.parseInt(correctAns.getText().toString()) : resultSet.getInt(0);
        int bestWrongScore = (resultSet.getInt(1) < Integer.parseInt(wrongAns.getText().toString())) ? Integer.parseInt(wrongAns.getText().toString()) : resultSet.getInt(1);

        saveIntoDatabase(bestScore, bestWrongScore);

        resultSet = getDataFromDatabase();
        resultSet.moveToFirst();

        int overallBestScore = resultSet.getInt(0);
        textViewShowText(R.id.bestScore, "" + overallBestScore);

        Answers.getInstance().logLevelEnd(new LevelEndEvent()
                .putLevelName("Level 1")
                .putScore(bestScore)
                .putSuccess(true));

//                .putCustomAttribute("CurrentScore" + bestScore)
//                .putCustomAttribute("bestWrongScore", bestWrongScore)
//                .putCustomAttribute("bestScore", overallBestScore)
//                .putCustomAttribute("DeviceDetails", Build.PRODUCT + " : " + Build.DEVICE));

        Log.d(msg, "I am back to main activity.... ");
        if (myTimer != null && myTimer.countDownInSec <= 0 && !this.isFinishing() && !this.isDestroyed()) {
            onButtonShowPopupWindowClick(myActivity);
        }
    }


    public Cursor getDataFromDatabase() {
        Cursor resultSet = getMydbConnection().rawQuery("Select * from TutorialsPoint", null);
        return resultSet;
    }

    public Cursor saveIntoDatabase(int correctAns, int wrongAns) {
        int myCorrectAns = correctAns;
        int myWrongAns = wrongAns;
        getMydbConnection().execSQL("delete from TutorialsPoint");
        Date currentDate = new Date();
        getMydbConnection().execSQL("INSERT INTO TutorialsPoint VALUES(" + myCorrectAns + "," + myWrongAns + "," + currentDate.getTime() + ");");
        return getDataFromDatabase();
    }

    @Override
    public void onBackPressed() {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setMessage("Do you wish to quit the game?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                finishAffinity();
                finish();
            } else {
                if (numberManager != null) {
                    numberManager.shuffleAround();
                }

            }
        }

        //super.onBackPressed();

    };

    private AdView mAdView;

    public void onButtonShowPopupWindowClick(View view) {

        if (this.isFinishing() || this.isDestroyed()) {
            return;
        }
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);
        TextView scoreCS = findViewById(R.id.correctAns);
        TextView scoreBS = findViewById(R.id.bestScore);
        TextView popuptextCS = popupView.findViewById(R.id.showcs);
        popuptextCS.setText(String.format("%s%s", popuptextCS.getText(), scoreCS.getText().toString()));
        TextView popuptextBS = popupView.findViewById(R.id.showbs);
        popuptextBS.setText(String.format("%s%s", popuptextBS.getText(), scoreBS.getText().toString()));

        ImageView shareButton = popupView.findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchScoreSharingUtil();
            }
        });

        ImageView exitButton = popupView.findViewById(R.id.exitButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scorePopupWindow.dismiss();
            }
        });

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        mAdView = popupView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        scorePopupWindow = new PopupWindow(popupView, width, height, focusable);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            scorePopupWindow.setElevation(2);
        }

        scorePopupWindow.setAnimationStyle(R.anim.bottom_up);
        scorePopupWindow.setFocusable(true);
        scorePopupWindow.setOutsideTouchable(true);
        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        scorePopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);


//
//        // dismiss the popup window when touched
//        popupView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    scorePopupWindow.dismiss();
//                }
//                return true;
//            }
//        });
    }

    private void launchScoreSharingUtil() {

        final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=in.ngsc.sixty");
//        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
//        intent.setType("image/png");
        startActivity(Intent.createChooser(intent, "Share image via"));

    }

    private SQLiteDatabase getMydbConnection() {
        return DbManager.getInstance(this).getWritableDatabase();
    }
}
