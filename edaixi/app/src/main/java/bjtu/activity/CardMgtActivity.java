package bjtu.activity;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.util.ArrayList;
import java.util.List;

import bjtu.controller.DiscountRulesController;
import bjtu.controller.UserController;
import bjtu.deJson.DiscountRulesDeJson;
import bjtu.deJson.UserDeJson;
import bjtu.model.DiscountRules;

public class CardMgtActivity extends AppCompatActivity {

    private Button rechargeBtn;
    private PieChart moneyPieChart;
    private TextView rechargeRules;
    private EditText rechargeMoneyEdt;

    private UserController userController = new UserController();
    private UserDeJson userDeJson = new UserDeJson();
    private DiscountRulesController discountRulesController = new DiscountRulesController();
    private DiscountRules finalRule = new DiscountRules();

    private String moneyStr = "金额";
    private int fake_money = 0;
    private int true_money = 0;
    private String fake_money_str = "充值优惠金额";
    private String true_money_str = "已充值金额";
    private List<DiscountRules> drList = new ArrayList<>();
    private String ruleStr = "";
    private String rechargeMoney = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_mgt);

        rechargeMoneyEdt = (EditText) findViewById(R.id.moneyRecharge);
        rechargeRules = (TextView) findViewById(R.id.rechargeRule);

        moneyPieChart = (PieChart) findViewById(R.id.cardMoney);
        Runnable getSettlementTask = new Runnable() {
            @Override
            public void run() {
                drList = discountRulesController.getDiscountRules("0");
                for(int i=0;i<drList.size();i++){
                    ruleStr =ruleStr + "充"+drList.get(i).getBase_money()+"；送"+drList.get(i).getAdded_money()+"哦！\n";
                }
                String cardStr = userController.getCardData();
                fake_money = userDeJson.parseInt(cardStr,"fake_money");
                true_money = userDeJson.parseInt(cardStr,"true_money");
                Message msg = new Message();
                msg.what = 0;
                msg.obj = ruleStr;
                handler.sendMessage(msg);
            }
        };
        Thread getSettlementThread = new Thread(getSettlementTask);
        getSettlementThread.start();

        rechargeBtn = (Button) findViewById(R.id.rechargeBtn);
        rechargeBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Runnable getDiscountRule1Task = new Runnable() {
                    @Override
                    public void run() {
                        rechargeMoney = rechargeMoneyEdt.getText().toString().trim();
                        int finalMoney = Integer.parseInt(rechargeMoney);
                        for(int i=0;i<drList.size();i++){
                            if(finalMoney > drList.get(i).getBase_money()){
                                finalRule = drList.get(i);
                            }
                        }
                        String result = userController.userRecharge(finalMoney,finalRule.getAdded_money());
                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                };
                Thread getDiscountRule1Thread = new Thread(getDiscountRule1Task);
                getDiscountRule1Thread.start();
            }
        });
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    PieData moneyPieData = getPieData(fake_money,true_money,fake_money_str,true_money_str,moneyStr);
                    showChart(moneyPieChart,moneyPieData,moneyStr);
                    rechargeRules.setText(String.valueOf(msg.obj));
                    rechargeMoneyEdt.setText("");
                    break;
                case 1:
                    Toast.makeText(CardMgtActivity.this,"充值"+String.valueOf(msg.obj),Toast.LENGTH_SHORT).show();
                    Message refreshMsg = new Message();
                    refreshMsg.what = 0;
                    refreshMsg.obj = ruleStr;
                    handler.sendMessage(refreshMsg);
                    break;
            }
        }
    };

    private void showChart(PieChart pieChart, PieData pieData,String descStr) {
        pieChart.setHoleColorTransparent(true);

        pieChart.setHoleRadius(60f);  //半径
        pieChart.setTransparentCircleRadius(64f); // 半透明圈
        //pieChart.setHoleRadius(0)  //实心圆

        pieChart.setDescription(descStr);

        // mChart.setDrawYValues(true);
        pieChart.setDrawCenterText(true);  //饼状图中间可以添加文字

        pieChart.setDrawHoleEnabled(true);

        pieChart.setRotationAngle(90); // 初始旋转角度

        // draws the corresponding description value into the slice
        // mChart.setDrawXValues(true);

        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true); // 可以手动旋转

        // display percentage values
        pieChart.setUsePercentValues(false);  //显示成百分比
        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
//      mChart.setOnChartValueSelectedListener(this);
        // mChart.setTouchEnabled(false);

//      mChart.setOnAnimationListener(this);

        pieChart.setCenterText(descStr);  //饼状图中间的文字

        //设置数据
        pieChart.setData(pieData);

        // undo all highlights
//      pieChart.highlightValues(null);
//      pieChart.invalidate();

        Legend mLegend = pieChart.getLegend();  //设置比例图
        mLegend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);  //最右边显示
//      mLegend.setForm(LegendForm.LINE);  //设置比例图的形状，默认是方形
        mLegend.setXEntrySpace(7f);
        mLegend.setYEntrySpace(5f);

        pieChart.animateXY(1000, 1000);  //设置动画
        // mChart.spin(2000, 0, 360);
    }

    private PieData getPieData(int arg1,int arg2,String kind1,String kind2,String desc) {
        ArrayList<String> xValues = new ArrayList<String>();  //xVals用来表示每个饼块上的内容
        xValues.add(0,kind1);
        xValues.add(1,kind2);
        ArrayList<Entry> yValues = new ArrayList<Entry>();  //yVals用来表示封装每个饼块的实际数据
        // 饼图数据
        /**
         * 将一个饼形图分成四部分， 四部分的数值比例为14:14:34:38
         * 所以 14代表的百分比就是14%
         */
//        float quarterly1 = 14;
//        float quarterly2 = 14;
//        float quarterly3 = 34;
//        float quarterly4 = 38;

        yValues.add(new Entry(arg1, 0));
        yValues.add(new Entry(arg2, 1));
//        yValues.add(new Entry(quarterly3, 2));
//        yValues.add(new Entry(quarterly4, 3));
        //y轴的集合
        PieDataSet pieDataSet = new PieDataSet(yValues, desc/*显示在比例图上*/);
        pieDataSet.setSliceSpace(0f); //设置个饼状图之间的距离

        ArrayList<Integer> colors = new ArrayList<Integer>();

        // 饼图颜色
        colors.add(Color.rgb(205, 205, 205));   //#cdcdcd 灰
//        colors.add(Color.rgb(114, 188, 223));   //#72bcdf 浅蓝
        colors.add(Color.rgb(255, 123, 124));   //#ff7b7c 红
//        colors.add(Color.rgb(57, 135, 200));    //#3987c8 深蓝

        pieDataSet.setColors(colors);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = 5 * (metrics.densityDpi / 160f);
        pieDataSet.setSelectionShift(px); // 选中态多出的长度
        PieData pieData = new PieData(xValues, pieDataSet);
        return pieData;
    }
}
