package com.taifua.material;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomImageButton extends LinearLayout
{
    private TextView textView;
    private ImageView imageView;

    public CustomImageButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        imageView = new ImageView(context, attrs);
        imageView.setPadding(2, 2, 2, 2);

        textView = new TextView(context, attrs);
        textView.setBackgroundColor(Color.TRANSPARENT);
        textView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        textView.setPadding(0, 0, 0, 0);

        setClickable(true);
        setFocusable(true);
        setOrientation(LinearLayout.VERTICAL);
        addView(textView);
        addView(imageView);
    }

    public void setText(String text)
    {
        textView.setText(text);
    }

    public void setButtonEnable(Boolean bool)
    {
        imageView.setEnabled(bool);
    }

    public void clearnImage()
    {
        textView.setVisibility(View.VISIBLE);
        imageView.setImageDrawable(null);
    }

    public void setBitmap(Bitmap bm)
    {
        textView.setVisibility(View.GONE);
        imageView.setImageDrawable(null);
        imageView.setImageBitmap(bm);

    }

    public Bitmap cropBitmap(Bitmap bm, int ivbWidth, int ivbHeight)
    {
        Bitmap resizeBmp = null;
        try
        {
            Matrix matrix = new Matrix();
            float scale;
            if (ivbWidth <= ivbHeight)
            {
                scale = (float) ivbWidth / bm.getWidth();
                matrix.postScale(scale, scale); //长和宽放大缩小的比例
                resizeBmp = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
            }
            else
            {
                scale = (float) ivbHeight / bm.getHeight();
                matrix.postScale(scale, scale);
                resizeBmp = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
            }
            if (!bm.isRecycled())
                bm.recycle();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return resizeBmp;
    }
}