package bjtu.deJson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import bjtu.model.Location;

/**
 * Created by 李奕杭_lyh on 2017/5/5.
 */

public class LocationDeJson {
    public String parseLocComment(String jsonStr){
        String parseResult = null;
        try{
            int start = jsonStr.indexOf('{');
            int end = jsonStr.length()-1;
            jsonStr = jsonStr.substring(start,end);
            System.out.print(jsonStr+"lyh------------------point");
            JSONObject jObject = new JSONObject(jsonStr);
            String result = jObject.getString("result");
            System.out.print(result+"lyh------------------point");
            JSONObject jo = new JSONObject(result);
            parseResult = jo.getString("formatted_address");
            System.out.print(parseResult+"lyh------------------point");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return parseResult;
    }

    public List<Location> deJson(String jsonStr){
        List<Location> locList = new ArrayList<>();

        return locList;
    }
}
