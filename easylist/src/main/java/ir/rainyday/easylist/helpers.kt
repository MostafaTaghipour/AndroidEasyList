package ir.rainyday.easylist

import android.os.Handler
import android.os.Looper


internal fun runAsync(action: () -> Unit) = Thread(Runnable(action)).start()

internal fun runOnUiThread(action: () -> Unit) {
    if (isMainLooperAlive()) {
        action()
    } else {
        Handler(Looper.getMainLooper()).post(Runnable(action))
    }
}

private fun isMainLooperAlive() = Looper.myLooper() == Looper.getMainLooper()

