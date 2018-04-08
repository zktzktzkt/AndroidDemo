package demo.zkttestdemo.effect.elemebtn;

/**
 * Created by zkt on 2018-4-8.
 */

public class GoodsBean {
    private String name;
    private int count;
    private int maxCount;

    public GoodsBean(String name, int count, int maxCount) {
        this.name = name;
        this.count = count;
        this.maxCount = maxCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }
}
