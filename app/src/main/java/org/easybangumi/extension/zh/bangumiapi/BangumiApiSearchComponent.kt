package org.easybangumi.extension.zh.bangumiapi

import com.heyanle.bangumi_source_api.api.SourceResult
import com.heyanle.bangumi_source_api.api.component.ComponentWrapper
import com.heyanle.bangumi_source_api.api.component.search.SearchComponent
import com.heyanle.bangumi_source_api.api.entity.CartoonCover
import com.heyanle.bangumi_source_api.api.entity.CartoonCoverImpl
import com.heyanle.bangumi_source_api.api.withResult
import kotlinx.coroutines.Dispatchers

class BangumiApiSearchComponent(source: BangumiApiSource) : ComponentWrapper(source), SearchComponent {
    override fun getFirstSearchKey(keyword: String): Int {
        return 0
    }

    override suspend fun search(
        pageKey: Int,
        keyword: String
    ): SourceResult<Pair<Int?, List<CartoonCover>>> {
        return withResult(Dispatchers.IO) {
            val doc = getJson("/search/subject/$keyword?type=2&responseGroup=small&max_results=20&start=$pageKey")
                .getOrElse { throw it }
            val next = if (doc["results"].asInt > pageKey+20) pageKey+20 else null
            val list = arrayListOf<CartoonCover>()

            doc["list"].asJsonArray.forEach{
                val nameCN = it["name_cn"].asString

                list.add(
                    CartoonCoverImpl(
                        id = it["id"].asString,
                        source = this.source.key,
                        url = it["url"].asString,
                        title = nameCN.ifEmpty { it["name"].asString },
                        intro = "",
                        coverUrl = if (it["images"].isJsonNull) "" else it["images"]["common"].asString,
                    )
                )
            }

            return@withResult next to list
        }
    }
}