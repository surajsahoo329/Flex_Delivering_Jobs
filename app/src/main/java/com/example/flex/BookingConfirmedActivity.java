package com.example.flex;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.Objects;

public class BookingConfirmedActivity extends AppCompatActivity {

    @RequiresApi(api=Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_confirmed);

        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1db945")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView iv = (ImageView) findViewById(R.id.ivHouse);
        TextView tvCompany = (TextView) findViewById(R.id.tvCompany);
        TextView tvTimings = (TextView) findViewById(R.id.tvTimings);
        TextView tvAddress = (TextView) findViewById(R.id.tvAddress);
        ImageView ivCompany = (ImageView) findViewById(R.id.ivImage);

        Intent it = getIntent();

        final int company = it.getIntExtra("company",0);
        final String date = it.getStringExtra("date");
        final String time = it.getStringExtra("time");
        final String hours = it.getStringExtra("hours");

        char[] dateArr=date.toCharArray();
        char[] modDateArr=new char[date.length()];
        int count = 0;

        for(int i =0; i< dateArr.length; i++)
        {
            if(dateArr[i] == '-')
                count ++;
            if(count == 2)
                break;

            modDateArr[i] = dateArr[i];
        }

        final String modTimeStr = String.valueOf(modDateArr)+", "+time+" | "+hours;
        tvTimings.setText(modTimeStr);

        if(company == 2)
        {
            tvCompany.setText("Fedex");
            tvAddress.setText("Master Canteen, Bhubaneswar");
            ivCompany.setImageResource(R.drawable.ic_fedex);
        }
        else if(company == 3)
        {
            tvCompany.setText("Aramex");
            tvAddress.setText("Jayadev Vihar, Bhubaneswar");
            ivCompany.setImageResource(R.drawable.ic_aramex);
        }
        else if(company == 4)
        {
            tvCompany.setText("Delhivery");
            tvAddress.setText("Nayapalli, Bhubaneswar");
            Glide.with(BookingConfirmedActivity.this)
                    .load("https://nexusvp.com/wp-content/uploads/2014/04/oie_JbUn8ia6Q3Zq.png")
                    .into(ivCompany);
        }
        else if(company == 5)
        {
            tvCompany.setText("Blue Dart");
            tvAddress.setText("Kharabela Nagar, Bhubaneswar");
            ivCompany.setImageResource(R.drawable.ic_bluedart);
        }
        else if(company == 6)
        {
            tvCompany.setText("DTDC");
            tvAddress.setText("Master Canteen Area, Bhubaneswar");
            ivCompany.setImageResource(R.drawable.ic_dtdc);
        }
        else if(company == 7)
        {
            tvCompany.setText("Indian Post");
            tvAddress.setText("Bapuji Nagar, Bhubaneswar");
            ivCompany.setImageResource(R.drawable.ic_indianpost);
        }

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(BookingConfirmedActivity.this,MainActivity.class));
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

        startActivity(new Intent(BookingConfirmedActivity.this,MainActivity.class));
        finish();
    }
}
