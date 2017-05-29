package bjtu.util;

import java.util.ArrayList;
import java.util.List;

import bjtu.model.Product;

public class Util {
    public List<String> getNameList(List<Product> list){
        List<String> nameList = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            nameList.add(list.get(i).getName());
        }
        return nameList;
    }

    public List<String> getPriceList(List<Product> list){
        List<String> priceList = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            priceList.add(list.get(i).getPrice());
        }
        return priceList;
    }

    public List<String> getUrlList(List<Product> list){
        List<String> urlList = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            urlList.add(list.get(i).getLogo());
        }
        return urlList;
    }
}
