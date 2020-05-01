package com.vispractice.zipkin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.kristofa.brave.Brave;

import okhttp3.Request;

public class TracelinkCutdown {



	public static void CutdownKongtoService(HttpServletRequest req,HttpServletResponse res,String linkTraceID)
	{
		//提供服务改TraceID和SpanID
        String sTraceID=req.getHeader("straceid");
        String sSpanID=req.getHeader("sspanid");
        Brave brv=(Brave)SpringUtil.getBean("brave");
        if(sTraceID!=null && sSpanID!=null)
        {
        	Long sid=UtilId.parseString16ToLong(linkTraceID);
        	Long pid=UtilId.parseString16ToLong(sSpanID);
        	Long tid=UtilId.parseString16ToLong(sTraceID);
        	if(brv.serverSpanThreadBinder().getCurrentServerSpan().getSpan()!=null)
        	{
	        	brv.serverSpanThreadBinder().getCurrentServerSpan().getSpan().setTrace_id(tid);
	        	brv.serverSpanThreadBinder().getCurrentServerSpan().getSpan().setId(sid);
	        	brv.serverSpanThreadBinder().getCurrentServerSpan().getSpan().setParent_id(pid);
        	}
        	if(brv.clientSpanThreadBinder().getCurrentClientSpan()!=null)
        	{
        		brv.clientSpanThreadBinder().getCurrentClientSpan().setTrace_id(tid);
        		brv.clientSpanThreadBinder().getCurrentClientSpan().setId(sid);
        		brv.clientSpanThreadBinder().getCurrentClientSpan().setParent_id(pid);
        	}
        }else
        {
        	Long ss=UtilId.parseString16ToLong(linkTraceID);
        	if(brv.serverSpanThreadBinder().getCurrentServerSpan().getSpan()!=null)
        	{
	        	brv.serverSpanThreadBinder().getCurrentServerSpan().getSpan().setTrace_id(ss);
	        	brv.serverSpanThreadBinder().getCurrentServerSpan().getSpan().setId(ss);
        	}
        	if(brv.clientSpanThreadBinder().getCurrentClientSpan()!=null)
        	{
        		brv.clientSpanThreadBinder().getCurrentClientSpan().setTrace_id(ss);
        		brv.clientSpanThreadBinder().getCurrentClientSpan().setId(ss);
        	}
        }
	}

	public static Request CutdownServicetoKong(String url,String linkTraceID)
	{
		Request.Builder requestBuilder=new Request.Builder().url(url);
        String childnewTraceID=UtilId.randomHexString(16);
        String c_parentspanID=UtilId.randomHexString(16);
        requestBuilder.header("x-b3-traceid",childnewTraceID);
        requestBuilder.header("x-b3-spanid",childnewTraceID);
        requestBuilder.header("x-b3-parentspanid",c_parentspanID);
        requestBuilder.header("straceid",linkTraceID);
        requestBuilder.header("sspanid",linkTraceID);
        Request request = requestBuilder.build();
        return request;
	}

}
