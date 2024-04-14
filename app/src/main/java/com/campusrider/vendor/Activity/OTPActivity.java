package com.campusrider.vendor.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.campusrider.vendor.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OTPActivity extends AppCompatActivity {
    EditText otptxt;
    Long timeoutsec=60L;
    TextView num,resend;
    String verificationcode;
    PhoneAuthProvider.ForceResendingToken resendingToken;
    ProgressBar progressBar;
    Button btn;
    String phone;
    FirebaseAuth mAuth= FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpactivity);
        otptxt=findViewById(R.id.otpeditTextNumber);
        progressBar=findViewById(R.id.progressBar);
        btn=findViewById(R.id.sendnbtn);
        num=findViewById(R.id.textview_num);
        resend=findViewById(R.id.resendOtp);

        phone=getIntent().getStringExtra("phone");
        num.setText("Enter OTP send on "+phone);
        sendOtp(phone,false);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteresOTP=otptxt.getText().toString();
                PhoneAuthCredential credential= PhoneAuthProvider.getCredential(verificationcode,enteresOTP);
                singIN(credential);

            }
        });


    }

    void sendOtp(String num,boolean isresend){
        PhoneAuthOptions.Builder builder=PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phone)
                .setTimeout(timeoutsec, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        singIN(phoneAuthCredential);
                        setInprogress(false);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(OTPActivity.this,"Verification Failed",Toast.LENGTH_SHORT).show();
                        setInprogress(false);
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verificationcode=s;
                        resendingToken=forceResendingToken;
                        Toast.makeText(OTPActivity.this,"OTP Sent Successfully",Toast.LENGTH_SHORT).show();
                        setInprogress(false);
                    }
                });
        System.out.println("OTP"+ verificationcode);
        if(isresend){
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(resendingToken).build());
        }else{
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }

    }
    void singIN(PhoneAuthCredential phoneAuthCredential){

        setInprogress(true);
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                setInprogress(false);
                if(task.isSuccessful()){
                    startActivity(new Intent(OTPActivity.this, LoginActivity.class));
                }else{
                    Toast.makeText(OTPActivity.this,"OTP verification failed",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    void  setInprogress(boolean inprogress){
        if(inprogress){
            progressBar.setVisibility(View.VISIBLE);
            btn.setVisibility(View.GONE);
        }
        else {
            progressBar.setVisibility(View.GONE);
            btn.setVisibility(View.VISIBLE);
        }
    }

}