package bjtu.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import bjtu.controller.DiscountRulesController;
import bjtu.controller.OrderController;
import bjtu.controller.WaybillController;
import bjtu.model.DiscountRules;
import bjtu.model.Product;

public class CreateOrderActivity extends AppCompatActivity {
    private LinearLayout locationLayout;
    private TextView locationTxt;
    private TextView dateTxt;
    private TextView timeTxt;
    private Button createOrderBtn;

    private int checkedIndex = 0;
    private String dateStr = null;
    private String[] timeArray = new String[]{"10:00-12:00","12:00-14:00","14:00-16:00","16:00-18:00",
            "18:00-20:00","20:00-22:00"};
    private int userID = 0;
    private String categoryID = null;
    private int orderId = 0;
    private double totalPrice = 0;
    private List<Product> shoppingCarList = new ArrayList<>();
    private List<DiscountRules> drList = new ArrayList<>();
    private DiscountRules finalRule = new DiscountRules();
    private String ruleStr = "";
    private OrderController orderController = new OrderController();
    private WaybillController waybillController = new WaybillController();
    private DiscountRulesController discountRulesController = new DiscountRulesController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);

        Intent intent = this.getIntent();
        shoppingCarList = (List<Product>)intent.getSerializableExtra("shoppingCarList");
        categoryID = intent.getStringExtra("categoryID");
        userID = intent.getIntExtra("userID",0);

        for(int i=0;i<shoppingCarList.size();i++){
            totalPrice += shoppingCarList.get(i).getNumOfProduct()*Double.valueOf(shoppingCarList.get(i).getPrice());
        }

        locationTxt = (TextView) findViewById(R.id.selectLocation);
        locationLayout = (LinearLayout) findViewById(R.id.locationLayout);
        locationLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to LocationMgtActivity
                Intent locMgtIntent = new Intent(CreateOrderActivity.this,LocationMgtActivity.class);
                locMgtIntent.putExtra("userID",userID);
                locMgtIntent.putExtra("routes","fromCreateOrder");
                startActivityForResult(locMgtIntent,1);
            }
        });
        dateTxt = (TextView) findViewById(R.id.selectDate);
        dateTxt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showEstDateArrivalPickDlg();
            }
        });
        timeTxt = (TextView) findViewById(R.id.selectTime);
        timeTxt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(CreateOrderActivity.this).setTitle("请选择预约时间")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setSingleChoiceItems(timeArray, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                timeTxt.setText(timeArray[which]);
                            }
                        }).setPositiveButton("确定",null).show();
            }
        });

        createOrderBtn = (Button) findViewById(R.id.createOrderBtn);
        createOrderBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取满减优惠信息
                Runnable getRulesTask = new Runnable() {
                    @Override
                    public void run() {
                        //获取优惠规则，并且构造为一个字符串
                        drList = discountRulesController.getDiscountRules("0");
                        finalRule = new DiscountRules();
                        int temp =0;
                        for(int i=0;i<drList.size();i++){
                            ruleStr =ruleStr + "满"+drList.get(i).getBase_money()+"；减"+drList.get(i).getAdded_money()+"哦！\n";
                            if(totalPrice > drList.get(i).getBase_money()){
                                finalRule = drList.get(i);
                            }
                        }
                        ruleStr += String.valueOf("总金额："+totalPrice+"元");
                        //传递给handler值，进行界面更新
                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = ruleStr;
                        handler.sendMessage(msg);
                    }
                };
                Thread getRulesThread = new Thread(getRulesTask);
                getRulesThread.start();
            }
        });
    }

    public void showEstDateArrivalPickDlg() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                CreateOrderActivity.this,
                new OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        CreateOrderActivity.this.dateTxt
                                .setText((monthOfYear+1) + "月" + dayOfMonth+"日");
                        dateStr = year + "-" + (monthOfYear+1) + "-"+ dayOfMonth;
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    protected void onActivityResult(int requestCode, int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                String str = data.getExtras().getString("locDetail");
                locationTxt.setText(str);
            }
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1: //显示满减的优惠规则
                    new AlertDialog.Builder(CreateOrderActivity.this).setTitle("优惠：")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setMessage((String)msg.obj)
                            .setNegativeButton("取消",null)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Runnable createOrderTask = new Runnable() {
                                        @Override
                                        public void run() {
                                            String createResult = orderController.createOrder(categoryID,userID,1,totalPrice,finalRule);//创建订单
                                            if(createResult.equals("refused")){
                                                Message msg = new Message();
                                                msg.what=2;
                                                msg.obj="支付失败，没钱了。";
                                                handler.sendMessage(msg);
                                            }else{
                                                orderId = Integer.parseInt(createResult);
                                                orderController.createItem(shoppingCarList,orderId);    //添加items
                                                waybillController.createWaybill(orderId,userID,timeTxt.getText().toString().trim());
                                                Intent goBackIntent = new Intent(CreateOrderActivity.this,PTPageActivity.class);
                                                startActivity(goBackIntent);
                                                CreateOrderActivity.this.finish();
                                            }
                                        }
                                    };
                                   Thread createOrderThread = new Thread(createOrderTask);
                                    createOrderThread.start();
                                }
                            }).show();
                    break;
                case 2:
                    Toast.makeText(CreateOrderActivity.this,(String) msg.obj,Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
