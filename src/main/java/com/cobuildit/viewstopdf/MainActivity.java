package com.cobuildit.viewstopdf;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final int STORAGE_CODE = 1000;
    //declaring all the variables
    EditText mTextEt;
    Button mSaveBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialising the views
        mTextEt = findViewById(R.id.textEt);
        mSaveBtn = findViewById(R.id.saveBtn);

        //handles button click here
        mSaveBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                //we need to handle runtime permissions for devices with marshmallow and above
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    //checking if permission is enabled or not
                    if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                            PackageManager.PERMISSION_DENIED){
                        //permission was not granted, requires it
                        String [] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permissions, STORAGE_CODE);

                    }else{
                        //permission already granted, call save pdf method
                        savePdf();
                    }
                }else{
                     //system os is less than marshmallow, call pdf method
                    savePdf();
                }
            }
        });
    }

    private void savePdf() {
        //create object of Document class
        Document mDoc = new Document();
        //pdf file name
        String mFileName = new SimpleDateFormat("yyyymmdd_HHmmss",
                Locale.getDefault()).format(System.currentTimeMillis());
        //pdf file path
        String mFilePath = Environment.getExternalStorageDirectory() + "/" + mFileName + ".pdf";
        try{
            PdfWriter.getInstance(mDoc, new FileOutputStream(mFilePath));
            //open the document for waiting
            mDoc.open();
            //get text from EditText , i.e mText
            String mText = mTextEt.getText().toString();
            //add author of the document (optional)
            mDoc.addAuthor("Gabriel Jonah");
            //add paragraph to the document
            mDoc.add(new Paragraph("THIS IS THE PARAGRAPH" +mText));

            //close the document
            mDoc.close();
            //show message that file s saved, this will show both file name and its path
            Toast.makeText(this, mFileName + ".pdf\nis Saved to \n"+ mFilePath, Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            //if any thing goes wrong cousing eception, get and show exception message
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //handle permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case STORAGE_CODE:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //permission was granted from popup, call savepdf method
                    savePdf();
                }else{
                    //permission was denied from popup, show error messgae
                    Toast.makeText(this, "Permission denied....!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
//This template or driver workes correctly and it is completed today wednesday 3rd 2019
//This was handcoded by Gabriel Jonah
