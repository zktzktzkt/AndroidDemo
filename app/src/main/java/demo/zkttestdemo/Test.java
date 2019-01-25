package demo.zkttestdemo;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okio.Buffer;
import okio.Okio;
import okio.Source;

/**
 * Created by zkt on 2018-7-7.
 * Description:
 */

public class Test {
    public static void main(String[] arg) {

        createCodeFile();

        io1();
        io2();
        io3();
        io4();
        nio1();
    }

    private static void okio1() {
        //读文件,okio已经不区分字节还是字符，全是Source
        //读是Okio.source，写是Okio.sink
        try(Source source = Okio.source(new File("./io_19/text.txt"))) {
            Buffer buffer = new Buffer();
            source.read(buffer, 1024);

            System.out.println(buffer.readUtf8());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createCodeFile() {
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
                    //创建目录
                    File dir = new File(file.getParent());
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    //创建文件
                    if (file.exists()) {
                        boolean delete = file.delete();
                        Log.e("测试F", "删除->" + delete);
                    }
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

    private static void nio1() {
        try {
            RandomAccessFile file = new RandomAccessFile("./io_19/text.txt", "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void io4() {
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            Socket socket = serverSocket.accept();
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            String data;
            while (true) {
                data = reader.readLine();
                writer.write("data:" + data);
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void io3() {

        try (InputStream inputStream = new FileInputStream("./io_19/text.txt");
             OutputStream outputStream = new FileOutputStream("./io_19/text.txt")) {

            byte[] data = new byte[1024];
            int read;
            //每次读1024个字节写到文件里
            while ((read = inputStream.read(data)) != -1) {
                outputStream.write(data, 0, read);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void io2() {
        try (OutputStream outputStream = new FileOutputStream("./io_19/text.txt");
             Writer outputStreamWriter = new OutputStreamWriter(outputStream);
             //用buffer缓冲的好处是可以提高性能，减少IO操作，但可能会导致并没有全输出出去
             BufferedWriter writer = new BufferedWriter(outputStreamWriter)) {

            outputStream.write('a');//写一个字节（一个字符等于一个字节。 一个汉字是两个字节）
            outputStreamWriter.write("b");//写一个字符
            writer.write("asd");

            //如果做关闭操作了，关闭时会自动做flush这个动作，注意，不是调flush方法
            //flush是把没输出完的输出，
            // writer.flush();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void io1() {
        //java7的特性 写在try的参数里会自动关闭流
        try (InputStream inputStream = new FileInputStream("./io_19/text.txt");
             Reader inputStreamReader = new InputStreamReader(inputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

            inputStream.read();//读一个字节
            inputStreamReader.read();//读一个字符
            bufferedReader.readLine();//读一行

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
