package org.example.common.exception

class CustomException(
    private val codeInterface: CodeInterface,
    private val additionalMessage: String? = null // 값이 필요할 때만 쓰기
): RuntimeException(
    if (additionalMessage == null) {
        codeInterface.message
    } else {
        "${codeInterface.message} - $additionalMessage"
    }
)