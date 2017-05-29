package bjtu.model;

import java.io.Serializable;

/**
 * Created by 李奕杭_lyh on 2017/4/15.
 */

public class User implements Serializable{
    private int id;
    private String name;
    private String password;
    private String email;
    private String mobile;
    private String role;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMobile() {
        return mobile;
    }
    public void setRole(String role){
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
