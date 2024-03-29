package com.tqri.activity;

import java.io.IOException;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class AlarmAlert extends Activity {
	private MediaPlayer mediaPlayer1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tixing);
		mediaPlayer1 = new MediaPlayer();
		//控制响铃时长
		try {
			//播放当前默认铃声
			mediaPlayer1.setDataSource(this, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
			mediaPlayer1.prepare();
			mediaPlayer1.setLooping(true);
			mediaPlayer1.start();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String content=getIntent().getExtras().getString("content");
		TextView dialog_content=(TextView) findViewById(R.id.dialog_content);
		Button dialog_button_cancel=(Button)findViewById(R.id.dialog_button_cancel);
		dialog_content.setText(content);
		dialog_button_cancel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});
	}
	@Override
	protected void onStop() {
		ScheduleView.setAlart(AlarmAlert.this);
		if(mediaPlayer1!=null){
			mediaPlayer1.stop();
		}
		super.onStop();
	}

}
