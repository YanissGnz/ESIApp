package com.example.esiapp.adapters;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.esiapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class TodoAdapter extends RecyclerView.Adapter <TodoAdapter.MyViewHolder> {
    private Context mContext;
    private List<Notetodo> mData;
    private String userId;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public TodoAdapter(Context mContext, List<Notetodo> mData) {
        this.mContext = mContext;
        this.mData = mData;
        userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.row_does_item, parent, false);
        return new MyViewHolder(row);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.titletext.setText(mData.get(position).getText());
        //holder.datetext.setText((mData.get(position).getDate()));
        holder.timetext.setText((mData.get(position).getTime()));
        /*holder.faitoupas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    FirebaseDatabase.getInstance().getReference()
                            .child("is cheked").child(mData.get(position).getKey()).child(userId).setValue(true);

                } else {
                    FirebaseDatabase.getInstance().getReference()
                            .child("is cheked").child(mData.get(position).getKey()).child(userId).removeValue();
                }
            }
        });*/
       //switch_active(holder.faitoupas, mData.get(position).getKey());
       Alarm_System(mData.get(position).getTime_in_milli());
      //  Alarm_System(calendar.getTimeInMillis());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView titletext, datetext, timetext;
        RadioButton faitoupas;

        MyViewHolder(@NonNull View itemView) {

            super(itemView);

            titletext = itemView.findViewById(R.id.to_do_title);
            //datetext = itemView.findViewById(R.id.date);
            timetext = itemView.findViewById(R.id.to_do_time);
            faitoupas = itemView.findViewById(R.id.activate);
        }
    }
  /*  private void switch_active(final Switch switch1, String key) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("is cheked").child(key);
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(userId).exists()) {
                    switch1.setChecked(true);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }*/
    private void Alarm_System( long time) {
        AlarmManager  am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(mContext, MyAlarm.class);
            PendingIntent pi = PendingIntent.getBroadcast(mContext, 0, i, 0);
            assert am != null;
            am.set(AlarmManager.RTC, time , pi);
        }
    }