package com.nexflare.cloud126;

import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoViewAttacher;

import static com.nexflare.cloud126.R.id.imageView;

public class AnnapuraMenu extends AppCompatActivity {
    ImageView iv;
    Matrix mMatrix=new Matrix();
    Float scale=1f;
    ScaleGestureDetector SGF;
    PhotoViewAttacher mAttacher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annapura_menu);

        iv= (ImageView) findViewById(imageView);
        Picasso.with(this).load("https://www.jiitsimplified.com/img/menu_gif.gif").fit().centerInside().into(iv);
        //SGF=new ScaleGestureDetector(this,new scaleListner());
        mAttacher=new PhotoViewAttacher(iv);
        mAttacher.update();
    }

    /*private class scaleListner extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scale=scale*detector.getScaleFactor();
            scale=Math.max(0.1f,Math.min(scale,5f));
            mMatrix.setScale(scale,scale);
            iv.setImageMatrix(mMatrix);
            return true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        SGF.onTouchEvent(event);
        return true;
    }*/
}
