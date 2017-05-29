package bjtu.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bjtu.deJson.ProductDeJson;
import bjtu.model.Product;
import bjtu.network.HttpClient;
import bjtu.util.Config;

public class ProductController {
    HttpClient httpClient = new HttpClient();
    ProductDeJson productDeJson = new ProductDeJson();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date currDate = new Date();

    public List<Product> getProductByCategory(String categoryID,int regionId){
        List<Product> productList = new ArrayList<Product>();
        String region_id = String.valueOf(regionId);
        String currDateStr = sdf.format(currDate);
        String gradeUrl = Config.developmentHost+"/price_rules/findPriceRule?price_rule[region_id]="+Config.getRegion_id()
                +"&price_rule[category_id]="+categoryID+"&price_rule[from_date]="+currDateStr;
        String gradeResult = httpClient.doPost(gradeUrl,"GET");
        int grade = productDeJson.parseGrade(gradeResult);

        String productUrl = Config.developmentHost+"/products/getProductNotDel?page=1&product[categories_id]="+categoryID;
        String method = "GET";
        String result = httpClient.doPost(productUrl,method);
        productDeJson = new ProductDeJson();
        productList = productDeJson.deJson(result);

        //重置产品价格
        for(int i=0;i<productList.size();i++){

            String priceUrl = Config.developmentHost+"/prices/findByProductId?price[product_id]="+productList.get(i).getId();
            String priceResult = httpClient.doPost(priceUrl,"GET");
            productList.get(i).setPrice(String.valueOf(productDeJson.parsePrice(priceResult,"price"+grade)));
        }
        return productList;
    }

    public int getRegionId(String region){
        String url = Config.developmentHost+"/regions/getRegionId?region[name]="+region;
        String method = "GET";
        String result = httpClient.doPost(url,method);
        int region_id = productDeJson.parseRegionId(result);
        return region_id;
    }
}
