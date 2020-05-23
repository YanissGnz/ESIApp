package com.example.esiapp;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.esiapp.adapters.Notetodo;
import com.example.esiapp.adapters.TodoAdapter;
import com.example.esiapp.fragment.ToDo;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Calendar;
import java.util.Objects;

public class Addtodo extends AppCompatActivity  implements DatePickerDialog.OnDateSetListener ,  TimePickerDialog.OnTimeSetListener {
    private static final String TAG = "MainActivity";
    Button addtodo;
    String stitle;
    String sdate;
    String uname;
    String userId;
    String stime;
    FirebaseUser currentUser;
    FirebaseAuth mAuth;
    TextInputEditText textInputEditText;
    DatabaseReference ref;
    TextView addtododialog;
    int hour,minute1,day,mounth;
    Calendar calendar;



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("ShowToast")
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_do);
        calendar=Calendar.getInstance();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString("Ha") != null) {
                Toast.makeText(getApplicationContext(), "hello" + bundle.getString("Ha"), Toast.LENGTH_SHORT);
            }

        }
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        assert currentUser != null;
        uname = currentUser.getDisplayName();
        addtododialog = findViewById(R.id.add_do_button);
        textInputEditText = findViewById(R.id.task_title);
        addtododialog.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                userId = currentUser.getUid();
                uname = currentUser.getDisplayName();
                stitle = Objects.requireNonNull(textInputEditText.getText()).toString();
                if (TextUtils.isEmpty(stitle))
                    Addtodo.this.textInputEditText.setError("title is required");

                else {
                    showDatePickerDialog();
                }
            }
        });
       // showAlertDialog();
    }

    private void showAlertDialog() {
        // Creating alert Dialog with one Button
        addtododialog = findViewById(R.id.add_do_button);
        textInputEditText = findViewById(R.id.task_title);
        addtododialog.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                userId = currentUser.getUid();
                uname = currentUser.getDisplayName();
                stitle = Objects.requireNonNull(textInputEditText.getText()).toString();
                if (TextUtils.isEmpty(stitle))
                    Addtodo.this.textInputEditText.setError("title is required");

                else {
                    showDatePickerDialog();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void addNote() {
        calendar.set(calendar.get(Calendar.YEAR), mounth, calendar.get(Calendar.DAY_OF_MONTH),
                hour, minute1, 0);
        stitle = Objects.requireNonNull(textInputEditText.getText()).toString();
        userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        Notetodo note = new Notetodo(stitle, sdate, stime, uname,calendar.getTimeInMillis());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference("NoteTodo").child(userId).push();
        String key = ref.getKey();
        note.setKey(key);
        ref.setValue(note);
        Intent in = new Intent(getApplicationContext(), Home.class);
        startActivity(in);
        overridePendingTransition(R.anim.slide_in_top, R.anim.none);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                Objects.requireNonNull(this),
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                (DatePickerDialog.OnDateSetListener) this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (TimePickerDialog.OnTimeSetListener) this,
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE),
                true);
        timePickerDialog.show();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        sdate = dayOfMonth + "/" + (month + 1) + "/" + year;
        day=dayOfMonth;
        mounth=month+1;
        showTimePickerDialog();

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (hourOfDay < 10) {
            stime = "0" + hourOfDay + ":" + minute;
        }
        else if (minute < 10) {
            stime = hourOfDay + ":" + "0" + minute;
        } else {
            stime = hourOfDay + ":" + minute;
        }
        hour=hourOfDay;
        minute1=minute;

//  }
        addNote();
    }
}