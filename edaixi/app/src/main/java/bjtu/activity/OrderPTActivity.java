package bjtu.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bjtu.adapter.OrderListPTAdapter;
import bjtu.controller.OrderController;
import bjtu.model.OrderItemPT;

public class OrderPTActivity extends AppCompatActivity {
    //申明控件
    private TextView hintTextView;
    private ListView orderListPTView;

    private List<OrderItemPT> dataList = new ArrayList<>();
    private OrderListPTAdapter adapter;

    private OrderController orderController = new OrderController();
    private int userId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pt);
        Intent intent = this.getIntent();
        userId = intent.getIntExtra("userID",0);

        //请求order数据，保存到dataList中
        Runnable getOrderDataTask = new Runnable() {
            @Override
            public void run() {
                List<String> list = orderController.getOrderDataByUserId(userId);
                dataList = orderController.getOrderItemList(list);
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        };
        Thread getOrderDataThread = new Thread(getOrderDataTask);
        getOrderDataThread.start();

        //初始化控件
        hintTextView = (TextView) findViewById(R.id.hintForNoOrders);
        orderListPTView = (ListView) findViewById(R.id.orderListPTView);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    if(dataList.size() <= 0){
                        hintTextView.setVisibility(View.VISIBLE);
                        orderListPTView.setVisibility(View.INVISIBLE);
                    }else{
                        adapter = new OrderListPTAdapter(OrderPTActivity.this,dataList);
                        orderListPTView.setAdapter(adapter);
                    }
                    break;
            }
        }
    };
}
