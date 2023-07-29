package org.easybangumi.extension.zh.bangumiapi

import com.heyanle.bangumi_source_api.api.SourceResult
import com.heyanle.bangumi_source_api.api.component.ComponentWrapper
import com.heyanle.bangumi_source_api.api.component.detailed.DetailedComponent
import com.heyanle.bangumi_source_api.api.entity.Cartoon
import com.heyanle.bangumi_source_api.api.entity.CartoonImpl
import com.heyanle.bangumi_source_api.api.entity.CartoonSummary
import com.heyanle.bangumi_source_api.api.entity.PlayLine
import com.heyanle.bangumi_source_api.api.withResult
import kotlinx.coroutines.Dispatchers

class BangumiApiDetailedComponent(source: BangumiApiSource) : ComponentWrapper(source), DetailedComponent {

    override suspend fun getAll(summary: CartoonSummary): SourceResult<Pair<Cartoon, List<PlayLine>>> {
        return withResult(Dispatchers.IO) {
            getDetailByID(summary.id) to arrayListOf()
        }
    }

    override suspend fun getDetailed(summary: CartoonSummary): SourceResult<Cartoon> {
        return withResult(Dispatchers.IO) {
            getDetailByID(summary.id)
        }
    }

    override suspend fun getPlayLine(summary: CartoonSummary): SourceResult<List<PlayLine>> {
        return withResult(Dispatchers.IO) {
            arrayListOf()
        }
    }

    private fun getDetailByID(id: String): Cartoon {
        val doc = getJson("/v0/subjects/$id").getOrElse { throw it }

        val nameCN = doc["name_cn"].asString
        val score = doc["rating"]["score"].asString

        val tags = arrayListOf<String>()
        doc["tags"].asJsonArray.forEachIndexed { i, it ->
            if (i > 10) return@forEachIndexed
            tags.add(it["name"].asString)
        }

        return CartoonImpl(
            id = id,
            url = "http://bgm.tv/subject/$id",
            source = this.source.key,

            title = nameCN.ifEmpty { doc["name"].asString },
            coverUrl = if (doc["images"].isJsonNull) "" else doc["images"]["common"].asString,

            intro = "bgm "+if (score.equals("0")) "未评分" else score,
            description = doc["summary"].asString,

            genre = tags.joinToString(","),

            status = Cartoon.STATUS_UNKNOWN,
            updateStrategy = Cartoon.UPDATE_STRATEGY_NEVER
        )
    }
}