package in.ngsc.sixty.helpers;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import in.ngsc.sixty.MainActivity;
import  in.ngsc.sixty.R;

import java.util.Timer;
import java.util.TimerTask;

public class GameTimer extends Helper {

    Activity myView;
    MainActivity myMainActivity;
    private String msg = "GameTimer Class ::";
    ProgressBar progressBar;
    TextView countDownTime;
    double progressInt;
    TimerTask mTt1;
    Timer mTimer1 ;
    android.os.Handler mTimerHandler = new android.os.Handler();
    public int countDownInSec;
    public int countDown=0;

    public GameTimer(){

    }
    public GameTimer(Activity myView, MainActivity myMainActivity) {
        this.myView=myView;
        this.myMainActivity=myMainActivity;
        this.countDown=61000;
    }


    public void timerInit( ){
        buttonVisiblity(R.id.startGame,View.GONE, myMainActivity);
        buttonVisiblity(R.id.restart,View.GONE,myMainActivity);
        startTimer();
    }
    public void startTimer(){
        Log.d(msg, "startTimer : ");
        progressBar= myView.findViewById(R.id.circularProgressbar);
        progressBar.setProgress(0);
        progressBar.setSecondaryProgress(100);
        countDownTime =(TextView) myView.findViewById(R.id.countDownTime);
        progressInt=0.1;
        mTimer1 = new Timer();
        mTt1 = new TimerTask() {
            public void run() {
                mTimerHandler.post(new Runnable() {

                    public void run(){
                        //Log.d(msg, "Timer is started................   ");
                        countDown=countDown-1000;
                        progressBar.setProgress((int)Math.ceil(progressInt));
                        progressInt+=1.65;
                        countDownInSec=(countDown/1000);
                        if(countDownInSec<=20)
                            countDownTime.setTextColor(Color.RED);
                        else if(countDownInSec>20 && countDownInSec<=40)
                            countDownTime.setTextColor(myView.getResources().getColor(R.color.orang));
                        else if(countDownInSec>40)
                            countDownTime.setTextColor(myView.getResources().getColor(R.color.green));

                        if(countDownInSec<0)
                            countDownTime.setText("0");
                        else
                            countDownTime.setText(Integer.toString(countDownInSec));


                        if(countDown<=1){
                            Log.d(msg, "startTimer : countDown="+countDown);
                            timeClose();
                            mTimer1.cancel();
                            progressBar.setProgress(100);
                        }
                    }
                });
            }
        };

        mTimer1.schedule(mTt1, 1, 1000);
    }

    public void timeClose(){
        myMainActivity.gameOver();
        buttonVisiblity(R.id.restart,View.VISIBLE,myMainActivity);
     }
}
