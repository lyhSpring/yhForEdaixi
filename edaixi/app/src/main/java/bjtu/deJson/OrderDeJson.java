package bjtu.deJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李奕杭_lyh on 2017/5/6.
 */

public class OrderDeJson {

    public int parseOrderID(String jsonStr){
        int parseResult = 0;
        try{
            JSONObject jObject = new JSONObject(jsonStr);
            parseResult = jObject.getInt("id");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return parseResult;
    }

    public String parseStatus(String jsonStr){
        String parseResult = "";
        try{
            JSONObject jObject = new JSONObject(jsonStr);
            parseResult = jObject.getString("status");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return parseResult;
    }

    public List<String> parse2JsonList(String jsonStr){
        List<String> list = new ArrayList<>();
        try{
            JSONArray jsonArray = new JSONArray(jsonStr);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jo = jsonArray.getJSONObject(i);
                String str = jo.toString();
                list.add(str);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return list;
    }

    public int parseInt(String jsonStr,String index){
        int result = 0;
        try{
            JSONObject jObject = new JSONObject(jsonStr);
            result = jObject.getInt(index);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return result;
    }

    public String parseString(String jsonStr,String index){
        String result = "";
        try{
            JSONObject jObject = new JSONObject(jsonStr);
            result = jObject.getString(index);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return result;
    }
}
