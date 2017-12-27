package com.example.weather;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class PullXmlUtil {
    private InputStream is;
    public PullXmlUtil(InputStream is){
        this.is=is;
    }
    public List getAllElement() throws XmlPullParserException, IOException {
        List myData=null;
        XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
        //获取解析器工厂对象
        XmlPullParser xpp=factory.newPullParser();
        xpp.setInput(is,"UTF-8");
        int eventType=xpp.getEventType();
        String elementName="";
        while(eventType!=XmlPullParser.END_DOCUMENT){
            //文档还未结束 END_DOCMENT：文档结束
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                    myData=new ArrayList();
                    break;
                case XmlPullParser.START_TAG:
                    //元素开始
                    elementName=xpp.getName();
                    break;
                case XmlPullParser.TEXT:
                    //内容
                    if(elementName.equals("string"))
                        myData.add(xpp.getText());
                    break;
                case XmlPullParser.END_TAG:
                    //元素结束
                    break;
            }
            eventType=xpp.next();
        }
        return myData;
    }
}
/*
START_DOCUMENT：该常量用来标识开始解析文档的事件。

 START_TAG：元素开始
 END_TAG：元素结束
 COMMENT：注释
 TEXT：内容
 nextTag()：读取下一个标记
 getEventType()：取得事件类型
 getText()：取得内容
 getName()：取得元素名称
 */