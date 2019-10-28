package com.example.flex;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.util.Objects;

public class BookingFailedActivity extends AppCompatActivity {

    @RequiresApi(api=Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_failed);

        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1db945")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView iv = (ImageView) findViewById(R.id.ivHomeFailed);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(BookingFailedActivity.this,MainActivity.class));
                finish();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent it = new Intent(getApplicationContext(),MainActivity.class);
        startActivityForResult(it,0);
        return true;
    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(BookingFailedActivity.this,MainActivity.class));
        finish();
    }
}
