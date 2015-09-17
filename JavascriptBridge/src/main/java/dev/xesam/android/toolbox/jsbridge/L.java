package dev.xesam.android.toolbox.jsbridge;

import android.util.Log;

/**
 * Created by xesamguo@gmail.com on 9/17/15.
 */
class L {
    public static void log(String tag, String string) {
        Log.d(tag, string == null ? "null" : string);
    }
}
