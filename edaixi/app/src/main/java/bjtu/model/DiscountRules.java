package bjtu.model;

import java.util.Date;

public class DiscountRules {
    private int id;
    private String rule_type;
    private int base_money;
    private int added_money;
    private Date from_date;
    private Date end_date;

    public DiscountRules() {
        this.rule_type = "0";
        this.base_money = 0;
        this.added_money = 0;
    }

    public int getId() {
        return id;
    }

    public String getRule_type() {
        return rule_type;
    }

    public int getBase_money() {
        return base_money;
    }

    public int getAdded_money() {
        return added_money;
    }

    public Date getFrom_date() {
        return from_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRule_type(String rule_type) {
        this.rule_type = rule_type;
    }

    public void setBase_money(int base_money) {
        this.base_money = base_money;
    }

    public void setAdded_money(int added_money) {
        this.added_money = added_money;
    }

    public void setFrom_date(Date from_date) {
        this.from_date = from_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }
}
