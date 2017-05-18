package com.evcar.subsidy;

import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.Response;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.Async;

import javax.xml.ws.AsyncHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static javafx.scene.input.KeyCode.F;
import static javafx.scene.input.KeyCode.L;

public class HelloTest {
    private static Logger s_logger = LoggerFactory.getLogger(HelloTest.class);

    @Test
    public void HelloTestFoo(){
        int a = 12;
        int b = 5;
        int c = a % b;

        s_logger.info("{} mod {} = {}", a, b, c);
        Assert.assertTrue("A junit test sample", c == 2);
    }

    @Test
    public void getHttp() throws Exception{

        DefaultAsyncHttpClientConfig t = new DefaultAsyncHttpClientConfig.Builder()
                .setMaxConnections(300)
                .setIoThreadsCount(20)
                .setMaxConnectionsPerHost(300).build();

        AsyncHttpClient asyncHttpClient = new DefaultAsyncHttpClient(t);
        Long start = System.currentTimeMillis() ;
        int num = 0;

        List<Future<Response>> futureList = new ArrayList<>() ;
        List<Future<Response>> canRemoved = new ArrayList<>();
        int co = 100 ;
        int task = 300 ;

        boolean bEnd = false;

        AtomicBoolean bb = new AtomicBoolean(false);
        if(bb.compareAndSet(false, true)){
            // DO 5 min
        }

//        i++;
//        synchronized (){
//
//        }
//
//        if(bb.get() == false){
//            bb.set(true);
//
//            // DO ... 5 min
//        }

        while(!bEnd){
            while(task > 0 && futureList.size() < co){
                Future<Response> f = asyncHttpClient.prepareGet("http://www.incars.com.cn").execute();
                task--;
                futureList.add(f);
            }


            for(Future<Response> f : futureList){
                if(f.isDone()){
                    canRemoved.add(f);

                    Response r = f.get();
                    num += r.getResponseBody().length() ;
                }
            }
            for(Future<Response> f:canRemoved)
                futureList.remove(f);
            if(canRemoved.size()==0) {
                System.out.println("miss");
                Thread.sleep(500);
            }
            else{
                System.out.println(canRemoved.size() + " tasks done, " + (task + futureList.size()) + " tasks left");
                canRemoved.clear();
            }

            if(task == 0 && futureList.size() == 0){
                bEnd = true;
            }
        }


        System.out.println(num);
        System.out.println("执行时间："+(System.currentTimeMillis()-start));
    }


    @Test
    public void test() throws Exception {


    }

}
