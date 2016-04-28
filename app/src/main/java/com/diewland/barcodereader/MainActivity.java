package com.diewland.barcodereader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    String TAG = "DIEWLAND";
    String SERVER_IP   = "10.4.202.76";
    String SERVER_PORT = "8000";

    private String contents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void doScan(){
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.setPackage("com.google.zxing.client.android");
        //intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

                // DEBUG
                Log.d(TAG, contents);
                Log.d(TAG, format);

                // GET
                Thread thread = new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            String url_str = "http://" + SERVER_IP + ":" + SERVER_PORT + "/" + contents;
                            Log.d(TAG, url_str);
                            new URL(url_str).openStream();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();

            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        doScan();
    }
}
