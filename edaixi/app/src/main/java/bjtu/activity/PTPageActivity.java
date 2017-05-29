package bjtu.activity;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.AlertDialog.Builder;

import java.util.ArrayList;
import java.util.List;

public class PTPageActivity extends ActivityGroup {

    //控件定义
    private LinearLayout xiyiPtLayout,orderPTLayout,myselfPTLayout;
    private ImageView xiyiPTImg,orderPTImg,myselfPTImg;
    private TextView xiyiPTTxt,orderPTTxt,myselfPTTxt;
    private View xiyiPTView = null;
    private View orderPTView = null;
    private View myselfPTView = null;

    private PagerAdapter pagerAdapter = null;
    private android.support.v4.view.ViewPager ptViewPager;
    private List<View> viewList = new ArrayList<View>();
    private int userID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pt_page);
        Intent intent = this.getIntent();
        userID = intent.getIntExtra("userID",0);
        initView();
    }

    private void initView(){
        ptViewPager = (ViewPager)findViewById(R.id.ptFramePager);
        //初始化LinearLayout，作为button使用
        xiyiPtLayout = (LinearLayout) findViewById(R.id.xiyiPTLayout);
        orderPTLayout = (LinearLayout) findViewById(R.id.orderPTLayout);
        myselfPTLayout = (LinearLayout) findViewById(R.id.myselfPTLayout);
        //初试ImageView
        xiyiPTImg = (ImageView) findViewById(R.id.xiyiPTImg);
        orderPTImg = (ImageView) findViewById(R.id.orderPTImg);
        myselfPTImg = (ImageView) findViewById(R.id.myselfPTImg);
        //初始化TextView
        xiyiPTTxt = (TextView) findViewById(R.id.xiyiPTTxt);
        orderPTTxt = (TextView) findViewById(R.id.orderPTTxt);
        myselfPTTxt = (TextView) findViewById(R.id.myselfPTTxt);

        createView();

        pagerAdapter = new PagerAdapter() {
            @Override
            public void destroyItem(ViewGroup container, int position,Object object){
                container.removeView(viewList.get(position));
            }
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View v = viewList.get(position);
                container.addView(v);
                return v;
            }
            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        };
        ptViewPager.setAdapter(pagerAdapter);
        //为LinearLayout添加监听事件——>Button
        PTViewOnClickListener myViewOnClickListener = new PTViewOnClickListener();
        xiyiPtLayout.setOnClickListener(myViewOnClickListener);
        orderPTLayout.setOnClickListener(myViewOnClickListener);
        myselfPTLayout.setOnClickListener(myViewOnClickListener);
        ptViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //clear style of bottem
                initBottemBtn();
                //change to selected view and change the style of bottem
                int flag = (Integer) viewList.get(position).getTag();
                if(flag == 0){
                    xiyiPTImg.setImageResource(R.drawable.xiyi);
                    xiyiPTTxt.setTextColor(getResources().getColor(R.color.changedBackground));
                }else if(flag == 1){
                    orderPTImg.setImageResource(R.drawable.order_list);
                    orderPTTxt.setTextColor(getResources().getColor(R.color.changedBackground));
                }else if(flag == 2){
                    myselfPTImg.setImageResource(R.drawable.myself);
                    myselfPTTxt.setTextColor(getResources().getColor(R.color.changedBackground));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void createView(){
        //create xiyi view
        Intent xiyiIntent = new Intent(PTPageActivity.this,XiyiPtActivity.class);
        xiyiIntent.putExtra("userID",userID);
        xiyiPTView = this.getLocalActivityManager().startActivity("xiyiPTView",xiyiIntent)
                .getDecorView();
        xiyiPTView.setTag(0);
        viewList.add(xiyiPTView);
        //create order view
        Intent orderIntent = new Intent(PTPageActivity.this,OrderPTActivity.class);
        orderIntent.putExtra("userID",userID);
        orderPTView = this.getLocalActivityManager().startActivity("orederPTView",orderIntent)
                .getDecorView();
        orderPTView.setTag(1);
        viewList.add(orderPTView);
        //create myself view
        Intent myIntent = new Intent(PTPageActivity.this,MyselfActivity.class);
        myIntent.putExtra("userID",userID);
        myselfPTView = this.getLocalActivityManager().startActivity("myselfPtView",myIntent)
                .getDecorView();
        myselfPTView.setTag(2);
        viewList.add(myselfPTView);
    }

    private class PTViewOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v){
            int mBtnId = v.getId();
            switch (mBtnId){
                case R.id.xiyiPTLayout:
                    ptViewPager.setCurrentItem(0);
                    initBottemBtn();
                    xiyiPTImg.setImageResource(R.drawable.xiyi);
                    xiyiPTTxt.setTextColor(getResources().getColor(R.color.changedBackground));
                    break;
                case R.id.orderPTLayout:
                    ptViewPager.setCurrentItem(1);
                    initBottemBtn();
                    orderPTImg.setImageResource(R.drawable.order_list);
                    orderPTTxt.setTextColor(getResources().getColor(R.color.changedBackground));
                    break;
                case R.id.myselfPTLayout:
                    ptViewPager.setCurrentItem(2);
                    initBottemBtn();
                    myselfPTImg.setImageResource(R.drawable.myself);
                    myselfPTTxt.setTextColor(getResources().getColor(R.color.changedBackground));
                    break;
            }
        }
    }
    //初始化控件样式
    private void initBottemBtn(){
        xiyiPTImg.setImageResource(R.drawable.xiyi1);
        orderPTImg.setImageResource(R.drawable.order_list1);
        myselfPTImg.setImageResource(R.drawable.myself1);

        xiyiPTTxt.setTextColor(getResources().getColor(R.color.initBackground));
        orderPTTxt.setTextColor(getResources().getColor(R.color.initBackground));
        myselfPTTxt.setTextColor(getResources().getColor(R.color.initBackground));
    }

    //监听退出事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                Builder builder = new Builder(PTPageActivity.this);
                builder.setTitle("提示");
                builder.setMessage("你确定要退出吗？");
                builder.setIcon(R.drawable.ic_launcher);

                DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                        if (arg1 == DialogInterface.BUTTON_POSITIVE) {
                            arg0.cancel();
                        } else if (arg1 == DialogInterface.BUTTON_NEGATIVE) {
                            PTPageActivity.this.finish();
                        }
                    }
                };
                builder.setPositiveButton("取消", dialog);
                builder.setNegativeButton("确定", dialog);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        }
        return false;
    }
}
