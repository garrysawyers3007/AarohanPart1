package com.example.android.aarohanpart1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

//CHOOSING FILE CODE STARTS FROM HERE

    public void chooseFile(View view) {

        //open file manager
        Intent myFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        //select which type of file you wanna select
        myFileIntent.setType("*/*");
        startActivityForResult(myFileIntent, 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        switch (requestCode) {
            case 10:

                if (resultCode == RESULT_OK) {

                    //it will get the path which is selected
                    String pathName = data.getData().getPath();
                    //now display path of file


                    readFile(pathName);
                }

                break;
        }
    }


//CHOOSING FILE CODE OVER

// READING FILE CODE STARTS
    private void readFile(String pathName) {

        try {
            File excel = new File(pathName);

            FileInputStream fis;


            fis = new FileInputStream(excel);  //error line

            TextView txt_pathShow = (TextView) findViewById(R.id.txt_path);
            txt_pathShow.setText("i am here");

            XSSFWorkbook book = new XSSFWorkbook(fis);

            XSSFSheet sheet = book.getSheetAt(0);


            Iterator<Row> itr = sheet.iterator();



            // Iterating over Excel file in Java
            while (itr.hasNext()) {
                Row row = itr.next();

                // Iterating over each column of Excel file
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {

                    Cell cell = cellIterator.next();

                    switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_STRING:

                            display(cell.getStringCellValue() + "\t");
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            display(cell.getNumericCellValue() + "\t");
                            break;
                        case Cell.CELL_TYPE_BOOLEAN:
                            display(cell.getBooleanCellValue() + "\t");
                            break;
                        default:

                    }
                }
                display("");
            }



            // Close workbook, OutputStream and Excel file to prevent leak
            book.close();
            fis.close();

        } catch (FileNotFoundException fe) {
            fe.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

//READING EXCEL FILE CODE OVER

// CODE FOR SENDING MESSAGE STARTS FROM HERE


    String finalMessage = "";
    String name = " Naman ";
    String number = " 9929922419 ";

    public void btn_print_name(View view) {
        EditText txt_message = (EditText) findViewById(R.id.txt_message);
        String Message = txt_message.getText().toString();
        Message = Message + name;
        txt_message.setText("");
        finalMessage += Message;
        display(finalMessage);
    }

    public void btn_print_number(View view) {
        EditText txt_message = (EditText) findViewById(R.id.txt_message);
        String Message = txt_message.getText().toString();
        Message = Message + number;
        txt_message.setText("");
        finalMessage += Message;
        display(finalMessage);
    }

    private void display(String message) {
        TextView show_message = (TextView) findViewById(R.id.show_message);
        show_message.setText(message);
    }


    public void btn_send(View view) {

        //Permission which we are using
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);

        //checking the permission
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {

            //If user grants perm. then what to do
            MyMessage();
        } else {
            //if user denies, then again request
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 0);

        }
    }

    private void MyMessage() {

        //To send msg , use msg Manager
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(number, null, finalMessage, null, null);

        //displays a toast msg
        Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show();
    }

    //mtd for handling permission, for request code generated will be matched by using a switch condition
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 0:

                if (grantResults.length >= 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    MyMessage();
                } else {
                    Toast.makeText(this, "You don't have Required Permissions to execute", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }


}
