package com.taifua.hunnuphoto;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class FileManager
{
    private static FileManager mInstance;
    private static Context mContext;
    private static ContentResolver mContentResolver;
    private static Object mLock = new Object();

    public static FileManager getInstance(Context context)
    {
        if (mInstance == null)
        {
            synchronized (mLock)
            {
                if (mInstance == null)
                {
                    mInstance = new FileManager();
                    mContext = context;
                    mContentResolver = context.getContentResolver();
                }
            }
        }
        return mInstance;
    }


    /**
     * 通过图片文件夹的路径获取该目录下的图片
     */
    public List<String> getImgListByDir(String dir)
    {
        ArrayList<String> imgPaths = new ArrayList<>();
        File directory = new File(dir);
        if (directory == null || !directory.exists())
        {
            return imgPaths;
        }
        File[] files = directory.listFiles();
        for (File file : files)
        {
            String path = file.getAbsolutePath();
            if (FileUtils.isPicFile(path))
            {
                imgPaths.add(path);
            }
        }
        return imgPaths;
    }

    /**
     * 得到图片文件夹集合
     */
    public List<ImgFolderBean> getImageFolders()
    {
        List<ImgFolderBean> folders = new ArrayList<ImgFolderBean>();
        // 扫描图片
        Cursor c = null;
        try
        {
            c = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media.MIME_TYPE + "= ? or " + MediaStore.Images.Media.MIME_TYPE + "= ?", new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);
            List<String> mDirs = new ArrayList<String>();//用于保存已经添加过的文件夹目录
            while (c.moveToNext())
            {
                String path = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));// 路径
                File parentFile = new File(path).getParentFile();
                if (parentFile == null) continue;

                String dir = parentFile.getAbsolutePath();
                if (mDirs.contains(dir))//如果已经添加过
                    continue;

                mDirs.add(dir);//添加到保存目录的集合中
                ImgFolderBean folderBean = new ImgFolderBean();
                folderBean.setDir(dir);
                folderBean.setFistImgPath(path);
                if (parentFile.list() == null) continue;
                int count = parentFile.list(new FilenameFilter()
                {
                    @Override
                    public boolean accept(File dir, String filename)
                    {
                        if (filename.endsWith(".jpeg") || filename.endsWith(".jpg") || filename.endsWith(".png"))
                        {
                            return true;
                        }
                        return false;
                    }
                }).length;

                folderBean.setCount(count);
                folders.add(folderBean);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            if (c != null)
            {
                c.close();
            }
        }

        return folders;
    }
}
