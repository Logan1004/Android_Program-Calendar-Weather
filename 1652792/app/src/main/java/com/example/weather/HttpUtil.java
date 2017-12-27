package com.example.weather;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

public class HttpUtil {
	 public static List doGetJson(String u){
	        HttpURLConnection httpUrlConnection=null;
	        InputStream is=null;
	        List list=null;
	        try {
	            URL url=new URL(u);
	            httpUrlConnection= (HttpURLConnection) url.openConnection();
	            httpUrlConnection.setConnectTimeout(5*1000);  //设置超时时间
	            httpUrlConnection.setReadTimeout(5*1000);     //设置读取时间
	            if(httpUrlConnection.getResponseCode()==200){ //判断是否连接成功
	                is=httpUrlConnection.getInputStream();    //获取输入
	                PullJsonUtil pullJsonUtil=new PullJsonUtil(is);
	                try {
	                    list=pullJsonUtil.getAllElement();
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }

	            }
	        } catch (MalformedURLException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                if(is!=null){
	                    is.close();
	                }
	                if(httpUrlConnection!=null){
	                    httpUrlConnection.disconnect();
	                }
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	        return list;
	    }
 public static List doGetXml(String u){
        HttpURLConnection httpUrlConnection=null;
        InputStream is=null;
        List list=null;
        try {
            URL url=new URL(u);
            httpUrlConnection= (HttpURLConnection) url.openConnection();
            httpUrlConnection.setConnectTimeout(5*1000);  //设置超时时间
            httpUrlConnection.setReadTimeout(5*1000);     //设置读取时间
            if(httpUrlConnection.getResponseCode()==200){ //判断是否连接成功
                is=httpUrlConnection.getInputStream();    //获取输入
                PullXmlUtil pullXmlUtil=new PullXmlUtil(is);
                try {
                    list=pullXmlUtil.getAllElement();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(is!=null){
                    is.close();
                }
                if(httpUrlConnection!=null){
                    httpUrlConnection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}