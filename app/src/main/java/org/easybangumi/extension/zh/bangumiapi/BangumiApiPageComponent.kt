package org.easybangumi.extension.zh.bangumiapi

import com.heyanle.bangumi_source_api.api.component.ComponentWrapper
import com.heyanle.bangumi_source_api.api.component.page.PageComponent
import com.heyanle.bangumi_source_api.api.component.page.SourcePage
import com.heyanle.bangumi_source_api.api.entity.CartoonCover
import com.heyanle.bangumi_source_api.api.entity.CartoonCoverImpl
import com.heyanle.bangumi_source_api.api.withResult
import kotlinx.coroutines.Dispatchers

class BangumiApiPageComponent(source: BangumiApiSource): ComponentWrapper(source), PageComponent {
    override fun getPages(): List<SourcePage> {
        return  listOf(
            // 新番时刻表
            SourcePage.Group(
                "每日更新列表",
                false,
            ) {
                withResult(Dispatchers.IO) {
                    CalendarGroup()
                }
            },
        )
    }

    // 功能函数
    fun CalendarGroup(): List<SourcePage.SingleCartoonPage> {
        val group = arrayListOf<SourcePage.SingleCartoonPage>()
        val doc = getJson("/calendar").getOrElse { throw it }
        doc.asJsonArray.forEach{
            val items = arrayListOf<CartoonCover>()
            it["items"].asJsonArray.forEach{item ->
                val nameCN = item["name_cn"].asString

                items.add(CartoonCoverImpl(
                    id = item["id"].asString,
                    source = this.source.key,
                    url = item["url"].asString,
                    title = nameCN.ifEmpty { item["name"].asString },
                    intro = item["summary"].asString,
                    coverUrl = if (item["images"].isJsonNull) "" else item["images"]["common"].asString,
                ))
            }
            group.add(SourcePage.SingleCartoonPage.WithCover(
                label = it["weekday"]["cn"].asString,
                firstKey = {1},
            ) {
                withResult(Dispatchers.IO) {
                    null to items
                }
            })

        }
        return group
    }
}