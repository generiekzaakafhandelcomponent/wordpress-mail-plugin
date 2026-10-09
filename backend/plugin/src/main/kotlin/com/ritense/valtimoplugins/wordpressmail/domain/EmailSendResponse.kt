package com.ritense.valtimoplugins.wordpressmail.domain

import com.ritense.valtimo.contract.basictype.EmailAddress
import com.ritense.valtimo.contract.mail.model.MailMessageStatus

data class EmailSendResponse(
    val success: Boolean? = false,
    val message: String?,
    val data: Data,
) {
    data class Data(
        val recipient: String,
        val subject: String,
        val content: String,
    )

    fun toMailMessageStatus(): MailMessageStatus {
        return MailMessageStatus
            .with(getRecipientEmailAddress(), "SENT", hashCode().toString())
            .build()
    }

    private fun getRecipientEmailAddress(): EmailAddress {
        return EmailAddress.from(Regex("<(.+?)>").find(data.recipient)!!.groupValues[1])
    }
}
