package xyz.kripthor.pwnagotchibeacons;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import android.Manifest;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST = 31337;
    private WifiManager wifiManager;
    private BroadcastReceiver wifiReceiver;
    private ScanThread scanThread;

    // TODO, add multiple states for multiple devices
    private static PwnagotchiState state = new PwnagotchiState();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiReceiver = new BeaconsReceiver(wifiManager,state);
        scanThread = new ScanThread(wifiManager);

        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(this, "WiFi is disabled ... We need to enable it", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }

        WebView webView = findViewById(R.id.webview_ui);
        WebSettings webSetting = webView.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(state,"pwnagotchiState");
        webView.loadUrl("file:///android_asset/ui.html");

        scanThread.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    scanWifi();
                } else {
                    // permission denied
                }
                return;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        scanThread.setPaused(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanThread.setPaused(true);
    }

    private void scanWifi() {
        wifiManager.startScan();
        Toast.makeText(this, "Scanning for pwnagotchi beacons ...", Toast.LENGTH_SHORT).show();
    }

    public void scanNow(View view) {
       if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST);
        } else {
            scanWifi();
        }
    }

}
