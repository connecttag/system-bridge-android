package org.connecttag.lib.services.system

public interface IClipboardManager {
    public fun copyText(label: String = "Copied Text", text: String, showToast: Boolean = true)
    public fun pasteText(): CharSequence?
    public fun clear(showToast: Boolean = true)
}
