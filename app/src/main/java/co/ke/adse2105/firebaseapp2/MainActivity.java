package co.ke.adse2105.firebaseapp2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    //Constant used as the request code to the Still image camera Intent Action
    private static final int GET_IMAGE_REQUEST_CODE = 888;
    //Constant used to get the permissions to the camera and writing to the external storage
    private static final int PERMISSIONS_CODE = 1000;

    //VIEW/UI declaration
    ImageView ivPhoto;
    //
    Uri image_uri;

    //Use ButterKnife bind view to re views/widgets
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.activity_main_btn_Capture_image)
    Button btnCapture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //code to make ButterKnife bidding on view work
        ButterKnife.bind(this);

        //Initialize the image view
        ivPhoto = findViewById(R.id.activity_main_iv_photo);

    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.activity_main_btn_Capture_image)
    public void handleBtnCaptureClick() {

        //CHECK IF THE Phones method to
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Check whether the app had been pre
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                //String array to hold permissions request
                String appPermissions[] = {Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE};

                //Display a pop up  REQUESTING camera & storage permissions
                requestPermissions(appPermissions, PERMISSIONS_CODE);


            } else { //App already had the camera & external permissions
                captureImage();
            }

        }

    }

    private void captureImage() {
        //String to generate the date and time the image was
        String captureDate = new SimpleDateFormat("ddmmyyyy").format(new Date());
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Image " + captureDate);
        values.put(MediaStore.Images.Media.DESCRIPTION, "Capture for ADSE firebase app " + captureDate);
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        //camera Intent
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, GET_IMAGE_REQUEST_CODE);


    }

    //Override the activity result method to process the returned data
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Set the returned thumbnail to the imageview once we get the required data
        if (requestCode == GET_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {

            //Set
            ivPhoto.setImageURI(image_uri);
        }
    }


}