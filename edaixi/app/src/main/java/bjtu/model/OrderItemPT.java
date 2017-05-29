package bjtu.model;

public class OrderItemPT {
    private int userId;
    public void setUserId(int userId){this.userId = userId;}
    public int getUserId(){return userId;}

    private String time_exp;
    public void setTime_exp(String time_exp){this.time_exp = time_exp;}
    public String getTime_exp(){return time_exp;}

    private String washing_status;
    public void setWashing_status(String washing_status){this.washing_status = washing_status;}
    public String getWashing_status(){return washing_status;}

    private String address;
    public void setAddress(String address){this.address = address;}
    public String getAddress(){return address;}

    private float total_price;
    public void setTotal_price(float total_price){this.total_price = total_price;}
    public float getTotal_price(){return total_price;}
}
