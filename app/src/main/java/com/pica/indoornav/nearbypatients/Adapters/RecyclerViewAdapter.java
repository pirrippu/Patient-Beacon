package com.pica.indoornav.nearbypatients.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pica.indoornav.nearbypatients.R;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;

/**
 * Created by Anj on 27 Aug 2016.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.PatientViewHolder> {

    ArrayList<Beacon> mBeacons;
    Activity mActivity;

    final String TAG = "RecyclerViewAdapter";

    public RecyclerViewAdapter(ArrayList<Beacon> beacons, Activity activity) {
        this.mBeacons = beacons;
        this.mActivity = activity;
    }

    public static class PatientViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout layoutPatient;
        ImageView imgPatient;
        TextView txtPatientName;
        TextView txtRoomLocation;
        ImageView imgPatientInfo;

        public PatientViewHolder(View view) {
            super(view);
            layoutPatient = (RelativeLayout) view.findViewById(R.id.patient);
            imgPatient = (ImageView) view.findViewById(R.id.patient_image);
            txtPatientName = (TextView) view.findViewById(R.id.patient_name);
            txtRoomLocation = (TextView) view.findViewById(R.id.patient_room_loc);
            imgPatientInfo = (ImageView) view.findViewById(R.id.patient_info);
        }
    }

    @Override
    public RecyclerViewAdapter.PatientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        final PatientViewHolder viewHolder = new PatientViewHolder(v);
        if(v != null) {
            v.findViewById(R.id.patient).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i(TAG, "Patient " + viewHolder.txtPatientName.getText() + " clicked.");
                }
            });
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PatientViewHolder holder, int position) {
        holder.imgPatient.setImageDrawable(ContextCompat.getDrawable(mActivity, R.drawable.ic_patient_image_holder));
        holder.txtPatientName.setText(mBeacons.get(position).getId1().toString());
        holder.txtRoomLocation.setText(mBeacons.get(position).getBluetoothAddress());
        holder.imgPatientInfo.setImageDrawable(ContextCompat.getDrawable(mActivity, R.drawable.ic_patient_info));
    }

    @Override
    public int getItemCount() {
        if(mBeacons != null)
            return mBeacons.size();
        else
            return 0;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void updateBeaconList(ArrayList<Beacon> beacons) {
        mBeacons = beacons;

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }
}
