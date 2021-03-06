package com.auribises.cpdemo;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class AllUsersActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    ContentResolver resolver;
    ListView listView;
    ArrayList<User> userList;
    UserAdapter adapter;
    User user;
    int pos;

    EditText eTxtSearch;

    void initViews(){
        resolver = getContentResolver();
        listView = (ListView)findViewById(R.id.listView);
        userList = new ArrayList<>();
        eTxtSearch = (EditText)findViewById(R.id.ediTextSearch);

        eTxtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = charSequence.toString();
                //Toast.makeText(AllUsersActivity.this,"You Entered: "+str,Toast.LENGTH_LONG).show();
                if(adapter!=null) {
                    adapter.filter(str);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);
        initViews();
        retrieveUsersFromDB();
    }

    void retrieveUsersFromDB(){
        String[] projection = {Util.COL_ID,Util.COL_NAME,Util.COL_PHONE,Util.COL_EMAIL,Util.COL_GENDER,Util.COL_CITY};
        Cursor cursor = resolver.query(Util.USER_URI,projection,null,null,null);

        int i=0;
        String n="",e="",p="",g="",c="";
        if(cursor!=null) {
            while (cursor.moveToNext()){
                i = cursor.getInt(cursor.getColumnIndex(Util.COL_ID));
                n = cursor.getString(cursor.getColumnIndex(Util.COL_NAME));
                p = cursor.getString(cursor.getColumnIndex(Util.COL_PHONE));
                e = cursor.getString(cursor.getColumnIndex(Util.COL_EMAIL));
                g = cursor.getString(cursor.getColumnIndex(Util.COL_GENDER));
                c = cursor.getString(cursor.getColumnIndex(Util.COL_CITY));

                User user = new User(i,n,p,e,g,c);
                userList.add(user);
            }
        }

        adapter = new UserAdapter(this,R.layout.list_item,userList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        user = userList.get(i);

        showOptions();

        pos = i;

    }

    void showOptions(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] items = {"View","Update","Delete"};
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case 0:
                        showUser();
                        break;

                    case 1:
                        Intent intent = new Intent(AllUsersActivity.this,UserRegistrationActivity.class);
                        intent.putExtra("keyUser",user);
                        startActivity(intent);
                        break;

                    case 2:
                        deleteUser();
                        break;
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void showUser(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(user.getName());
        builder.setMessage("ID: "+user.getId()+"\nName: "+user.getName()+"\nEmail: "+user.getEmail());
        builder.setPositiveButton("Done",null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void deleteUser(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete "+user.getName());
        builder.setMessage("Are you sure you wish to delete ?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String where = Util.COL_ID+" = "+user.getId();
                //String where = Util.COL_EMAIL+" = '"+user.getEmail()+"'";

                int row = resolver.delete(Util.USER_URI,where,null);
                if(row>0){
                    userList.remove(pos);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        builder.setNegativeButton("Cancel",null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
