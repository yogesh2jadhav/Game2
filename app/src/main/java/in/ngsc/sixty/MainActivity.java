package in.ngsc.sixty;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import  in.ngsc.sixty.R;

import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import in.ngsc.sixty.GameTimer;
import in.ngsc.sixty.Helper;
import in.ngsc.sixty.NumberManager;

//https://developers.facebook.com/quickstarts/1597458496947906/?platform=android

public class MainActivity extends Helper {
    String msg = "MainActivity :: ";
    Map<String, Integer> variablesMap = new Hashtable<>();
    SQLiteDatabase mydatabase;
    NumberManager numberManager;
    GameTimer myTimer;
 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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


    public void startGame(View v){
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
}
