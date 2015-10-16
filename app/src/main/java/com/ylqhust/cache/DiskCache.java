package com.ylqhust.cache;

import android.os.Environment;

import com.ylqhust.utils.MD5;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by apple on 15/9/29.
 */
public class DiskCache {
    private static DiskCache diskCache = null;
    private static final String SUB_DIR = "Cache";
    private File root = null;
    private HashMap<String,File> fileMap = null;
    Object mLock = new Object();

    private DiskCache()
    {
        root = new File(Environment.getExternalStorageDirectory(),SUB_DIR);
        root.mkdir();
        fileMap = toMap(root.listFiles());
    }



    public static DiskCache getInstance()
    {
        if (diskCache == null)
        {
            diskCache = new DiskCache();
        }
        return diskCache;
    }


    //根据文件名获取文件路径
    public File getFile(String key)
    {
        String md5 = MD5.GetMD5Code(key);
        if (fileMap != null && fileMap.containsKey(md5))
        {
            return fileMap.get(md5);
        }
        return null;
    }

    public String getString(String key) throws Exception {
        File file = getFile(key);
        if (file == null)
            return null;
        return readInputStream(new FileInputStream(file));
    }

    private String readInputStream(InputStream instream) throws Exception
    {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[]  buffer = new byte[1204];
        int len = 0;
        while ((len = instream.read(buffer)) != -1)
        {
            outStream.write(buffer,0,len);
        }
        instream.close();
        return new String(outStream.toByteArray());
    }

    //根据文件名删除文件
    public void delete(String key)
    {
        synchronized (mLock)
        {
            String md5 = MD5.GetMD5Code(key);
            //判断此文件是否存在
            if (fileMap!= null && fileMap.containsKey(md5))
            {
                //delete file
                fileMap.get(md5).delete();
                fileMap.remove(md5);
                System.out.println("Delete " + key + " Success");
            }
            return;
        }
    }

    //根据文件名和二进制数据保存文件
    public void save(String key,byte[] bytes)
    {
        synchronized (mLock)
        {
            String md5 = MD5.GetMD5Code(key);
            //判断是否存在
            if (fileMap != null && fileMap.containsKey(md5))
            {
                //此数据已经存在，删除
                delete(key);
            }
            //保存数据
            if (root != null && fileMap != null)
            {
                try {
                    File newFile = new File(root,md5);
                    FileOutputStream fos = new FileOutputStream(newFile);
                    fos.write(bytes);
                    fos.close();
                    fileMap.put(md5,newFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Save "+key+" Success");
            }
        }

    }

    private HashMap<String,File> toMap(File[] allFiles)
    {
        HashMap<String,File> hashMap = new HashMap<String,File>();

        if (allFiles != null)
        {
            for (File file : allFiles)
            {
                hashMap.put(file.getName(),file);
            }
        }
        return hashMap;
    }

    public void put(String key,File file)
    {
        String md5 = MD5.GetMD5Code(key);
        if (fileMap != null)
        {
            fileMap.put(md5,file);
        }
    }

}
