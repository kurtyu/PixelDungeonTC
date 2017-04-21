package com.kurtyu.pd;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.watabou.pixeldungeon.PixelDungeon;
import com.watabou.pixeldungeon.ui.Toast;
import com.watabou.utils.PDPlatformSupport;

public class AndroidLauncher extends AndroidApplication
{
//    String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmCmvo/aF7JYBoltOGRvI82GYVRWWcMEGc5g7QpxcD4P+kWXV9MG+PxgFaO+f79NydQSqA2NbnfMpvG8fz4SlAEgHwLRkTsSePmedWJ6cEWjMDDhWMj52zg2JR59XuDStk3PwdsKfYgopciDtbKBhGPWBzVFbto+z4o366f5yhpdqMaWlD01UF9z7lIQyAVWb6DSylXvvTyXqA3ZMAFE+2HYs7hTdlNALhzdPsP4JjZlmf/uaLhRHAzlw6MvpC8kmLg8Lupcas8SeHhbG3esqAmasZGykD6Q8kji6dKwjtkLy+dOjhewqGLhECj6QWkdxQkczk5ARiXGkQqFklZO3ZwIDAQAB";
//    IabHelper mHelper;

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    AndroidApplicationConfiguration config;
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Log.d("DEBUG", "AndroidLauncher::onCreate");

//        if(isSupportGoogle())
//        {
//            mHelper = new IabHelper(this, base64EncodedPublicKey);
//            mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener()
//            {
//                public void onIabSetupFinished(IabResult result)
//                {
//                    if (!result.isSuccess())
//                    {
//                        // Oh noes, there was a problem.
//                        Log.d("IAB", "Problem setting up In-app Billing: " + result);
//                    }
//                    // Hooray, IAB is fully set up!
//                    Log.d("IAB", "Billing Success: " + result);
//
//                    // 檢查Purchase
//                    processPurchases();
//                }
//            });
//        }
//        else
//        {
//            Log.d("IAB", "not support In-app Billing");
//        }

        config = new AndroidApplicationConfiguration();
        config.useWakelock = true;

//        String version;
//        try
//        {
//            version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
//        }
//        catch (PackageManager.NameNotFoundException e)
//        {
//            version = "???";
//        }

        // 請求授權
//        requestPermissions();

        launch(config);

    }

//    private void insertDummyContactWrapper()
//    {
//        int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.WRITE_CONTACTS);
//        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED)
//        {
//            requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS},
//                    REQUEST_CODE_ASK_PERMISSIONS);
//            return;
//        }
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
//    {
//        switch (requestCode)
//        {
//            case REQUEST_CODE_ASK_PERMISSIONS:
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
//                {
//                    // Permission Granted
//
//                }
//                else
//                {
//                    // Permission Denied
//                    Toast.makeText(MainActivity.this, "WRITE_CONTACTS Denied", Toast.LENGTH_SHORT)
//                            .show();
//                }
//                break;
//            default:
//                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        }
//    }

    private void launch(AndroidApplicationConfiguration config)
    {
        getDefaultTracker();
        initialize(new PixelDungeon(new PDPlatformSupport<>("1.0.3", "pdtc/", new AndroidInputProcessor(this))), config);
    }


//    synchronized public Tracker getDefaultTracker() {
//        if (mTracker == null) {
//            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
//            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
//            mTracker = analytics.newTracker(R.xml.global_tracker);
//        }
//        return mTracker;
//    }


    synchronized public Tracker getDefaultTracker()
    {
        if (mTracker == null)
        {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
            mTracker.enableAutoActivityTracking(true);
            mTracker.enableExceptionReporting(true);
        }
        return mTracker;
    }
}
