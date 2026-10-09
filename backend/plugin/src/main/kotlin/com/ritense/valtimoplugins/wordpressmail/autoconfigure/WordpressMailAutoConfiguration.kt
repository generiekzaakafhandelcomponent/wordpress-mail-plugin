/*
 * Copyright 2015-2026 Ritense BV, the Netherlands.
 *
 * Licensed under EUPL, Version 1.2 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ritense.valtimoplugins.wordpressmail.autoconfigure

import com.ritense.documentenapi.service.DocumentenApiService
import com.ritense.plugin.service.PluginService
import com.ritense.valtimo.contract.mail.MailFilter
import com.ritense.valtimoplugins.wordpressmail.client.WordpressMailPluginClient
import com.ritense.valtimoplugins.wordpressmail.plugin.WordpressMailPluginFactory
import com.ritense.valtimoplugins.wordpressmail.service.WordpressMailPluginSender
import com.ritense.valtimoplugins.wordpressmail.service.WordpressMailPluginService
import com.ritense.valueresolver.ValueResolverService
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.web.client.RestClient

@AutoConfiguration
class WordpressMailAutoConfiguration {

    @Bean
    fun wordpressMailPluginClient(
        wordpressMailRestClientBuilder: RestClient.Builder
    ): WordpressMailPluginClient {
        return WordpressMailPluginClient(wordpressMailRestClientBuilder)
    }

    @Bean
    fun wordpressMailPluginFactory(
        pluginService: PluginService,
        wordpressMailPluginService: WordpressMailPluginService
    ): WordpressMailPluginFactory {
        return WordpressMailPluginFactory(
            pluginService,
            wordpressMailPluginService
        )
    }

    @Bean
    fun pluginMailSender(
        wordpressMailPluginClient: WordpressMailPluginClient,
        applicationEventPublisher: ApplicationEventPublisher,
        filters: Collection<MailFilter>
    ): WordpressMailPluginSender {
        return WordpressMailPluginSender(
            wordpressMailPluginClient,
            applicationEventPublisher,
            filters
        )
    }

    @Bean
    fun pluginMailService(
        valueResolverService: ValueResolverService,
        wordpressMailPluginSender: WordpressMailPluginSender,
        pluginService: PluginService,
        documentenApiService: DocumentenApiService
    ): WordpressMailPluginService {
        return WordpressMailPluginService(
            valueResolverService,
            wordpressMailPluginSender,
            pluginService,
            documentenApiService
        )
    }
}