package bjtu.deJson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import bjtu.model.DiscountRules;

public class DiscountRulesDeJson {
    List<DiscountRules> list = new ArrayList<>();

    public List<DiscountRules> deJson(String jsonStr){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try{
            JSONArray jsonArray = new JSONArray(jsonStr);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                DiscountRules dr = new DiscountRules();
                dr.setId(jsonObject.getInt("id"));
                dr.setRule_type(jsonObject.getString("rule_type"));
                dr.setBase_money(jsonObject.getInt("base_money"));
                dr.setAdded_money(jsonObject.getInt("added_money"));
                dr.setFrom_date(sdf.parse(jsonObject.getString("from_date")));
                dr.setEnd_date(sdf.parse(jsonObject.getString("end_date")));
                list.add(dr);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
}
