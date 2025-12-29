package org.fehse.intersection

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest

data class ErrorResponse(
    val timestamp: Long,
    val status: Int,
    val error: String,
    val message: String,
    val path: String,
)

@ControllerAdvice
class GlobalExceptionHandler {

    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(Exception::class)
    fun handleGlobalException(
        exception: Exception,
        request: WebRequest,
    ): ResponseEntity<ErrorResponse> {
        val errorResponse =
            ErrorResponse(
                timestamp = System.currentTimeMillis(),
                status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
                error = "Internal Server Error",
                message = exception.message ?: "An unexpected error occurred.",
                path = request.getDescription(false).replace("uri=", ""),
            )
        logger.error("Exception caught while handling request", exception)
        return ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(
        exception: MethodArgumentNotValidException,
        request: WebRequest,
    ): ResponseEntity<ErrorResponse> {
        val errorResponse =
            ErrorResponse(
                timestamp = System.currentTimeMillis(),
                status = HttpStatus.BAD_REQUEST.value(),
                error = "Validation Error",
                message =
                    exception.bindingResult.fieldErrors.joinToString { error ->
                        "${error.field}: ${error.defaultMessage ?: "NO MESSAGE"}\n"
                    },
                path = request.getDescription(false).replace("uri=", ""),
            )
        logger.error("Validation failed while handling request", exception)
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }
}
