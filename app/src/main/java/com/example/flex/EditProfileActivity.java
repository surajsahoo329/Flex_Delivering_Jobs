package com.example.flex;

import android.annotation.SuppressLint;
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

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class EditProfileActivity extends AppCompatActivity {

    EditText etName, etPhone;
    Button btnConfirm, btnChangePassword;
    DatabaseReference dbRef,usrRef;
    String checkEmail,uemail, updateName, updatePhone;
    public static int updateFlag = 0;
    String id;
    View parentLayout;

    @RequiresApi(api=Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(EditProfileActivity.this, android.R.color.background_light));// set status background white
        }

        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable()); // Add Color.Parse("#000") inside ColorDrawable() for color change
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        @SuppressLint("PrivateResource") final Drawable upArrow=getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(android.R.color.background_dark), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        Spannable text=new SpannableString(getSupportActionBar().getTitle());
        text.setSpan(new ForegroundColorSpan(Color.BLACK), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        getSupportActionBar().setTitle(text);


        etName=findViewById(R.id.edName);
        etPhone=findViewById(R.id.edPhone);
        btnConfirm=findViewById(R.id.btnEditProfile);
        btnChangePassword=findViewById(R.id.btnChange);
        parentLayout = findViewById(android.R.id.content);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        checkEmail = user.getEmail();

        dbRef = FirebaseDatabase.getInstance().getReference();

        btnConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                usrRef=dbRef.child("User");
                updateName = etName.getText().toString();
                updatePhone = etPhone.getText().toString();

                if (updatePhone.length() == 0 && updateName.length() == 0) {
                    Snackbar.make(parentLayout, "Please enter your name/phone number", Snackbar.LENGTH_LONG)
                            .setDuration(3000)
                            .setAction("Close", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            })
                            .setActionTextColor(getResources().getColor(android.R.color.background_light))
                            .show();

                } else if (updatePhone.length() != 10 && updatePhone.length() != 0) {
                    Snackbar.make(parentLayout, "Please enter a valid phone number", Snackbar.LENGTH_LONG)
                            .setDuration(3000)
                            .setAction("Close", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            })
                            .setActionTextColor(getResources().getColor(android.R.color.background_light))
                            .show();

                } else {
                    ValueEventListener userListener=new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                uemail=ds.child("userMail").getValue(String.class);

                                assert uemail != null;
                                if (uemail.equals(checkEmail)) {

                                    id = ds.child("userId").getValue(String.class);

                                    if (updateName.length() != 0) {
                                        assert id != null;
                                        usrRef.child(id).child("userName").setValue(updateName);
                                        updateFlag=1;
                                    }


                                    if (updatePhone.length() != 0) {
                                        assert id != null;
                                        usrRef.child(id).child("userPhone").setValue(updatePhone);
                                        updateFlag=1;
                                    }


                                    break;
                                }

                            }

                            Intent intent=new Intent(EditProfileActivity.this, MainActivity.class);
                            intent.putExtra("openProfile", true);
                            overridePendingTransition(0, 0);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            finish();
                            startActivity(intent);
                            finish();


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                            Toast.makeText(EditProfileActivity.this, databaseError.getCode(), Toast.LENGTH_LONG).show();
                        }
                    };

                    usrRef.addListenerForSingleValueEvent(userListener);
                }

            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(EditProfileActivity.this, ChangePasswordActivity.class));
                finish();

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Intent intent=new Intent(EditProfileActivity.this, MainActivity.class);
        intent.putExtra("openProfile", true);
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        startActivity(intent);
        return true;
    }

    @Override
    public void onBackPressed() {

        Intent intent=new Intent(EditProfileActivity.this, MainActivity.class);
        intent.putExtra("openProfile", true);
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        startActivity(intent);
        finish();

    }
}
