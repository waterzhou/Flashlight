package com.example.flashlight;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends Activity implements View.OnClickListener {
	Parameters parameter;
	Camera camera;
    private EditText editTextinterval;
    private Button startButton;
    private Button stopButton;
    private boolean bOnOff = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        editTextinterval = (EditText) findViewById(R.id.editTextinterval);
        startButton = (Button) findViewById(R.id.button_loop);
        stopButton = (Button) findViewById(R.id.button_stop);
        startButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);
		camera = Camera.open();
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
                case MESSAGE.LOOPFLASH_ONOFF:
                    int delay = 0;
                    if (editTextinterval.getText().toString().trim().equals(""))
                        delay = 0;
                    else
                          delay = Integer.parseInt(editTextinterval.getText().toString());
                    if (bOnOff)
                        FlashOff();
                    else
                        FlashOn();
                    mHandler.sendEmptyMessageDelayed(MESSAGE.LOOPFLASH_ONOFF, delay);
                    break;
			}
		}
	};
    public void FlashOn()
    {
        bOnOff = true;
        FlashlightManager.turnLightOn(camera);
    }

    public void FlashOff()
    {
        bOnOff = false;
        FlashlightManager.turnLightOff(camera);
    }

	public void onClick(View v) {
		if (camera == null) {
			camera = Camera.open();
		}
		int vID = v.getId();
		switch (vID) {

            case R.id.button_loop:
                mHandler.sendEmptyMessageDelayed(MESSAGE.LOOPFLASH_ONOFF, 0);
				break;
            case R.id.button_stop:
                mHandler.removeMessages(MESSAGE.LOOPFLASH_ONOFF);
                if (bOnOff)
                    FlashOff();
                break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (camera != null) {
			camera.release();
		}
	}

}
