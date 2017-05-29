package bjtu.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import java.util.ArrayList;
import java.util.List;
import bjtu.activity.R;

public class GridAdapter extends MyBaseAdapter{

    private List<String> mNameList = new ArrayList<String>();
    private List<String> urlList = new ArrayList<String>();
    private List<String> priceList = new ArrayList<String>();

    private LayoutInflater layoutInflater;
    private Context mContext;
    LinearLayout.LayoutParams params;

    public GridAdapter(Context context,List<String> nameList,
                       List<String> urlList,List<String> priceList){
        this.mNameList = nameList;
        this.urlList = urlList;
        this.priceList = priceList;
        this.imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        this.layoutInflater = LayoutInflater.from(context);

        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
    }

    @Override
    public int getCount() {
        return mNameList.size();
    }

    @Override
    public Object getItem(int position) {
        return mNameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemViewTag viewTag = null;
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.product_grid_item,null);
            viewTag = new ItemViewTag((ImageView)convertView.findViewById(R.id.productImg),
                    (TextView)convertView.findViewById(R.id.productName),
                    (TextView)convertView.findViewById(R.id.productPrice));
            convertView.setTag(viewTag);
        }else{
            viewTag = (ItemViewTag) convertView.getTag();
        }
        viewTag.productName.setText(mNameList.get(position));
        viewTag.productPrice.setText(priceList.get(position));
        //这里没有监听异步加载图片出错的原因
        imageLoader.displayImage(urlList.get(position),viewTag.productImg,options);
        return convertView;
    }

    class ItemViewTag{
        protected ImageView productImg;
        protected TextView productPrice;
        protected TextView productName;

        public ItemViewTag(ImageView img,TextView productName,TextView productPrice){
            this.productImg = img;
            this.productName = productName;
            this.productPrice = productPrice;
        }
    }
}
