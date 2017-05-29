package bjtu.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.format.DateFormat;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

public class UploadFileActivity extends AppCompatActivity {

    private int userID = 0;
    private String frontFilename = null;
    private String backFilename = null;

    private ImageView frontImg;
    private ImageView backImg;
    private Button uploadFileBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_file);

        Intent intent = this.getIntent();
        userID = intent.getIntExtra("userID",0);

        uploadFileBtn = (Button) findViewById(R.id.uploadFileBtn);
        frontImg = (ImageView) findViewById(R.id.idCardFront);
        backImg = (ImageView) findViewById(R.id.idCardBack);
        TakeFrontPhotoListener takeFrontPhotoListener = new TakeFrontPhotoListener();
        frontImg.setOnClickListener(takeFrontPhotoListener);
        TakeBackPhotoListener takeBackPhotoListener = new TakeBackPhotoListener();
        backImg.setOnClickListener(takeBackPhotoListener);

    }

    class TakeFrontPhotoListener implements OnClickListener{
        @Override
        public void onClick(View v){
            Toast.makeText(UploadFileActivity.this,"in click",Toast.LENGTH_SHORT).show();
            Intent photoFrontIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(ContextCompat.checkSelfPermission(UploadFileActivity.this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(UploadFileActivity.this,"in if",Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(UploadFileActivity.this,new String[]{Manifest.permission.CAMERA},
                        1);
            }else{
                Toast.makeText(UploadFileActivity.this,"in else",Toast.LENGTH_SHORT).show();
                startActivityForResult(photoFrontIntent,1);
            }
        }
    }
    class TakeBackPhotoListener implements OnClickListener{
        @Override
        public void onClick(View v){
            Toast.makeText(UploadFileActivity.this,"in click",Toast.LENGTH_SHORT).show();
            Intent photoBackIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(ContextCompat.checkSelfPermission(UploadFileActivity.this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(UploadFileActivity.this,"in if",Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(UploadFileActivity.this,new String[]{Manifest.permission.CAMERA},
                        1);
            }else{
                Toast.makeText(UploadFileActivity.this,"in else",Toast.LENGTH_SHORT).show();
                startActivityForResult(photoBackIntent,2);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK && requestCode == 1){
            String sdStatus = Environment.getExternalStorageState();
            if(!sdStatus.equals(Environment.MEDIA_MOUNTED)){
                Log.i("try fail","SD card is not for writing right now.");
                return;
            }
            new DateFormat();
            String name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CANADA))+".jpg";
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");

            FileOutputStream fos = null;
            File file = new File("/sdcard/Image");
            file.mkdirs();
            frontFilename = "/sdcard/Image"+name;
            try{
                fos = new FileOutputStream(frontFilename);
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
            }catch(FileNotFoundException e){
                e.printStackTrace();
            }finally {
                try{
                    fos.flush();
                    fos.close();;
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            try{
                frontImg.setImageBitmap(bitmap);
            }catch (Exception e){
                Log.e("error",e.getMessage());
            }
        }else if(resultCode == RESULT_OK && requestCode == 2){
            String sdStatus = Environment.getExternalStorageState();
            if(!sdStatus.equals(Environment.MEDIA_MOUNTED)){
                Log.i("try fail","SD card is not for writing right now.");
                return;
            }
            new DateFormat();
            String name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CANADA))+".jpg";
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");

            FileOutputStream fos = null;
            File file = new File("/sdcard/Image");
            file.mkdirs();
            backFilename = "/sdcard/Image"+name;
            try{
                fos = new FileOutputStream(backFilename);
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
            }catch(FileNotFoundException e){
                e.printStackTrace();
            }finally {
                try{
                    fos.flush();
                    fos.close();;
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            try{
                backImg.setImageBitmap(bitmap);
            }catch (Exception e){
                Log.e("error",e.getMessage());
            }
        }
    }
}
