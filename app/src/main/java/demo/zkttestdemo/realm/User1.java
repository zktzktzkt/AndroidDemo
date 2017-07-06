package demo.zkttestdemo.realm;

import java.io.Serializable;

/**
 * Created by zkt on 2017/6/7.
 */

public class User1 implements Serializable {
    private String name;
    private int age;

    public User1(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
