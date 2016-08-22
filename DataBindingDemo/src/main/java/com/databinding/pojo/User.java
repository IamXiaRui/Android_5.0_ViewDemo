package com.databinding.pojo;

/**
 * @Description: User 的 Bean
 */
public class User {

    private String sex;
    private String phone;

    public User(String sex, String phone) {
        this.sex = sex;
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
