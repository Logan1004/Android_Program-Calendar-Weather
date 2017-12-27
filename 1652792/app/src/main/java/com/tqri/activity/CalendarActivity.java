package com.tqri.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ViewFlipper;


import com.tqri.borderText.BorderText;
import com.tqri.DATABASE.ScheduleDAO;
import com.tqri.vo.ScheduleVO;

/**
 * 日历显示activity
 */
public class CalendarActivity extends Activity implements OnGestureListener,OnClickListener,OnLongClickListener {

	private ViewFlipper flipper = null;
	private GestureDetector gestureDetector = null;
	private CalendarView calendarView = null;
	private GridView gridView = null;
	private BorderText topText = null;
	private Drawable draw = null;
	private static int jumpMonth = 0;      //每次滑动，增加或减去一个月,默认为0（即显示当前月）
	private static int jumpYear = 0;       //滑动跨越一年，则增加或者减去一年,默认为0(即当前年)
	private int yearOfCurrent = 0;
	private int monthOfCurrent = 0;
	private int dayOfCurrent = 0;
	private String currentDate = "";
	private ScheduleDAO database = null;
	private ScheduleVO scheduleVO;
	private String[] scheduleIDs;
	private  ArrayList<String> scheduleDate;
	private Dialog builder;
	private ScheduleVO scheduleVO_del;

	public CalendarActivity() {

		Date date = new Date();
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-d");
    	//设置日期格式 yyyy-M-d
    	currentDate = simpleDateFormat.format(date);  //当期日期
    	yearOfCurrent = Integer.parseInt(currentDate.split("-")[0]);
    	monthOfCurrent = Integer.parseInt(currentDate.split("-")[1]);
    	dayOfCurrent = Integer.parseInt(currentDate.split("-")[2]);
    	
    	database = new ScheduleDAO(this);
	}

	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rili);
		gestureDetector = new GestureDetector(this);
		// 触摸手势判断
		flipper = (ViewFlipper) findViewById(R.id.flipper);
		flipper.removeAllViews();
		// 切换界面
		calendarView = new CalendarView(this, getResources(), jumpMonth, jumpYear, yearOfCurrent, monthOfCurrent, dayOfCurrent);
        // 自定义CalendarView对象   jumpyear：跳转的年数    jumpmonth：跳转的月份数   yearOfCurrent, monthOfCurrent, day_c当前的年月日


		addGridView();
		gridView.setAdapter(calendarView);
		//用gradView的方式将当前的日历输出
		flipper.addView(gridView, 0);
		//切换时的缓存界面
		topText = (BorderText) findViewById(R.id.toptext);
		addTextToTopTextView(topText);
	}
	
	
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		int gridViewFlag = 0;         //每次添加gridview到viewflipper中时给的标记
		if (e1.getX() - e2.getX() > 50) {
            //像左滑动
			addGridView();   //添加一个gridView
			jumpMonth++;     //下一个月
			
			calendarView = new CalendarView(this, getResources(),jumpMonth,jumpYear,yearOfCurrent,monthOfCurrent,dayOfCurrent);
	        gridView.setAdapter(calendarView);
	        //flipper.addView(gridView);
	        addTextToTopTextView(topText);
	        gridViewFlag++;
	        flipper.addView(gridView, gridViewFlag);
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.flipper_left_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.flipper_left_out));
			this.flipper.showNext();
			flipper.removeViewAt(0);
			return true;
		} else if (e1.getX() - e2.getX() < -50) {
            //向右滑动
			addGridView();   //添加一个gridView
			jumpMonth--;     //上一个月
			
			calendarView = new CalendarView(this, getResources(),jumpMonth,jumpYear,yearOfCurrent,monthOfCurrent,dayOfCurrent);
	        gridView.setAdapter(calendarView);
	        gridViewFlag++;
	        addTextToTopTextView(topText);
	        flipper.addView(gridView,gridViewFlag);
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.flipper_right_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.flipper_right_out));
			this.flipper.showPrevious();
			flipper.removeViewAt(0);
			return true;
		}
		return false;
	}
	
	/**
	 * 创建菜单
	 */
	
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(0, menu.FIRST, menu.FIRST, "今天");
		//菜单上的今天 跳转至今天
		menu.add(0, menu.FIRST+1, menu.FIRST+1, "跳转");
		//菜单上的跳转 跳转至给定的日期
		menu.add(0, menu.FIRST+2, menu.FIRST+2, "天气");
		//菜单上的天气 显示天气
		return super.onCreateOptionsMenu(menu);
	}
	
	/**
	 * 选择菜单
	 */
	
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()){
        case Menu.FIRST:
        	//跳转到今天
        	int xMonth = jumpMonth;
        	int xYear = jumpYear;
        	int gridViewFlag =0;
        	jumpMonth = 0;
        	jumpYear = 0;
        	addGridView();   //添加一个gridView
        	yearOfCurrent = Integer.parseInt(currentDate.split("-")[0]);
        	monthOfCurrent = Integer.parseInt(currentDate.split("-")[1]);
        	dayOfCurrent = Integer.parseInt(currentDate.split("-")[2]);
        	calendarView = new CalendarView(this, getResources(),jumpMonth,jumpYear,yearOfCurrent,monthOfCurrent,dayOfCurrent);
	        gridView.setAdapter(calendarView);
	        addTextToTopTextView(topText);
	        gridViewFlag++;
	        flipper.addView(gridView,gridViewFlag);
	        if(xMonth == 0 && xYear == 0){
	        	//nothing to do
	        }else if((xYear == 0 && xMonth >0) || xYear >0){
	        	//如果日期在今天之后 向左划
	        	this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.flipper_left_in));
				this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.flipper_left_out));
				this.flipper.showNext();
	        }else{
				//如果日期在今天之前 向右划
	        	this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.flipper_right_in));
				this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.flipper_right_out));
				this.flipper.showPrevious();
	        }
			flipper.removeViewAt(0);
        	break;
        case Menu.FIRST+1:
        	new DatePickerDialog(this, new OnDateSetListener() {
				
				
				public void onDateSet(DatePicker view, int year, int monthOfYear,
						int dayOfMonth) {
					//1901-1-1 ----> 2049-12-31
					if(year < 1901 || year > 2049){
						//不在查询范围内
						new AlertDialog.Builder(CalendarActivity.this).setTitle("错误日期").setMessage("跳转日期范围(1901/1/1-2049/12/31)").setPositiveButton("确认", null).show();
					}else{
						int gridViewFlag = 0;
						addGridView();   //添加一个gridView
			        	calendarView = new CalendarView(CalendarActivity.this, CalendarActivity.this.getResources(),year,monthOfYear+1,dayOfMonth);
				        gridView.setAdapter(calendarView);
				        addTextToTopTextView(topText);
				        gridViewFlag++;
				        flipper.addView(gridView,gridViewFlag);
				        if(year == yearOfCurrent && monthOfYear+1 == monthOfCurrent){
				        	//nothing to do
				        }
				        if((year == yearOfCurrent && monthOfYear+1 > monthOfCurrent) || year > yearOfCurrent ){
				        	CalendarActivity.this.flipper.setInAnimation(AnimationUtils.loadAnimation(CalendarActivity.this,R.anim.flipper_left_in));
				        	CalendarActivity.this.flipper.setOutAnimation(AnimationUtils.loadAnimation(CalendarActivity.this,R.anim.flipper_left_out));
				        	CalendarActivity.this.flipper.showNext();
				        }else{
				        	CalendarActivity.this.flipper.setInAnimation(AnimationUtils.loadAnimation(CalendarActivity.this,R.anim.flipper_right_in));
				        	CalendarActivity.this.flipper.setOutAnimation(AnimationUtils.loadAnimation(CalendarActivity.this,R.anim.flipper_right_out));
				        	CalendarActivity.this.flipper.showPrevious();
				        }
				        flipper.removeViewAt(0);
				        //跳转之后将跳转之后的日期设置为当期日期
				        yearOfCurrent = year;
						monthOfCurrent = monthOfYear+1;
						dayOfCurrent = dayOfMonth;
						jumpMonth = 0;
						jumpYear = 0;
					}
				}
			},yearOfCurrent, monthOfCurrent-1, dayOfCurrent).show();
        	break;

        case Menu.FIRST+2:
			Intent intent = new Intent();  					         
        	intent.setClass(CalendarActivity.this, com.example.weather.MainActivity.class);
        	startActivity(intent);
        	break;
        }
		return super.onMenuItemSelected(featureId, item);
	}
	
	
	public boolean onTouchEvent(MotionEvent event) {

		return this.gestureDetector.onTouchEvent(event);
	}

	
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	
	//添加头部的年份 月等信息
	public void addTextToTopTextView(TextView view){
		StringBuffer textDate = new StringBuffer();
		draw = getResources().getDrawable(R.drawable.top_day);
		view.setBackgroundDrawable(draw);
		textDate.append(calendarView.getShowYear()).append("年").append(
				calendarView.getShowMonth()).append("月").append("\t");
		if (!calendarView.getLeapMonth().equals("") && calendarView.getLeapMonth() != null) {
			textDate.append("闰").append(calendarView.getLeapMonth()).append("月")
					.append("\t");
		}
		textDate.append(calendarView.getAnimalsYear()).append("年").append("(").append(
				calendarView.getCyclical()).append("年)");
		view.setText(textDate);
		view.setTextColor(Color.BLACK);
		view.setTypeface(Typeface.DEFAULT_BOLD);
	}
	
	//添加gridview
	private void addGridView() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		//取得屏幕的宽度和高度
		WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int Width = display.getWidth(); 
        int Height = display.getHeight();
        
		gridView = new GridView(this);
		gridView.setNumColumns(7);
		gridView.setColumnWidth(46);
		if(Width == 480 && Height == 800){
			gridView.setColumnWidth(69);
		}
		gridView.setGravity(Gravity.CENTER_VERTICAL);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT)); // 去除gridView边框
		gridView.setVerticalSpacing(1);
		gridView.setHorizontalSpacing(1);
        gridView.setBackgroundResource(R.drawable.gridview_bk);
		gridView.setOnTouchListener(new OnTouchListener() {
            //将gridview中的触摸事件回传给gestureDetector
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return CalendarActivity.this.gestureDetector
						.onTouchEvent(event);
			}
		});

		
		gridView.setOnItemClickListener(new OnItemClickListener() {
            //gridView中的每一个item的点击事件
			
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				  //点击任何一个item，得到这个item的日期(排除点击的是周日到周六(点击不响应))
				  int startPosition = calendarView.getStartPositon();
				  int endPosition = calendarView.getEndPosition();
				  if(startPosition <= position  && position <= endPosition){
					  String scheduleDay = calendarView.getDateByClickItem(position).split("\\.")[0];  //这一天的阳历
	                  String scheduleYear = calendarView.getShowYear();
	                  String scheduleMonth = calendarView.getShowMonth();
	                  String week = "";
	                  
	                  //通过日期查询这一天是否被标记，如果标记了日程就查询出这天的所有日程信息
	                  scheduleIDs = database.getScheduleByTagDate(Integer.parseInt(scheduleYear), Integer.parseInt(scheduleMonth), Integer.parseInt(scheduleDay));
	                  //得到这一天是星期几
	                  switch(position % 7){
	                  case 0:
	                	  week = "星期日";
	                	  break;
	                  case 1:
	                	  week = "星期一";
	                	  break;
	                  case 2:
	                	  week = "星期二";
	                	  break;
	                  case 3:
	                	  week = "星期三";
	                	  break;
	                  case 4:
	                	  week = "星期四";
	                	  break;
	                  case 5:
	                	  week = "星期五";
	                	  break;
	                  case 6:
	                	  week = "星期六";
	                	  break;
	                  }
	                  scheduleDate = new ArrayList<String>();
	                  scheduleDate.add(scheduleYear);
	                  scheduleDate.add(scheduleMonth);
	                  scheduleDate.add(scheduleDay);
	                  scheduleDate.add(week);
	                  if(scheduleIDs != null && scheduleIDs.length > 0){
	                	  LayoutInflater inflater=getLayoutInflater();
	                	  //动态添加布局（提醒)
		              		View linearlayout= inflater.inflate(R.layout.mydialog, null);
		              		ImageButton imgCreate=(ImageButton)linearlayout.findViewById(R.id.img_creat);
		              		ImageButton imgDel=(ImageButton) linearlayout.findViewById(R.id.img_close);
		              		TextView day_textView=(TextView) linearlayout.findViewById(R.id.day_tv);
		              		day_textView.setText(scheduleDay+"日");
		              		TableLayout dialog_tab=(TableLayout) linearlayout.findViewById(R.id.dialog_tab);
		              		imgCreate.setOnClickListener(new OnClickListener() {
								
								public void onClick(View v) {
									// TODO Auto-generated method stub
									if(builder!=null&&builder.isShowing()){
										builder.dismiss();
										Intent intent = new Intent();
						                  intent.putStringArrayListExtra("scheduleDate", scheduleDate);
						                  intent.setClass(CalendarActivity.this, ScheduleView.class);
						                  startActivity(intent);
									}
								}
							});imgDel.setOnClickListener(new OnClickListener() {
								
								public void onClick(View v) {
									// TODO Auto-generated method stub
									if(builder!=null&&builder.isShowing()){
										builder.dismiss();
									}
								}
							});
							//关闭提醒
							ScheduleDAO database=new ScheduleDAO(CalendarActivity.this);
	                	  for(int i=0;i<scheduleIDs.length;i++) {
							  scheduleVO = database.getScheduleByID(CalendarActivity.this, Integer.parseInt(scheduleIDs[i]));
							  String info = "";
							  info = scheduleVO.getTime() + "   " + scheduleVO.getScheduleContent();
							  TableRow localTableRow = new TableRow(CalendarActivity.this);
							  TextView textView = new TextView(CalendarActivity.this);
							  textView.setPadding(10, 5, 5, 5);
							  textView.setTextSize(18.0F);
							  textView.setTextColor(R.color.black);
							  textView.setSingleLine(true);
							  textView.setText(info);
							  localTableRow.addView(textView);
							  localTableRow.setTag(scheduleVO);
							  localTableRow.setOnLongClickListener(CalendarActivity.this);
							  localTableRow.setOnClickListener(CalendarActivity.this);
							  dialog_tab.addView(localTableRow);
						  }
	                	   //自定义dialog设置到底部，设置宽度
	                	  builder =	new Dialog(CalendarActivity.this,R.style.FullScreenDialog);
	                	  builder.setContentView(linearlayout);
	                	  WindowManager windowManager = getWindowManager();
	                	  Display display = windowManager.getDefaultDisplay();
	                	  WindowManager.LayoutParams lp = builder.getWindow().getAttributes();
	                	  lp.width = (int)(display.getWidth()); //设置宽度
	                	  lp.y=display.getHeight()-10;
	                	  builder.getWindow().setAttributes(lp); 
	                	  builder.setCanceledOnTouchOutside(true);
	                	  builder.show();
	                  }else{
	                  //直接跳转到需要添加日程的界面
		                  Intent intent = new Intent();
		                  intent.putStringArrayListExtra("scheduleDate", scheduleDate);
		                  intent.setClass(CalendarActivity.this,ScheduleView.class);
		                  startActivity(intent);
	                  }
				  }
			}
		});
		gridView.setLayoutParams(params);
	}
	
	@Override
	protected void onRestart()
	//开始时显示当前日期的日历
	{
    	int gridViewFlag =0;
    	jumpMonth = 0;
    	jumpYear = 0;
    	addGridView();   //添加一个gridView
    	yearOfCurrent = Integer.parseInt(currentDate.split("-")[0]);
    	monthOfCurrent = Integer.parseInt(currentDate.split("-")[1]);
    	dayOfCurrent = Integer.parseInt(currentDate.split("-")[2]);
    	calendarView = new CalendarView(this, getResources(),jumpMonth,jumpYear,yearOfCurrent,monthOfCurrent,dayOfCurrent);
        gridView.setAdapter(calendarView);
        addTextToTopTextView(topText);
        gridViewFlag++;
        flipper.addView(gridView,gridViewFlag);
		flipper.removeViewAt(0);
		super.onRestart();
	}


	public void onClick(View v)
	//点击判断
	{
		// TODO Auto-generated method stub
		if(builder!=null&&builder.isShowing()){
			builder.dismiss();
		}
		ScheduleVO scheduleVO=  (ScheduleVO) v.getTag();
		Intent intent = new Intent();
		if(scheduleDate!=null){
			intent.putStringArrayListExtra("scheduleDate", scheduleDate);
		}
		intent.setClass(CalendarActivity.this,ScheduleInfoView.class);
        intent.putExtra("scheduleVO", scheduleVO);
			  startActivity(intent);
	}


	public boolean onLongClick(View v)
	//长按删除
	{
		scheduleVO_del=  (ScheduleVO) v.getTag();
		Dialog alertDialog = new AlertDialog.Builder(CalendarActivity.this). 
        setMessage("删除日程信息？"). 
        setPositiveButton("确定", new DialogInterface.OnClickListener() { 
             
            public void onClick(DialogInterface dialog, int which) { 
            	database.delete(scheduleVO_del.getScheduleID());
            	ScheduleView.setAlart(CalendarActivity.this);
            	if(builder!=null&&builder.isShowing()){
            		builder.dismiss();
            	}
            }
 
        }). 
        setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            } 
        }). 
        create(); 
		alertDialog.show();
		return false;
	}
}