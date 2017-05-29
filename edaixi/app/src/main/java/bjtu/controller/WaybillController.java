package bjtu.controller;

import bjtu.network.HttpClient;
import bjtu.util.Config;

public class WaybillController {

    private HttpClient httpClient = new HttpClient();

    public void createWaybill(int orderId,int userId,String exp_time){
        String waybillUrl = Config.developmentHost+"waybills?waybill[sender_type]=0&waybill[sender_id]="+userId
                +"&waybill[status]=0&waybill[recieve_type]=1&waybill[exp_time]="+exp_time+"&waybill[order_id]="+orderId;
        String method = "POST";
        httpClient.doPost(waybillUrl,method);
    }
}
