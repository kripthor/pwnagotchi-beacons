package xyz.kripthor.pwnagotchibeacons;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;
import java.util.List;

public class BeaconsReceiver extends BroadcastReceiver {

    private WifiManager wifiManager;
    private PwnagotchiState state;
    private long lastUpdate;

    public BeaconsReceiver(WifiManager wm, PwnagotchiState state) {
        this.wifiManager = wm;
        this.state = state;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        List<ScanResult> results = wifiManager.getScanResults();

        for (ScanResult scanResult : results) {
           // Log.d("Beacons",scanResult.BSSID + " - " + scanResult.SSID+" - freq: "+scanResult.frequency);
            String pwnTag = scanResult.BSSID.substring(3,8);
            if (pwnTag.equals("13:37")) {
                //Probably a Pwnagotchi beacon
                //TODO: implement more tests
                long now = System.currentTimeMillis();
                Log.d("Beacons", "pwnagotchi detected :) -> " + scanResult.BSSID + " | last update was "+((now-lastUpdate)/1000)+"secs ago.");
                state.setState(scanResult.SSID, scanResult.frequency);
                lastUpdate = now;
            }
        }
    };

   }
