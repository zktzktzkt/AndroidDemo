package demo.zkttestdemo.recyclerview.diffUtil;

class TestBean implements Cloneable {
    private int id;
    private String name;
    private String desc;
    private int pic;

    public TestBean(int id, String name, String desc, int pic) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.pic = pic;
    }

    //仅写DEMO 用 实现克隆方法
    @Override
    public TestBean clone() throws CloneNotSupportedException {
        TestBean bean = null;
        try {
            bean = (TestBean) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return bean;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getPic() {
        return pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }
}