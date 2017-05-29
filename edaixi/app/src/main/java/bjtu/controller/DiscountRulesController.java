package bjtu.controller;

import java.util.ArrayList;
import java.util.List;

import bjtu.deJson.DiscountRulesDeJson;
import bjtu.model.DiscountRules;
import bjtu.network.HttpClient;
import bjtu.util.Config;

public class DiscountRulesController {
    private HttpClient httpClient = new HttpClient();
    private DiscountRulesDeJson deJson = new DiscountRulesDeJson();
    private List<DiscountRules> list = new ArrayList<>();

    public List<DiscountRules> getDiscountRules(String rule_type){
        String url = Config.developmentHost+"discount_rules/getRulesByType?discount_rule[rule_type]="+rule_type;
        String method = "GET";
        String result = httpClient.doPost(url,method);
        list = deJson.deJson(result);
        return list;
    }
}
