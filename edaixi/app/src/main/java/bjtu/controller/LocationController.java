package bjtu.controller;

import java.util.ArrayList;
import java.util.List;

import bjtu.deJson.LocationDeJson;
import bjtu.network.HttpClient;

public class LocationController {
    HttpClient httpClient = new HttpClient();
    LocationDeJson locationJsonDeJson = new LocationDeJson();

    public String getLocCommentByLatLng(String lat,String lng){
        String url= "http://api.map.baidu.com/geocoder/v2/?callback=renderReverse&mcode="
                +"E8:09:63:F8:38:1F:78:C7:46:99:DC:39:4F:E2:B8:9C:E2:E1:15:B0;bjtu.activity&location="
                +lat+","+lng+"&output=json&pois=0&ak=WGCO3GIQKNPEZU2BRHRV9VB9sIQD0EFe";
        String method = "GET";
        String result = httpClient.doPost(url,method);
        String comment = locationJsonDeJson.parseLocComment(result);
        return comment;
    }

    public void addNewLoc(String comment){

    }

    public List<String> getAllLocOfUser(String userId){
        List<String> commentList = new ArrayList<>();

        return commentList;
    }

}
