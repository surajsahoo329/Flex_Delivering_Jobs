package com.example.flex;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class DeleteAccountActivity extends AppCompatActivity {

    DatabaseReference usrRef, dbRef, fdbRef, slotRef, dlRef, hisRef;
    String uEmail, password;
    StorageReference mStorageReference;
    FirebaseUser user;
    EditText etDeletePass;
    View parentLayout;
    static int deleteAccountFlag = 0;
    ProgressDialog pd;

    @RequiresApi(api=Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);

        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1db945")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        parentLayout = findViewById(android.R.id.content);
        Button btnDeleteAccount=findViewById(R.id.btnConfirmDeleteAccount);
        etDeletePass=findViewById(R.id.etDeletePassword);

        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        uEmail = user.getEmail();
        mStorageReference = FirebaseStorage.getInstance().getReference();
        dbRef= FirebaseDatabase.getInstance().getReference();
        usrRef=dbRef.child("User");
        fdbRef=dbRef.child("Feedback");
        slotRef=dbRef.child("Slot");
        dlRef=dbRef.child("DL");
        hisRef=dbRef.child("History");

        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pd=ProgressDialog.show(DeleteAccountActivity.this, "Deleting account", "Please wait...", true);

                password=etDeletePass.getText().toString();

                if (password.equals(""))
                {
                    pd.dismiss();

                    Snackbar.make(parentLayout,"Please re-enter your password", Snackbar.LENGTH_LONG)
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

                    AuthCredential credential=EmailAuthProvider.getCredential(uEmail, password);

                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {

                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {

                                pd.dismiss();

                                new AlertDialog.Builder(DeleteAccountActivity.this)
                                        .setIcon(R.drawable.ic_launcher_round)
                                        .setTitle("Deleting account")
                                        .setMessage("Are you sure ?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                pd=ProgressDialog.show(DeleteAccountActivity.this, "Deleting account", "Please wait...", true);

                                                Query emailQuery=usrRef.orderByChild("userMail").equalTo(uEmail);

                                                emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                        for (DataSnapshot ds : dataSnapshot.getChildren())
                                                            ds.getRef().removeValue();
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        pd.dismiss();
                                                    }
                                                });//User Database deleted


                                                Query feedbackQuery=fdbRef.orderByChild("userMail").equalTo(uEmail);

                                                feedbackQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                        for (DataSnapshot ds : dataSnapshot.getChildren())
                                                            ds.getRef().removeValue();


                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        pd.dismiss();

                                                    }
                                                });//Feedback Database deleted

                                                Query slotQuery=slotRef.orderByChild("userMail").equalTo(uEmail);

                                                slotQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                        for (DataSnapshot ds : dataSnapshot.getChildren())
                                                            ds.getRef().removeValue();

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        pd.dismiss();
                                                    }
                                                });//Slot Database deleted

                                                Query histQuery=hisRef.orderByChild("userMail").equalTo(uEmail);

                                                histQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                        for (DataSnapshot ds : dataSnapshot.getChildren())
                                                            ds.getRef().removeValue();

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        pd.dismiss();
                                                    }
                                                });//History Database deleted


                                                Query dlQuery=dlRef.orderByChild("userMail").equalTo(uEmail);

                                                dlQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                        for (DataSnapshot ds : dataSnapshot.getChildren())
                                                            ds.getRef().removeValue();

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        pd.dismiss();
                                                    }
                                                });//DL Database deleted

                                                StorageReference photoRef=mStorageReference.child(uEmail).child("photo.jpg");
                                                photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {

                                                        pd.dismiss();
                                                    }
                                                }); // Photo deleted

                                                StorageReference DLRef=mStorageReference.child(uEmail).child("userDL.pdf");
                                                DLRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {

                                                        pd.dismiss();
                                                     }
                                                }); // DL deleted

                                                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {

                                                            pd.dismiss();
                                                            deleteAccountFlag = 1;
                                                            startActivity(new Intent(DeleteAccountActivity.this, LoginActivity.class));

                                                        } else {
                                                            pd.dismiss();
                                                        }
                                                    }
                                                });

                                            }
                                        }).setNegativeButton("No", null).show();
                            } else {

                                pd.dismiss();
                                Snackbar.make(parentLayout,"Invalid password", Snackbar.LENGTH_LONG)
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

                    });//Auth deleted

                }
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
}
