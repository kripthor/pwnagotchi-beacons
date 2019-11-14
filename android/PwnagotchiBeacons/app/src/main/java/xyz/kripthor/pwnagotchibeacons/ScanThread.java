package xyz.kripthor.pwnagotchibeacons;

import android.net.wifi.WifiManager;

import java.security.SecureRandom;
import java.util.Random;

public class ScanThread extends Thread {

    private WifiManager wifiManager;
    private boolean paused = false;
    private Random r = new SecureRandom();

    public ScanThread(WifiManager wm) {
        this.wifiManager = wm;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (!getPaused()) wifiManager.startScan();
                Thread.sleep(3000+r.nextInt(2000));
            }
            catch (Exception e) {

            }
        }
    }

    public synchronized void setPaused(boolean p) {
        paused = p;
    }
    public synchronized boolean getPaused() {
        return paused;
    }

}

