package org.techtown.hanselandgretel;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Comment;

public class MainActivity extends AppCompatActivity {

    //현재 단계 저장
    private int stagelevel;

    private static MediaPlayer backMusic;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mReference;
    ImageView cookie, logo;
    TextView stage, cur, goal, gpscur, gpsgoal;

    String answers[] = {"곡성","simple","10","ㅊㅍㄱ","seeyouagain"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //인증 정보를 잘 가져오는지 확인하는과정 인스턴스를 가져온다
        mFirebaseAuth =  FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mReference = mFirebaseDatabase.getReference();

        if(mFirebaseUser == null){
            startActivity(new Intent(MainActivity.this, AuthActivity.class));
            finish();
            return;
        }


        //이미지, 버튼 설정
        cookie = (ImageView) findViewById(R.id.cookie);
        stage = (TextView) findViewById(R.id.stage);
        logo = (ImageView)findViewById(R.id.logo);
        cur = (TextView) findViewById(R.id.cur);
        goal = (TextView) findViewById(R.id.goal);
        gpscur = (TextView) findViewById(R.id.gpscur);
        gpsgoal = (TextView) findViewById(R.id.gpsgoal);

        Button key = (Button) findViewById(R.id.key) ;
        Button question = (Button) findViewById(R.id.question) ;
        ImageButton dod = (ImageButton)findViewById(R.id.dod);


        //배경음악
        backMusic = MediaPlayer.create(this, R.raw.bgm);
        backMusic.setLooping(true);
        backMusic.start();

        //gsp 설정 해줘야함
        gpscur.setText(" 12345"+" . "+"233");
        gpsgoal.setText(" 19845"+" . "+"4423");

       //답설정

        //단계설정 database에서 가져와야함 ***
        Query myMostViewedPostsQuery = mReference.child("users").child(mFirebaseUser.getUid())
                .orderByChild("stagelevel");
        myMostViewedPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Log.d("MainActivity", "Single ValueEventListener :" + snapshot.getValue());

                        stagelevel = Integer.parseInt(snapshot.getValue().toString());

                        if(stagelevel >= 5){
                            Toast.makeText(getApplicationContext(),"완료하였으므로 데이터를 초기화 합니다",Toast.LENGTH_LONG).show();
                            stagelevel =0;
                        }
                        Log.d("mian stage level: ", ""+stagelevel);
                    stage.setText(String.valueOf(stagelevel));

                    //int i = Integer.valueOf((String) object);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        stage.setText(String.valueOf(stagelevel));


        //문제확인버튼 클릭시
        question.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(getApplicationContext(),quiz.class);
                //startActivityForResult(intent,sub);//액티비티 띄우기
                Intent intent = new Intent(MainActivity.this, QuizActivity.class);
                Log.d("stage level btn : ", ""+stagelevel);
                intent.putExtra("stagelevel", stagelevel);
                startActivity(intent);
            }
        });

        //돋보기 버튼 클릭시
        dod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        //키제출 버튼 클릭시
        key.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnPopupClick(view);
            }


        });

    }





    //음악 출력 메서드
    @Override
    protected void onUserLeaveHint() {
        backMusic.pause();
        super.onUserLeaveHint();
    }

    @Override
    protected void onPostResume() {
        backMusic.start();
        super.onPostResume();
    }

    @Override
    protected void onDestroy() {
        backMusic.stop();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        backMusic.stop();
        super.onBackPressed();
    }


    //정답입력 팝업 창
    public void mOnPopupClick(View v){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("정답을 입력하세요");
        alert.setMessage("");


        final EditText name = new EditText(this);
        alert.setView(name);

        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String answer = name.getText().toString();
                if(answer.equalsIgnoreCase(answers[stagelevel]))
                {
                    Toast.makeText(getApplicationContext(),"정답입니다!",Toast.LENGTH_LONG).show();
                    stagelevel++;
                    if(stagelevel >= 5) {
                        Toast.makeText(getApplicationContext(), "미션 Clear!!!--Ending", Toast.LENGTH_LONG).show();
                        stagelevel =0;
                    }
                    stage.setText(String.valueOf(stagelevel));

                    //Log.d("mainActivity: ", mFirebaseUser.getUid());

                    //데이터베이스에 현재 스테이지 레벨 저장.
                    mReference.child("users").child(mFirebaseUser.getUid()).child("stagelevel").setValue(stagelevel);


                }
                else
                {
                    Toast.makeText(getApplicationContext(),"틀렸습니다!",Toast.LENGTH_LONG).show();
                }
            }
        });

        alert.setNegativeButton("취소",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });

        alert.show();
    }

}
