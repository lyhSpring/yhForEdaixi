package bjtu.activity;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class QSPageActivity extends ActivityGroup {
    //控件定义
    private LinearLayout orderQSLayout,homeQSLayout,myselfQSLayout;
    private ImageView orderQSImg,homeQSImg,myselfQSImg;
    private TextView orderQSTxt,homeQSTxt,myselfQSTxt;
    private View orderQSView = null;
    private View homeQSView = null;
    private View myselfQSView = null;

    private PagerAdapter pagerAdapter = null;
    private android.support.v4.view.ViewPager qsViewPager;
    private List<View> viewList = new ArrayList<View>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qspage);
        initView();
    }
    private void initView(){
        qsViewPager = (ViewPager)findViewById(R.id.qsFramePager);
        //初始化LinearLayout，作为button使用
        orderQSLayout = (LinearLayout) findViewById(R.id.orderQSLayout);
        homeQSLayout = (LinearLayout) findViewById(R.id.homeQSLayout);
        myselfQSLayout = (LinearLayout) findViewById(R.id.myselfQSLayout);
        //初试ImageView
        orderQSImg = (ImageView) findViewById(R.id.orderQSImg);
        homeQSImg = (ImageView) findViewById(R.id.homeQSImg);
        myselfQSImg = (ImageView) findViewById(R.id.myselfQSImg);
        //初始化TextView
        orderQSTxt = (TextView) findViewById(R.id.orderQSTxt);
        homeQSTxt = (TextView) findViewById(R.id.homeQSTxt);
        myselfQSTxt = (TextView) findViewById(R.id.myselfQSTxt);

        createView();

        pagerAdapter = new PagerAdapter() {
            @Override
            public void destroyItem(ViewGroup container, int position, Object object){
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
        qsViewPager.setAdapter(pagerAdapter);
        //为LinearLayout添加监听事件——>Button
        QSViewOnClickListener myViewOnClickListener = new QSViewOnClickListener();
        orderQSLayout.setOnClickListener(myViewOnClickListener);
        homeQSLayout.setOnClickListener(myViewOnClickListener);
        myselfQSLayout.setOnClickListener(myViewOnClickListener);
        qsViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
                    orderQSImg.setImageResource(R.drawable.order_list);
                    orderQSTxt.setTextColor(getResources().getColor(R.color.changedBackground));
                }else if(flag == 1){
                    homeQSImg.setImageResource(R.drawable.homepage);
                    homeQSTxt.setTextColor(getResources().getColor(R.color.changedBackground));
                }else if(flag == 2){
                    myselfQSImg.setImageResource(R.drawable.myself);
                    myselfQSTxt.setTextColor(getResources().getColor(R.color.changedBackground));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void createView(){
        //create order view
        orderQSView = this.getLocalActivityManager().startActivity("orederQSView",new Intent(QSPageActivity.this,OrderQSActivity.class))
                .getDecorView();
        orderQSView.setTag(0);
        viewList.add(orderQSView);
        //create home view
        homeQSView = this.getLocalActivityManager().startActivity("homeQSView",new Intent(QSPageActivity.this,OrderHomeActivity.class))
                .getDecorView();
        homeQSView.setTag(1);
        viewList.add(homeQSView);
        //create myself view
        myselfQSView = this.getLocalActivityManager().startActivity("myselfQSView",new Intent(QSPageActivity.this,MyselfActivity.class))
                .getDecorView();
        myselfQSView.setTag(2);
        viewList.add(myselfQSView);
    }

    private class QSViewOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v){
            int mBtnId = v.getId();
            switch (mBtnId){
                case R.id.orderQSLayout:
                    qsViewPager.setCurrentItem(0);
                    initBottemBtn();
                    orderQSImg.setImageResource(R.drawable.order_list);
                    orderQSTxt.setTextColor(getResources().getColor(R.color.changedBackground));
                    break;
                case R.id.homeQSLayout:
                    qsViewPager.setCurrentItem(1);
                    initBottemBtn();
                    homeQSImg.setImageResource(R.drawable.homepage);
                    homeQSTxt.setTextColor(getResources().getColor(R.color.changedBackground));
                    break;
                case R.id.myselfQSLayout:
                    qsViewPager.setCurrentItem(2);
                    initBottemBtn();
                    myselfQSImg.setImageResource(R.drawable.myself);
                    myselfQSTxt.setTextColor(getResources().getColor(R.color.changedBackground));
                    break;
            }
        }
    }
    //初始化控件样式
    private void initBottemBtn(){
        orderQSImg.setImageResource(R.drawable.order_list1);
        homeQSImg.setImageResource(R.drawable.homepage1);
        myselfQSImg.setImageResource(R.drawable.myself1);

        orderQSTxt.setTextColor(getResources().getColor(R.color.initBackground));
        homeQSTxt.setTextColor(getResources().getColor(R.color.initBackground));
        myselfQSTxt.setTextColor(getResources().getColor(R.color.initBackground));
    }

    //监听退出事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                AlertDialog.Builder builder = new AlertDialog.Builder(QSPageActivity.this);
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
                            QSPageActivity.this.finish();
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
