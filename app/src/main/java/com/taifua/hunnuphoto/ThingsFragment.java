package com.taifua.hunnuphoto;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;
import static android.support.constraint.Constraints.TAG;


public class ThingsFragment extends Fragment implements View.OnClickListener
{
    private static final int CHOOSE_PHOTO = 102;
    private static final int TAKE_PHOTO = 101;
    private Button mButton1;
    private Button mButton2;
    private Button mButton3;
    private Button mButton4;
    private ImageView imageView;
    private TextView imageResult;
    private TextView imageSize;
    private TextView imageName;
    private Uri imageUri;
    private static final int COM_RATIO = 100;
    private static final int UPDATE_TEXT = 1;
    private final static String sampleName = "image.jpg";
    StringBuffer resultText = new StringBuffer();
    HashMap<String, String> thingsMap = new HashMap<>();
    String[] thingsType = {"isBookMaterial", "isOutdoorScenery", "isEquipment", "isSocialPlace", "isGroupPhoto"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_things, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        mButton1 = view.findViewById(R.id.isBookMaterialButton);
        mButton2 = view.findViewById(R.id.isOutdoorSceneryButton);
        mButton2 = view.findViewById(R.id.isGroupPhotoButton);
        mButton2 = view.findViewById(R.id.isSocialPlaceButton);
        mButton1.setOnClickListener(this);
        mButton2.setOnClickListener(this);
        mButton2.setOnClickListener(this);
        mButton2.setOnClickListener(this);
    }


    void thingsInit()
    {
        thingsMap.put("isBookMaterial", "书籍资料");
        thingsMap.put("isOutdoorScenery", "室外风景");
        thingsMap.put("isEquipment", "设备器材");
        thingsMap.put("isSocialPlace", "社交场所");
        thingsMap.put("isGroupPhoto", "人物合影");
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.take_photo:
                takePhoto();
                break;
            case R.id.choose_from_album:
                openAlbum();
                break;
            default:
                break;
        }
    }


    /*
     * 异步消息处理，在主线程中更改UI
     */

    private final Handler handler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case UPDATE_TEXT:
                    imageResult.setText(resultText);
                    break;
                default:
                    break;
            }
        }
    };

    /*
     * 打开相机
     */

    private void takePhoto()
    {
        Intent intent;
        File outputImage = new File(getActivity().getExternalCacheDir(), sampleName);
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
            imageUri = FileProvider.getUriForFile(getContext(), "com.taifua.hunnuphoto.fileprovider", outputImage);
        else
            imageUri = Uri.fromFile(outputImage);
        intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    /*
     * 打开相册
     */
    private void openAlbum()
    {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        switch (requestCode)
        {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK)
                {
                    try
                    {
                        imageView.setImageURI(imageUri);
                        Bitmap bm = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
                        String imageBase64 = bitmapToBase64(bm);
                        String address = "http://139.9.83.2:8080/Photos/GetVector.do";
                        String imageType = "jpg";
                        int imageHeight = bm.getHeight();
                        int imageWidth = bm.getWidth();
                        imageName.setText(sampleName);
                        imageSize.setText(imageWidth + " * " + imageHeight);
                        fetchDataByPost(address, imageBase64, imageType);
                    }
                    catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK)
                {
                    Uri uri = data.getData();
                    imageView.setImageURI(uri);
                    String imagePath = getRealPathFromUri(getContext(), uri);
                    int posPath = imagePath.lastIndexOf("/");
                    int posType = imagePath.lastIndexOf(".");
                    String imageNameText = imagePath.substring(posPath + 1);
                    String imageType = imagePath.substring(posType + 1);
                    String address = "http://139.9.83.2:8080/Photos/GetVector.do";
                    BitmapFactory.Options bfoOptions = new BitmapFactory.Options();
                    bfoOptions.inScaled = false;
                    Bitmap bm = BitmapFactory.decodeFile(imagePath, bfoOptions);
                    String imageBase64 = bitmapToBase64(bm);
                    int imageHeight = bm.getHeight();
                    int imageWidth = bm.getWidth();
                    imageName.setText(imageNameText);
                    imageSize.setText(imageWidth + " * " + imageHeight);
                    fetchDataByPost(address, imageBase64, imageType);
                }
                break;
            default:
                break;
        }
    }

    private String bitmapToBase64(Bitmap bitmap)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        bitmap = changeBitmapSize(bitmap);
        bitmap.compress(Bitmap.CompressFormat.JPEG, COM_RATIO, bos);
        byte[] bytes = bos.toByteArray();
        return new String(Base64.encode(bytes, Base64.NO_WRAP));
    }

    private void fetchDataByPost(final String address, String imageBase64, String imageType)
    {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("image", imageBase64);
        paramsMap.put("type", imageType);
        final FormBody.Builder builder = new FormBody.Builder();
        for (String key : paramsMap.keySet())
            builder.add(key, paramsMap.get(key));
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(100, TimeUnit.SECONDS).readTimeout(100, TimeUnit.SECONDS).build();
                RequestBody formBody = builder.build();
                Request request = new Request.Builder().url(address).post(formBody).build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback()
                {
                    @Override
                    public void onFailure(Call call, IOException e)
                    {
                        e.printStackTrace();
                        Log.d(TAG, "onFailure: fail");
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException
                    {
                        String result = response.body().string();
                        Log.d(TAG, "onResponse: success" + result);
                        parseJSONWithJSONObject(result);
                    }
                });
            }
        }).start();
    }

    private void parseJSONWithJSONObject(String jsonData)
    {
        resultText = null;
        try
        {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String result = jsonObject.getString(thingsType[i]);
                if (result.equals("true"))
                    resultText.append(thingsMap.get(thingsType[i])).append(" ");
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        Log.d(TAG, "parseJSONWithJSONObject: " + resultText);
        Message message = new Message();
        message.what = UPDATE_TEXT;
        handler.sendMessage(message);
    }

    /*
     * 改变图片分辨率
     */

    private Bitmap changeBitmapSize(Bitmap bitmap)
    {
        int bWidth = bitmap.getWidth();
        int bHeight = bitmap.getHeight();
        if (bWidth > 2000)
        {
            float scale = bWidth / 1000;
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bWidth, bHeight, matrix, true);
        }
        return bitmap;
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs)
    {
        String path = null;
        String[] projection = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try
        {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst())
            {
                int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
                path = cursor.getString(columnIndex);
            }
        } catch (Exception e)
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
        return path;
    }

    private static String getRealPathFromUri(Context context, Uri uri)
    {
        String filePath = null;
        if (DocumentsContract.isDocumentUri(context, uri))
        {
            // 如果是document类型的 uri, 则通过document id来进行处理
            String documentId = DocumentsContract.getDocumentId(uri);
            if (isMediaDocument(uri))
            { // MediaProvider
                // 使用':'分割
                String id = documentId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = {id};
                filePath = getDataColumn(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, selectionArgs);
            }
            else if (isDownloadsDocument(uri))
            { // DownloadsProvider
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId));
                filePath = getDataColumn(context, contentUri, null, null);
            }
        }
        else if ("content".equalsIgnoreCase(uri.getScheme()))
        {
            // 如果是 content 类型的 Uri
            filePath = getDataColumn(context, uri, null, null);
        }
        else if ("file".equals(uri.getScheme()))
        {
            // 如果是 file 类型的 Uri,直接获取图片对应的路径
            filePath = uri.getPath();
        }
        return filePath;
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is MediaProvider
     */
    private static boolean isMediaDocument(Uri uri)
    {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is DownloadsProvider
     */
    private static boolean isDownloadsDocument(Uri uri)
    {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
}
