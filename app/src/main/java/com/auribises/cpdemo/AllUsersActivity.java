package com.auribises.cpdemo;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class AllUsersActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    ContentResolver resolver;
    ListView listView;
    ArrayList<User> userList;
    UserAdapter adapter;
    User user;

    void initViews(){
        resolver = getContentResolver();
        listView = (ListView)findViewById(R.id.listView);
        userList = new ArrayList<>();
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

                        break;

                    case 2:

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
}
