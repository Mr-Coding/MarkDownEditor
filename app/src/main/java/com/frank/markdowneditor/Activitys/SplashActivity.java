package com.frank.markdowneditor.Activitys;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.frank.markdowneditor.R;
import com.frank.markdowneditor.Views.Winds;

public class SplashActivity extends AppCompatActivity {
    private Thread thread = new Thread(){
        @Override
        public void run() {
            super.run();
            try {
                sleep(800);
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                SplashActivity.this.startActivity(i);
                SplashActivity.this.finish();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        if (Build.VERSION.SDK_INT >= 23) {
            int checkWriteStoragePermission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(checkWriteStoragePermission != PackageManager.PERMISSION_GRANTED){

                new AlertDialog.Builder(this).setTitle("本应用需要读写权限才能正常运行哦! (●'◡'●)")
                        .setIcon(R.drawable.dialog_info)
                        .setPositiveButton("授权", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(
                                        SplashActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 111);
                            }
                        })
                        .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SplashActivity.this.finish();
                            }
                        }).setCancelable(false).show();
            }else {
                thread.start();
            }
        }else {
            thread.start();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "授权失败！", Toast.LENGTH_LONG).show();
            this.finish();
        }else {
            Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
            SplashActivity.this.startActivity(mainIntent);
            this.finish();
        }
    }
}
