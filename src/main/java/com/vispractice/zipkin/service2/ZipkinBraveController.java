package com.vispractice.zipkin.service2;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.SocketTimeoutException;
import java.util.Enumeration;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.MimeHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.kristofa.brave.Brave;
import com.vispractice.zipkin.SpringUtil;
import com.vispractice.zipkin.TracelinkCutdown;
import com.vispractice.zipkin.UtilId;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Api("service的API接口")
@RestController
@RequestMapping("/service2")
public class ZipkinBraveController {

    @Autowired
    private OkHttpClient client;

    //单次服务调用的唯一TraceID
    private String linkTraceID=UtilId.randomHexString(16);

    @ApiOperation("trace第二步")
    @RequestMapping("/test")
    public String service1(HttpServletRequest req,HttpServletResponse res) throws Exception {
    	//提供服务改TraceID和SpanID
        TracelinkCutdown.CutdownKongtoService(req, res, linkTraceID);

        //消费server3，改TraceID和SpanID，设置头中sTraceID和sSpanID
        Request request3 = TracelinkCutdown.CutdownServicetoKong("http://192.168.43.129:8000/zipkin-service3", linkTraceID);
        String respstr3="";
        try
        {
        	Response response3=client.newCall(request3).execute();
        	respstr3=response3.toString();
        }catch(SocketTimeoutException ex)
        {
        	//System.out.print("BraveInterceptor异步返回Response到网关地址而未到原服务,导致超时,可修改Brave-okhttp的源码或者忽略该错误");
        }


        //消费server4，改TraceID和SpanID，设置头中sTraceID和sSpanID
        Request request4 = TracelinkCutdown.CutdownServicetoKong("http://192.168.43.129:8000/zipkin-service4", linkTraceID);
        String respstr4="";
        try
        {
        	Response response4=client.newCall(request4).execute();
        	respstr4=response4.toString();
        }catch(SocketTimeoutException ex)
        {
        	//System.out.print("BraveInterceptor异步返回Response到网关地址而未到原服务,导致超时,可修改Brave-okhttp的源码或者忽略该错误");
        }

        return respstr3+respstr4;
    }



}
