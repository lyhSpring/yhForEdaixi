package bjtu.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bjtu.controller.LocationController;

public class LocationMgtActivity extends AppCompatActivity {

    private int userID = 0;
    private String routes = null;
    private List<Map<String,Object>> locList = new ArrayList<>();
    private String username = "lyh";
    private String userPhone = "18811439847";
    private String[] comment = {"北京市海淀区上园村3号","西直门凯德mall5层","五道口","四道口"};
    private LocationController locationController = new LocationController();

    private ListView locListView;
    private SimpleAdapter adapter;
    private EditText unEdit ;
    private EditText upEdit ;
    private EditText ucEdit ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_mgt);
        Intent intent = this.getIntent();
        userID = intent.getIntExtra("userID",0);
        routes = intent.getStringExtra("routes");
        //这里需要通过UserID从服务器获取地址，人懒了，就直接写值吧


        locListView = (ListView)findViewById(R.id.locationListView);
        for(int i =0;i<4;i++){
            Map<String,Object> item = new HashMap<>();
            item.put("locUserName",username);
            item.put("locUserPhone",userPhone);
            item.put("locComment",comment[i]);
            locList.add(item);
        }
        adapter = new SimpleAdapter(this,locList,R.layout.location_list_item,
                new String[]{"locUserName","locUserPhone","locComment"},
                new int[]{R.id.locUserName,R.id.locUserPhone,R.id.locComment});
        locListView.setAdapter(adapter);
        if(routes.equals("fromMyself")){
            locListView.setClickable(false);
        }else{
            locListView.setClickable(true);
            locListView.setOnItemClickListener(new LocOnItemClickListener());
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addNewLocation);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent locIntent = new Intent(LocationMgtActivity.this,MapActivity.class);
                startActivityForResult(locIntent,1);
            }
        });
    }

    class LocOnItemClickListener implements OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String str = locList.get(position).get("locUserName")+"\n"
                    + locList.get(position).get("locUserPhone")+"\n"
                    + locList.get(position).get("locComment")+"\n";
            setResult(RESULT_OK,new Intent().putExtra("locDetail",str));
            LocationMgtActivity.this.finish();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                final String latStr = String.valueOf(data.getExtras().getDouble("lat"));
                final String lngStr = String.valueOf(data.getExtras().getDouble("lng"));
                Runnable getAddressComment = new Runnable() {
                    @Override
                    public void run() {
                        String comment = locationController.getLocCommentByLatLng(latStr,lngStr);
                        Message msg = new Message();
                        msg.what=1;
                        msg.obj = comment;
                        handler.sendMessage(msg);
                    }
                };
                Thread getAddressThread = new Thread(getAddressComment);
                getAddressThread.start();
            }
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    final String locComment = (String)msg.obj;
                    System.out.print(locComment+"lyh------------------point");
                    final View dialogView = LayoutInflater.from(LocationMgtActivity.this)
                            .inflate(R.layout.add_new_loc,null);
                    unEdit = (EditText) dialogView.findViewById(R.id.addLocUsername);
                    upEdit = (EditText) dialogView.findViewById(R.id.addLocUserPhone);
                    ucEdit = (EditText) dialogView.findViewById(R.id.addLocDetail);
                    ucEdit.setText(locComment);
                    new AlertDialog.Builder(LocationMgtActivity.this).setTitle("输入新的联系地址")
                            .setView(dialogView).setPositiveButton("确定", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Map<String,Object> item = new HashMap<>();
                            item.put("locUserName",unEdit.getText().toString().trim());
                            item.put("locUserPhone",upEdit.getText().toString().trim());
                            item.put("locComment",ucEdit.getText().toString().trim());
                            locList.add(item);
                            adapter.notifyDataSetChanged();
                        }
                    }).show();

                    break;
            }
        }
    };
}
