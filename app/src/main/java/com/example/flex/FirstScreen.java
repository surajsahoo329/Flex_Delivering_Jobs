package com.example.flex;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirstScreen extends Activity  {

    Handler handler;
    TextView tv1,tv2;
    ImageView imageView;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_screen);

        tv1 = (TextView)findViewById(R.id.flex_id);
        Typeface myFont1 = Typeface.createFromAsset(getAssets(),"fonts/coolvetica_i.ttf");
        tv1.setTypeface(myFont1);

        tv2 = (TextView)findViewById(R.id.catch_line);
        Typeface myFont2 = Typeface.createFromAsset(getAssets(),"fonts/coolvetica_i.ttf");
        tv2.setTypeface(myFont2);

        imageView = (ImageView)findViewById(R.id.logo_id);

        Bitmap bm = ((BitmapDrawable)getResources().getDrawable(R.drawable.delivery)).getBitmap();
        Bitmap ir = Bitmap.createBitmap(bm.getWidth(),bm.getHeight(),bm.getConfig());
        Canvas canvas = new Canvas(ir);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(bm, Shader.TileMode.CLAMP,Shader.TileMode.CLAMP));
        canvas.drawRoundRect((new RectF(0,0,bm.getWidth(),bm.getHeight())),100,100,paint);
        imageView.setImageBitmap(ir);

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
        },3000);



    }
}