package com.auribises.cpdemo;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

public class UserRegistrationActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, AdapterView.OnItemSelectedListener{

    EditText eTxtName, eTxtPhone, eTxtEmail;
    Spinner spCity;
    RadioButton rbMale, rbFemale;
    Button btnSubmit;

    ArrayAdapter<String> adapter;

    User user,rcvUser;

    ContentResolver resolver;

    boolean updateMode;

    void initViews(){
        eTxtName = (EditText)findViewById(R.id.editTextName);
        eTxtPhone = (EditText)findViewById(R.id.editTextPhone);
        eTxtEmail = (EditText)findViewById(R.id.editTextEmail);

        spCity = (Spinner)findViewById(R.id.spinnerCity);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item);
        adapter.add("--Select City--");
        adapter.add("Ludhiana");
        adapter.add("Chandigarh");
        adapter.add("Delhi");
        adapter.add("Bengaluru");
        adapter.add("California");
        spCity.setAdapter(adapter);


        rbMale = (RadioButton)findViewById(R.id.radioButtonMale);
        rbFemale = (RadioButton)findViewById(R.id.radioButtonFemale);

        btnSubmit = (Button)findViewById(R.id.buttonSubmit);

        spCity.setOnItemSelectedListener(this);

        rbMale.setOnCheckedChangeListener(this);
        rbFemale.setOnCheckedChangeListener(this);

        btnSubmit.setOnClickListener(this);

        user = new User();

        resolver = getContentResolver();

        Intent rcv = getIntent();
        updateMode = rcv.hasExtra("keyUser");
        if(updateMode) {
            rcvUser = (User) rcv.getSerializableExtra("keyUser");
            eTxtName.setText(rcvUser.getName());
            eTxtPhone.setText(rcvUser.getPhone());
            eTxtEmail.setText(rcvUser.getEmail());

            if(rcvUser.getGender().equals("Male")){
                rbMale.setChecked(true);
            }else{
                rbFemale.setChecked(true);
            }

            int p = 0;
            for(int i=0;i<adapter.getCount();i++){
                if(adapter.getItem(i).equals(rcvUser.getCity())){
                    p = i;
                    break;
                }
            }
            spCity.setSelection(p);

            btnSubmit.setText("Update "+rcvUser.getName());
        }



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        initViews();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.buttonSubmit){
            user.setName(eTxtName.getText().toString().trim());
            user.setPhone(eTxtPhone.getText().toString().trim());
            user.setEmail(eTxtEmail.getText().toString().trim());

            insertIntoDB();
        }
    }

    void insertIntoDB(){

        ContentValues values = new ContentValues();
        values.put(Util.COL_NAME,user.getName());
        values.put(Util.COL_PHONE,user.getPhone());
        values.put(Util.COL_EMAIL,user.getEmail());
        values.put(Util.COL_GENDER,user.getGender());
        values.put(Util.COL_CITY,user.getCity());

        if(updateMode){
            // Update Mode
            String where = Util.COL_ID+" = "+rcvUser.getId();
            int i = resolver.update(Util.USER_URI,values,where,null);
            if(i>0){
                Toast.makeText(this,"User Updated Successfully",Toast.LENGTH_LONG).show();
                finish();
            }
        }else{
            // Insert Mode
            Uri dummy = resolver.insert(Util.USER_URI,values);
            Toast.makeText(this,"Record Inserted: "+user.getName()+" - "+dummy.getLastPathSegment(),Toast.LENGTH_LONG).show();

            clearFields();

        }

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int id = compoundButton.getId();

        if(b) {
            if (id == R.id.radioButtonMale) {
                user.setGender("Male");
            } else {
                user.setGender("Female");
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String city = adapter.getItem(i);
        if(i!=0){
            user.setCity(city);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    void clearFields(){
        eTxtName.setText("");
        eTxtPhone.setText("");
        eTxtEmail.setText("");
        spCity.setSelection(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(1,101,0,"All Users");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == 101){
            Intent intent = new Intent(UserRegistrationActivity.this,AllUsersActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
