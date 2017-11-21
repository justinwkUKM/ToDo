package com.todo.waqas.todo.activity;


import android.app.AlarmManager;
import android.app.Dialog;
import android.app.LauncherActivity;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.todo.waqas.todo.R;
import com.todo.waqas.todo.adapters.ToDoListAdapter;
import com.todo.waqas.todo.modal.StaticConfig;
import com.todo.waqas.todo.modal.ToDoData;
import com.todo.waqas.todo.sqlite.SqliteHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    FloatingActionButton addTask;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<ToDoData> tdd = new ArrayList<>();
    SqliteHelper mysqlite;
    SwipeRefreshLayout swipeRefreshLayout;

    DatabaseReference mDB;
    DatabaseReference mListItemRef;
    private ArrayList<ToDoData> myListItems;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_s);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        addTask = (FloatingActionButton) findViewById(R.id.imageButton);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        adapter = new ToDoListAdapter(tdd, getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.accent), getResources().getColor(R.color.divider));
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                updateCardView();
            }
        });

        mDB= FirebaseDatabase.getInstance().getReference();
        mListItemRef = mDB.child("task");
        myListItems = new ArrayList<>();

        initFirebase();

        mListItemRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                dataSnapshot.getKey();
                Log.d(TAG+"Added",dataSnapshot.getValue(ToDoData.class).toString());
                //fetchData(dataSnapshot);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG+"Changed",dataSnapshot.getKey());
                //updateUI();
                //mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG+"Removed",dataSnapshot.getValue(ToDoData.class).toString());
                //updateUI();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG+"Moved",dataSnapshot.getValue(ToDoData.class).toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG+"Cancelled",databaseError.toString());
            }
        });


        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.custom_dailog);
                dialog.show();
                Button save = (Button) dialog.findViewById(R.id.btn_save);
                Button cancel = (Button) dialog.findViewById(R.id.btn_cancel);
                CheckBox cb = (CheckBox) dialog.findViewById(R.id.checkbox);
                TextView tvstatus = (TextView) dialog.findViewById(R.id.status);
                cb.setVisibility(View.GONE);
                tvstatus.setVisibility(View.GONE);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String key = FirebaseDatabase.getInstance().getReference().child("listItem").push().getKey();
                        EditText todoText = (EditText) dialog.findViewById(R.id.input_task_desc);
                        EditText todoNotes = (EditText) dialog.findViewById(R.id.input_task_notes);
                        if (todoText.getText().length() >= 2) {
                            RadioGroup proritySelection = (RadioGroup) dialog.findViewById(R.id.toDoRG);
                            String RadioSelection = new String();
                            if (proritySelection.getCheckedRadioButtonId() != -1) {
                                int id = proritySelection.getCheckedRadioButtonId();
                                View radiobutton = proritySelection.findViewById(id);
                                int radioId = proritySelection.indexOfChild(radiobutton);
                                RadioButton btn = (RadioButton) proritySelection.getChildAt(radioId);
                                RadioSelection = (String) btn.getText();
                            }
                            Spinner getTime = (Spinner) dialog.findViewById(R.id.spinner);
                            EditText timeInNumb = (EditText) dialog.findViewById(R.id.input_task_time);
                            if(getTime.getSelectedItem().toString().matches("Days") && !(timeInNumb.getText().toString().matches(""))) {
                                // Convert timeInNumb to Days in Miliseconds
                                int longtime = Integer.parseInt(timeInNumb.getText().toString());
                                long miliTime = longtime * 24 * 60 * 60 * 1000 ;
                                scheduleNotification(miliTime,todoText.getText().toString(),RadioSelection);
                            } else if (getTime.getSelectedItem().toString().matches("Minutes") && !(timeInNumb.getText().toString().matches(""))) {
                                // Convert timeInNumb to Minutes in Miliseconds
                                int longtime = Integer.parseInt(timeInNumb.getText().toString());
                                long miliTime = longtime * 60 * 1000 ;
                                scheduleNotification(miliTime,todoText.getText().toString(),RadioSelection);
                            } else if (getTime.getSelectedItem().toString().matches("Hours") && !(timeInNumb.getText().toString().matches(""))) {
                                // Convert timeInNumb to Hours in Miliseconds
                                int longtime = Integer.parseInt(timeInNumb.getText().toString());
                                long miliTime = longtime * 60 * 60 * 1000 ;
                                scheduleNotification(miliTime,todoText.getText().toString(),RadioSelection);
                            }
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(SqliteHelper.Col_2, todoText.getText().toString());
                            contentValues.put(SqliteHelper.Col_3, RadioSelection);
                            contentValues.put(SqliteHelper.Col_4, "Incomplete");
                            contentValues.put(SqliteHelper.Col_5, todoNotes.getText().toString());
                            contentValues.put(SqliteHelper.Col_6, "TODAY");
                            contentValues.put(SqliteHelper.Col_7, "ME");
                            mysqlite = new SqliteHelper(getApplicationContext());

                            String listItemText = todoText.getText().toString();
                            ToDoData listItem = new ToDoData(listItemText,FirebaseAuth.getInstance().getCurrentUser().getEmail(), false );
                            Map<String, Object> listItemValues = listItem.toMap();
                            Map<String, Object> childUpdates = new HashMap<>();
                            childUpdates.put("/task/" + key, listItemValues);
                            FirebaseDatabase.getInstance().getReference().updateChildren(childUpdates);

                            Boolean b = mysqlite.insertInto(contentValues);
                            if (b) {
                                dialog.hide();
                                updateCardView();
                            } else {
                                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "Please enter To Do Task", Toast.LENGTH_SHORT).show();
                        }


                    }
                });

            }
        });
    }

    private void initFirebase() {
        //Khoi tao thanh phan de dang nhap, dang ky
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    StaticConfig.UID = user.getUid();
                } else {
                    MainActivity.this.finish();
                    // User is signed in
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    public void scheduleNotification(long time, String TaskTitle, String TaskPriority) {
        Calendar Calendar_Object = Calendar.getInstance();
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        final int _id = (int) System.currentTimeMillis();
        Intent myIntent = new Intent(MainActivity.this, AlarmRecever.class);
        myIntent.putExtra("TaskTitle", TaskTitle);
        myIntent.putExtra("TaskPriority",TaskPriority);
        myIntent.putExtra("id",_id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,
                _id, myIntent, PendingIntent.FLAG_ONE_SHOT);
        alarmManager.set(AlarmManager.RTC, Calendar_Object.getTimeInMillis() + time,
                pendingIntent);

    }

    public void updateCardView() {
        swipeRefreshLayout.setRefreshing(true);
        mysqlite = new SqliteHelper(getApplicationContext());
        Cursor result = mysqlite.selectAllData();
        if (result.getCount() == 0) {
            tdd.clear();
            adapter.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(), "No Tasks", Toast.LENGTH_SHORT).show();
        } else {
            tdd.clear();
            adapter.notifyDataSetChanged();
            while (result.moveToNext()) {
                ToDoData tddObj = new ToDoData();
                tddObj.setToDoID(result.getInt(0));
                tddObj.setToDoTaskDetails(result.getString(1));
                tddObj.setToDoTaskPriority(result.getString(2));
                tddObj.setToDoTaskStatus(result.getString(3));
                tddObj.setToDoNotes(result.getString(4));
                tddObj.setToDoCreationDate(result.getString(5));
                tddObj.setToDoAuthor(result.getString(6));
                tdd.add(tddObj);
                Log.e("toString",tddObj.toString());
            }


            adapter.notifyDataSetChanged();
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onRefresh() {
        updateCardView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        switch(id){
            case R.id.about:
                //deleteAllListItems();

                break;
            case R.id.action_logout:
                mAuth.getInstance().signOut();
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
