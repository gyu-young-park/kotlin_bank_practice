package org.example.common.exception

interface CodeInterface {
    val code: Int
    val message: String
}

enum class ErrorCode(
    override val code: Int,
    override val message: String
) : CodeInterface {
    AUTH_CONFIG_NOT_FOUND(-100, "auth config not found"),
    FAILED_TO_CALL_CLIENT(-101, "Failed to call client"),
    CALL_RESULT_BODY_NULL(-102, "body is nil"),
}