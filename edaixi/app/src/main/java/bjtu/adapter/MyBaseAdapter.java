package bjtu.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.List;

import bjtu.activity.R;
import bjtu.model.OrderItemPT;

/**
 * Created by 李奕杭_lyh on 2017/4/17.
 */

public class MyBaseAdapter extends BaseAdapter {

    private List data;
    private LayoutInflater layoutInflater;
    private Context context;

    protected ImageLoader imageLoader;
    protected DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.drawable.product)
            .showImageOnFail(R.drawable.product)
            .resetViewBeforeLoading(true).cacheOnDisc(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .displayer(new FadeInBitmapDisplayer(300)).build();

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
        return convertView;
    }
}
