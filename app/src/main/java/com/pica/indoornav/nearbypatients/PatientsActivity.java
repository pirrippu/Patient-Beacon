package com.pica.indoornav.nearbypatients;

import android.Manifest;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.pica.indoornav.nearbypatients.Adapters.BeaconAdapter;
import com.pica.indoornav.nearbypatients.Adapters.RecyclerViewAdapter;

public class PatientsActivity extends AppCompatActivity {

    BluetoothAdapter mBluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 1;

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mRVLayoutManager;

    RecyclerViewAdapter mAdapter;

    BeaconAdapter mBeaconAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patients);

        enableLocationForVersionM();
        enableBluetooth();
    }

    private void enableLocationForVersionM() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check 
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect beacons.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                    }
                });
                builder.show();
            }
        }
    }

    private void enableBluetooth() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // TODO: Dialog box for devices that does not support bluetooth
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            } else {
                initialize();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("PermissionGranted", "coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
                return;
            }
        }
    }

    private void initialize() {
        mRVLayoutManager = new LinearLayoutManager(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_patients_list);
        mRecyclerView.setLayoutManager(mRVLayoutManager);

        mAdapter = new RecyclerViewAdapter(null, this);

        mRecyclerView.setAdapter(mAdapter);

        mBeaconAdapter = new BeaconAdapter(this, mAdapter);
        mBeaconAdapter.onCreate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            initialize();
        } else {
            // TODO: Prompt for "Bluetooth is required" message
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBeaconAdapter.onDestroy();
    }
}
