package cn.itxdl.test;

import cn.itxdl.datasource.MyDataSource;

import java.sql.Connection;

public class ThreadTwo implements Runnable {
    private MyDataSource myDataSource;

    public ThreadTwo(MyDataSource myDataSource) {
        this.myDataSource = myDataSource;
    }

    @Override
    public void run() {
        for(int i=0;i<10;i++){
            Connection connection = myDataSource.getConnection();
            System.out.println(i+1+"次");
            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
