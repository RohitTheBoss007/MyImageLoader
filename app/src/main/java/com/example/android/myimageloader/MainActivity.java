package com.example.android.myimageloader;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    ImageView img;
    Button load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //img = findViewById(R.id.img);
        img=new ImageView(this);
        img.setLayoutParams(new LinearLayout.LayoutParams(160,160));
        img.setImageResource(R.mipmap.ic_launcher);
        LinearLayout linearLayout=findViewById(R.id.container);
        if(linearLayout!=null)
        {
            linearLayout.addView(img);
        }

        load = findViewById(R.id.load);
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadImage loadImage=new LoadImage(MainActivity.this);
                loadImage.execute("https://firebasestorage.googleapis.com/v0/b/fir-pushnotifications-cba4a.appspot.com/o/images%2FEnsvHyCJbJeTYTGRsQdNGJYw9QI3.jpg?alt=media&token=523b368b-8025-4f70-9bee-ee5536c9f0fe");
            }
        });


    }






    class LoadImage extends AsyncTask<String,Integer,Bitmap>
    {
        ProgressDialog progressDialog;
        LoadImage(Context context)
        {
            progressDialog=ProgressDialog.show(context,"Loading...","Please wait.");
            progressDialog.setCanceledOnTouchOutside(false);
        }



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap=null;
            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            File mypath=new File(directory,md5(strings[0]));
            try {
                File f=new File(directory, md5(strings[0]));
                bitmap = BitmapFactory.decodeStream(new FileInputStream(f));

            }
            catch (FileNotFoundException e)
            {

                try {
                    InputStream inputStream= new URL(strings[0]).openStream();
                    bitmap=BitmapFactory.decodeStream(inputStream);

                } catch (IOException f) {
                    f.printStackTrace();
                }
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(mypath);
                    // Use the compress method on the BitMap object to write image to the OutputStream
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

                    return bitmap;
                } catch (Exception g) {
                    g.printStackTrace();
                } finally {
                    try {
                        fos.close();
                    } catch (IOException h) {
                        h.printStackTrace();
                    }
                }
            }
            return bitmap;

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            img.setImageBitmap(bitmap);
            progressDialog.dismiss();
        }
    }



    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}
