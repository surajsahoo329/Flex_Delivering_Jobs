package com.example.flex;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.google.android.material.snackbar.Snackbar;
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

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private ArrayList<String> mImageUrls = new ArrayList<>();

    private Activity refActivity;
    private EditText etDate;
    private DatePickerDialog datePickerDialog;
    private View parentHolder, parentLayout ;
    private String monthStr;
    private TextView tvIDate;
    private int row_index, column_index, dlFlag = 0, companyFlag = 0, slotFlagCheck ;
    private String strTime, strHours ;

    private DatabaseReference dbRef, sldbRef, dlRef;
    FirebaseAuth auth;
    FirebaseUser user;
    private String uEmail,id;
    private String checkMail;
    private String[] startTime={"Choose start time", "8 am", "9 am", "10 am", "11 am", "12 am", "1 pm", "2 pm", "3 pm", "4 pm", "5 pm", "6 pm"};
    private String[] workHours={"Choose working hours", "2 hours", "3 hours", "4 hours", "5 hours", "6 hours", "7 hours", "8 hours", "9 hours", "10 hours", "11 hours", "12 hours"};
    private String checkDate, checkID;
    private DatabaseReference assRef;

    public HomeFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        refActivity = getActivity();
        parentHolder = inflater.inflate(R.layout.fragment_home, container,
                false);

        parentLayout=refActivity.findViewById(android.R.id.content);

        final Button btnBookSlot = (Button) parentHolder.findViewById(R.id.btnBookSlot);
        dbRef = FirebaseDatabase.getInstance().getReference();
        user= FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        checkMail=user.getEmail();

        etDate = (EditText) parentHolder.findViewById(R.id.etDate);

        Spinner spStartTime =(Spinner) parentHolder.findViewById(R.id.spStartTime);
        ArrayAdapter arrStartTime =new ArrayAdapter(refActivity,android.R.layout.simple_list_item_1,startTime);
        spStartTime.setAdapter(arrStartTime);
        spStartTime.setOnItemSelectedListener(new startTimeClick());

        Spinner spWorkHours =(Spinner) parentHolder.findViewById(R.id.spWorkHours);
        ArrayAdapter arrWorkHours =new ArrayAdapter(refActivity,android.R.layout.simple_list_item_1,workHours);
        spWorkHours.setAdapter(arrWorkHours);
        spWorkHours.setOnItemSelectedListener(new workHoursClick());


        tvIDate = (TextView) parentHolder.findViewById(R.id.tvInvisibleDate);

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar c = Calendar.getInstance();
                int date = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(refActivity, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

                        if(month == 0)
                            monthStr = "Jan";
                        else if(month == 1)
                            monthStr = "Feb";
                        else if(month == 2)
                            monthStr="Mar";
                        else if(month == 3)
                            monthStr = "Apr";
                        else if(month == 4)
                            monthStr ="May";
                        else if(month == 5)
                            monthStr="Jun";
                        else if(month == 6)
                            monthStr="Jul";
                        else if(month == 7)
                            monthStr = "Aug";
                        else if(month == 8)
                            monthStr = "Sep";
                        else if(month == 9)
                            monthStr = "Oct";
                        else if(month == 10)
                            monthStr = "Nov";
                        else
                            monthStr = "Dec";

                        etDate.setText("  "+dayOfMonth+"-"+monthStr+"-"+year);
                        tvIDate.setText(dayOfMonth+"-"+monthStr+"-"+year);

                    }
                }, year,month,date);

                datePickerDialog.getDatePicker().setMinDate((System.currentTimeMillis()+ 24 * 60 * 60 * 1000L) - 1000);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + 10 * 24 * 60 * 60 * 1000L);

                datePickerDialog.show();


            }
        });


        btnBookSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(tvIDate.length()==0)
                {
                    Snackbar.make(parentLayout,"Please enter your slot date",Snackbar.LENGTH_LONG)
                            .setDuration(3000)
                            .setAction("Close", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            })
                            .setActionTextColor(getResources().getColor(android.R.color.background_light))
                            .show();

                }
                else if(row_index == 0)
                {
                    Snackbar.make(parentLayout,"Please choose a start time",Snackbar.LENGTH_LONG)
                            .setDuration(3000)
                            .setAction("Close", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            })
                            .setActionTextColor(getResources().getColor(android.R.color.background_light))
                            .show();

                }
                else if(column_index == 0)
                {
                    Snackbar.make(parentLayout,"Please choose working hours",Snackbar.LENGTH_LONG)
                            .setDuration(3000)
                            .setAction("Close", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            })
                            .setActionTextColor(getResources().getColor(android.R.color.background_light))
                            .show();

                }
                else if( row_index + column_index > 12)
                {
                    Snackbar.make(parentLayout,"Invalid slot time chosen.",Snackbar.LENGTH_LONG)
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


                    if(row_index == 1)
                        strTime = "8 am";
                    else if(row_index == 2)
                        strTime = "9 am";
                    else if(row_index == 3)
                        strTime = "10 am";
                    else if(row_index == 4)
                        strTime = "11 am";
                    else if(row_index == 5)
                        strTime = "12 pm";
                    else if(row_index == 6)
                        strTime = "1 pm";
                    else if(row_index == 7)
                        strTime = "2 pm";
                    else if(row_index == 8)
                        strTime = "3 pm";
                    else if(row_index == 9)
                        strTime = "4 pm";
                    else if(row_index == 10)
                        strTime = "5 pm";
                    else
                        strTime = "6 pm";

                    if(column_index == 1)
                        strHours = "2 hours";
                    else if(column_index == 2)
                        strHours = "3 hours";
                    else if(column_index == 3)
                        strHours = "4 hours";
                    else if(column_index == 4)
                        strHours = "5 hours";
                    else if(column_index == 5)
                        strHours = "6 hours";
                    else if(column_index == 6)
                        strHours = "7 hours";
                    else if(column_index == 7)
                        strHours = "8 hours";
                    else if(column_index == 8)
                        strHours = "9 hours";
                    else if(column_index == 9)
                        strHours = "10 hours";
                    else if(column_index == 10)
                        strHours = "11 hours";
                    else
                        strHours = "12 hours";

                    new AlertDialog.Builder(refActivity)
                            .setIcon(R.drawable.ic_launcher_round)
                            .setTitle("Assigning Slot")
                            .setMessage("Confirm booking ?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    final ProgressDialog pd = ProgressDialog.show(refActivity,"Assigning slot","Please wait...",true);

                                    dlRef = dbRef.child("DL");

                                    ValueEventListener dlListener = new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            for(DataSnapshot ds : dataSnapshot.getChildren())
                                            {
                                                uEmail = ds.child("userMail").getValue(String.class);

                                                if(checkMail.equals(uEmail))
                                                {
                                                    dlFlag = ds.child("userDLFlag").getValue(Integer.class);
                                                    String date = ds.child("licenseExpiryDate").getValue(String.class);
                                                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                                                    Date strDate = null;
                                                    try {
                                                        strDate = df.parse(date);
                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }

                                                    Date currDate = Calendar.getInstance().getTime();
                                                    if(dlFlag == 0) {

                                                        pd.dismiss();

                                                         Snackbar.make(parentLayout, "Please fill up your driving license's details first", Snackbar.LENGTH_LONG)
                                                                .setDuration(3000)
                                                                .setAction("Close", new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {

                                                                    }
                                                                })
                                                                .setActionTextColor(getResources().getColor(android.R.color.background_light))
                                                                .show();
                                                    }
                                                    else if(currDate.compareTo(strDate) >= 0)
                                                    {
                                                        pd.dismiss();
                                                        Snackbar.make(parentLayout,"Driving License expired",Snackbar.LENGTH_LONG)
                                                                .setDuration(3000)
                                                                .setAction("close", new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {

                                                                    }
                                                                })
                                                                .setActionTextColor(getResources().getColor(android.R.color.background_light))
                                                                .show();

                                                    }
                                                    else
                                                    {
                                                        sldbRef = dbRef.child("Slot");
                                                        ValueEventListener slotListener = new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                for (DataSnapshot ds : dataSnapshot.getChildren())
                                                                {
                                                                    uEmail = ds.child("userMail").getValue(String.class);

                                                                    if(checkMail.equals(uEmail))
                                                                    {
                                                                        final int slotFlag = ds.child("slotFlag").getValue(Integer.class);

                                                                        if(slotFlag >=1 && slotFlag <= 7 )
                                                                        {

                                                                            Snackbar.make(parentLayout,"Please finish your current slot's work first to book another slot",Snackbar.LENGTH_LONG)
                                                                                    .setDuration(3000)
                                                                                    .setAction("Close", new View.OnClickListener() {
                                                                                        @Override
                                                                                        public void onClick(View v) {

                                                                                        }
                                                                                    })
                                                                                    .setActionTextColor(getResources().getColor(android.R.color.background_light))
                                                                                    .show();

                                                                            pd.dismiss();

                                                                        }
                                                                        else
                                                                        {
                                                                            final String date = tvIDate.getText().toString().trim();
                                                                            final int startTimeIndex = row_index;

                                                                            dbRef = FirebaseDatabase.getInstance().getReference();
                                                                            assRef = dbRef.child("Assignment");

                                                                            assRef.addListenerForSingleValueEvent( new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                                                            checkDate=ds.child("date").getValue(String.class);

                                                                                            assert checkDate != null;
                                                                                            if (checkDate.equals(date)) {
                                                                                                checkID=ds.child("id").getValue(String.class);
                                                                                                String loopID=ds.child("id").getValue(String.class);
                                                                                                int intStartTime=startTimeIndex;

                                                                                                assert loopID != null;
                                                                                                if (loopID.equals(checkID)) {
                                                                                                    int val1=ds.child(Integer.toString(intStartTime)).getValue(Integer.class);

                                                                                                    if (val1 < 10) {
                                                                                                        assRef.child(loopID).child(Integer.toString(intStartTime)).setValue(val1 + 1);
                                                                                                        companyFlag=1;
                                                                                                        break;
                                                                                                    } else {
                                                                                                        intStartTime+=11;
                                                                                                        int val2=ds.child(Integer.toString(intStartTime)).getValue(Integer.class);

                                                                                                        if (val2 < 10) {
                                                                                                            assRef.child(loopID).child(Integer.toString(intStartTime)).setValue(val2 + 1);
                                                                                                            companyFlag=2;
                                                                                                            break;
                                                                                                        } else {
                                                                                                            intStartTime+=11;
                                                                                                            int val3=ds.child(Integer.toString(intStartTime)).getValue(Integer.class);

                                                                                                            if (val3 < 10) {
                                                                                                                assRef.child(loopID).child(Integer.toString(intStartTime)).setValue(val3 + 1);
                                                                                                                companyFlag=3;
                                                                                                                break;
                                                                                                            } else {
                                                                                                                intStartTime+=11;
                                                                                                                int val4=ds.child(Integer.toString(intStartTime)).getValue(Integer.class);

                                                                                                                if (val4 < 10) {
                                                                                                                    assRef.child(loopID).child(Integer.toString(intStartTime)).setValue(val4 + 1);
                                                                                                                    companyFlag=4;
                                                                                                                    break;
                                                                                                                } else {
                                                                                                                    intStartTime+=11;
                                                                                                                    int val5=ds.child(Integer.toString(intStartTime)).getValue(Integer.class);

                                                                                                                    if (val5 < 10) {
                                                                                                                        assRef.child(loopID).child(Integer.toString(intStartTime)).setValue(val5 + 1);
                                                                                                                        companyFlag=5;
                                                                                                                        break;
                                                                                                                    } else {
                                                                                                                        intStartTime+=11;
                                                                                                                        int val6=ds.child(Integer.toString(intStartTime)).getValue(Integer.class);

                                                                                                                        if (val6 < 10) {
                                                                                                                            assRef.child(loopID).child(Integer.toString(intStartTime)).setValue(val6 + 1);
                                                                                                                            companyFlag=6;
                                                                                                                            break;
                                                                                                                        } else {
                                                                                                                            intStartTime+=11;
                                                                                                                            int val7=ds.child(Integer.toString(intStartTime)).getValue(Integer.class);

                                                                                                                            if (val7 < 10) {
                                                                                                                                assRef.child(loopID).child(Integer.toString(intStartTime)).setValue(val7 + 1);
                                                                                                                                companyFlag=7;
                                                                                                                                break;
                                                                                                                            } else {

                                                                                                                                Snackbar.make(parentLayout, "Slots full. Please select different slot timing.", Snackbar.LENGTH_LONG)
                                                                                                                                        .setDuration(3000)
                                                                                                                                        .setAction("Close", new View.OnClickListener() {
                                                                                                                                            @Override
                                                                                                                                            public void onClick(View v) {

                                                                                                                                            }
                                                                                                                                        })
                                                                                                                                        .setActionTextColor(getResources().getColor(android.R.color.background_light))
                                                                                                                                        .show();

                                                                                                                                pd.dismiss();

                                                                                                                                break;
                                                                                                                            }

                                                                                                                        }

                                                                                                                    }

                                                                                                                }

                                                                                                            }


                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                                break;
                                                                                            }

                                                                                        }

                                                                                        if (!checkDate.equals(date))
                                                                                            addDate(date);

                                                                                        sldbRef=dbRef.child("Slot");

                                                                                            ValueEventListener slotListener=new ValueEventListener() {
                                                                                                @Override
                                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                                                                                        uEmail = ds.child("userMail").getValue(String.class);
                                                                                                        slotFlagCheck = ds.child("slotFlag").getValue(Integer.class);

                                                                                                        assert uEmail != null;
                                                                                                        if (uEmail.equals(checkMail) && (slotFlagCheck == 0 || slotFlagCheck == 8)) {

                                                                                                            id=ds.child("userId").getValue(String.class);
                                                                                                            assert id != null;
                                                                                                            Intent intent = new Intent(refActivity, BookingConfirmedActivity.class);

                                                                                                            switch (companyFlag) {

                                                                                                                case 2:
                                                                                                                    sldbRef.child(id).child("showDate").setValue(date);
                                                                                                                    sldbRef.child(id).child("showStartTime").setValue(strTime);
                                                                                                                    sldbRef.child(id).child("showEndTime").setValue(strHours);
                                                                                                                    sldbRef.child(id).child("slotFlag").setValue(2);
                                                                                                                    intent.putExtra("company", companyFlag);
                                                                                                                    intent.putExtra("date", date);
                                                                                                                    intent.putExtra("time", strTime);
                                                                                                                    intent.putExtra("hours", strHours);
                                                                                                                    startActivity(intent);
                                                                                                                    break;
                                                                                                                case 3:
                                                                                                                    sldbRef.child(id).child("showDate").setValue(date);
                                                                                                                    sldbRef.child(id).child("showStartTime").setValue(strTime);
                                                                                                                    sldbRef.child(id).child("showEndTime").setValue(strHours);
                                                                                                                    sldbRef.child(id).child("slotFlag").setValue(3);
                                                                                                                    intent.putExtra("company", companyFlag);
                                                                                                                    intent.putExtra("date", date);
                                                                                                                    intent.putExtra("time", strTime);
                                                                                                                    intent.putExtra("hours", strHours);
                                                                                                                    startActivity(intent);
                                                                                                                    break;
                                                                                                                case 4:
                                                                                                                    sldbRef.child(id).child("showDate").setValue(date);
                                                                                                                    sldbRef.child(id).child("showStartTime").setValue(strTime);
                                                                                                                    sldbRef.child(id).child("showEndTime").setValue(strHours);
                                                                                                                    sldbRef.child(id).child("slotFlag").setValue(4);
                                                                                                                    intent.putExtra("company", companyFlag);
                                                                                                                    intent.putExtra("date", date);
                                                                                                                    intent.putExtra("time", strTime);
                                                                                                                    intent.putExtra("hours", strHours);
                                                                                                                    startActivity(intent);
                                                                                                                    break;
                                                                                                                case 5:
                                                                                                                    sldbRef.child(id).child("showDate").setValue(date);
                                                                                                                    sldbRef.child(id).child("showStartTime").setValue(strTime);
                                                                                                                    sldbRef.child(id).child("showEndTime").setValue(strHours);
                                                                                                                    sldbRef.child(id).child("slotFlag").setValue(5);
                                                                                                                    intent.putExtra("company", companyFlag);
                                                                                                                    intent.putExtra("date", date);
                                                                                                                    intent.putExtra("time", strTime);
                                                                                                                    intent.putExtra("hours", strHours);
                                                                                                                    startActivity(intent);
                                                                                                                    break;
                                                                                                                case 6:
                                                                                                                    sldbRef.child(id).child("showDate").setValue(date);
                                                                                                                    sldbRef.child(id).child("showStartTime").setValue(strTime);
                                                                                                                    sldbRef.child(id).child("showEndTime").setValue(strHours);
                                                                                                                    sldbRef.child(id).child("slotFlag").setValue(6);
                                                                                                                    intent.putExtra("company", companyFlag);
                                                                                                                    intent.putExtra("date", date);
                                                                                                                    intent.putExtra("time", strTime);
                                                                                                                    intent.putExtra("hours", strHours);
                                                                                                                    startActivity(intent);
                                                                                                                    break;
                                                                                                                case 7:
                                                                                                                    sldbRef.child(id).child("showDate").setValue(date);
                                                                                                                    sldbRef.child(id).child("showStartTime").setValue(strTime);
                                                                                                                    sldbRef.child(id).child("showEndTime").setValue(strHours);
                                                                                                                    sldbRef.child(id).child("slotFlag").setValue(7);
                                                                                                                    intent.putExtra("company", companyFlag);
                                                                                                                    intent.putExtra("date", date);
                                                                                                                    intent.putExtra("time", strTime);
                                                                                                                    intent.putExtra("hours", strHours);
                                                                                                                    startActivity(intent);
                                                                                                                    break;
                                                                                                                default:
                                                                                                                    sldbRef.child(id).child("showDate").setValue(date);
                                                                                                                    sldbRef.child(id).child("showStartTime").setValue(strTime);
                                                                                                                    sldbRef.child(id).child("showEndTime").setValue(strHours);
                                                                                                                    sldbRef.child(id).child("slotFlag").setValue(1);
                                                                                                                    intent.putExtra("company", companyFlag);
                                                                                                                    intent.putExtra("date", date);
                                                                                                                    intent.putExtra("time", strTime);
                                                                                                                    intent.putExtra("hours", strHours);
                                                                                                                    startActivity(intent);

                                                                                                            }

                                                                                                        }
                                                                                                    }



                                                                                                }

                                                                                                @Override
                                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                    startActivity(new Intent(refActivity, BookingFailedActivity.class));

                                                                                                }
                                                                                            };

                                                                                            sldbRef.addListenerForSingleValueEvent(slotListener);



                                                                                    }

                                                                                    @Override
                                                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                    pd.dismiss();

                                                                                    }

                                                                                });


                                                                        }
                                                                    }
                                                                }

                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                pd.dismiss();

                                                            }
                                                        };

                                                        sldbRef.addListenerForSingleValueEvent(slotListener);
                                                        pd.dismiss();


                                                    }

                                                    break;

                                                }
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                            pd.dismiss();

                                        }


                                    };

                                    dlRef.addListenerForSingleValueEvent(dlListener);

                                }
                            })
                            .setNegativeButton("No", null).show();

                }

            }


        });

        getImages();

        return parentHolder;

    }

    private void getImages()
    {
        mImageUrls.add("https://www.underconsideration.com/brandnew/archives/flipkart_logo_detail.jpg");
        mImageUrls.add("https://vietbrands.vn/wp-content/uploads/2017/08/FedEx-Express-logo-1024x768.png");
        mImageUrls.add("https://planetexpress.com/wp-content/uploads/2018/11/aramex.png");
        mImageUrls.add("https://darwinbox.com/images/Delhivery_Logo_Final.png");
        mImageUrls.add("https://www.phoeniixx.com/wp-content/uploads/2014/11/blue-dart-logo.png?x53938");
        mImageUrls.add("https://content1.jdmagicbox.com/comp/bangalore/n1/080pxx80.xx80.170707151527.w9n1/catalogue/dtdc-courier-sarjapura-bangalore-courier-services-eqizi.jpg?interpolation=lanczos-none&output-format=jpg&resize=1024:*&crop=1024:270px;*,*");
        mImageUrls.add("https://2.bp.blogspot.com/-3a7iMhpW5T8/Vg84-GHzZ4I/AAAAAAAAIU8/ZmLvzCQnwIw/s1600/India%2BPost%2BLOGO.jpg");
        initRecyclerView();
    }

    private void initRecyclerView()
    {
        Log.d(TAG,"initRecyclerView: init recyclerView");

        final LinearLayoutManager layoutManager = new LinearLayoutManager(refActivity,LinearLayoutManager.HORIZONTAL,false);
        RecyclerView recyclerView = (RecyclerView) parentHolder.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        SnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(recyclerView);
        RecyclerViewImageAdapter adapter = new RecyclerViewImageAdapter(refActivity,mImageUrls);
        recyclerView.setAdapter(adapter);


    }

    public class startTimeClick implements AdapterView.OnItemSelectedListener
    {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                 row_index = i;
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    public class workHoursClick implements AdapterView.OnItemSelectedListener
    {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            column_index = i;
            //Toast.makeText(refActivity,"wh "+column_index, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    private void addDate(String date)
    {
        assRef = FirebaseDatabase.getInstance().getReference("Assignment");
        String id = assRef.push().getKey();
        int i ;
        assert id != null;
        assRef.child(id).child("date").setValue(date);
        assRef.child(id).child("id").setValue(id);

        for (i = 1; i<=77; i++)
        {
            String strNum = Integer.toString(i);
            assRef.child(id).child(strNum).setValue(0);
        }

        assRef.child(id).child(Integer.toString(row_index)).setValue(1);

    }


}



