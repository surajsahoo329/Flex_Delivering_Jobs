package com.example.flex;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */

public class SlotFragment extends Fragment {

    private String checkMail, uEmail;
    FirebaseUser user;
    private TextView tvSlotHeading, tvActiveSlots, tvSlotMsg, tvCompany, tvTimings, tvAddress, tvHistory;
    private ImageView ivCompany;
    private RelativeLayout relativeLayout;
    private ListView historyListViewSlot;
    private Slot historySlot;
    private DatabaseReference historyRef;

    private static final String[] company = new String[] { "Ekart Logistics",
            "Fedex", "Aramex", "Delhivery", "Blue Dart", "DTDC" ,"Indian Post"};

    private static final String[] address = new String[] {
            "Baramunda, Bhubaneswar",
            "Master Canteen, Bhubaneswar", "Jaydev Vihar, Bhubaneswar",
            "Nayapalli, Bhubaneswar","Kharbela Nagar, Bhubaneswar","Master Canteen, Bhubaneswar","Bapuji Nagar, Bhubaneswar" };


    private static final Integer[] images = { R.drawable.ic_flipkart, R.drawable.ic_fedex,
            R.drawable.ic_aramex, R.drawable.ic_delhivery, R.drawable.ic_bluedart, R.drawable.ic_dtdc, R.drawable.ic_indianpost };

    private List<Company> companies;


    public SlotFragment() {
        // Required empty public constructor
    }


    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api=Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final Activity refActivity=getActivity();
        final View parentHolder=inflater.inflate(R.layout.fragment_slot, container, false);

        final ProgressDialog pd  = ProgressDialog.show(refActivity,"Loading slots","Please wait...",true);

        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        checkMail = user.getEmail();
        assert refActivity != null;

        tvSlotHeading=parentHolder.findViewById(R.id.slotHeading);
        tvSlotMsg=parentHolder.findViewById(R.id.noBookingMsg);
        tvActiveSlots=parentHolder.findViewById(R.id.tvActiveSlots);
        tvHistory=parentHolder.findViewById(R.id.historySlot);
        tvCompany=parentHolder.findViewById(R.id.tvCompany);
        tvTimings=parentHolder.findViewById(R.id.tvTimings);
        tvAddress=parentHolder.findViewById(R.id.tvAddress);
        ivCompany=parentHolder.findViewById(R.id.ivImage);
        relativeLayout=parentHolder.findViewById(R.id.relLayout);
        historyListViewSlot=parentHolder.findViewById(R.id.historyListViewSlot);
        companies=new ArrayList<>();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("Slot");
        historyRef = database.getReference("History");
        historyListViewSlot.setVisibility(View.GONE);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    uEmail =  ds.child("userMail").getValue(String.class);
                    assert uEmail != null;
                    if (uEmail.equals(checkMail)) {

                        int slotFlag = ds.child("slotFlag").getValue(Integer.class);

                        if(slotFlag >= 1 && slotFlag <= 7) {

                            tvSlotHeading.setText("Your Slots");
                            tvSlotMsg.setVisibility(View.GONE);
                            tvActiveSlots.setVisibility(View.VISIBLE);
                            relativeLayout.setVisibility(View.VISIBLE);

                            String id = ds.child("userId").getValue(String.class);
                            String date = ds.child("showDate").getValue(String.class);
                            String stTime = ds.child("showStartTime").getValue(String.class);
                            String hours = ds.child("showEndTime").getValue(String.class);

                            @SuppressLint("SimpleDateFormat") SimpleDateFormat df=new SimpleDateFormat("dd-MMM-yyyy");
                            Date strDate = null;
                            try {
                                strDate = df.parse(date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            Date currDate = Calendar.getInstance().getTime();

                            //currDate.compareTo(strDate) > 0 means slot date has passed or same date

                            if (currDate.compareTo(strDate) > 0) {

                                int timeCount=8, hourCount=2;
                                assert stTime != null;
                                switch (stTime) {

                                    case "9 am":
                                        timeCount=9;
                                        break;
                                    case "10 am":
                                        timeCount=10;
                                        break;
                                    case "11 am":
                                        timeCount=11;
                                        break;
                                    case "12 pm":
                                        timeCount=12;
                                        break;
                                    case "1 pm":
                                        timeCount=13;
                                        break;
                                    case "2 pm":
                                        timeCount=14;
                                        break;
                                    case "3 pm":
                                        timeCount=15;
                                        break;
                                    case "4 pm":
                                        timeCount=16;
                                        break;
                                    case "5 pm":
                                        timeCount=17;
                                        break;
                                    case "6 pm":
                                        timeCount=18;
                                        break;
                                }

                                assert hours != null;
                                switch (hours) {
                                    case "3 hours":
                                        hourCount=3;
                                        break;
                                    case "4 hours":
                                        hourCount=4;
                                        break;
                                    case "5 hours":
                                        hourCount=5;
                                        break;
                                    case "6 hours":
                                        hourCount=6;
                                        break;
                                    case "7 hours":
                                        hourCount=7;
                                        break;
                                    case "8 hours":
                                        hourCount=8;
                                        break;
                                    case "9 hours":
                                        hourCount=9;
                                        break;
                                    case "10 hours":
                                        hourCount=10;
                                        break;
                                    case "11 hours":
                                        hourCount=11;
                                        break;
                                    case "12 hours":
                                        hourCount=12;
                                        break;
                                }

                                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf=new SimpleDateFormat("dd-MMM-yyyy");
                                Date sDate=new Date();
                                String currentDate=sdf.format(sDate);

                                int currentHour=Calendar.getInstance().get(Calendar.HOUR_OF_DAY); //Current hour

                                if (currentDate.equals(date)) {

                                    if (currentHour >= (timeCount + hourCount)) {


                                        DatabaseReference dbSlot=FirebaseDatabase.getInstance().getReference("Slot");
                                        assert id != null;
                                        dbSlot.child(id).child("slotFlag").setValue(8);
                                        DatabaseReference databaseHistory=database.getReference("History");
                                        String idHistory=databaseHistory.push().getKey();
                                        Slot slot=new Slot(checkMail, idHistory, slotFlag, date, stTime, hours);
                                        assert idHistory != null;
                                        databaseHistory.child(idHistory).setValue(slot);

                                        tvActiveSlots.setVisibility(View.GONE);
                                        relativeLayout.setVisibility(View.GONE);

                                        historyRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                                    uEmail = ds.child("userMail").getValue(String.class);
                                                    String dateHis = ds.child("showDate").getValue(String.class);
                                                    String timeHis = ds.child("showStartTime").getValue(String.class);
                                                    String hoursHis = ds.child("showEndTime").getValue(String.class);

                                                    assert uEmail != null;
                                                    if(uEmail.equals(checkMail)) {

                                                        historySlot = ds.getValue(Slot.class);
                                                        tvHistory.setVisibility(View.VISIBLE);
                                                        historyListViewSlot.setVisibility(View.VISIBLE);

                                                        int i = historySlot.getSlotFlag();

                                                        assert dateHis != null;
                                                        char[] dateArr=dateHis.toCharArray();
                                                        char[] modDateArr=new char[dateHis.length()];
                                                        int count=0;

                                                        for (int j=0; j < dateArr.length; j++) {
                                                            if (dateArr[j] == '-')
                                                                count++;
                                                            if (count == 2)
                                                                break;

                                                            modDateArr[j]=dateArr[j];
                                                        }

                                                        String modTime=String.valueOf(modDateArr) + ", " + timeHis + " | " + hoursHis;
                                                        Company item = new Company(images[i-1], company[i-1],modTime, address[i-1]);
                                                        companies.add(item);

                                                    }
                                                }


                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                pd.dismiss();

                                            }
                                        });


                                    } else {

                                        char[] dateArr=date.toCharArray();
                                        char[] modDateArr=new char[date.length()];
                                        int count=0;

                                        for (int i=0; i < dateArr.length; i++) {
                                            if (dateArr[i] == '-')
                                                count++;
                                            if (count == 2)
                                                break;

                                            modDateArr[i]=dateArr[i];
                                        }

                                        final String modTime=String.valueOf(modDateArr) + ", " + stTime + " | " + hours;

                                        if (slotFlag == 2) {
                                            tvCompany.setText("Fedex");
                                            tvAddress.setText("Master Canteen, Bhubaneswar");
                                            ivCompany.setImageResource(R.drawable.ic_fedex);
                                        } else if (slotFlag == 3) {
                                            tvCompany.setText("Aramex");
                                            tvAddress.setText("Jaydev Vihar, Bhubaneswar");
                                            ivCompany.setImageResource(R.drawable.ic_aramex);
                                        } else if (slotFlag == 4) {
                                            tvCompany.setText("Delhivery");
                                            tvAddress.setText("Nayapalli, Bhubaneswar");
                                            Glide.with(refActivity)
                                                    .load("https://nexusvp.com/wp-content/uploads/2014/04/oie_JbUn8ia6Q3Zq.png")
                                                    .into(ivCompany);
                                        } else if (slotFlag == 5) {
                                            tvCompany.setText("Blue Dart");
                                            tvAddress.setText("Kharbela Nagar, Bhubaneswar");
                                            ivCompany.setImageResource(R.drawable.ic_bluedart);
                                        } else if (slotFlag == 6) {
                                            tvCompany.setText("DTDC");
                                            tvAddress.setText("Master Canteen, Bhubaneswar");
                                            ivCompany.setImageResource(R.drawable.ic_dtdc);
                                        } else if (slotFlag == 7) {
                                            tvCompany.setText("Indian Post");
                                            tvAddress.setText("Bapuji Nagar, Bhubaneswar");
                                            ivCompany.setImageResource(R.drawable.ic_indianpost);
                                        }

                                        tvTimings.setText(modTime);

                                        historyRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                                    uEmail = ds.child("userMail").getValue(String.class);
                                                    String dateHis = ds.child("showDate").getValue(String.class);
                                                    String timeHis = ds.child("showStartTime").getValue(String.class);
                                                    String hoursHis = ds.child("showEndTime").getValue(String.class);

                                                    assert uEmail != null;
                                                    if(uEmail.equals(checkMail)) {

                                                        historySlot = ds.getValue(Slot.class);
                                                        tvHistory.setVisibility(View.VISIBLE);
                                                        historyListViewSlot.setVisibility(View.VISIBLE);

                                                        int i = historySlot.getSlotFlag();

                                                        assert dateHis != null;
                                                        char[] dateArr=dateHis.toCharArray();
                                                        char[] modDateArr=new char[dateHis.length()];
                                                        int count=0;

                                                        for (int j=0; j < dateArr.length; j++) {
                                                            if (dateArr[j] == '-')
                                                                count++;
                                                            if (count == 2)
                                                                break;

                                                            modDateArr[j]=dateArr[j];
                                                        }

                                                        String modTime=String.valueOf(modDateArr) + ", " + timeHis + " | " + hoursHis;
                                                        Company item = new Company(images[i-1], company[i-1],modTime, address[i-1]);
                                                        companies.add(item);

                                                    }
                                                }

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                pd.dismiss();

                                            }
                                        });
                                    }
                                } else {

                                    DatabaseReference dbSlot1=FirebaseDatabase.getInstance().getReference("Slot");
                                    assert id != null;
                                    dbSlot1.child(id).child("slotFlag").setValue(8);
                                    DatabaseReference databaseHistory1=database.getReference("History");
                                    String idHistory1=databaseHistory1.push().getKey();
                                    Slot slot1=new Slot(checkMail, idHistory1, slotFlag, date, stTime, hours);
                                    assert idHistory1 != null;
                                    databaseHistory1.child(idHistory1).setValue(slot1);

                                    tvActiveSlots.setVisibility(View.GONE);
                                    relativeLayout.setVisibility(View.GONE);

                                    historyRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                                uEmail = ds.child("userMail").getValue(String.class);
                                                String dateHis = ds.child("showDate").getValue(String.class);
                                                String timeHis = ds.child("showStartTime").getValue(String.class);
                                                String hoursHis = ds.child("showEndTime").getValue(String.class);

                                                assert uEmail != null;
                                                if(uEmail.equals(checkMail)) {

                                                    historySlot = ds.getValue(Slot.class);
                                                    tvHistory.setVisibility(View.VISIBLE);
                                                    historyListViewSlot.setVisibility(View.VISIBLE);

                                                    int i = historySlot.getSlotFlag();

                                                    assert dateHis != null;
                                                    char[] dateArr=dateHis.toCharArray();
                                                    char[] modDateArr=new char[dateHis.length()];
                                                    int count=0;

                                                    for (int j=0; j < dateArr.length; j++) {
                                                        if (dateArr[j] == '-')
                                                            count++;
                                                        if (count == 2)
                                                            break;

                                                        modDateArr[j]=dateArr[j];
                                                    }

                                                    String modTime=String.valueOf(modDateArr) + ", " + timeHis + " | " + hoursHis;
                                                    Company item = new Company(images[i-1], company[i-1],modTime, address[i-1]);
                                                    companies.add(item);

                                                }
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                            pd.dismiss();

                                        }
                                    });

                                }
                            } else {

                                int timeCount=8, hourCount=2;
                                assert stTime != null;
                                switch (stTime) {

                                    case "9 am":
                                        timeCount=9;
                                        break;
                                    case "10 am":
                                        timeCount=10;
                                        break;
                                    case "11 am":
                                        timeCount=11;
                                        break;
                                    case "12 pm":
                                        timeCount=12;
                                        break;
                                    case "1 pm":
                                        timeCount=13;
                                        break;
                                    case "2 pm":
                                        timeCount=14;
                                        break;
                                    case "3 pm":
                                        timeCount=15;
                                        break;
                                    case "4 pm":
                                        timeCount=16;
                                        break;
                                    case "5 pm":
                                        timeCount=17;
                                        break;
                                    case "6 pm":
                                        timeCount=18;
                                        break;
                                }

                                assert hours != null;
                                switch (hours) {
                                    case "3 hours":
                                        hourCount=3;
                                        break;
                                    case "4 hours":
                                        hourCount=4;
                                        break;
                                    case "5 hours":
                                        hourCount=5;
                                        break;
                                    case "6 hours":
                                        hourCount=6;
                                        break;
                                    case "7 hours":
                                        hourCount=7;
                                        break;
                                    case "8 hours":
                                        hourCount=8;
                                        break;
                                    case "9 hours":
                                        hourCount=9;
                                        break;
                                    case "10 hours":
                                        hourCount=10;
                                        break;
                                    case "11 hours":
                                        hourCount=11;
                                        break;
                                    case "12 hours":
                                        hourCount=12;
                                        break;
                                }

                                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf=new SimpleDateFormat("dd-MMM-yyyy");
                                Date sDate=new Date();
                                String currentDate=sdf.format(sDate);

                                int currentHour=Calendar.getInstance().get(Calendar.HOUR_OF_DAY); //Current hour

                                if (currentDate.equals(date)) {

                                    if (currentHour >= (timeCount + hourCount)) {


                                        DatabaseReference dbSlot=FirebaseDatabase.getInstance().getReference("Slot");
                                        assert id != null;
                                        dbSlot.child(id).child("slotFlag").setValue(8);
                                        DatabaseReference databaseHistory=database.getReference("History");
                                        String idHistory=databaseHistory.push().getKey();
                                        Slot slot=new Slot(checkMail, idHistory, slotFlag, date, stTime, hours);
                                        assert idHistory != null;
                                        databaseHistory.child(idHistory).setValue(slot);

                                        tvActiveSlots.setVisibility(View.GONE);
                                        relativeLayout.setVisibility(View.GONE);


                                        historyRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                                    uEmail = ds.child("userMail").getValue(String.class);
                                                    String dateHis = ds.child("showDate").getValue(String.class);
                                                    String timeHis = ds.child("showStartTime").getValue(String.class);
                                                    String hoursHis = ds.child("showEndTime").getValue(String.class);

                                                    assert uEmail != null;
                                                    if(uEmail.equals(checkMail)) {

                                                        historySlot = ds.getValue(Slot.class);
                                                        tvHistory.setVisibility(View.VISIBLE);
                                                        historyListViewSlot.setVisibility(View.VISIBLE);

                                                        int i = historySlot.getSlotFlag();

                                                        assert dateHis != null;
                                                        char[] dateArr=dateHis.toCharArray();
                                                        char[] modDateArr=new char[dateHis.length()];
                                                        int count=0;

                                                        for (int j=0; j < dateArr.length; j++) {
                                                            if (dateArr[j] == '-')
                                                                count++;
                                                            if (count == 2)
                                                                break;

                                                            modDateArr[j]=dateArr[j];
                                                        }

                                                        String modTime=String.valueOf(modDateArr) + ", " + timeHis + " | " + hoursHis;
                                                        Company item = new Company(images[i-1], company[i-1],modTime, address[i-1]);
                                                        companies.add(item);

                                                    }
                                                }


                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                pd.dismiss();

                                            }
                                        });


                                    } else {

                                        char[] dateArr=date.toCharArray();
                                        char[] modDateArr=new char[date.length()];
                                        int count=0;

                                        for (int i=0; i < dateArr.length; i++) {
                                            if (dateArr[i] == '-')
                                                count++;
                                            if (count == 2)
                                                break;

                                            modDateArr[i]=dateArr[i];
                                        }

                                        final String modTime=String.valueOf(modDateArr) + ", " + stTime + " | " + hours;

                                        if (slotFlag == 2) {
                                            tvCompany.setText("Fedex");
                                            tvAddress.setText("Master Canteen, Bhubaneswar");
                                            ivCompany.setImageResource(R.drawable.ic_fedex);
                                        } else if (slotFlag == 3) {
                                            tvCompany.setText("Aramex");
                                            tvAddress.setText("Jaydev Vihar, Bhubaneswar");
                                            ivCompany.setImageResource(R.drawable.ic_aramex);
                                        } else if (slotFlag == 4) {
                                            tvCompany.setText("Delhivery");
                                            tvAddress.setText("Nayapalli, Bhubaneswar");
                                            Glide.with(refActivity)
                                                    .load("https://nexusvp.com/wp-content/uploads/2014/04/oie_JbUn8ia6Q3Zq.png")
                                                    .into(ivCompany);
                                        } else if (slotFlag == 5) {
                                            tvCompany.setText("Blue Dart");
                                            tvAddress.setText("Kharbela Nagar, Bhubaneswar");
                                            ivCompany.setImageResource(R.drawable.ic_bluedart);
                                        } else if (slotFlag == 6) {
                                            tvCompany.setText("DTDC");
                                            tvAddress.setText("Master Canteen, Bhubaneswar");
                                            ivCompany.setImageResource(R.drawable.ic_dtdc);
                                        } else if (slotFlag == 7) {
                                            tvCompany.setText("Indian Post");
                                            tvAddress.setText("Bapuji Nagar, Bhubaneswar");
                                            ivCompany.setImageResource(R.drawable.ic_indianpost);
                                        }

                                        tvTimings.setText(modTime);

                                        historyRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                                    uEmail = ds.child("userMail").getValue(String.class);
                                                    String dateHis = ds.child("showDate").getValue(String.class);
                                                    String timeHis = ds.child("showStartTime").getValue(String.class);
                                                    String hoursHis = ds.child("showEndTime").getValue(String.class);

                                                    assert uEmail != null;
                                                    if(uEmail.equals(checkMail)) {

                                                        historySlot = ds.getValue(Slot.class);
                                                        tvHistory.setVisibility(View.VISIBLE);
                                                        historyListViewSlot.setVisibility(View.VISIBLE);

                                                        int i = historySlot.getSlotFlag();

                                                        assert dateHis != null;
                                                        char[] dateArr=dateHis.toCharArray();
                                                        char[] modDateArr=new char[dateHis.length()];
                                                        int count=0;

                                                        for (int j=0; j < dateArr.length; j++) {
                                                            if (dateArr[j] == '-')
                                                                count++;
                                                            if (count == 2)
                                                                break;

                                                            modDateArr[j]=dateArr[j];
                                                        }

                                                        String modTime=String.valueOf(modDateArr) + ", " + timeHis + " | " + hoursHis;
                                                        Company item = new Company(images[i-1], company[i-1],modTime, address[i-1]);
                                                        companies.add(item);

                                                    }
                                                }

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                pd.dismiss();

                                            }
                                        });
                                    }
                                } else {
                                    assert date != null;
                                    char[] dateArr=date.toCharArray();
                                    char[] modDateArr=new char[date.length()];
                                    int count=0;

                                    for (int i=0; i < dateArr.length; i++) {
                                        if (dateArr[i] == '-')
                                            count++;
                                        if (count == 2)
                                            break;

                                        modDateArr[i]=dateArr[i];
                                    }

                                    final String modTime=String.valueOf(modDateArr) + ", " + stTime + " | " + hours;

                                    if (slotFlag == 2) {
                                        tvCompany.setText("Fedex");
                                        tvAddress.setText("Master Canteen, Bhubaneswar");
                                        ivCompany.setImageResource(R.drawable.ic_fedex);
                                    } else if (slotFlag == 3) {
                                        tvCompany.setText("Aramex");
                                        tvAddress.setText("Jaydev Vihar, Bhubaneswar");
                                        ivCompany.setImageResource(R.drawable.ic_aramex);
                                    } else if (slotFlag == 4) {
                                        tvCompany.setText("Delhivery");
                                        tvAddress.setText("Nayapalli, Bhubaneswar");
                                        Glide.with(refActivity)
                                                .load("https://nexusvp.com/wp-content/uploads/2014/04/oie_JbUn8ia6Q3Zq.png")
                                                .into(ivCompany);
                                    } else if (slotFlag == 5) {
                                        tvCompany.setText("Blue Dart");
                                        tvAddress.setText("Kharbela Nagar, Bhubaneswar");
                                        ivCompany.setImageResource(R.drawable.ic_bluedart);
                                    } else if (slotFlag == 6) {
                                        tvCompany.setText("DTDC");
                                        tvAddress.setText("Master Canteen, Bhubaneswar");
                                        ivCompany.setImageResource(R.drawable.ic_dtdc);
                                    } else if (slotFlag == 7) {
                                        tvCompany.setText("Indian Post");
                                        tvAddress.setText("Bapuji Nagar, Bhubaneswar");
                                        ivCompany.setImageResource(R.drawable.ic_indianpost);
                                    }

                                    tvTimings.setText(modTime);

                                    historyRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                                uEmail = ds.child("userMail").getValue(String.class);
                                                String dateHis = ds.child("showDate").getValue(String.class);
                                                String timeHis = ds.child("showStartTime").getValue(String.class);
                                                String hoursHis = ds.child("showEndTime").getValue(String.class);
                                                int slotFlag  = ds.child("slotFlag").getValue(Integer.class);

                                                assert uEmail != null;
                                                if(uEmail.equals(checkMail) && slotFlag != 0) {

                                                    historySlot = ds.getValue(Slot.class);
                                                    tvHistory.setVisibility(View.VISIBLE);
                                                    historyListViewSlot.setVisibility(View.VISIBLE);

                                                    int i = historySlot.getSlotFlag();

                                                    assert dateHis != null;
                                                    char[] dateArr=dateHis.toCharArray();
                                                    char[] modDateArr=new char[dateHis.length()];
                                                    int count=0;

                                                    for (int j=0; j < dateArr.length; j++) {
                                                        if (dateArr[j] == '-')
                                                            count++;
                                                        if (count == 2)
                                                            break;

                                                        modDateArr[j]=dateArr[j];
                                                    }

                                                    String modTime=String.valueOf(modDateArr) + ", " + timeHis + " | " + hoursHis;
                                                    Company item = new Company(images[i-1],company[i-1],modTime,address[i-1]);
                                                    companies.add(item);

                                                }
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                            pd.dismiss();

                                        }
                                    });

                                }
                            }

                        } else if(slotFlag == 8) {
                            tvSlotHeading.setText("Your Slots");
                            tvSlotMsg.setVisibility(View.GONE);

                            final DatabaseReference historyRef = database.getReference("History");

                            historyRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    for (DataSnapshot ds :dataSnapshot.getChildren()) {
                                        uEmail = ds.child("userMail").getValue(String.class);
                                        String dateHis = ds.child("showDate").getValue(String.class);
                                        String timeHis = ds.child("showStartTime").getValue(String.class);
                                        String hoursHis = ds.child("showEndTime").getValue(String.class);

                                        assert uEmail != null;
                                        if(uEmail.equals(checkMail)) {

                                            historySlot = ds.getValue(Slot.class);
                                            tvHistory.setVisibility(View.VISIBLE);
                                            historyListViewSlot.setVisibility(View.VISIBLE);

                                            int i = historySlot.getSlotFlag();

                                            assert dateHis != null;
                                            char[] dateArr=dateHis.toCharArray();
                                            char[] modDateArr=new char[dateHis.length()];
                                            int count=0;

                                            for (int j=0; j < dateArr.length; j++) {
                                                if (dateArr[j] == '-')
                                                    count++;
                                                if (count == 2)
                                                    break;

                                                modDateArr[j]=dateArr[j];
                                            }

                                            String modTime=String.valueOf(modDateArr) + ", " + timeHis + " | " + hoursHis;
                                            Company item = new Company(images[i-1], company[i-1],modTime, address[i-1]);
                                            companies.add(item);

                                        }
                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                    pd.dismiss();

                                }
                            });

                        }


                    }
                }

                CustomListViewAdapter adapter = new CustomListViewAdapter(refActivity,
                        R.layout.list_slot, companies);

                historyListViewSlot.setAdapter(adapter);

                pd.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                pd.dismiss();
            }
        });

        return parentHolder;
    }

}