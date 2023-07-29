package org.easybangumi.extension.zh.bangumiapi

import com.heyanle.bangumi_source_api.api.SourceFactory

class Factory: SourceFactory {
    override fun create() = listOf(BangumiApiSource())
}