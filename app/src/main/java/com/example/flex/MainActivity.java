package com.example.flex;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    FirebaseAuth auth;
    public  static int flag = 0;
    TextView tvWelcome;
    DatabaseReference dbRef,usrRef;
    String uEmail,checkEmail;
    String id,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavigationView navigationView = findViewById(R.id.nav_view);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        auth = FirebaseAuth.getInstance();

        View hView = navigationView.getHeaderView(0);
        tvWelcome = (TextView) hView.findViewById(R.id.tvWelcome);

        View parentLayout = findViewById(android.R.id.content);

        getUName();

        if(ResetPasswordActivity.resetPass == 1)
        {
            Snackbar.make(parentLayout,"Password changed successfully", Snackbar.LENGTH_LONG)
                    .setDuration(3000)
                    .setAction("Close", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    })
                    .setActionTextColor(getResources().getColor(android.R.color.background_light))
                    .show();
            ResetPasswordActivity.resetPass = 0;
        }


        if(flag == 0) {

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container, new HomeFragment());
            ft.commit();
        }

        else
        {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container, new ProfileFragment());
            ft.commit();

        }

        drawer.closeDrawer(GravityCompat.START);

    }

    private void getUName() {


        dbRef= FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        checkEmail = user.getEmail();
        usrRef=dbRef.child("User");

        ValueEventListener userListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    uEmail = ds.child("userMail").getValue(String.class);

                    assert uEmail != null;
                    if (uEmail.equals(checkEmail)) {

                        name = ds.child("userName").getValue(String.class);
                        assert name != null;
                        String[] firstName=name.trim().split(" +");
                        tvWelcome.setText("Welcome, "+firstName[0]);
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
            case R.id.nav_home:
                ob = new HomeFragment();
                ft.replace(R.id.fragment_container,ob);
                ft.addToBackStack(null);
                ft.commit();
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_profile:
                ob = new ProfileFragment();
                ft.replace(R.id.fragment_container,ob);
                ft.addToBackStack(null);
                ft.commit();
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_dl:
                ob = new DLFragment();
                ft.replace(R.id.fragment_container,ob);
                ft.addToBackStack(null);
                ft.commit();
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_slots:
                ob = new SlotFragment();
                ft.replace(R.id.fragment_container,ob);
                ft.addToBackStack(null);
                ft.commit();
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_contacts:
                ob = new ContactsFragment();
                ft.replace(R.id.fragment_container,ob);
                ft.addToBackStack(null);
                ft.commit();
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_feedback:
                ob = new FeedbackFragment();
                ft.replace(R.id.fragment_container,ob);
                ft.addToBackStack(null);
                ft.commit();
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_about:
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


