package com.gsonarraydemo.bean;

import java.util.List;

/**
 * Created by xiarui on 2016/8/30.
 * 返回所有结果的Bean
 */
public class ResultBean {
    //注意变量名与字段名一致
    private int code;

    private String msg;

    private List<UserBean> muser;

    public class UserBean{

        private String name ;

        private String age;

        private String phone;

        private String email;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<UserBean> getMuser() {
        return muser;
    }

    public void setMuser(List<UserBean> muser) {
        this.muser = muser;
    }
}
