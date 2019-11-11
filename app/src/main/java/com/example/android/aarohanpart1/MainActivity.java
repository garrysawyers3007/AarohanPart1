package com.example.android.aarohanpart1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

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
                    String pathName1 = data.getData().getPath();
                    //now display path of file
                    String[] pathName = pathName1.split("t1");

                    readFile(pathName[1]);

                }

                break;
        }
    }


//CHOOSING FILE CODE OVER

    // READING FILE CODE STARTS
    private void readFile(String pathName) {

        try {

            File excel = new File(pathName);

            FileInputStream fis = new FileInputStream(excel);  //error line

            Log.d("Error","Error");
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

        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    //new file reader code
//    public void ExcelReader(String pathName) {
//
//        try {
//            String SAMPLE_XLSX_FILE_PATH = "excelFile/example.xlsx";
//
//            // Creating a Workbook from an Excel file (.xls or .xlsx)
//            Workbook workbook = WorkbookFactory.create(new File(SAMPLE_XLSX_FILE_PATH));
//            TextView txt_pathShow = (TextView) findViewById(R.id.txt_path);
//            txt_pathShow.setText("i am here");
//
//
//            //Iterating over all the rows and columns in a Sheet
//            Sheet sheet = workbook.getSheetAt(0);
//
//            // Create a DataFormatter to format and get each cell's value as String
//            DataFormatter dataFormatter = new DataFormatter();
//
//            System.out.println("\n\nIterating over Rows and Columns using Iterator\n");
//            Iterator<Row> rowIterator = sheet.rowIterator();
//            while (rowIterator.hasNext()) {
//                Row row = rowIterator.next();
//
//                // Now let's iterate over the columns of the current row
//                Iterator<Cell> cellIterator = row.cellIterator();
//
//                while (cellIterator.hasNext()) {
//                    Cell cell = cellIterator.next();
//                    String cellValue = dataFormatter.formatCellValue(cell);
//                    System.out.print(cellValue + "\t");
//                }
//                System.out.println();
//            }
//            // Closing the workbook
//            workbook.close();
//
//        }catch (FileNotFoundException fe) {
//
//        } catch (IOException ie) {
//
//        } catch (InvalidFormatException e) {
//        }
//
//    }

    //ends  --- new file reader code

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
