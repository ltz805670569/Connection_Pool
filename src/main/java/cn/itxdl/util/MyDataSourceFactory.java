package cn.itxdl.util;

import cn.itxdl.datasource.MyDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ResourceBundle;

public class MyDataSourceFactory {

    private static ResourceBundle bundle = null;

    private static String driver = null;
    private static String url = null;
    private static String username = null;
    private static String password = null;
    private static int initialSize;
    private static int maxActive;
    private static long maxWait;

    static {
        bundle = ResourceBundle.getBundle("jdbc");
        driver = bundle.getString("driver");
        url = bundle.getString("url");
        username = bundle.getString("username");
        password = bundle.getString("password");
        initialSize = Integer.parseInt(bundle.getString("initialSize"));
        maxActive = Integer.parseInt(bundle.getString("maxActive"));
        maxWait = Long.parseLong(bundle.getString("maxWait"));
    }

    public static MyDataSource createDataSorce(){
        MyDataSource dataSource = new MyDataSource();
        dataSource.setInitialSize(initialSize);
        dataSource.setMaxActive(maxActive);
        dataSource.setMaxWait(maxWait);
        dataSource.createPool(initialSize);
        return dataSource;
    }

    /**
     * 获取连接
     * @return
     */
    public static Connection getConnection(){
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url,username,password);
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
