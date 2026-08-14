package com.exteragram.messenger.icons

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.net.Uri
import android.util.SparseArray
import androidx.core.graphics.drawable.IconCompat
import com.exteragram.messenger.ExteraConfig
import com.exteragram.messenger.IconPackType
import kotlinx.coroutines.*
import org.telegram.messenger.AndroidUtilities
import org.telegram.messenger.MessageObject
import org.telegram.ui.ActionBar.BaseFragment
import java.io.File
import java.util.concurrent.CopyOnWriteArrayList

object IconManager {
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val mutationDispatcher = Dispatchers.IO
    val activePacks = CopyOnWriteArrayList<IconPack>()
    var initializationGeneration: Long = 0L
    private var initializationJob: Job? = null
    private var prewarmJob: Job? = null
    private val resultCallbacks = SparseArray<(Uri?) -> Unit>()

    @JvmStatic
    fun getNotificationIcon(): IconCompat? {
        return null
    }

    @JvmStatic
    fun getNotificationSystemIcon(): Icon? {
        return null
    }

    @JvmStatic
    fun showIconPackError(baseFragment: BaseFragment?, error: IconPackStorageError?) {
    }

    @JvmStatic
    fun prefetchCustomPacks() {
    }

    @JvmStatic
    @JvmOverloads
    fun getDrawable(
        id: Int,
        density: Int = AndroidUtilities.displayMetrics.densityDpi,
        theme: Resources.Theme? = null
    ): Drawable? {
        return null
    }

    @JvmStatic
    fun getPackIconDrawable(pack: IconPack?, resId: Int): Drawable? {
        return null
    }

    @JvmStatic
    @JvmOverloads
    fun createBitmapFromFile(
        path: String,
        originalResId: Int,
        density: Int = AndroidUtilities.displayMetrics.densityDpi,
        theme: Resources.Theme? = null
    ): Bitmap? {
        return null
    }

    @JvmStatic
    @JvmOverloads
    fun getPackIconBitmap(
        pack: IconPack?,
        resId: Int,
        density: Int = AndroidUtilities.displayMetrics.densityDpi,
        theme: Resources.Theme? = null,
        knownResourceName: String? = null,
        cacheResult: Boolean = true
    ): Bitmap? {
        return null
    }

    @JvmStatic
    fun saveCustomIcon(packId: String, resId: Int, tempFile: File, originalName: String) {
    }

    @JvmStatic
    fun resetCustomIcon(packId: String, resId: Int) {
    }

    @JvmStatic
    fun getIcon(resId: Int): Int {
        return resId
    }

    @JvmStatic
    @JvmOverloads
    fun initialize(update: Boolean = false) {
    }

    @JvmStatic
    fun setActiveCustomPack(packId: String?) {
    }

    @JvmStatic
    fun findPackById(packId: String?): IconPack? {
        return null
    }

    @JvmStatic
    fun bundlePackBlocking(packId: String?): File? {
        return null
    }

    @JvmStatic
    fun saveIconPackMetadata(iconPack: IconPack): Boolean {
        return true
    }

    @JvmStatic
    fun deletePack(packId: String) {
    }

    @JvmStatic
    fun isIconPack(messageObject: MessageObject?): Boolean {
        return false
    }

    @JvmStatic
    fun handleIconPack(baseFragment: BaseFragment?, messageObject: MessageObject?) {
    }

    @JvmStatic
    fun handleIconPack(baseFragment: BaseFragment?, path: String?) {
    }

    @JvmStatic
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        if (requestCode == 43) {
            val callback = resultCallbacks.get(43)
            resultCallbacks.remove(43)
            if (resultCode == Activity.RESULT_OK && data != null) {
                callback?.invoke(data.data)
            } else {
                callback?.invoke(null)
            }
            return true
        }
        return false
    }

    @JvmStatic
    fun startIconPicker(activity: Activity, selectFromFiles: Boolean, callback: (Uri?) -> Unit) {
        resultCallbacks.put(43, callback)
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        activity.startActivityForResult(intent, 43)
    }

    @JvmStatic
    fun showReplaceAlert(context: Context, resId: Int, iconPack: IconPack?) {
    }

    @JvmStatic
    fun isBasePackOnly(basePackType: IconPackType): Boolean {
        return ExteraConfig.getIconPack() == basePackType && activePacks.isEmpty()
    }
}
