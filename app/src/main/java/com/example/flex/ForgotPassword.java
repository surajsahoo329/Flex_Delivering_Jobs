package com.example.flex;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ForgotPassword extends AppCompatActivity {

    EditText etEmail;
    Button btnResetPassword;
    View parentLayout;
    static int sentMailFlag = 0;

    @RequiresApi(api=Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        TextView textView=findViewById(R.id.forgotPassTitle);
        Typeface myFont=Typeface.createFromAsset(Objects.requireNonNull(this).getAssets(), "fonts/coolvetica_i.ttf");
        textView.setTypeface(myFont);

        etEmail=findViewById(R.id.etEmail);
        btnResetPassword=findViewById(R.id.btnChPass);
        parentLayout=findViewById(android.R.id.content);


        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().sendPasswordResetEmail(etEmail.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                ProgressDialog pd =ProgressDialog.show(ForgotPassword.this,"Sending email","Please wait...",true);

                                if( task.isSuccessful()) {
                                    pd.dismiss();
                                    sentMailFlag = 1;
                                    Intent it = new Intent(ForgotPassword.this, LoginActivity.class);
                                    startActivity(it);
                                    finish();
                                } else {
                                    pd.dismiss();
                                    Snackbar.make(parentLayout,"Try again", Snackbar.LENGTH_LONG)
                                            .setDuration(3000)
                                            .setAction("Close", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                }
                                            })
                                            .setActionTextColor(getResources().getColor(android.R.color.background_light))
                                            .show();

                                }

                            }
                        });
            }
        });

    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(ForgotPassword.this,LoginActivity.class));
        finish();
    }
}
