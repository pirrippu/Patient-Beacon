package com.pica.indoornav.nearbypatients.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.RemoteException;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.pica.indoornav.nearbypatients.R;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Anj on 27 Aug 2016.
 */
public class BeaconAdapter implements BeaconConsumer {

    Activity mActivity;
    RecyclerViewAdapter mAdapter;

    BeaconManager beaconManager;
    Map<String, Beacon> beacons;
    ArrayList<Beacon> beaconsByDistance;

    public BeaconAdapter(Activity activity, RecyclerViewAdapter adapter) {
        this.mActivity = activity;
        this.mAdapter = adapter;
    }

    public void onCreate() {
        Resources res = mActivity.getResources();
        beaconManager = BeaconManager.getInstanceForApplication(mActivity);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(res.getString(R.string.beacon_layout)));
//        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(res.getString(R.string.altbeacon_layout)));
//        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(res.getString(R.string.eddystone_uid_layout)));
        beaconManager.bind(this);

        beacons = new HashMap<>();
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> list, Region region) {
                if (list.size() > 0) {
                    for(int i = 0; i < list.size(); i++) {
                        Beacon beacon = list.iterator().next();

                        String key = "";
                        key += (beacon.getBluetoothAddress().substring(0, 4));
                        key += (beacon.getId1().toString().substring(0, 7));
                        key += (beacon.getId2());
                        key += (beacon.getId3());

                        beacons.put(key, beacon);
                    }

                    beaconsByDistance = new ArrayList<>(beacons.values());
                    Collections.sort(beaconsByDistance, new Comparator<Beacon>() {
                        @Override
                        public int compare(Beacon lhs, Beacon rhs) {
                            return Double.compare(lhs.getDistance(), rhs.getDistance());
                        }
                    });

                    mAdapter.updateBeaconList(beaconsByDistance);
                } else {
                    Log.i("RangeBeaconsInRegion", "No beacons detected");
                }
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("beaconRegion", null, null, null));
        } catch (RemoteException e) {    }
    }

    public void onDestroy() {
        beaconManager.unbind(this);
    }

    @Override
    public Context getApplicationContext() {
        return mActivity.getApplicationContext();
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {
        mActivity.unbindService(serviceConnection);
    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        return mActivity.bindService(intent, serviceConnection, i);
    }

    public ArrayList<Beacon> getBeacons() {
        return beaconsByDistance;
    }
}
