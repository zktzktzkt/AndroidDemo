package demo.zkttestdemo;

import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by zkt on 2018-7-7.
 * Description:
 */

public class Test {
    public static void main(String[] arg) {

        new Thread(new Runnable() {

            public String getRandomString(int length) {
                //        String str = "0123456789";
                String str = "abcdefghijklmnopqrstuvwxyz0123456789";
                Random random = new Random();
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < length; i++) {
                    //            int number = random.nextInt(36);
                    int number = random.nextInt(str.length());
                    sb.append(str.charAt(number));
                }
                return sb.toString();
            }

            @Override
            public void run() {
                try {
                    // TODO: 2018-7-7
                    List<String> list = new ArrayList<>(10000);
                    File file = new File("", "F码文件.txt");
                    if (file.exists()) {
                        boolean delete = file.delete();
                        Log.e("测试F", "删除->" + delete);
                    }
                    File dir = new File(file.getParent());
                    dir.mkdirs();
                    file.createNewFile();

                    // 001 手环 //002 体脂称
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < 10000; i++) {
                        String randomString = getRandomString(16);
                        if (!list.contains(randomString)) {
                            if (i < 5000)
                                list.add("001" + randomString);
                            if (i >= 5000 && i < 10000)
                                list.add("002" + randomString);
                        } else {
                            i--;
                        }
                    }
                    Log.e("测试F", "List长度->" + list.size());

                    for (int i = 0; i < list.size(); i++) {
                        sb.append(list.get(i));
                        sb.append("\n");
                    }
                    Log.e("测试F", "添加完成");

                    FileWriter writer = new FileWriter(file);
                    writer.write(sb.toString());
                    writer.flush();
                    writer.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
}
