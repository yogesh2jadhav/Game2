package in.ngsc.sixty;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import  in.ngsc.sixty.R;

import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import in.ngsc.sixty.GameTimer;
import in.ngsc.sixty.Helper;
import in.ngsc.sixty.NumberManager;
import io.fabric.sdk.android.Fabric;

//https://developers.facebook.com/quickstarts/1597458496947906/?platform=android

public class MainActivity extends Helper {
    String msg = "MainActivity :: ";
    Map<String, Integer> variablesMap = new Hashtable();
    View myActivity;
    SQLiteDatabase mydatabase;
    NumberManager numberManager;
    GameTimer myTimer;
    int helperStatus = 0;
 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!BuildConfig.DEBUG){
            Fabric.with(this, new Crashlytics());
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        createDatabase();
        Log.d(msg, "########### > The onCreate() event");
    }

    /** Called when the activity is about to become visible. */
    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_main);
        createDatabase();
        Cursor resultSet = getDataFromDatabase();
        resultSet.moveToFirst();
        textViewShowText(R.id.bestScore,""+resultSet.getInt(0));
        if (resultSet.getInt(0)==0){
            helperStatus=0;
            linearLayoutVisiblity(R.id.MainLinear0,View.VISIBLE);
            linearLayoutVisiblity(R.id.MainLinear1,View.GONE);
            linearLayoutVisiblity(R.id.MainLinear2,View.GONE);
            linearLayoutVisiblity(R.id.MainLinear3,View.GONE);
            linearLayoutVisiblity(R.id.MainLinear4,View.GONE);
            linearLayoutVisiblity(R.id.MainLinear5,View.GONE);
        }
        Log.d(msg, "########### > The onStart() event");
    }

    /** Called when the activity has become visible. */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(msg, "########### > The onResume() event");
    }

    /** Called when another activity is taking focus. */
    @Override
    protected void onPause() {
        super.onPause();
        gameOver();
        if(myTimer==null)
            myTimer =  new GameTimer(this, this);
        myTimer.countDown=0;
        Log.d(msg, "########### >The onPause() event");
    }

    /** Called when the activity is no longer visible. */
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(msg, "########### >The onStop() event");
    }

    /** Called just before the activity is destroyed. */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(msg, "########### > The onDestroy() event");
    }


    public void showHelp(View v){

        ImageView helpImage = findViewById(R.id.helpImage);
        String backgroundImageName = String.valueOf(helpImage.getTag());
        if(helperStatus==0){
            helpImage.setImageResource(R.drawable.help02);
            helperStatus=1;
        }
        else{
            linearLayoutVisiblity(R.id.MainLinear0,View.GONE);
            linearLayoutVisiblity(R.id.MainLinear1,View.VISIBLE);
            linearLayoutVisiblity(R.id.MainLinear2,View.VISIBLE);
            linearLayoutVisiblity(R.id.MainLinear3,View.VISIBLE);
            linearLayoutVisiblity(R.id.MainLinear4,View.VISIBLE);
            linearLayoutVisiblity(R.id.MainLinear5,View.VISIBLE);
        }

    }


    public void startGame(View v){
        myActivity=v;
        System.out.println(msg + "Started");
       // createDatabase();
        myTimer = new GameTimer(this, this);
        myTimer.timerInit();
        numberManager = new NumberManager(this, myTimer);
        numberManager.assignNumbers();

        //Cursor resultSet = getDataFromDatabase();
        //resultSet.moveToFirst();
        //textViewShowText(R.id.bestScore,""+resultSet.getInt(0));
        textViewVisiblity(R.id.correctAns,View.VISIBLE);
        textViewShowText(R.id.correctAns,"0");
        textViewVisiblity(R.id.wrongAns,View.VISIBLE);
        textViewShowText(R.id.wrongAns,"0");
    }


    public void restartGame(View v){
        variablesMap = null;
        //SQLiteDatabase mydatabase;
        numberManager=null;
        startGame(v);

    }

    public void checkMyAnswer(View v){
        if(numberManager!=null)
            numberManager.checkMyAnswer(v);
    }
    public void gameOver(){

        Cursor resultSet = getDataFromDatabase();
        resultSet.moveToFirst();
        TextView correctAns = findViewById(R.id.correctAns);
        TextView wrongAns = findViewById(R.id.wrongAns);

        int bestScore = (resultSet.getInt(0)<Integer.parseInt(correctAns.getText().toString()))?Integer.parseInt(correctAns.getText().toString()):resultSet.getInt(0);
        int bestWrongScore = (resultSet.getInt(1)<Integer.parseInt(wrongAns.getText().toString()))?Integer.parseInt(wrongAns.getText().toString()):resultSet.getInt(1);

        saveIntoDatabase(bestScore,bestWrongScore);

        resultSet = getDataFromDatabase();
        resultSet.moveToFirst();
        textViewShowText(R.id.bestScore,""+resultSet.getInt(0));
        Log.d(msg, "I am back to main activity.... ");
        if(myTimer.countDownInSec<=0) onButtonShowPopupWindowClick(myActivity);
    }

    public void createDatabase(){
        mydatabase = openOrCreateDatabase("myAppDb",MODE_PRIVATE,null);
        Date currentDate = new Date();
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS TutorialsPoint(correctAnswers INT,wrongAnswer INT, createdDate DATE);");
        mydatabase.execSQL("INSERT INTO TutorialsPoint VALUES(0,0,"+currentDate.getTime()+");");
    }


    public Cursor getDataFromDatabase(){
        Cursor resultSet = mydatabase.rawQuery("Select * from TutorialsPoint",null);
        return resultSet;
    }

    public Cursor saveIntoDatabase(int correctAns, int wrongAns){
        int myCorrectAns = correctAns;
        int myWrongAns = wrongAns;
        mydatabase.execSQL("delete from TutorialsPoint");
        Date currentDate = new Date();
        mydatabase.execSQL("INSERT INTO TutorialsPoint VALUES("+myCorrectAns+","+myWrongAns+","+currentDate.getTime()+");");
        return getDataFromDatabase();
    }

    @Override
    public void onBackPressed() {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setMessage("Do you wish to quit the game?").setPositiveButton("Yes",dialogClickListener).setNegativeButton("No", dialogClickListener).show();
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                finishAffinity();
                finish();
            } else {
                if(numberManager!=null){
                    numberManager.shuffleAround();
                }

            }
        }

        //super.onBackPressed();

    };


    public void onButtonShowPopupWindowClick(View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);
        TextView scoreCS = findViewById(R.id.correctAns);
        TextView scoreBS = findViewById(R.id.bestScore);
        TextView popuptextCS = popupView.findViewById(R.id.showcs);
        popuptextCS.setText(String.format("%s%s", popuptextCS.getText(), scoreCS.getText().toString()));
        TextView popuptextBS = popupView.findViewById(R.id.showbs);
        popuptextBS.setText(String.format("%s%s", popuptextBS.getText(), scoreBS.getText().toString()));

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.setElevation(2);
        }
        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);


        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }
}
