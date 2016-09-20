package xr.firstmvpdemo.bean;

/**
 * @author xiarui 16/09/20
 * @description Person的Bean类
 */
public class PersonBean {

    private String name ;
    private String pwd;

    public PersonBean(String name, String pwd) {
        this.name = name;
        this.pwd = pwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
