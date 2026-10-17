package org.connecttag.lib.services.system

import android.app.Activity

public interface IKeyboardManager {
    public fun attachDismissListener(activity: Activity, onDismiss: () -> Unit)
    public fun release(activity: Activity)
}
