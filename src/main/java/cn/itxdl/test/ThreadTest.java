package cn.itxdl.test;

import cn.itxdl.datasource.MyDataSource;
import cn.itxdl.util.MyDataSourceFactory;

public class ThreadTest {
    public static void main(String[] args) {
        MyDataSource dataSorce = MyDataSourceFactory.createDataSorce();
        ThreadOne one = new ThreadOne(dataSorce);
        ThreadTwo two = new ThreadTwo(dataSorce);
        ThreadThree three = new ThreadThree(dataSorce);

        Thread t1 = new Thread(one);
        Thread t2 = new Thread(two);
        Thread t3 = new Thread(three);
        t1.start();
        t2.start();
        t3.start();
    }
}
