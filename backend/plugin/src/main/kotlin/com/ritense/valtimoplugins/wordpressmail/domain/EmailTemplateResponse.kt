package com.ritense.valtimoplugins.wordpressmail.domain

import com.fasterxml.jackson.annotation.JsonProperty

data class EmailTemplateResponse(
    val success: Boolean? = false,
    val message: String?,
    val emails: List<EmailTemplate>,
) {
    data class EmailTemplate(
        val id: String,
        @JsonProperty("post_title") val postTitle: String,
    )
}
