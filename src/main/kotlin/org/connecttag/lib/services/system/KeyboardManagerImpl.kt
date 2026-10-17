package org.connecttag.lib.services.system

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import java.util.WeakHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
public class KeyboardManagerImpl @Inject constructor() : IKeyboardManager {

    private val listeners = WeakHashMap<Activity, ViewTreeObserver.OnGlobalLayoutListener>()

    override fun attachDismissListener(activity: Activity, onDismiss: () -> Unit) {
        val rootView = activity.findViewById<View>(android.R.id.content)
        val listener = object : ViewTreeObserver.OnGlobalLayoutListener {
            private var isKeyboardClosed: Boolean = false
            override fun onGlobalLayout() {
                val r = Rect()
                rootView.getWindowVisibleDisplayFrame(r)
                val screenHeight = rootView.rootView.height
                val keypadHeight = screenHeight - r.bottom
                if (keypadHeight > screenHeight * 0.15) {
                    isKeyboardClosed = false
                } else if (!isKeyboardClosed) {
                    isKeyboardClosed = true
                    onDismiss.invoke()
                }
            }
        }
        listeners[activity] = listener
        rootView.viewTreeObserver.addOnGlobalLayoutListener(listener)
    }

    override fun release(activity: Activity) {
        val rootView = activity.findViewById<View>(android.R.id.content)
        listeners.remove(activity)?.let {
            rootView.viewTreeObserver.removeOnGlobalLayoutListener(it)
        }
    }
}
