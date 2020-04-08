package com.example.image_picker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final int toAlbum = 0;

    final int toCamera = 1;

     Uri  saveUri  = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
      if (savedInstanceState != null) {
           saveUri = Uri.parse(savedInstanceState.getString("saveUri"));
                };

        Button toAlbum_btn = findViewById(R.id.toAlbum_btn);
        Button toCamera_btn = findViewById(R.id.toCamera_btn);

        permission();

        toAlbum_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toAlbum ();

            }
        });

        toCamera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toCamera ();

            }
        });

    }

     protected void  permission(){

       List<String> permissionList = new ArrayList<>() ;

       List<String> request_permissionList = new ArrayList<>() ;

       permissionList.add(0,android.Manifest.permission.CAMERA);
       permissionList.add(1,android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
       permissionList.add(2,android.Manifest.permission.READ_EXTERNAL_STORAGE);


      for(int i = 0; i < permissionList.size();i++){

        int permission = ActivityCompat.checkSelfPermission(this, permissionList.get(i));

        if(permission != PackageManager.PERMISSION_GRANTED){

            request_permissionList.add(permissionList.get(i));

       }

    }

         if(request_permissionList.size() != 0){

             String[] request_array = new String[request_permissionList.size()];

             ActivityCompat.requestPermissions(this, request_permissionList.toArray(request_array), 0);
         }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        ImageView img = findViewById(R.id.imageView);


        switch (requestCode) {

            case toAlbum:

                switch (resultCode){

                    case Activity.RESULT_OK:
                        
                        if(data != null ){

                             Uri uri = data.getData();

                             img.setImageURI(uri);

                        }

                     break;

                    case Activity.RESULT_CANCELED:

                    break;

                }

                break;

            case toCamera:

                switch (resultCode){

                    case Activity.RESULT_OK:

                       img.setImageURI(saveUri);

                        break;

                    case Activity.RESULT_CANCELED:

                        break;

                }

        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        if (saveUri != null){

            String uriString = saveUri.toString();
            outState.putString("saveUri", uriString);

        }
    }

    private void toAlbum (){
      Intent intent =new  Intent(Intent.ACTION_GET_CONTENT);
      intent.setType("image/*");
      startActivityForResult(intent, toAlbum);

    }

    private void toCamera (){

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File tmpFile = new File(Environment.getExternalStorageDirectory().toString(),Long.toString(System.currentTimeMillis()) + ".jpg");

        Uri uriForCamera = FileProvider.getUriForFile(this,"com.example.image_picker.fileprovider",tmpFile);

        saveUri = uriForCamera;

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriForCamera);

        startActivityForResult(intent, toCamera);

    }


}
