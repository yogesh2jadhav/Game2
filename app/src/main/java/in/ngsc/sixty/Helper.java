package in.ngsc.sixty;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by yogesh on 7/1/17.
 */

public class Helper extends AppCompatActivity {

    public void textViewVisiblity ( Integer id, Integer value){
        TextView textVew= (TextView) findViewById(id);
        textVew.setVisibility(value);
    }

    public void linearLayoutVisiblity ( Integer id, Integer value){
        LinearLayout linearLayout= (LinearLayout) findViewById(id);
        linearLayout.setVisibility(value);
    }

    public void textViewShowText( Integer id, String value){
        TextView textVew= (TextView) findViewById(id);
        textVew.setText(value);
    }

    public void textViewShowText( Integer id, String value, Activity myActivity){
        TextView textVew= (TextView) myActivity.findViewById(id);
        textVew.setText(value);
    }

    public void buttonVisiblity(Integer id, Integer value){
        Button button= (Button) findViewById(id);
        button.setVisibility(value);
    }

    public void buttonVisiblity(Integer id, Integer value, Activity myActivity){
        Button button= (Button) myActivity.findViewById(id);
        button.setVisibility(value);
    }

    public void buttonShowText( Integer id, String value){
        Button button= (Button) findViewById(id);
        button.setText(value);
    }


    public void buttonShowText( String id, String value){

        int resID = getResources().getIdentifier(id, "id", getPackageName());
        Button myButton = (Button) findViewById(resID);
        myButton.setText(value);

    }

    public void buttonShowText( String id, String value, Activity myActivity){

        int resID = myActivity.getResources().getIdentifier(id, "id", myActivity.getPackageName());
        Button myButton = (Button) myActivity.findViewById(resID);
        myButton.setText(value);

    }
}
