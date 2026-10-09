package com.ritense.valtimoplugins.wordpressmail.plugin

import com.ritense.plugin.PluginFactory
import com.ritense.plugin.service.PluginService
import com.ritense.valtimoplugins.wordpressmail.service.WordpressMailPluginService

class WordpressMailPluginFactory(
    pluginService: PluginService,
    private val wordpressMailPluginService: WordpressMailPluginService
) : PluginFactory<WordpressMailPlugin>(pluginService) {

    override fun create(): WordpressMailPlugin {
        return WordpressMailPlugin(
            wordpressMailPluginService
        )
    }
}