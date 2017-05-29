package bjtu.util;

/**
 * Created by 李奕杭_lyh on 2017/4/30.
 */

public class Config {
    public static String productionHost = "";
//    public static String developmentHost = "http://192.168.1.105:3000";
//    public static String developmentHost = "http://10.0.2.2:3000/";
    public static String developmentHost = "http://180.76.165.224:3000/";
    public static String localhost = "http://10.0.2.2:3000/";

    private static int card_id = 0;
    private static int region_id = 0;
    private static int factory_id = 0;

    public static int getCard_id() {
        return card_id;
    }

    public static void setCard_id(int card_id) {
        Config.card_id = card_id;
    }

    public static int getRegion_id() {
        return region_id;
    }

    public static void setRegion_id(int region_id) {
        Config.region_id = region_id;
    }

    public static int getFactory_id() {
        return factory_id;
    }

    public static void setFactory_id(int factory_id) {
        Config.factory_id = factory_id;
    }
}
