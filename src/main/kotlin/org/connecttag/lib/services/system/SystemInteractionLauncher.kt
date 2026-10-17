package org.connecttag.lib.services.system

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import android.provider.Settings
import android.widget.Toast
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

public interface SystemInteractionLauncher {
    public fun openWeb(url: String)
    public fun openUri(uri: Uri)
    public fun openApplicationDetailsSettings()
    public fun copyToClipboard(value: String, label: String = "label", toastMessage: String? = null)
    public fun shareText(text: String, chooserTitle: String? = null)
    public fun sendEmail(to: String, subject: String = "", body: String = "", missingAppMessage: String? = null)
    public fun makeCall(number: String)
    public fun addContact(number: String, name: String)
    public fun sendSms(phoneNumber: String, message: String)
    public fun shareWithPackage(packageName: String, message: String)
}

public class AndroidSystemInteractionLauncher @Inject constructor(
    @ApplicationContext private val context: Context,
) : SystemInteractionLauncher {

    override fun openWeb(url: String) {
        openUri(Uri.parse(url))
    }

    override fun openUri(uri: Uri) {
        runCatching {
            context.startActivity(Intent(Intent.ACTION_VIEW, uri).withNewTaskIfNeeded())
        }
    }

    override fun openApplicationDetailsSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }
        context.startActivity(intent.withNewTaskIfNeeded())
    }

    override fun copyToClipboard(value: String, label: String, toastMessage: String?) {
        if (value.isBlank()) return
        ClipboardManagerImpl(context).copyText(label = label, text = value, showToast = false)
        toastMessage?.let { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
    }

    override fun shareText(text: String, chooserTitle: String?) {
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        context.startActivity(Intent.createChooser(sendIntent, chooserTitle).withNewTaskIfNeeded())
    }

    override fun sendEmail(to: String, subject: String, body: String, missingAppMessage: String?) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent.withNewTaskIfNeeded())
        } else {
            missingAppMessage?.let { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
        }
    }

    override fun makeCall(number: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", number, null))
        context.startActivity(intent.withNewTaskIfNeeded())
    }

    override fun addContact(number: String, name: String) {
        val intent = Intent(Intent.ACTION_INSERT).apply {
            type = ContactsContract.Contacts.CONTENT_TYPE
            putExtra(ContactsContract.Intents.Insert.NAME, name)
            putExtra(ContactsContract.Intents.Insert.PHONE, number)
        }
        runCatching { context.startActivity(intent.withNewTaskIfNeeded()) }
    }

    override fun sendSms(phoneNumber: String, message: String) {
        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:$phoneNumber")).apply {
            putExtra("sms_body", message)
        }
        runCatching { context.startActivity(intent.withNewTaskIfNeeded()) }
    }

    override fun shareWithPackage(packageName: String, message: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            setPackage(packageName)
            putExtra(Intent.EXTRA_TEXT, message)
        }
        runCatching { context.startActivity(intent.withNewTaskIfNeeded()) }
    }

    private fun Intent.withNewTaskIfNeeded(): Intent {
        if (context !is android.app.Activity) addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        return this
    }
}

public object SystemInteractionsUtils {
    public object OpenWebAppUtils {
        public fun openWeb(context: Context, url: String) {
            AndroidSystemInteractionLauncher(context).openWeb(url)
        }

        public fun openUrlByUri(context: Context, uri: Uri) {
            AndroidSystemInteractionLauncher(context).openUri(uri)
        }
    }

    public object AppPackageUtils {
        public fun Context.openSystemSettings() {
            AndroidSystemInteractionLauncher(this).openApplicationDetailsSettings()
        }

        public fun openInstagramPage(context: Context, url: String) {
            OpenWebAppUtils.openWeb(context, "https://instagram.com/$url")
        }

        public fun openFacebookPage(context: Context) {
            OpenWebAppUtils.openWeb(context, "https://www.facebook.com")
        }

        public fun openTelegram(context: Context, phoneNumber: String, message: String) {
            OpenWebAppUtils.openWeb(context, "https://t.me/share/url?text=$message&phone=$phoneNumber")
        }
    }

    public object ClipboardUtils {
        public fun copyToClipboard(context: Context, value: String, label: String = "label") {
            AndroidSystemInteractionLauncher(context).copyToClipboard(
                value = value,
                label = label,
                toastMessage = "تم النسخ",
            )
        }
    }

    public object ShareUtils {
        public fun shareText(context: Context, text: String) {
            AndroidSystemInteractionLauncher(context).shareText(text, chooserTitle = "Share via")
        }

        public val shareApp: (Context, String) -> Unit = { ctx, txt ->
            AndroidSystemInteractionLauncher(ctx).shareText(txt)
        }

        public val shareNote: (Context, String, String, () -> Unit) -> Unit = { ctx, title, description, then ->
            AndroidSystemInteractionLauncher(ctx).shareText("$title\n\n$description")
            then()
        }

        public fun shareWithWhatsapp(context: Context, message: String) {
            AndroidSystemInteractionLauncher(context).shareWithPackage("com.whatsapp", message)
        }

        public fun shareWithTelegram(context: Context, message: String) {
            AndroidSystemInteractionLauncher(context).shareWithPackage("org.telegram.messenger", message)
        }

        public fun shareWithFacebook(context: Context, message: String) {
            AndroidSystemInteractionLauncher(context).shareWithPackage("com.facebook.orca", message)
        }
    }

    public object EmailUtils {
        public fun sendEmailByGmail(context: Context, email: String) {
            sendEmail(context, email)
        }

        public fun sendEmail(context: Context, to: String, subject: String = "", body: String = "") {
            AndroidSystemInteractionLauncher(context).sendEmail(
                to = to,
                subject = subject,
                body = body,
                missingAppMessage = "No email app found!",
            )
        }

        public fun openEmailDraft(context: Context, to: String, subject: String = "", body: String = "") {
            sendEmail(context, to, subject, body)
        }

        public val mailTo: (Context, String) -> Unit = { ctx, to ->
            sendEmail(ctx, to)
        }
    }

    public object CallUtils {
        public fun makeCall(context: Context, phoneNumber: String) {
            AndroidSystemInteractionLauncher(context).makeCall(phoneNumber)
        }

        public fun makeCallToNumber(context: Context, number: String) {
            AndroidSystemInteractionLauncher(context).makeCall(number)
        }

        public val callNumber: (Context, String) -> Unit = { ctx, number ->
            AndroidSystemInteractionLauncher(ctx).makeCall(number)
        }
    }

    public object ContactsUtils {
        public fun addNewContacts(context: Context, number: String, name: String) {
            AndroidSystemInteractionLauncher(context).addContact(number, name)
        }
    }

    public object SMSUtils {
        public fun sendSMSToNumberWithMessage(context: Context, phoneNumber: String, message: String) {
            AndroidSystemInteractionLauncher(context).sendSms(phoneNumber, message)
        }

        public fun sendSMS(context: Context, textMessage: String) {
            AndroidSystemInteractionLauncher(context).shareText(textMessage)
        }
    }
}
