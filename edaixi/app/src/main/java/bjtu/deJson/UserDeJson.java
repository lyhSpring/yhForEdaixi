package bjtu.deJson;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import bjtu.model.User;

public class UserDeJson {
    public User deJson(String jsonStr){
        User user = new User();
        try{
            JSONObject jObject = new JSONObject(jsonStr);
            user.setId(jObject.getInt("id"));
            user.setName(jObject.getString("name"));
            user.setPassword(jObject.getString("password"));
            user.setEmail(jObject.getString("email"));
            user.setMobile(jObject.getString("mobile"));
            user.setRole(jObject.getString("role"));
        }catch (JSONException e){
            e.printStackTrace();
        }
        return user;
    }

    public String parseFlagString(String jsonStr){
        String parseResult = null;
        try{
            JSONObject jObject = new JSONObject(jsonStr);
            parseResult = jObject.getString("flag");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return parseResult;
    }

    public int parseFlagInt(String jsonStr){
         int parseResult = 0;
        try{
            JSONObject jObject = new JSONObject(jsonStr);
            parseResult = jObject.getInt("flag");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return parseResult;
    }

    public String parseCode(String jsonStr){
        String code = null;
        try{
            JSONObject jObject = new JSONObject(jsonStr);
            code = jObject.getString("code");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return code;
    }

    public String parseResponseCode(String jsonStr){
        String code = null;
        try{
            JSONObject jObject = new JSONObject(jsonStr);
            code = jObject.getString("code");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return code;
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

    public List<String > parse2List(String jsonStr,String index){
        List<String> list = new ArrayList<>();
        try{
            JSONArray jsonArray = new JSONArray(jsonStr);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jo = jsonArray.getJSONObject(i);
                String str = jo.getString(index);
                list.add(str);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return list;
    }
}
