package org.techtown.hanselandgretel;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class QuizActivity extends AppCompatActivity {

    ConstraintLayout layout;
    Adapter adapter;
    ViewPager viewPager;

    String hint[] = {"방","탄","최","고"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // 아까 만든 view
        viewPager = (ViewPager)findViewById(R.id.view);
        //adapter 초기화
        adapter = new Adapter(this);
        viewPager.setAdapter(adapter);

        layout = (ConstraintLayout)findViewById(R.id.layout2);
        new CountDownTask().execute();


    }
    private class CountDownTask extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            for(int i=3; i>=0; i--){
                try{Thread.sleep(1000);
                    publishProgress(i);
                }catch (Exception e){}
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            Button btn = new Button(getApplicationContext());

            btn.setText("힌트");
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(QuizActivity.this);


                    builder.setTitle("힌트").setMessage(hint[0]);

                    AlertDialog alertDialog = builder.create();

                    alertDialog.show();
                }
            });
            btn.setTextColor(Color.RED);
            btn.setTextSize(20);
            layout.addView(btn);
        }
    }
}