package bjtu.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import bjtu.adapter.GridAdapter;
import bjtu.controller.ProductController;
import bjtu.listener.MyLocationListener;
import bjtu.model.Product;
import bjtu.util.Config;
import bjtu.util.Util;

public class ProductGridActivity extends AppCompatActivity {

    private List<Product> productList = new ArrayList();
    private List<String> nameList = new ArrayList();
    private List<String> priceList = new ArrayList();
    private List<String> urlList = new ArrayList();
    private ProductController productController = new ProductController();
    private Util util = new Util();
    private List<Product> shoppingCarList = new ArrayList<>();
    private boolean isClicked[] = new boolean[100];
    private String categoryName = null;
    private String categoryID = null;
    private int region_id = 0;
    private int userID = 0;

    private TextView showCategoryTxt;
    private TextView noProductTxt;
    private GridView productGridView;

    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_grid);
        Intent intent = this.getIntent();
//        region_id = intent.getIntExtra("regionId",110108);
        categoryName = intent.getStringExtra("categoryName");
        categoryID = intent.getStringExtra("categoryID");
        userID = intent.getIntExtra("userID",0);

        showCategoryTxt = (TextView) findViewById(R.id.showCategoryTxt);
        showCategoryTxt.setText(categoryName);
        noProductTxt = (TextView) findViewById(R.id.hintForNoProducts);
        productGridView = (GridView) findViewById(R.id.productGridView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.goToShoppingCar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shoppingCarIntent = new Intent(ProductGridActivity.this,ShoppingCarActivity.class);
                shoppingCarIntent.putExtra("shoppingCarList",(Serializable) shoppingCarList);
                shoppingCarIntent.putExtra("userID",userID);
                shoppingCarIntent.putExtra("categoryID",categoryID);
                startActivity(shoppingCarIntent);
            }
        });

//        mLocationClient = new LocationClient(getApplicationContext());
//        initLocation();
//        mLocationClient.registerLocationListener(myListener);
//        mLocationClient.start();
//        String region = mLocationClient.getLastKnownLocation().getAddrStr();
//        System.out.print(region);

        Runnable getDataNetWork = new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                productList = productController.getProductByCategory(categoryID, Config.getRegion_id());
                if(productList.size()>0){
                    msg.what=1;
                    handler.sendMessage(msg);
                }else{
                    msg.what=2;
                    handler.sendMessage(msg);
                }

            }
        };
        Thread getDataThread = new Thread(getDataNetWork);
        getDataThread.start();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    for(int i=0;i<productList.size();i++){
                        isClicked[i] = false;
                    }
                    nameList = util.getNameList(productList);
                    priceList = util.getPriceList(productList);
                    urlList = util.getUrlList(productList);
                    productGridView.setAdapter(new GridAdapter(ProductGridActivity.this,nameList,urlList,priceList));
                    productGridView.setOnItemClickListener(new ProductOnItemClickListener());
                    break;
                case 2:
                    productGridView.setVisibility(View.INVISIBLE);
                    noProductTxt.setVisibility(View.VISIBLE);
                    Toast.makeText(ProductGridActivity.this,"暂时没有商品哦",Toast.LENGTH_SHORT).show();
                    break;
                case 3:

                    break;
            }
        }
    };

    class ProductOnItemClickListener implements OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(!isClicked[position]){ //该项目没有被选取
                Product product = productList.get(position);
                product.setNumOfProduct(product.getNumOfProduct()+1);
                shoppingCarList.add(product);
                isClicked[position]=true;
            }else{ //该项目被选取，增加数量
                for(int i=0;i<shoppingCarList.size();i++){
                    if(productList.get(position).getId() == shoppingCarList.get(i).getId()){
                        shoppingCarList.get(i).setNumOfProduct(shoppingCarList.get(i).getNumOfProduct()+1);
                    }
                }
            }
        }
    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps
        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }
}
