package com.ritense.valtimoplugins.wordpressmail.domain

import org.springframework.core.io.ByteArrayResource

class NamedByteArrayResource(
    private val fileName: String,
    byteArray: ByteArray
) : ByteArrayResource(byteArray, null) {

    override fun getFilename(): String {
        return fileName
    }
}
