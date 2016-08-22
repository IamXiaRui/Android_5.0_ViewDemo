package com.databinding.bean;

/**
 * @Description: Person的Bean
 */
public class Person {

    private boolean isTrue;

    private String name;

    private String phone ;

    public Person(boolean isTrue, String name, String phone) {
        this.isTrue = isTrue;
        this.name = name;
        this.phone = phone;
    }

    public boolean isTrue() {
        return isTrue;
    }

    public void setTrue(boolean aTrue) {
        isTrue = aTrue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
