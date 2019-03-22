package com.example.challenge1part2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import static android.widget.TextView.*;
import static android.widget.TextView.BufferType.EDITABLE;


public class MainActivity extends AppCompatActivity {

    private static final int REQ_CODE_TAKE_PICTURE = 210;
    Button button, button2;
    EditText editText;
    ImageView imageView;
    String [] perms = {"android.permission.CAMERA"};
    DatabaseHelper myDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDb = new DatabaseHelper(this);

        button = (Button) findViewById(R.id.button);
        imageView = (ImageView) findViewById(R.id.imageView);
        editText = (EditText) findViewById(R.id.editText);
        button2 = (Button) findViewById(R.id.button2);

        //myDb.onCreate(null);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(perms, REQ_CODE_TAKE_PICTURE);
                }
                else {
                    Intent picIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(picIntent, REQ_CODE_TAKE_PICTURE);
                }
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = Integer.parseInt(editText.getText().toString());

                byte[] BytesRepOfImage = myDb.getData(id);

                if (BytesRepOfImage.length == 0) {
                    Toast.makeText(getBaseContext(), "That ID does not exist", Toast.LENGTH_LONG).show();
                }
                else {
                    Bitmap bm = BitmapFactory.decodeByteArray(BytesRepOfImage, 0, BytesRepOfImage.length);

                    //Setting the image view and edit text.
                    imageView.setImageBitmap(bm);
                    editText.setText("", EDITABLE);
                }
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_TAKE_PICTURE /*&& resultCode == RESULT_OK*/) {
            Bitmap bmp = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bmp);

            //converting bitmap to byte
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] imageByte = stream.toByteArray();
            myDb.insertData(imageByte);
        }
    }


}
