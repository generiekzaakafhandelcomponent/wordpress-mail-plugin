package com.ritense.valtimoplugins.wordpressmail.domain

import com.ritense.valtimo.contract.mail.model.TemplatedMailMessage
import com.ritense.valtimo.contract.mail.model.value.Recipient
import com.ritense.valtimo.contract.mail.model.value.Subject
import io.github.oshai.kotlinlogging.KotlinLogging

data class EmailSendRequest(
    val variables: Map<String, Any>?,
    val to: String
) {
    companion object {
        fun from(templatedMailMessage: TemplatedMailMessage): List<EmailSendRequest> {
            if (templatedMailMessage.recipients.get().isEmpty()) {
                logger.warn { "No recipient found for email: ${templatedMailMessage.templateIdentifier}" }
            }

            val variables = toVariablesField(templatedMailMessage.placeholders, templatedMailMessage.subject)
            return templatedMailMessage.recipients.filterTo()
                .map { EmailSendRequest(variables, toToField(it)) }
        }

        private fun toToField(recipient: Recipient): String {
            return "${recipient.name.get()} <${recipient.email.get()}>"
        }

        private fun toVariablesField(placeholders: Map<String, Any>?, subject: Subject): Map<String, Any> {
            val variables = toVariablesField("", placeholders).toMap().toMutableMap()
            if (subject.isPresent) {
                variables["SUBJECT"] = subject.get()
            }
            return variables
        }

        private fun toVariablesField(prefix: String, placeholders: Map<String, Any>?): List<Pair<String, Any>> {
            val entries = mutableListOf<Pair<String, Any>>()
            placeholders?.entries?.forEach {
                val key = "$prefix${toVariableKey(it.key)}"
                val value = it.value
                when {
                    value is Map<*, *> -> {
                        @Suppress("UNCHECKED_CAST")
                        entries.addAll(toVariablesField("${key}_", value as Map<String, Any>))
                    }
                    value is String || value is Number || value is Boolean -> entries.add(Pair(key, value))
                }
            }
            return entries
        }

        /* Wordpress Mail placeholder-key can only handle uppercase letters and underscore. So, no lower case and no numbers */
        private fun toVariableKey(placeholderKey: String): String {
            val snakeCase = placeholderKey.replace(Regex("([a-z])([A-Z]+)"), "\$1_\$2")
            val upperCase = snakeCase.uppercase()
            return upperCase.replace(Regex("[^A-Z_]"), "_")
        }

        private val logger = KotlinLogging.logger {}
    }
}
