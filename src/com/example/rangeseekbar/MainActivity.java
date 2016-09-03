package com.example.rangeseekbar;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.rangeseekbar.RangeSeekBar.OnRangeChangeListener;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		RangeSeekBar rsb = (RangeSeekBar) findViewById(R.id.rsb);
		rsb.setStartValue(0);
		rsb.setEndValue(12);
		rsb.setOnRangeChangeListener(new OnRangeChangeListener() {

			@Override
			public void onRangeChange(int start, int end) {
				Toast.makeText(MainActivity.this, start + "," + end, Toast.LENGTH_SHORT).show();
			}
		});
	}
}
