package bjtu.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import bjtu.deJson.OrderDeJson;
import bjtu.model.DiscountRules;
import bjtu.model.OrderItemPT;
import bjtu.model.Product;
import bjtu.network.HttpClient;
import bjtu.util.Config;

public class OrderController {
    private HttpClient httpClient = new HttpClient();
    private OrderDeJson orderDeJson = new OrderDeJson();

    public String createOrder(String categoryID, int userID, int addressID, double totalPrice, DiscountRules rule){
        String result = "";
        int orderId = 0;
        int act_pay = (int)totalPrice - rule.getAdded_money();
        String payResult = payment(userID,act_pay);
        if("success".equals(payResult)){
            String url = Config.developmentHost+"/orders?order[categories_id]="+categoryID+"&order[user_id]="+userID
                    +"&order[address_id]="+addressID+"&order[totalprice]="+totalPrice+"&order[status]=1&order[voucher_status]=1"
                    +"&order[act_pay]="+String.valueOf(act_pay)+"&order[factory_id]="+Config.getFactory_id();
            String method = "POST";
            result = httpClient.doPost(url,method);
            orderId = orderDeJson.parseOrderID(result);
        }else {
            return payResult;
        }
        return String.valueOf(orderId);
    }

    public String payment(int userID,int act_pay){
        //扣除会员卡的金额
        String true_money = String.valueOf(act_pay*0.8);
        String fake_money = String.valueOf(act_pay*0.2);
        System.out.printf("user_id= ---------------" + userID);
        String url = Config.developmentHost+"/cards/spend?card[id]="+userID+"&card[true_money]="+
                true_money+"&card[fake_money]="+fake_money;
        String method = "POST";
        String result = httpClient.doPost(url,method);
        return orderDeJson.parseStatus(result);
    }

    public void createItem(List<Product> list,int orderId){
        for(int i =0;i<list.size();i++){
            Product product = list.get(i);
            String itemUrl = Config.developmentHost+"items?item[product_id]="+product.getId()
                    +"&item[order_id]="+orderId+"&item[product_number]="+product.getNumOfProduct()
                    +"&item[total_price]="+product.getNumOfProduct()*Double.valueOf(product.getPrice());
            String method = "POST";
            httpClient.doPost(itemUrl,method);
        }
    }

    //获取用户全部的order信息
    public List<String> getOrderDataByUserId(int userId){
        String method = "GET";
        String url = Config.developmentHost+"orders/getOrdersByUserId?order[user_id]="+userId;
        String result = httpClient.doPost(url,method);
        List<String> list = orderDeJson.parse2JsonList(result);
        return list;
    }

    //根据order信息的list获取OrderItemPT对象的List
    public List<OrderItemPT> getOrderItemList(List<String > list){
        List<OrderItemPT> itemList = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            OrderItemPT orderItemPT = new OrderItemPT();
            orderItemPT.setAddress("地址ID为："+orderDeJson.parseInt(list.get(i),"address_id"));
            orderItemPT.setTime_exp("");
            switch (orderDeJson.parseInt(list.get(i),"status")){
                case 1:orderItemPT.setWashing_status("待抢单");break;
                case 2:orderItemPT.setWashing_status("待取件");break;
                case 3:orderItemPT.setWashing_status("配送中");break;
                case 4:orderItemPT.setWashing_status("清洗中");break;
                case 5:orderItemPT.setWashing_status("清洗完成");break;
                case 6:orderItemPT.setWashing_status("送回中");break;
                case 7:orderItemPT.setWashing_status("已完成");break;
            }
            orderItemPT.setTotal_price(orderDeJson.parseInt(list.get(i),"totalprice"));
            itemList.add(orderItemPT);
        }
        return itemList;
    }
}
