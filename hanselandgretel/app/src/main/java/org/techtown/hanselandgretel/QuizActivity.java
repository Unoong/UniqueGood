package org.techtown.hanselandgretel;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class QuizActivity extends AppCompatActivity {

    ConstraintLayout layout;
    Adapter adapter;
    ViewPager viewPager;
    public int stagelevel;
    public static Context context;
    String hint[] = {"\'눈\'을 거꾸로 봐보자!","Small..Middle..Large..?,","밭전 자에 테투리를 없애면..?","숫자 세기", "화살표 방향, 초록:green"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        stagelevel = getIntent().getIntExtra("stagelevel", 4);
        Log.d("stage",""+stagelevel);

        context = this;
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
            for(int i=10; i>=0; i--){
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

                    Log.d("stagehint : ", ""+stagelevel);
                    builder.setTitle("힌트").setMessage(hint[stagelevel]);

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