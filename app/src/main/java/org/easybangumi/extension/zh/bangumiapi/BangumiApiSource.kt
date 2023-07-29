package org.easybangumi.extension.zh.bangumiapi

import com.heyanle.bangumi_source_api.api.component.Component
import com.heyanle.extension_api.ExtensionIconSource
import com.heyanle.extension_api.ExtensionSource

class BangumiApiSource : ExtensionSource(), ExtensionIconSource  {
    override fun getIconResourcesId(): Int {
        return R.drawable.app_icon
    }

    override val describe: String?
        get() = "Bangumi 是由 Sai 于桂林发起的 ACG 分享与交流项目，致力于让阿宅们在欣赏ACG作品之余拥有一个轻松便捷独特的交流与沟通环境。"
    override val label: String
        get() = "番组计划"
    override val version: String
        get() = "1.0"
    override val versionCode: Int
        get() = 1
    override val sourceKey: String
        get() = "bangumiapi"

    override fun components(): List<Component> {
        return listOf(
            BangumiApiPageComponent(this),
            BangumiApiDetailedComponent(this),
            BangumiApiSearchComponent(this,)
        )
    }
}