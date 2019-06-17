package com.example.otocashz;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


public class HelperBTStateChangedBroadcastReceiver extends BroadcastReceiver {

    public Integer rv;

    @Override
    public void onReceive(Context context, Intent intent) {
        int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);

        rv = -1;
        switch (state) {
            case BluetoothAdapter.STATE_CONNECTED:
                Toast.makeText(context,
                        "BTStateChangedBroadcastReceiver: STATE_CONNECTED",
                        Toast.LENGTH_SHORT).show();
                rv = 1;
                break;
            case BluetoothAdapter.STATE_CONNECTING:
                Toast.makeText(context,
                        "BTStateChangedBroadcastReceiver: STATE_CONNECTING",
                        Toast.LENGTH_SHORT).show();
                rv = 2;
                break;
            case BluetoothAdapter.STATE_DISCONNECTED:
                Toast.makeText(context,
                        "BTStateChangedBroadcastReceiver: STATE_DISCONNECTED",
                        Toast.LENGTH_SHORT).show();
                rv = 3;
                break;
            case BluetoothAdapter.STATE_DISCONNECTING:
                Toast.makeText(context,
                        "BTStateChangedBroadcastReceiver: STATE_DISCONNECTING",
                        Toast.LENGTH_SHORT).show();
                rv = 4;
                break;
            case BluetoothAdapter.STATE_OFF:
                Toast.makeText(context,
                        "BTStateChangedBroadcastReceiver: STATE_OFF",
                        Toast.LENGTH_SHORT).show();
                rv = 5;
                break;
            case BluetoothAdapter.STATE_ON:
                Toast.makeText(context,
                        "BTStateChangedBroadcastReceiver: STATE_ON",
                        Toast.LENGTH_SHORT).show();
                rv = 6;
                break;
            case BluetoothAdapter.STATE_TURNING_OFF:
                Toast.makeText(context,
                        "BTStateChangedBroadcastReceiver: STATE_TURNING_OFF",
                        Toast.LENGTH_SHORT).show();
                rv = 7;
                break;
            case BluetoothAdapter.STATE_TURNING_ON:
                Toast.makeText(context,
                        "BTStateChangedBroadcastReceiver: STATE_TURNING_ON",
                        Toast.LENGTH_SHORT).show();
                rv = 8;
                break;
        }
    }

    public Integer onReceiveRes(Integer rv) {
        return rv;
    }
}