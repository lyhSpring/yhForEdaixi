package bjtu.controller;

import java.util.ArrayList;
import java.util.List;

import bjtu.deJson.UserDeJson;
import bjtu.model.User;
import bjtu.network.HttpClient;
import bjtu.util.Config;

public class UserController {
    HttpClient httpClient = new HttpClient();
    UserDeJson userDeJson = new UserDeJson();

    //login
    public User login(String mobile,String password){
        String url= Config.developmentHost+"/users/login?user[mobile]="+mobile+"&&user[password]="+password;
        String method = "GET";
        String result = httpClient.doPost(url,method);
        User user = userDeJson.deJson(result);
        Config.setCard_id(userDeJson.parseInt(result,"card_id"));
        return user;
    }
    //login
    public User register(String mobile,String password,String roleCode){
        String url=Config.developmentHost+"/users/login?user[mobile]="
                +mobile+"&&user[password]="+password+"&&user[role]="+roleCode;
        String method = "GET";
        String result = httpClient.doPost(url,method);
        User user = userDeJson.deJson(result);
        return user;
    }

    //send verification code
    public String send(String mobile,String timeOffset){
        String urlGet = "http://sms.tehir.cn/code/rcode/v1/get?account=18811439847&password=skill456&mobile="
                +mobile+"&expireTime="+timeOffset+"&length=4";
        String method = "GET";
        String resultGet = httpClient.doPost(urlGet,method);
        String code = userDeJson.parseCode(resultGet);

        String urlSend = "http://sms.tehir.cn/code/sms/api/v1/send?srcSeqId=123&account=18811439847&password=skill456&mobile="
                +mobile+"&code=3456&time="+timeOffset;
        String resultSend = httpClient.doPost(urlSend,method);
        if(userDeJson.parseResponseCode(resultSend).equals("0")){
            return "发送验证码成功";
        }else{
            return "发送验证码失败";
        }

    }

    //verify code
    public int verifyCode(String mobile,String code){
        String url = "http://sms.tehir.cn/code/rcode/v1/verify?mobile="+mobile+"&code="+code;
        String method = "GET";
        String verifyResult = httpClient.doPost(url,method);
        return userDeJson.parseFlagInt(verifyResult);
    }

    //获取会员卡金额
    public String getCardData(){
        String method = "GET";
        String url = Config.developmentHost+"cards/"+Config.getCard_id();
        String result = httpClient.doPost(url,method);
        return result;
    }

    //充值
    public String userRecharge(int true_money,int fake_money){
        String method = "POST";
        String url = Config.developmentHost+"cards/userRecharge?card[id]="+Config.getCard_id()
                +"&card[true_money]="+true_money+"&card[fake_money]="+fake_money;
        String result = httpClient.doPost(url,method);
        return result;
    }

    //获取当前全部开通的区域
    public List<String> getOpenedRegionList(){
        List<String> list = new ArrayList<>();
        String method = "GET";
        String url = Config.developmentHost+"regions/getRegionsStatus1";
        String result = httpClient.doPost(url,method);
        list = userDeJson.parse2List(result,"name");
        return list;
    }

    //获取区域的factory_id
    public int getFactoryId(int region_id){
        int factory_id = 0;
        String method = "GET";
        String url = Config.developmentHost+"factories/getFactoryByRegion?factory[region_id]="+region_id;
        String result = httpClient.doPost(url,method);
        factory_id = userDeJson.parseInt(result,"id");
        return factory_id;
    }
}
