package com.example.checkinemployee;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PageResultScaned extends AppCompatActivity {

    TextView dataPerson, EmName, EmDepartment, EmLocation, responseTV;
    EditText EmNumber;
    Button btn_save, btn_scanEm, btn_adddata;
    //Spinner EmLocation;
    //String[] LocationData = {"จุด A", "จุด B", "จุด C", "จุด D"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_result_scaned);

        dataPerson = findViewById(R.id.dataEm);
        responseTV = findViewById(R.id.responseTV);


        EmNumber = findViewById(R.id.txt_number);
        EmName = findViewById(R.id.txt_name);
        EmDepartment = findViewById(R.id.txt_department);
        EmLocation = findViewById(R.id.txt_location);

        Intent receiverIntent = getIntent();
        String receivedValue = receiverIntent.getStringExtra("KEY_SENDER");
        EmLocation.setText(receivedValue);

        //EmLocation = findViewById(R.id.spinner);

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_activated_1, LocationData);
//        EmLocation.setAdapter(adapter);

        btn_save = findViewById(R.id.savebtn);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EmNumber.getText().toString().isEmpty() && EmName.getText().toString().isEmpty() && EmDepartment.getText().toString().isEmpty() && EmLocation.getText().toString().isEmpty()){
                    Toast.makeText(PageResultScaned.this, "Please enter both the values", Toast.LENGTH_SHORT).show();
                    return;
                }
                postData(EmNumber.getText().toString(), EmName.getText().toString(), EmDepartment.getText().toString(), EmLocation.getText().toString());
            }
        });

        btn_scanEm = findViewById(R.id.scanEmployee);
        btn_scanEm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScanning();
            }
        });

        btn_adddata = findViewById(R.id.adddata);
        btn_adddata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check data in Textview
                String input = dataPerson.getText().toString();
                if(input.contains("||")) {
                    String[] inputArr = input.split("\\|\\|");
                    for (String str : inputArr) {
                        char firstChar = str.charAt(0);
                        if (Character.isDigit(firstChar)) {
                            EmNumber.setText(inputArr[0]);
                            String emnum = EmNumber.getText().toString();
                            emnum = emnum.trim();
                            EmNumber.setText(emnum);

                            EmName.setText(inputArr[1]);
                            String emname = EmName.getText().toString();
                            emname = emname.trim();
                            EmName.setText(emname);

                            EmDepartment.setText(inputArr[2]);
                            String emdepart = EmDepartment.getText().toString();
                            emdepart = emdepart.trim();
                            EmDepartment.setText(emdepart);

                            dataPerson.setText("");
                        } else if (Character.isLetter(firstChar)) {
                            EmNumber.setText(inputArr[2]);
                            String emnum = EmNumber.getText().toString();
                            emnum = emnum.trim();
                            EmNumber.setText(emnum);

                            EmName.setText(inputArr[0]);
                            String emname = EmName.getText().toString();
                            emname = emname.trim();
                            EmName.setText(emname);

                            EmDepartment.setText(inputArr[1]);
                            String emdepart = EmDepartment.getText().toString();
                            emdepart = emdepart.trim();
                            EmDepartment.setText(emdepart);

                            dataPerson.setText("");
                        }
                    }
                }else {
                    EmNumber.setText(dataPerson.getText());
                    dataPerson.setText("");
                }
            }
        });
    }

    //scan qr code
    private void startScanning() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("Scan a QR code");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            String scanResult = result.getContents();
            TextView dataPerson = findViewById(R.id.dataEm);
            dataPerson.setText(scanResult);
            responseTV.setText("");
        }
    }

    //ส่งข้อมูล sql ผ่าน api
    private void postData (String employeeID, String employeeName, String Department, String Location){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.10.123:3000/api/")
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        DataModal modal = new DataModal(employeeID, employeeName, Department, Location );
        Call<DataModal> call = retrofitAPI.createPost(modal);
        call.enqueue(new Callback<DataModal>() {
            @Override
            public void onResponse(Call<DataModal> call, Response<DataModal> response) {
                responseTV.setText("บันทึกข้อมูลสำเร็จ");
                //Toast.makeText(PageResultScaned.this, "Data added to API", Toast.LENGTH_SHORT).show();
                EmNumber.setText("");
                EmName.setText("");
                EmDepartment.setText("");
                //responseTV.setText("Name : " + response.body().getEmployeeName() + "\n" + "Location : " + response.body().getLocation());
            }
            @Override
            public void onFailure(Call<DataModal> call, Throwable t) {
                responseTV.setText("Error found is : " + t.getMessage());
            }
        });
    }
}