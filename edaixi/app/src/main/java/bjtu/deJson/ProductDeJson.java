package bjtu.deJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import bjtu.model.Product;

public class ProductDeJson {

    public List<Product> deJson(String str){
        List<Product> list = new ArrayList<>();

        try{
            JSONArray jsonArray = new JSONArray(str);
            for(int i = 0; i<jsonArray.length();i++){
                JSONObject jo = (JSONObject) jsonArray.get(i);
                Product product = new Product();
                product.setId(jo.getInt("id"));
                product.setName(jo.getString("name"));
                product.setLogo(jo.getString("logo"));
                product.setPrice(jo.getString("price"));
                product.setIs_del(jo.getString("is_del"));
                product.setCategories_id(jo.getInt("categories_id"));
                product.setNumOfProduct(0);
                list.add(product);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return list;
    }

    public int parseRegionId(String jsonStr){
        int region_id = 0;
        try{
            JSONArray jsonArray = new JSONArray(jsonStr);
            JSONObject jo = jsonArray.getJSONObject(0);
            region_id = jo.getInt("id");
        }catch (JSONException e){
            e.printStackTrace();
        }

        return region_id;
    }

    public int parseGrade(String jsonStr){
        int grade = 0;
        try{
            JSONObject jo = new JSONObject(jsonStr);
            grade = jo.getInt("grade");
            System.out.print(grade);
        }catch (JSONException e){
            e.printStackTrace();
        }

        return grade;
    }

    public int parsePrice(String jsonStr, String index){
        int price = 0;
        try{
            JSONObject jo = new JSONObject(jsonStr);
            price = jo.getInt(index);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return price;
    }
}
