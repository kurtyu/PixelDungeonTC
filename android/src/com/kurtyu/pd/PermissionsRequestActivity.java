package com.kurtyu.pd;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import static android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS;

/**
 * Created by kurtyu on 2017/4/21.
 */

public class PermissionsRequestActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback
{
    private static final int REQUEST = 1;

    private static String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    public static boolean verifyPermissions(int[] grantResults)
    {
        // At least one result must be checked.
        if (grantResults.length < 1)
        {
            return false;
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults)
        {
            if (result != PackageManager.PERMISSION_GRANTED)
            {
                return false;
            }
        }
        return true;
    }


    public void onStart()
    {
        super.onStart();
        checkPermissions();
    }

    public void checkPermissions()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissionsDialog();
        }
        else
        {
            Intent intent = new Intent(this, AndroidLauncher.class);
            startActivity(intent);
        }
    }

    public void requestPermissions()
    {
        ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST);
    }

    // 通知存取
    private void requestPermissionsDialog()
    {
        new AlertDialog.Builder(this)
                .setMessage("本遊戲必須允許儲存遊戲檔案才能夠進行遊戲，請再確認一次。")
                .setTitle("儲存授權")
                .setIconAttribute(android.R.attr.alertDialogIcon)
                .setCancelable(false) // 不能允許點旁邊取消
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                Log.d("DEBUG", "AlertDialog => Request");
                                requestPermissions();
                            }
                        })
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                // do nothing
                                finish();
                            }
                        })
                .create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {

        if (requestCode == REQUEST)
        {
            // FIXME 可能有問題
            Log.d("DEBUG", "onRequestPermissionsResult => " + grantResults[0]);
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Intent intent = new Intent(this, AndroidLauncher.class);
                startActivity(intent);
            }
            else
            {
                Log.d("DEBUG", "onRequestPermissionsResult => Request");
//                requestPermissions();

                startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID)));
            }
        }
        else
        {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}
