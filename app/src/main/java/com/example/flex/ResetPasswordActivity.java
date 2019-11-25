package com.example.flex;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ResetPasswordActivity extends AppCompatActivity {

    EditText etCurrentPassword;
    EditText etNewPassword;
    EditText etConfirmNewPassword;
    Button btnChangePassword;
    DatabaseReference dbRef,usrRef;
    String checkEmail,uEmail,id;
    String currentPass,confirmPass, newPass;
    static int resetPass = 0;
    View parentLayout;

    @RequiresApi(api=Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(ResetPasswordActivity.this, android.R.color.background_light));// set status background white
        }

        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable()); // Add Color.Parse("#000") inside ColorDrawable() for color change
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        @SuppressLint("PrivateResource") final Drawable upArrow=getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(android.R.color.background_dark), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        Spannable text=new SpannableString(getSupportActionBar().getTitle());
        text.setSpan(new ForegroundColorSpan(Color.BLACK), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        getSupportActionBar().setTitle(text);

        parentLayout = findViewById(android.R.id.content);
        etCurrentPassword=findViewById(R.id.edCurrentPassword);
        etNewPassword=findViewById(R.id.edNewPassword);
        etConfirmNewPassword=findViewById(R.id.edConfirmNewPassword);
        btnChangePassword=findViewById(R.id.ChangePassword);

        dbRef = FirebaseDatabase.getInstance().getReference();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        checkEmail = user.getEmail();


        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usrRef = dbRef.child("User");
                currentPass = etCurrentPassword.getText().toString();
                newPass = etNewPassword.getText().toString();
                confirmPass = etConfirmNewPassword.getText().toString();

                if (!newPass.equals(confirmPass) || newPass.length() == 0)
                {
                    Snackbar.make(parentLayout,"Passwords do not match or cannot be empty", Snackbar.LENGTH_LONG)
                            .setDuration(3000)
                            .setAction("Close", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            })
                            .setActionTextColor(getResources().getColor(android.R.color.background_light))
                            .show();

                }
                else if(confirmPass.length() < 8)
                {
                    Snackbar.make(parentLayout,"Password must be at least 8 characters", Snackbar.LENGTH_LONG)
                            .setDuration(3000)
                            .setAction("Close", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            })
                            .setActionTextColor(getResources().getColor(android.R.color.background_light))
                            .show();

                }
                else {

                    final ProgressDialog pd = ProgressDialog.show(ResetPasswordActivity.this,"Changing password","Please wait...",true);
                    ValueEventListener userListener = new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                uEmail = ds.child("userMail").getValue(String.class);

                                assert uEmail != null;
                                if (uEmail.equals(checkEmail)) {

                                    id = ds.child("userId").getValue(String.class);
                                    AuthCredential credential = EmailAuthProvider.getCredential(checkEmail,currentPass);

                                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {

                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful()) {

                                                user.updatePassword(confirmPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        if (task.isSuccessful()) {

                                                            pd.dismiss();
                                                            resetPass = 1;
                                                            usrRef.child(id).child("userPass").setValue(confirmPass);
                                                            startActivity(new Intent(ResetPasswordActivity.this, MainActivity.class));

                                                        } else {

                                                            pd.dismiss();

                                                        }
                                                    }


                                                });
                                            }
                                            else
                                            {
                                                pd.dismiss();
                                            }

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            pd.dismiss();
                                            Snackbar.make(parentLayout,"Old password and current password do not match", Snackbar.LENGTH_LONG)
                                                    .setDuration(3000)
                                                    .setAction("Close", new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {

                                                        }
                                                    })
                                                    .setActionTextColor(getResources().getColor(android.R.color.background_light))
                                                    .show();

                                        }
                                    });

                                    break;
                                }

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                            Toast.makeText(ResetPasswordActivity.this, databaseError.getCode(), Toast.LENGTH_LONG).show();
                        }
                    };

                    usrRef.addListenerForSingleValueEvent(userListener);


                }
            }
        });


    }

   @Override
    public void onBackPressed() {

        startActivity(new Intent(ResetPasswordActivity.this, EditProfileActivity.class));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Intent it = new Intent(getApplicationContext(),EditProfileActivity.class);
        startActivityForResult(it,0);
        return true;
    }
}
