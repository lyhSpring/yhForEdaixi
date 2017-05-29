package bjtu.model;

import java.io.Serializable;

public class Product implements Serializable{
    private int id;

    public void setId(int id){
        this.id=id;
    }

    public int getId() {
        return id;
    }

    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private String logo;

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLogo() {
        return logo;
    }

    private String is_del;

    public void setIs_del(String is_del) {
        this.is_del = is_del;
    }

    public String getIs_del() {
        return is_del;
    }

    private String price;

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }
    private int numOfProduct;

    public void setNumOfProduct(int numOfProduct) {
        this.numOfProduct = numOfProduct;
    }

    public int getNumOfProduct() {
        return numOfProduct;
    }

    private int categories_id;

    public void setCategories_id(int categories_id) {
        this.categories_id = categories_id;
    }

    public int getCategories_id() {
        return categories_id;
    }
}
