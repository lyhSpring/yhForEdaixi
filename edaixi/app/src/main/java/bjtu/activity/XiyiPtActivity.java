package bjtu.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;

import bjtu.adapter.GridAdapter;
import bjtu.controller.ProductController;
import bjtu.controller.UserController;
import bjtu.ownClass.CycleViewPager;
import bjtu.util.Config;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XiyiPtActivity extends AppCompatActivity {
    private  int[] imgSpinnerArray = {R.drawable.spinner_1,R.drawable.spinner_2,R.drawable.spinner_3};
    private int[] imgIdArray = { R.drawable.category_chenshan, R.drawable.category_xiezi,R.drawable.category_jiafang,R.drawable.category_chuanglian };
    private String[] txtStrArray = {"洗衣","洗鞋","洗家纺","洗窗帘"};

    private List<String> nameList = new ArrayList<>();
    private List<String> priceList = new ArrayList<>();
    private List<String> urlList = new ArrayList<>();
    private ProductController productController = new ProductController();
    private UserController userController = new UserController();
    private int userID = 0;
//    public String region = "海淀区";
    public int region_id = 110108;
    private List<String> regionList = new ArrayList<>();

    private PagerAdapter cycleViewPagerAdapter = new PagerAdapter() {
        private int count = 5;

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getCount() {
            return count;
        }

        public Object instantiateItem(android.view.ViewGroup container,
                                      int position) {
            ImageView item = new ImageView(XiyiPtActivity.this);
            item.setScaleType(ImageView.ScaleType.CENTER_CROP);
            item.setImageResource(imgSpinnerArray[position % 3]);
//            item.setImageBitmap(bitmap[position % 3]);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Toast.makeText(XiyiPtActivity.this, "position is " + 1,
                            Toast.LENGTH_SHORT).show();
                }
            });
            container.addView(item);
            return item;
        }

        public void destroyItem(android.view.ViewGroup container, int position,
                                Object object) { container.removeView((View) object);
        }
    };
    private ArrayAdapter adapter = null;

    private GridView businessGrid;
    private GridView hotGoodsGrid;
    private Spinner regionSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiyi_pt);
        Intent intent = this.getIntent();
        userID = intent.getIntExtra("userID",0);

        nameList.add("product 1");nameList.add("product 2");nameList.add("product 3");
        nameList.add("product 4");nameList.add("product 5");
        priceList.add("5");priceList.add("4");priceList.add("3");priceList.add("2");priceList.add("1");
        urlList.add("http://ons52g6mv.bkt.clouddn.com/product.png");
        urlList.add("http://ons52g6mv.bkt.clouddn.com/product.png");
        urlList.add("http://ons52g6mv.bkt.clouddn.com/product.png");
        urlList.add("http://ons52g6mv.bkt.clouddn.com/product.png");
        urlList.add("http://ons52g6mv.bkt.clouddn.com/product.png");

        CycleViewPager pager = (CycleViewPager) findViewById(R.id.scrollingViewpager);
        pager.setAdapter(cycleViewPagerAdapter);
        regionSpinner = (Spinner) findViewById(R.id.regionSpinner);
        businessGrid = (GridView) findViewById(R.id.businessGrid);
        hotGoodsGrid = (GridView) findViewById(R.id.hotGoodsGrid);

        //获取全部的区域
        Runnable getOpenedRegionsTask = new Runnable() {
            @Override
            public void run() {
                regionList = userController.getOpenedRegionList();
                Message msg = new Message();
                msg.what=1;
                handler.sendMessage(msg);
            }
        };
        Thread getOpenedRegionsThread = new Thread(getOpenedRegionsTask);
        getOpenedRegionsThread.start();

        final List<Map<String,Object>> businessItem = new ArrayList<Map<String, Object>>();
        for(int i =0;i<4;i++){
            Map<String,Object> item = new HashMap<>();
            item.put("imageItem",imgIdArray[i]);
            item.put("textItem",txtStrArray[i]);
            businessItem.add(item);
        }
        SimpleAdapter adapter = new SimpleAdapter(this,businessItem,R.layout.business_grid_item,
                new String[]{"imageItem","textItem"},new int[]{R.id.businessImg,R.id.businessTxt});
        businessGrid.setAdapter(adapter);
        businessGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent productIntent = new Intent(XiyiPtActivity.this,ProductGridActivity.class);
                productIntent.putExtra("regionId",Config.getRegion_id());
                productIntent.putExtra("categoryID",String.valueOf(position+1));
                productIntent.putExtra("categoryName",(String)businessItem.get(position).get("textItem"));
                productIntent.putExtra("userID",userID);
                startActivity(productIntent);
            }
        });


        GridAdapter gridAdapter = new GridAdapter(this,nameList,urlList,priceList);
        hotGoodsGrid.setAdapter(gridAdapter);
//        hotGoodsGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent productIntent = new Intent(XiyiPtActivity.this,ProductGridActivity.class);
//                startActivity(productIntent);
//            }
//        });

        regionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                Runnable getRegionIdTask = new Runnable() {
                    @Override
                    public void run() {
                        region_id = productController.getRegionId(regionList.get(position));
                        Config.setRegion_id(region_id);
                        Config.setFactory_id(userController.getFactoryId(region_id));
                    }
                };
                Thread getRegionIdThread = new Thread(getRegionIdTask);
                getRegionIdThread.start();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                region_id = 110108;
            }
        });
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    adapter = new ArrayAdapter(XiyiPtActivity.this,android.R.layout.simple_spinner_item,regionList);
                    regionSpinner.setAdapter(adapter);
                    break;
                case 2:
                    Toast.makeText(XiyiPtActivity.this,(String) msg.obj,Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
