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

package com.ritense.valtimoplugins.wordpressmail.client

import com.ritense.valtimoplugins.wordpressmail.domain.EmailSendRequest
import com.ritense.valtimoplugins.wordpressmail.domain.EmailSendResponse
import com.ritense.valtimoplugins.wordpressmail.domain.EmailTemplateResponse
import org.springframework.core.io.Resource
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.MULTIPART_FORM_DATA
import org.springframework.http.client.MultipartBodyBuilder
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

class WordpressMailPluginClient(
    private val wordpressMailRestClientBuilder: RestClient.Builder
) {
    private var wordpressMailProperties: WordpressMailProperties? = null

    /**
     * Sends email
     *
     * @param attachments The email-attachment. Note: Any attachment-resource MUST have a fileName WITH extension. The fileName-extension determines the file-type.
     */
    fun send(
        emailTemplateId: String,
        emailRequest: EmailSendRequest,
        attachments: List<Resource>?
    ): EmailSendResponse {
        val builder = MultipartBodyBuilder()
        builder.part("request", emailRequest, APPLICATION_JSON)
        attachments?.forEach { builder.part("attachments[]", it) }

        return restClient()
            .post()
            .uri { it.pathSegment("wp-json", "email", "v1", "send", "{emailTemplateId}").build(emailTemplateId) }
            .contentType(MULTIPART_FORM_DATA)
            .body(builder.build())
            .retrieve()
            .body<EmailSendResponse>()!!
    }

    fun getEmailTemplates(): EmailTemplateResponse {
        return restClient()
            .get()
            .uri { it.pathSegment("wp-json", "email", "v1", "get").build() }
            .retrieve()
            .body<EmailTemplateResponse>()!!
    }

    fun setProperties(wordpressMailProperties: WordpressMailProperties) {
        this.wordpressMailProperties = wordpressMailProperties
    }

    private fun restClient(): RestClient {
        return wordpressMailRestClientBuilder
            .clone()
            .baseUrl(wordpressMailProperties!!.properties["baseUrl"].toString())
            .build()
    }

}
