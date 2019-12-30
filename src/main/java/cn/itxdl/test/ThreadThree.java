package cn.itxdl.test;

import cn.itxdl.datasource.MyDataSource;

import java.sql.Connection;

public class ThreadThree implements Runnable{
    private MyDataSource myDataSource;

    public ThreadThree(MyDataSource myDataSource) {
        this.myDataSource = myDataSource;
    }

    @Override
    public void run() {
        for(int i=0;i<10;i++){
            Connection connection = myDataSource.getConnection();
            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
