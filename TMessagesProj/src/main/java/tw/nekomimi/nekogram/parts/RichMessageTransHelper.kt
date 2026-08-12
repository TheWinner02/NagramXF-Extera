package tw.nekomimi.nekogram.parts

import org.telegram.messenger.MessageObject

object RichMessageTransHelper {
    @JvmStatic
    fun isTranslated(messageObject: MessageObject?): Boolean = false

    @JvmStatic
    fun getTranslatedLanguage(messageObject: MessageObject?): String? = null

    @JvmStatic
    fun getCachedTranslation(messageObject: MessageObject?, plainText: String?): String? = null

    @JvmStatic
    fun collectPlainTexts(richMessage: Any?): List<String> = emptyList()

    @JvmStatic
    fun hasFullCache(richMessage: Any?, targetLanguage: String?): Boolean = false
}
