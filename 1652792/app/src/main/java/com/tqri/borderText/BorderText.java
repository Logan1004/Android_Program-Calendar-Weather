package com.tqri.borderText;

import android.content.Context;
import android.graphics.Canvas;

import android.graphics.Paint;

import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 实现带下边框的textView
 */

public class BorderText extends TextView {

	public BorderText(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 实例化一支画笔
		Paint paint = new Paint();
		canvas.drawLine(0, this.getHeight() - 1, this.getWidth() - 1, this
				.getHeight() - 1, paint);

	}

}
