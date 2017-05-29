package bjtu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bjtu.activity.R;
import bjtu.model.OrderItemPT;

public class OrderListPTAdapter extends MyBaseAdapter {
    private List<Map<String,Object>> statusUriList = new ArrayList<>();
    private String[] statusDes = {"待支付","待抢单","待取件","配送中","清洗中","清洗完成","送回中","已完成"};

    private List<OrderItemPT> data;
    private LayoutInflater layoutInflater;
    private Context context;
    private ImageView orderStatusImg;
    private TextView orderStatus;
    private TextView expTime;
    private TextView orderLoc;

    public OrderListPTAdapter(Context context,List<OrderItemPT> data){
        this.context = context;
        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);
        this.imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        initStatusData();
    }

    private void initStatusData(){
        for(int i=0;i<statusDes.length;i++){
            Map<String,Object> item = new HashMap<>();
            item.put(statusDes[i],"http://ons52g6mv.bkt.clouddn.com/order_item.png");
            statusUriList.add(item);
        }
    }
    @Override
    public int getCount(){
        return data.size();
    }
    @Override
    public Object getItem(int position){
        return data.get(position);
    }
    @Override
    public long getItemId(int position){
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        //实例化组件
        convertView = layoutInflater.inflate(R.layout.order_item_ptjg,null);
        orderStatus = (TextView) convertView.findViewById(R.id.orderStatus);
        orderStatus.setText(data.get(position).getWashing_status());
        expTime = (TextView) convertView.findViewById(R.id.expTime);
        expTime.setText("预约取件时间："+data.get(position).getTime_exp());
        orderLoc = (TextView) convertView.findViewById(R.id.orderLoc);
        orderLoc.setText("地址："+data.get(position).getAddress());
//        imageLoader.displayImage(String.valueOf(statusUriList.get(position).get(data.get(position).getWashing_status())),orderStatusImg,options);
        imageLoader.displayImage(String.valueOf("http://ons52g6mv.bkt.clouddn.com/order_item.png"),orderStatusImg,options);
        return convertView;
    }
}
