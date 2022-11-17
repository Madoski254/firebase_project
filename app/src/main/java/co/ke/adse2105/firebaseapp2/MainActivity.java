package co.ke.adse2105.firebaseapp2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    //Constant used as the request code to the Still image camera Intent Action
    private  static final int GET_IMAGE_REQUEST_CODE = 888;

    //VIEW/UI declaration
    ImageView ivPhoto;

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
    public  void handleBtnCaptureClick(){
        //Capture /take a photo and consume/use the returned image thumbnail
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(i.resolveActivity(getPackageManager()) != null){
            startActivityForResult(i,GET_IMAGE_REQUEST_CODE);
        }
    }

    //Override the activity result method to process the returned data
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        //Set the returned thumbnail to the imageview once we get the required data
        if(requestCode == GET_IMAGE_REQUEST_CODE && resultCode == RESULT_OK)
        {
            //Retrieve the data from the result intent and look for a bitmap
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //Set the image views image to the returned bitmap
            ivPhoto.setImageBitmap(imageBitmap);
        }
    }


}