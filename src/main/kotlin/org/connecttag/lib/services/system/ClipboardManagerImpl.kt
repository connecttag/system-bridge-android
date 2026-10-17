package org.connecttag.lib.services.system

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
public class ClipboardManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : IClipboardManager {

    private val clipboardManager: ClipboardManager by lazy {
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    override fun copyText(label: String, text: String, showToast: Boolean) {
        val clipData = ClipData.newPlainText(label, text)
        clipboardManager.setPrimaryClip(clipData)

        if (showToast) {
            Toast.makeText(context, "Copied: $text", Toast.LENGTH_SHORT).show()
        }
    }

    override fun pasteText(): CharSequence? {
        if (!clipboardManager.hasPrimaryClip()) return null
        return clipboardManager.primaryClip?.getItemAt(0)?.text
    }

    override fun clear(showToast: Boolean) {
        val clipData = ClipData.newPlainText("", "")
        clipboardManager.setPrimaryClip(clipData)

        if (showToast) {
            Toast.makeText(context, "Clipboard cleared", Toast.LENGTH_SHORT).show()
        }
    }
}
