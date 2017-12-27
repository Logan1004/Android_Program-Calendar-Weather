package com.tqri.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AbsListView.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tqri.borderText.BorderTextView;
import com.tqri.constant.CalendarConstant;
import com.tqri.DATABASE.ScheduleDAO;
import com.tqri.vo.ScheduleVO;

public class ScheduleInfoView extends Activity {

	private LinearLayout layout = null;
	private BorderTextView textTop = null;
	private ScheduleDAO scheduleDao = null;
	private ScheduleVO scheduleVO = null;
	private final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
	private ArrayList<String> scheduleDate;
	private ScheduleVO scheduleVO1;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		scheduleDao = new ScheduleDAO(this);

		params.setMargins(0, 5, 0, 0);
		layout = new LinearLayout(this); // 实例化布局对象
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setBackgroundResource(R.drawable.schedule_bk);
		layout.setLayoutParams(params);

		textTop = new BorderTextView(this, null);
		textTop.setTextColor(Color.BLACK);
		textTop.setBackgroundResource(R.drawable.top_day);
		textTop.setText("日程详情");
		textTop.setHeight(78);
		textTop.setGravity(Gravity.CENTER);

		layout.addView(textTop);


		Intent intent = getIntent();
		//一个日期可能对应多个标记日程(scheduleID)
		scheduleDate=intent.getStringArrayListExtra("scheduleDate");
		scheduleVO1=(ScheduleVO) intent.getExtras().getSerializable("scheduleVO");
		//显示日程详细信息
		handlerInfo(scheduleVO1);
		setContentView(layout);


	}

	/**
	 * 显示日程所有信息
	 */
	public void handlerInfo(ScheduleVO scheduleVO2){
		BorderTextView date = new BorderTextView(this, null);
		date.setTextColor(Color.BLACK);
		date.setBackgroundColor(Color.WHITE);
		date.setLayoutParams(params);
		date.setGravity(Gravity.CENTER_VERTICAL);
		date.setHeight(40);
		date.setPadding(10, 0, 10, 0);

		BorderTextView type = new BorderTextView(this, null);
		type.setTextColor(Color.BLACK);
		type.setBackgroundColor(Color.WHITE);
		type.setLayoutParams(params);
		type.setGravity(Gravity.CENTER);
		type.setHeight(67);
		type.setPadding(10, 0, 10, 0);
		type.setTag(scheduleVO2);

		final BorderTextView info = new BorderTextView(this, null);
		info.setTextColor(Color.BLACK);
		info.setBackgroundColor(Color.WHITE);
		info.setGravity(Gravity.CENTER_VERTICAL);
		info.setLayoutParams(params);
		info.setPadding(10, 5, 10, 5);


		layout.addView(type);
		layout.addView(date);
		layout.addView(info);
		date.setText(scheduleVO2.getScheduleDate());
		type.setText(CalendarConstant.sch_type[scheduleVO2.getScheduleTypeID()]);
		info.setText(scheduleVO2.getScheduleContent());



		//长时间按住日程类型textview就提示是否删除日程信息
		type.setOnLongClickListener(new OnLongClickListener() {


			public boolean onLongClick(View v) {

				scheduleVO = (ScheduleVO) v.getTag();

				new AlertDialog.Builder(ScheduleInfoView.this).setTitle("删除日程").setMessage("确认删除").setPositiveButton("确认",
						new OnClickListener() {


					public void onClick(DialogInterface dialog, int which) {
						scheduleDao.delete(scheduleVO.getScheduleID());
						Toast.makeText(ScheduleInfoView.this, "日程已删除", 0).show();
						ScheduleView.setAlart(ScheduleInfoView.this);
						finish();
					}
				}).setNegativeButton("取消", null).show();

				return true;
			}
		});


	}
}
