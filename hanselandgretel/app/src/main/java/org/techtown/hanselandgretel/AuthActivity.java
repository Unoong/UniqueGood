package org.techtown.hanselandgretel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import android.app.AlertDialog;

import java.util.ArrayList;


public class AuthActivity extends AppCompatActivity
    implements GoogleApiClient.OnConnectionFailedListener {

    private SignInButton mSigninBtn;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        mSigninBtn = (SignInButton) findViewById(R.id.sign_in_btn);
        mFirebaseAuth = FirebaseAuth.getInstance();
        //mFirebaseAuth.signOut();


        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("274703449775-i82jme7htuetqspdftp0d8nsffvh40ae.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,googleSignInOptions)
                .build();

        mSigninBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(intent, 100);
            }
        });

        ///*************
        Button logout_btn_google = (Button) findViewById(R.id.logout_google);
        logout_btn_google.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(final View view) {

                Log.v("알림", "구글 LOGOUT");

                AlertDialog.Builder alt_bld = new AlertDialog.Builder(view.getContext());

                alt_bld.setMessage("로그아웃 하시겠습니까?").setCancelable(false)

                        .setPositiveButton("네",

                                new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int id) {

                                        // 네 클릭
                                        // 로그아웃 함수 call
                                        signOut();

                                    }

                                }).setNegativeButton("아니오",

                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // 아니오 클릭. dialog 닫기.
                                dialog.cancel();

                            }

                        });

                AlertDialog alert = alt_bld.create();


                // 대화창 클릭시 뒷 배경 어두워지는 것 막기
                //alert.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                // 대화창 제목 설정

                alert.setTitle("로그아웃");
                // 대화창 아이콘 설정

                //alert.setIcon(R.drawable.check_dialog_64);


                // 대화창 배경 색 설정

                alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(255,62,79,92)));

                alert.show();

            }

        });
        //*************************

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            //Toast.makeText(AuthActivity.this, "로그인 되었습니다.",Toast.LENGTH_LONG).show();

            GoogleSignInResult result
                    = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            GoogleSignInAccount account = result.getSignInAccount();
            if (result.isSuccess()){
                Toast.makeText(AuthActivity.this,"로그인 되었습니다.",Toast.LENGTH_LONG ).show();
                firebaseWithGoogle(account);
            } else{
                Toast.makeText(AuthActivity.this,  "인증에 실패하셨습니다",Toast.LENGTH_LONG).show();
            }
            firebaseWithGoogle(account);
        }
    }

    //로그아웃 메서드
    public void signOut() {
        mGoogleApiClient.connect();
        mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                mFirebaseAuth.signOut();
                if (mGoogleApiClient.isConnected()) {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {

                        @Override
                        public void onResult(@NonNull Status status) {
                            if (status.isSuccess()) {
                                Log.v("알림", "로그아웃 성공");
                                setResult(1);
                            } else {
                                setResult(0);
                            }
                        }
                    });
                }
            }

            @Override
            public void onConnectionSuspended(int i) {
                Log.v("알림", "Google API Client Connection Suspended");
                setResult(-1);
                finish();
            }
        });
    }

    //구글 인증정보 가져오기
    private void firebaseWithGoogle(GoogleSignInAccount account){
        AuthCredential credential
                = GoogleAuthProvider.getCredential(account.getIdToken(),null);

        Task<AuthResult> authResultTask
                = mFirebaseAuth.signInWithCredential(credential);

        authResultTask.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Intent intent = new Intent(AuthActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                //finish();
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult){

    }

}
