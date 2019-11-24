package com.example.flex;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirstScreen extends Activity  {

    Handler handler;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_screen);

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
                    Intent it = new Intent(FirstScreen.this,LoginActivity.class);
                    startActivity(it);
                    finish();
                }
                else
                {
                    Intent it = new Intent(FirstScreen.this,MainActivity.class);
                    startActivity(it);
                    finish();
                }
            }
        }, 5000);



    }
}