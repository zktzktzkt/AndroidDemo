package demo.zkttestdemo.realm;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import demo.zkttestdemo.R;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by Administrator on 2016/12/28 0028.
 */

public class RealmActivity extends Activity implements View.OnClickListener {

    private Realm mRealm;
    private TextView tv_result;
    private RealmResults<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realm);

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("myrealm.mRealm") //数据库名称
                .schemaVersion(0) //版本号
                //.inMemory() // 保存在内存中
                .build();
        mRealm = Realm.getInstance(config);

        Button btn_add = (Button) findViewById(R.id.btn_add);
        Button btn_delete = (Button) findViewById(R.id.btn_delete);
        Button btn_update = (Button) findViewById(R.id.btn_update);
        Button btn_qry = (Button) findViewById(R.id.btn_qry);
        Button btn_deleteAll = (Button) findViewById(R.id.btn_deleteAll);
        tv_result = (TextView) findViewById(R.id.tv_result);

        btn_add.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        btn_update.setOnClickListener(this);
        btn_qry.setOnClickListener(this);
        btn_deleteAll.setOnClickListener(this);
    }

    /**
     * 增
     */
    public void add() {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (int i = 0; i < 10; i++) {
                    User user = realm.createObject(User.class);
                    user.setName("zkt");
                    user.setAge(i);
                }
                Toast.makeText(RealmActivity.this, "增", Toast.LENGTH_SHORT).show();

            }
        });
    }

    /**
     * 删
     */
    public void delete() {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                //   userList.deleteAllFromRealm();
                for (int i = 0; i < userList.size(); i++) {
                    if (userList.get(i).getAge() == 7) {
                        userList.deleteFromRealm(i);
                    }
                }
                Toast.makeText(RealmActivity.this, "删除年龄为 7 的数据", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 清空数据
     */
    public void deleteAll() {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                userList.deleteAllFromRealm();
                Toast.makeText(RealmActivity.this, "清空所有數據", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 查
     */
    public void query() {
        //同步查询
        userList = mRealm.where(User.class).findAll();
        //异步查询
        RealmResults<User> userListAsync = mRealm.where(User.class).equalTo("name", "zkt").findAllAsync();

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < userList.size(); i++) {
            sb.append("姓名：" + userList.get(i).getName() + " 年龄：" + userList.get(i).getAge() + "\n");
        }

        tv_result.setText(sb);

    }

    /**
     * 改
     */
    public void update() {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (int i = 0; i < userList.size(); i++) {
                    if (userList.get(i).getAge() == 7) {
                        User user = mRealm.where(User.class).equalTo("age", 7).findFirst();
                        user.setAge(44);
                    }
                }

                Toast.makeText(RealmActivity.this, "改", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 当Model中存在主键的时候，推荐使用 copyToRealmOrUpdate 方法插入数据。
     * 如果对象存在，就更新该对象；反之，它会创建一个新的对象。若该Model没有主键，使用 copyToRealm 方法，否则将抛出异常。
     */
    public void add_1() {
        final User user = new User();
        user.setName("zkt");
        user.setAge(10);
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(user);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                add();
                break;
            case R.id.btn_delete:
                if (userList == null || userList.size() == 0) {
                    Toast.makeText(this, "数据为空，不能删除", Toast.LENGTH_SHORT).show();
                    return;
                }
                delete();
                break;
            case R.id.btn_update:
                update();
                break;
            case R.id.btn_qry:
                query();
                break;
            case R.id.btn_deleteAll:
                if (userList == null || userList.size() == 0) {
                    Toast.makeText(this, "数据为空，不能删除", Toast.LENGTH_SHORT).show();
                    return;
                }
                deleteAll();
                break;

        }
    }
}
