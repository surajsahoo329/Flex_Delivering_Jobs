package com.example.flex;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    FirebaseAuth auth;
    TextView tvWelcome, tvEmail, tvDLTabTitle;
    DatabaseReference dbRef,usrRef;
    String uEmail,checkEmail;
    String id, name, getImageUrl;
    FrameLayout frameLayout;
    boolean openDL, openProfile, openBooking, openDLSubmit;
    private TabLayout tabLayout;

    ImageView imageView;
    private ViewPager viewPager;

    @RequiresApi(api=Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavigationView navigationView = findViewById(R.id.nav_view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this, android.R.color.background_light));// set status background white
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w=getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        toolbar.setTitle("Flex");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.background_dark));
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(android.R.color.background_dark));
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        frameLayout=findViewById(R.id.fragment_container);
        tvDLTabTitle=findViewById(R.id.tvDLTabTitle);
        viewPager=findViewById(R.id.viewPager);
        tabLayout=findViewById(R.id.tabLayout);


        dbRef=FirebaseDatabase.getInstance().getReference();
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        checkEmail=user.getEmail();
        usrRef=dbRef.child("User");

        final StorageReference mStorageRef=FirebaseStorage.getInstance().getReference();
        final StorageReference imgRef=mStorageRef.child(checkEmail + "/photo.jpg");
        final long TEN_MEGABYTES=10024 * 10024;

        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                getImageUrl=uri.toString();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


            }
        });

        imgRef.getBytes(TEN_MEGABYTES).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {

                Glide.with(MainActivity.this)
                        .load(getImageUrl)
                        .apply(RequestOptions.circleCropTransform())
                        .into(imageView);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


            }
        });


        auth = FirebaseAuth.getInstance();

        View hView = navigationView.getHeaderView(0);
        tvWelcome=hView.findViewById(R.id.tvWelcome);
        tvEmail=hView.findViewById(R.id.tvNavEmail);
        imageView=hView.findViewById(R.id.nav_header_imageView);

        View parentLayout = findViewById(android.R.id.content);

        getUName();

        if (ChangePasswordFragment.resetPass == 1) {
            Snackbar.make(parentLayout,"Password changed successfully", Snackbar.LENGTH_LONG)
                    .setDuration(3000)
                    .setAction("Close", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    })
                    .setActionTextColor(getResources().getColor(android.R.color.background_light))
                    .show();
            ChangePasswordFragment.resetPass=0;
        }

        if (EditFragment.updateFlag == 1) {
            Snackbar.make(parentLayout, "Profile Updated", Snackbar.LENGTH_LONG)
                    .setDuration(3000)
                    .setAction("Close", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    })
                    .setActionTextColor(getResources().getColor(android.R.color.background_light))
                    .show();

            EditFragment.updateFlag=0;
        }

        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();

        Bundle extras=getIntent().getExtras();

        if (extras != null && extras.containsKey("openProfile"))
            openProfile=extras.getBoolean("openProfile");
        if (openProfile) {

            tvDLTabTitle.setVisibility(View.GONE);
            ft.replace(R.id.fragment_container, new ProfileFragment());
            ft.commit();
        }


        if (extras != null && extras.containsKey("openDL"))
            openDL=extras.getBoolean("openDL");
        if (openDL) {
            ft.replace(R.id.fragment_container, new DLFragment());
            ft.commit();
        }

        if (extras != null && extras.containsKey("openBooking"))
            openBooking=extras.getBoolean("openBooking");
        if (openBooking) {

            tvDLTabTitle.setVisibility(View.GONE);
            ft.replace(R.id.fragment_container, new BookingFragment());
            ft.commit();
        }

        if (extras != null && extras.containsKey("openDLSubmit"))
            openDLSubmit=extras.getBoolean("openDLSubmit");
        if (openDLSubmit) {

            Snackbar.make(parentLayout, "License Details Submitted", Snackbar.LENGTH_LONG)
                    .setDuration(3000)
                    .setAction("Close", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    })
                    .setActionTextColor(getResources().getColor(android.R.color.background_light))
                    .show();

            frameLayout.setVisibility(View.GONE);
            tvDLTabTitle.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.VISIBLE);
            TabAdapter adapter=new TabAdapter(getSupportFragmentManager());
            adapter.addFragment(new DLFragment(), "Edit Details");
            adapter.addFragment(new LicenseDetailsFragment(), "View Details");
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
            drawer.closeDrawer(GravityCompat.START);
        }

    }

    private void getUName() {


        ValueEventListener userListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    uEmail = ds.child("userMail").getValue(String.class);

                    assert uEmail != null;
                    if (uEmail.equals(checkEmail)) {

                        name = ds.child("userName").getValue(String.class);
                        assert name != null;
                        tvWelcome.setText(name);
                        tvEmail.setText(checkEmail);
                        break;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(MainActivity.this, databaseError.getCode(),Toast.LENGTH_LONG).show();
            }
        };

        usrRef.addListenerForSingleValueEvent(userListener);

    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        if(getSupportFragmentManager().getBackStackEntryCount()==0)
        {
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.ic_launcher_round)
                    .setTitle("Closing App")
                    .setMessage("Do you want to close the app?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                                 Intent it = new Intent(Intent.ACTION_MAIN);
                                 it.addCategory(Intent.CATEGORY_HOME);
                                 it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                 startActivity(it);


                        }
                    })
                    .setNegativeButton("No",null).show();

        }
        else
            super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation view item clicks here.
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        Fragment ob ;
        switch (menuItem.getItemId()) {

            case R.id.nav_profile:
                tvDLTabTitle.setVisibility(View.GONE);
                viewPager.setVisibility(View.GONE);
                tabLayout.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
                ob=new ProfileFragment();
                ft.replace(R.id.fragment_container,ob);
                ft.addToBackStack(null);
                ft.commit();
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_dl:
                frameLayout.setVisibility(View.GONE);
                tvDLTabTitle.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.VISIBLE);
                tabLayout.setVisibility(View.VISIBLE);
                TabAdapter adapter=new TabAdapter(getSupportFragmentManager());
                adapter.addFragment(new DLFragment(), "Edit Details");
                adapter.addFragment(new LicenseDetailsFragment(), "View Details");
                viewPager.setAdapter(adapter);
                tabLayout.setupWithViewPager(viewPager);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_booking:
                tvDLTabTitle.setVisibility(View.GONE);
                viewPager.setVisibility(View.GONE);
                tabLayout.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
                ob=new BookingFragment();
                ft.replace(R.id.fragment_container,ob);
                ft.addToBackStack(null);
                ft.commit();
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_slots:
                tvDLTabTitle.setVisibility(View.GONE);
                viewPager.setVisibility(View.GONE);
                tabLayout.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
                ob = new SlotFragment();
                ft.replace(R.id.fragment_container,ob);
                ft.addToBackStack(null);
                ft.commit();
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_contacts:
                tvDLTabTitle.setVisibility(View.GONE);
                viewPager.setVisibility(View.GONE);
                tabLayout.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
                ob = new ContactsFragment();
                ft.replace(R.id.fragment_container,ob);
                ft.addToBackStack(null);
                ft.commit();
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_feedback:
                tvDLTabTitle.setVisibility(View.GONE);
                viewPager.setVisibility(View.GONE);
                tabLayout.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
                ob = new FeedbackFragment();
                ft.replace(R.id.fragment_container,ob);
                ft.addToBackStack(null);
                ft.commit();
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_about:
                tvDLTabTitle.setVisibility(View.GONE);
                viewPager.setVisibility(View.GONE);
                tabLayout.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
                ob = new AboutFragment();
                ft.replace(R.id.fragment_container,ob);
                ft.addToBackStack(null);
                ft.commit();
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_share:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Flex - Delivering Jobs");
                String message = "\nLet me recommend you this application - Flex (Beta)\n\n";
                message = message + "https://drive.google.com/drive/folders/1pw-ZdvNKvJ0ITXn9YvyXYb9v7zlIPb_q?usp=sharing \n\n";
                i.putExtra(Intent.EXTRA_TEXT, message);
                startActivity(Intent.createChooser(i, "Share Via"));
                break;
            case R.id.nav_logout:
                new AlertDialog.Builder(this)
                        .setIcon(R.drawable.ic_launcher_round)
                        .setTitle("Logging out")
                        .setMessage("Do you want to log out?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                auth.signOut();
                                Intent it = new Intent(MainActivity.this,LoginActivity.class);
                                startActivity(it);
                                finish();
                            }
                        })
                        .setNegativeButton("No",null).show();


        }


        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


}


