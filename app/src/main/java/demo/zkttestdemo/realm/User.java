package demo.zkttestdemo.realm;

import io.realm.RealmObject;

/**
 * Created by zkt on 2017/6/7.
 */

public class User extends RealmObject {
    private String name;
    private int age;

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
