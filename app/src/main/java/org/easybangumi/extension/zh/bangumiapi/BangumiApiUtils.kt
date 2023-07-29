package org.easybangumi.extension.zh.bangumiapi

import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonParser
import com.heyanle.lib_anim.utils.SourceUtils
import com.heyanle.lib_anim.utils.network.GET
import com.heyanle.lib_anim.utils.network.networkHelper
import okhttp3.Headers

var ROOT_URL = "https://api.bgm.tv"

fun getJson(target: String): Result<JsonElement> {
    return runCatching {
        val req = networkHelper.client.newCall(
            GET(url(target),
                Headers.Builder()
                    .add("User-Agent","EasyBangumi/4.1.0 (Android;API 1;lib 1.2-SNAPSHOT) EasyBangumi/bangumiapi/1.0 (Android;Extension)")
                    .build()
            )
        ).execute()
        val body = req.body!!.string()
        JsonParser.parseString(body)
    }
}

fun url(source: String): String {
    return SourceUtils.urlParser(ROOT_URL, source)
}

operator fun JsonElement.get(index: Int): JsonElement {
    return this.asJsonArray[index]!!
}

operator fun JsonElement.get(key: String): JsonElement {
    return this.asJsonObject[key]!!
}