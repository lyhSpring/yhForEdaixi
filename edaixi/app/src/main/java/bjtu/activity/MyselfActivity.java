package bjtu.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.view.View.OnClickListener;

public class MyselfActivity extends AppCompatActivity {
    private int userID = 0;

    private RelativeLayout locMgt,cardMgt,uploadFile,customerService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myself);
        Intent intent = this.getIntent();
        userID = intent.getIntExtra("userID",0);

        locMgt = (RelativeLayout) findViewById(R.id.locMgt);
        locMgt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent locMgtIntent = new Intent(MyselfActivity.this,LocationMgtActivity.class);
                locMgtIntent.putExtra("userID",userID);
                locMgtIntent.putExtra("routes","fromMyself");
                startActivity(locMgtIntent);
            }
        });
        cardMgt = (RelativeLayout)findViewById(R.id.cardMgt);
        cardMgt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //会员卡管理
                Intent cardMgtIntent = new Intent();
                cardMgtIntent.setClass(MyselfActivity.this,CardMgtActivity.class);
                startActivity(cardMgtIntent);
            }
        });
        uploadFile = (RelativeLayout) findViewById(R.id.uploadFile);
        uploadFile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent uploadFileIntent = new Intent(MyselfActivity.this,UploadFileActivity.class);
                uploadFileIntent.putExtra("userID",userID);
                startActivity(uploadFileIntent);
            }
        });

        customerService = (RelativeLayout)findViewById(R.id.customerService);
        customerService.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MyselfActivity.this).setTitle("是否要拨打客服电话？")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                callPhone("15201361205");
                            }
                        }).setNegativeButton("取消",null).show();
            }
        });
    }

    private void callPhone(String phoneNum){
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phoneNum));
        //判断是否具有权限
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MyselfActivity.this,new String[]{Manifest.permission.CALL_PHONE},1);
        }else{
            startActivity(callIntent);
        }
    }
}
