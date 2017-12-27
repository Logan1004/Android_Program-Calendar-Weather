package com.example.weather;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import com.tqri.activity.R;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {


    private String[] province={"北京","天津","石家","太原","呼和","沈阳","长春","哈尔","上海","南京","杭州","合肥","福州","南昌","济南","郑州","武汉","长沙","广州","南宁","海口","成都","重庆","贵阳","昆明","拉萨","西安","兰州","西宁","银川","乌鲁木齐","台北","香港","澳门"};

    private String URL_WEATHER="http://wthrcdn.etouch.cn/weather_mini?";  //http://wthrcdn.etouch.cn/weather_mini?city=北京



    private Spinner spinner1;
    private TextView textView,textView0,textView1;
    private ImageView[] iv=new ImageView[6];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner1= (Spinner) findViewById(R.id.sp1);
        ArrayAdapter aa=new ArrayAdapter(getBaseContext(),android.R.layout.simple_spinner_item,province);
        spinner1.setAdapter(aa);
        textView= (TextView) findViewById(R.id.tv);
        textView0= (TextView) findViewById(R.id.tv0);
        textView1= (TextView) findViewById(R.id.tv1);
        //textView 显示天气 textView0 显示空气质量 最高最低气温等 textView1显示提示

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("===","pos="+position+",id="+id);
                if(province!=null&&province.length>0){

                    ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
                    String city=province[position];
                    params.add(new BasicNameValuePair("city", city));
                    String param = URLEncodedUtils.format(params, "utf-8");
                    String url=URL_WEATHER+param;
                    new MyDoGetWeather().execute(url);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //初始化为北京天气
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        String city=province[0];
        params.add(new BasicNameValuePair("city", city));
        String param = URLEncodedUtils.format(params, "utf-8");
        String url=URL_WEATHER+param;
        new MyDoGetWeather().execute(url);

    }
    public class MyDoGetWeather extends AsyncTask<String,Void,List>{

        @Override
        protected List doInBackground(String... params) {
            return HttpUtil.doGetJson(params[0]);
        }
        @Override
        protected void onPostExecute(List list) {
            super.onPostExecute(list);
            if(list!=null&&list.size()>0){
                textView.setText(list.get(3).toString()+"  "+list.get(9).toString()); //
                String wea=list.get(1).toString();
                textView0.setText("空气质量："+list.get(1).toString()+"    "+list.get(8).toString()+"\r\n"+list.get(5).toString()+"  "+list.get(7).toString());
                textView1.setText("提示："+list.get(2).toString());
            }

        }
    }

}

