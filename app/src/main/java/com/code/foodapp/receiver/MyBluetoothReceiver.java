package com.code.foodapp.receiver;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class MyBluetoothReceiver extends BroadcastReceiver {

    private static final String TAG = "MyBluetoothReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "onReceive: action=" + action);

        if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
            Log.d(TAG, "Bluetooth state changed: " + state);
            switch (state) {
                case BluetoothAdapter.STATE_OFF:
                    showToast(context, "Bluetooth đã bị tắt");
                    Log.d(TAG, "Bluetooth đã bị tắt");
                    break;
                case BluetoothAdapter.STATE_ON:
                    showToast(context, "Bluetooth đã được bật");
                    Log.d(TAG, "Bluetooth đã được bật");
                    break;
                // Các trạng thái khác có thể được xử lý tại đây
            }
        }
    }

    private void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
