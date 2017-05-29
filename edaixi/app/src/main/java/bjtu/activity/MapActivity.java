package bjtu.activity;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import bjtu.controller.LocationController;
import bjtu.listener.MyLocationListener;

public class MapActivity extends AppCompatActivity {

    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    private LocationClient locationClient = null;
    private static final int UPDATE_TIME = 5000;
    private static int LOCATION_COUNTNS = 0;
    private LatLng currPoint = null;
    private Location location = null;

    private LocationController locationController = new LocationController();
    private String comment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_map);

        mMapView = (MapView) findViewById(R.id.bMapView);
        mBaiduMap = mMapView.getMap();
        //定义Marker坐标点
        LatLng currPoint = new LatLng(39.963175,116.400244);
//        currPoint = getCurrLoc();
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions options = new MarkerOptions().position(currPoint).icon(bitmap);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(options);
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(final LatLng latLng) {
                Intent locIntent = new Intent();
                locIntent.putExtra("lat",latLng.latitude);
                locIntent.putExtra("lng",latLng.longitude);
                setResult(RESULT_OK,locIntent);
                MapActivity.this.finish();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
    }

    //获取用户当前的经纬度
//    private LatLng getCurrLoc(){
//        Location location = null;
//        MyLocationListener locationListener = new MyLocationListener();
//        LatLng point = null;
//        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
//        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//        if(!gps && !network){   //既没有通过GPS定位，也没有通过网络定位
//            Intent GPSIntent = new Intent();
//            GPSIntent.setClassName("com.android.settings","com.android.settings.widget.SettingsAppWidgetProvide");
//            GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
//            GPSIntent.setData(Uri.parse("custom:3"));
//
//            try{
//                //使用PendingIntent发送广播告诉手机去开启GPS
//                PendingIntent.getBroadcast(this,0,GPSIntent,0).send();
//                if(ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
//                        != PackageManager.PERMISSION_GRANTED){
//                    ActivityCompat.requestPermissions(MapActivity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);
//                }else{
//                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
//                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//                    point = new LatLng(location.getLatitude(),location.getLongitude());
//                    System.out.print("GPS信息如下："+location.getLatitude()+"----"+location.getLongitude());
//                }
//            }catch(PendingIntent.CanceledException e){
//                e.printStackTrace();
//            }
//        }
//        return point;
//    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
}
