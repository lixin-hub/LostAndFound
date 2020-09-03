package com.example.belost.Util;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import java.io.ByteArrayOutputStream;

public class PictureUtil {
   private Activity activity;
    public PictureUtil(FragmentActivity activity){
        this.activity=activity;
    }

    public void openAlbum() {

        if (activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            activity.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }else{
            Intent intent = new Intent("android.intent.action.GET_CONTENT");
            intent.setType("image/*");
            activity.startActivityForResult(intent, 1); // 打开相册
        }
    }
    @TargetApi(19)
    public String handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        Log.d("TAG", "handleImageOnKitKat: uri is " + uri);
        if (DocumentsContract.isDocumentUri(activity, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            assert uri != null;
            if("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.parseLong(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else {
            assert uri != null;
            if ("content".equalsIgnoreCase(uri.getScheme())) {
                // 如果是content类型的Uri，则使用普通方式处理
                imagePath = getImagePath(uri, null);
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                // 如果是file类型的Uri，直接获取图片路径即可
                imagePath = uri.getPath();
            }
        }
        return imagePath; // 根据图片路径显示图片
    }


    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor =activity. getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /** * 图片转成string *  * @param bitmap * @return */
    public static String convertIconToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] appicon = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(appicon, Base64.DEFAULT);
    }


    /** * string转成bitmap *  * @param st */
    public static Bitmap convertStringToIcon(String st) {
        // OutputStream out;
        Bitmap bitmap = null;
        try {
            // out = new FileOutputStream("/sdcard/aa.jpg");
            byte[] bitmapArray;
            bitmapArray = Base64.decode(st, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
            // bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            return bitmap;
        } catch (Exception e) { return null; } }
    /**
     * 质量压缩
     * @param bmp 要压缩的图片
     * @param targetSize 最终要压缩的大小（KB）
     * @return
     */
    public Bitmap qualityCompress(Bitmap bmp, int targetSize) {

        //压缩后的图片输出到baos中
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, targetSize, baos);
        // 压缩程度，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > targetSize) {
            // 循环判断如果压缩后图片是否大于target,大于继续压缩
            baos.reset();// 重置baos即清空baos
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
            // 逐渐减少压缩率，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
            if (options <= 0) {
                break;
            }
        }
//        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        byte[] bytes = baos.toByteArray();
        // 得到最终质量压缩后的Bitmap
        Bitmap targetBmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        return targetBmp;
    }


}
