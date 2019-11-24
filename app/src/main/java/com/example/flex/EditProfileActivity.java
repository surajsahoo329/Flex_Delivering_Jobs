package com.example.flex;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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

        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable()); // Add Color.Parse("#000") inside ColorDrawable() for color change
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        @SuppressLint("PrivateResource") final Drawable upArrow=getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(android.R.color.background_dark), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        Spannable text=new SpannableString(getSupportActionBar().getTitle());
        text.setSpan(new ForegroundColorSpan(Color.BLACK), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        getSupportActionBar().setTitle(text);

        TextView textView=findViewById(R.id.editProfileTitle);
        Typeface myFont=Typeface.createFromAsset(Objects.requireNonNull(this).getAssets(), "fonts/coolvetica_i.ttf");
        textView.setTypeface(myFont);


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

                if(updatePhone.length() == 0 && updateName.length() == 0)
                {
                    Snackbar.make(parentLayout, "Please enter your name/phone number", Snackbar.LENGTH_LONG)
                            .setDuration(3000)
                            .setAction("Close", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            })
                            .setActionTextColor(getResources().getColor(android.R.color.background_light))
                            .show();

                }
                else
                {
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
                                        usrRef.child(id).child("userPhone").setValue(updatePhone);
                                        updateFlag=1;
                                    }


                                    break;
                                }

                            }

                            Intent it=new Intent(EditProfileActivity.this, MainActivity.class);
                            MainActivity.flag=1;
                            startActivityForResult(it, 0);
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

                startActivity(new Intent(EditProfileActivity.this,ResetPasswordActivity.class));
                finish();

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent it = new Intent(getApplicationContext(),MainActivity.class);
        MainActivity.flag = 1;
        startActivityForResult(it,0);
        return true;
    }

    @Override
    public void onBackPressed() {

        Intent it = new Intent(getApplicationContext(),MainActivity.class);
        MainActivity.flag = 1;
        startActivityForResult(it,0);
        finish();

    }
}
