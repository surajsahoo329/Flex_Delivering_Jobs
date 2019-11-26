package com.example.flex;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirstScreenActivity extends Activity {

    Handler handler;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        ); // sets status bar transparent

        /*imageView = (ImageView)findViewById(R.id.logo_id);

        Bitmap bm = ((BitmapDrawable)getResources().getDrawable(R.drawable.delivery)).getBitmap();
        Bitmap ir = Bitmap.createBitmap(bm.getWidth(),bm.getHeight(),bm.getConfig());
        Canvas canvas = new Canvas(ir);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(bm, Shader.TileMode.CLAMP,Shader.TileMode.CLAMP));
        canvas.drawRoundRect((new RectF(0,0,bm.getWidth(),bm.getHeight())),100,100,paint);
        imageView.setImageBitmap(ir);*/ //set circular image

        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if(currentUser==null)
                {
                    Intent it=new Intent(FirstScreenActivity.this, LoginActivity.class);
                    startActivity(it);
                    finish();
                }
                else
                {
                    Intent intent=new Intent(FirstScreenActivity.this, MainActivity.class);
                    intent.putExtra("openBooking", true);
                    overridePendingTransition(0, 0);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    finish();
                    startActivity(intent);
                }
            }
        }, 5000);



    }
}