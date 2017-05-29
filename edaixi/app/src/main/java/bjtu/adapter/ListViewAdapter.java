package bjtu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import java.util.List;
import bjtu.activity.R;
import bjtu.model.Product;

public class ListViewAdapter extends MyBaseAdapter{
    private List<Product> data;
    private LayoutInflater layoutInflater;
    private Context context;

    public ListViewAdapter(Context context, List<Product> data) {
        this.context = context;
        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);

        this.imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // 实例化组件
        // 这里的R.java文件要引用自己的R.java，引用系统的R文件找不到自定义的布局文件
        convertView = layoutInflater.inflate(R.layout.product_list_item, null);

        ImageView productImg = (ImageView) convertView.findViewById(R.id.productImg);
        TextView productName = (TextView) convertView.findViewById(R.id.productName);
        TextView productPrice = (TextView) convertView.findViewById(R.id.productPrice);
        TextView productNum = (TextView) convertView.findViewById(R.id.productNum);
        ImageView decreaseBtn = (ImageView) convertView.findViewById(R.id.decreaseBtn);
        ImageView increaseBtn = (ImageView) convertView.findViewById(R.id.increaseBtn);

        productName.setText(data.get(position).getName());
        productPrice.setText(data.get(position).getPrice());
        productNum.setText(String.valueOf(data.get(position).getNumOfProduct()));

        //这里没有监听异步加载图片出错的原因
        imageLoader.displayImage(data.get(position).getLogo(),productImg,options);

        //添加两个监听事件
        decreaseBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                data.get(position).setNumOfProduct(data.get(position).getNumOfProduct()-1);
                notifyDataSetChanged();
            }
        });
        increaseBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                data.get(position).setNumOfProduct(data.get(position).getNumOfProduct()+1);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }
}
