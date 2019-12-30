package cn.itxdl.datasource;

import cn.itxdl.util.MyDataSourceFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MyDataSource {
    private int initialSize;
    private int maxActive;
    private static int count;
    private long maxWait;

    public void setInitialSize(int initialSize) {
        this.initialSize = initialSize;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public void setMaxWait(long maxWait) {
        this.maxWait = maxWait;
    }

    private static List<Connection> pool  = Collections.synchronizedList(new LinkedList<Connection>());

    /**
     * 初始化连接池
     * @param initialSize
     */
    public void createPool(int initialSize){
        for(int i=0;i<initialSize;i++){
            addConnection();
        }
    }

    /**
     * 动态添加链接
     */
    public synchronized void dynamicLink(){
        if(pool.size() <= 0){
            if(count == maxActive){
                System.out.println("达到最大连接数");
                long future = System.currentTimeMillis()+maxWait;
                long remaining = maxWait;
                while(pool.size() <= 0 && remaining > 0){
                    try {
                        this.wait(remaining);
                        remaining = future - System.currentTimeMillis();
                        if(remaining < 0){
                            throw new RuntimeException("连接超时");
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }else{
                addConnection();
            }
        }
    }

    /**
     * 添加链接
     */
    private void addConnection() {
        Connection conn = MyDataSourceFactory.getConnection();
        pool.add(conn);
        count++;
    }

    /**
     * 获取单个连接
     * @return
     */
    public synchronized Connection getConnection(){
        dynamicLink();
        System.out.println("连接总数"+count+"剩余连接数："+pool.size());
        final Connection conn = pool.remove(0);
        Connection proxyConn = (Connection) Proxy.newProxyInstance(conn.getClass().getClassLoader(), new Class[]{Connection.class}, new InvocationHandler() {
            @Override
            public synchronized Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Object rtValue = null;
                if ("close".equals(method.getName())) {
                    Thread.sleep(500);
                    pool.add(conn);
                    this.notifyAll();
                } else {
                    rtValue = method.invoke(conn, args);
                }
                return rtValue;
            }
        });
        System.out.println(Thread.currentThread().getName()+",获取的"+proxyConn);
        return proxyConn;
    }

    /**
     * 获取连接池大小
     * @return
     */
    public synchronized int getSize(){

        return pool.size();
    }
}
