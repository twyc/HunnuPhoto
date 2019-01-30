package com.taifua.material;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;


public class AssistantFragment extends Fragment
{

    private CustomImageButton customImageButton;
    private TextView textView;
    private Bitmap bitmap;
    private Bitmap resizeBitmap;
    private Bitmap transBitmap;
    private FaceDetector.Face[] myFace;
    private Button mBtnphotograph;
    private Button mBtnChoose;
    private Uri imageUri;
    private final static int photographCode = 1;
    private final static int chooseCode = 2;
    private int faceDetectedNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_assistant, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        textView = view.findViewById(R.id.ass_face_text);
        mBtnphotograph = view.findViewById(R.id.ass_photograph);
        mBtnChoose = view.findViewById(R.id.ass_choose);
        customImageButton = view.findViewById(R.id.ass_face);
        customImageButton.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        setListeners();
    }

    private void setListeners()
    {
        Onclick onclick = new Onclick();
        mBtnphotograph.setOnClickListener(onclick);
        mBtnChoose.setOnClickListener(onclick);
    }

    private class Onclick implements View.OnClickListener
    {
        @Override
        public void onClick(View view)
        {
            Intent intent = null;
            switch (view.getId())
            {
                case R.id.ass_photograph:
                    File outputImage = new File(getActivity().getExternalCacheDir(), "iamge.jpg");
                    try
                    {
                        if (outputImage.exists())
                            outputImage.delete();
                        outputImage.createNewFile();
                    }catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    if (Build.VERSION.SDK_INT >= 24)
                        imageUri = FileProvider.getUriForFile(getContext(), "com.taifua.material.fileprovider", outputImage);
                    else
                        imageUri = Uri.fromFile(outputImage);
                    intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, photographCode);
                    break;
                case R.id.ass_choose:
                    intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, chooseCode);
                    break;
                default:
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        bitmap = null;
        switch (requestCode)
        {
            case photographCode:
                if (resultCode == RESULT_OK)
                {
                    try
                    {
                        bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
                    }catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                }
                break;
            case chooseCode:
                if (resultCode == RESULT_OK)
                {
                    try
                    {
                        Uri uri = data.getData();
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                break;
            default:
        }

        if (bitmap == null)
        {
            Toast.makeText(getContext(), "未选择图像", Toast.LENGTH_SHORT).show();
            return ;
        }

        transBitmap = bitmap.copy(Bitmap.Config.RGB_565, true);

        if (!bitmap.isRecycled())
            bitmap.isRecycled();

        detectFace();

        drawFace();

        int ibWidth = customImageButton.getWidth();
        int ibHeight = customImageButton.getHeight();
        resizeBitmap = customImageButton.cropBitmap(transBitmap, ibWidth, ibHeight);

        customImageButton.setBitmap(resizeBitmap);
    }

    private void detectFace()
    {
        int numberofFace = 12;
        FaceDetector myFaceDetect;
        int imageWidth = bitmap.getWidth();
        int imageHeight = bitmap.getHeight();

        myFace = new FaceDetector.Face[numberofFace];
        myFaceDetect = new FaceDetector(imageWidth, imageHeight, numberofFace);
        faceDetectedNumber = myFaceDetect.findFaces(transBitmap, myFace);
        textView.setText("检测到 " + faceDetectedNumber + " 个人脸");
    }

    private void drawFace()
    {
        Canvas canvas = new Canvas(transBitmap);
        Paint myPaint = new Paint();
        myPaint.setColor(Color.RED);
        myPaint.setStyle(Paint.Style.STROKE);
        myPaint.setStrokeWidth(3);
        for (int i = 0; i < faceDetectedNumber; i++)
        {
            FaceDetector.Face face = myFace[i];
            PointF myMidPoint = new PointF();
            face.getMidPoint(myMidPoint);
            float myEyesDistance = face.eyesDistance();
            canvas.drawRect((int) (myMidPoint.x - myEyesDistance * 1.5),
                    (int) (myMidPoint.y - myEyesDistance * 1.5),
                    (int) (myMidPoint.x + myEyesDistance * 1.5),
                    (int) (myMidPoint.y + myEyesDistance * 1.8), myPaint);
        }
    }
}
