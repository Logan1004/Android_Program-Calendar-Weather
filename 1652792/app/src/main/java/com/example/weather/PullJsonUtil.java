package com.example.weather;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class PullJsonUtil {
	private InputStream is;
	public PullJsonUtil(InputStream is){
		this.is=is;
	}
	public List getAllElement() throws JSONException, IOException {
		List<String> myData=new ArrayList<String>();
		String smsg="";
		int num = 0;
		byte[] buffer = new byte[1024];
		while(is.available()>0)
		{
			num = is.read(buffer,0,1024); // 读入数据
			smsg += new String(buffer, 0, num, "UTF-8");  //GBK   GBK  UTF-8
		}

		JSONObject jsonobject = new JSONObject(smsg);
		String desc=jsonobject.get("desc").toString();
		if(desc.equals("OK"))
		{
			JSONObject data=(JSONObject) jsonobject.get("data");
			JSONArray forecastArray =data.getJSONArray("forecast");
			JSONObject today=forecastArray.getJSONObject(0);

			myData.add(data.get("city").toString());  //0
			myData.add(data.get("aqi").toString());
			myData.add(data.get("ganmao").toString());
			myData.add(data.get("wendu").toString()); //3

			myData.add(today.get("date").toString()); //4
			myData.add(today.get("high").toString());
			myData.add(today.get("fengli").toString());
			myData.add(today.get("low").toString()); //7
			myData.add(today.get("fengxiang").toString());
			myData.add(today.get("type").toString());
		}
		else
		{
			myData.add(desc);
		}

		return myData;
	}
}