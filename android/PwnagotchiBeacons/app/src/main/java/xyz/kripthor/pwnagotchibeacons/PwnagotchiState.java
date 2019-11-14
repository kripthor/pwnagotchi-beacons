package xyz.kripthor.pwnagotchibeacons;

import android.icu.text.LocaleDisplayNames;
import android.util.Base64;
import android.util.Log;
import android.webkit.JavascriptInterface;

import java.util.Arrays;

public class PwnagotchiState {
    private int apsCurrentChannel;
    private int apsTotalAllChannels;
    private int uptime;
    private int face;
    private String status;
    private int pwned;
    private int pwnedTotal;
    private int mode;
    private int channel;
    private String hostname;
    private long lastUpdate;
    private boolean initialized = false;

    //  private String faces[] = {"( ⚆_⚆)", "(☉_☉ )", "( ◕‿◕)", "(◕‿◕ )", "(⇀‿‿↼)", "(≖‿‿≖)", "(◕‿‿◕)", "(-__-)", "(°▃▃°)", "(⌐■_■)", "(•‿‿•)", "(^‿‿^)", "(ᵔ◡◡ᵔ)", "(☼‿‿☼)", "(≖__≖)", "(✜‿‿✜)", "(ب__ب)", "(╥☁╥ )", "(-_-)", "(♥‿‿♥)", "(☓‿‿☓)", "(#__#)"};
    private String faces[] = {"( ⚆_⚆)", "(⚆_⚆ )", "( ◕◡◕)", "(◕◡◕ )", "( ⇀◡↼)", "( ≖◡≖)", "(◕◡-◕)", "(-__-)", "(°▃▃°)", "(⌐■_■)", "(•◡• )", "(^◡^ )", "(^u^) ", "(☼◡☼ )", "(≖__≖)", "(✜◡✜ )", "(ب__ب)", "(╥☁╥ )", "(-_-')", "(♥◡♥ )", "(☓__☓)", "(#__#)"};
    private String facemeaning[] = {"Looking around...", "Looking over there...", "Happily looking around...", "Happily looking over there...", "I'm sleepy...", "zzzZZZZz zZZZzz zzzZ ZzzzzZz", "I'm awake!", "I'm bored :/", "That was intense!", "Feeling good!", "Happy pwnagotchi is happy.", "Ain't life great?!", "Oh boy!", "Let's do this!", "Bummer...", "I'm concentrated.", "I'm so lonely...", "I'm sad.", "Get me out! Let's go!", "I've meet a new friend!", "I'm broken in the inside!", "!Matrix mode!"};
    private final int REPORT = 2412;
    private final int INFO = 2417;
    private final int PEERS = 2422;
    //TODO -> PEERS

    @JavascriptInterface
    public synchronized String getState() {
        String json = "{";
        json += toJsonField("aps",apsCurrentChannel+" ("+apsTotalAllChannels+")")+",";
        String c = Integer.toString(channel);
        if (channel == 63) c = "*";
        if (channel == 62) c = "-";
        json += toJsonField("initialised",initialized ? "true": "false")+",";
        json += toJsonField("channel",c)+",";
        json += toJsonField("pwnd_run",Integer.toString(pwned))+",";
        json += toJsonField("pwnd_tot",Integer.toString(pwnedTotal))+",";
        json += toJsonField("face",faces[face])+",";
        json += toJsonField("status",facemeaning[face])+",";

        switch (mode) {
            case 0:
                json += toJsonField("mode", "MANU")+",";
                break;
            case 128:
                json += toJsonField("mode", "AI")+",";
                break;
            case 64:
                json += toJsonField("mode", "AUTO")+",";
                break;
            default:
                json += toJsonField("mode","UNKN")+",";
                break;

        }
        json += toJsonField("uptime",formatSeconds(uptime))+",";
        json += toJsonField("name",hostname)+"}";
        return json;
    }

    public synchronized void setState(String base64state, int frequency) {
        try {
            byte content[] = Base64.decode(base64state, Base64.DEFAULT);
            Log.d("Pwnagotchi setState",byteArrayToHex(content));
            switch (frequency) {
                case REPORT:
                    apsCurrentChannel = content[0] & 0xFF;
                    apsTotalAllChannels = ((content[1] & 0xFF) << 8) + (content[2] & 0xFF);
                    pwned = ((content[3] & 0xFF) << 8) + (content[4] & 0xFF);
                    pwnedTotal = ((content[5] & 0xFF) << 8) + (content[6] & 0xFF);
                    uptime = ((content[7] & 0xFF) << 24) + ((content[8] & 0xFF) << 16) + ((content[9] & 0xFF) << 8) + (content[10] & 0xFF);
                    face = Math.min((content[11] & 0xFF),faces.length-1);
                    mode = (content[12] & 0xFF) & 0xC0;
                    channel = (content[12] & 0xFF) & 0x3F;
                    hostname = new String(Arrays.copyOfRange(content,13,content.length));
                    break;

                default:
                    break;
            }
            initialized = true;
        } catch (Exception e) {
            Log.e("Pwnagotchi setState",e.getMessage());
        }
    }

    private String toJsonField(String name, String value) {
        return "\""+name+"\":\""+value+"\"";
    }

    private String formatSeconds(int secs){
        return String.format("%02d",secs/3600)+":"+String.format("%02d",secs/60%60)+":" + String.format("%02d",secs%60);
    }

    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
