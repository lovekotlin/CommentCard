package com.example.commentcard.data.remote

import java.io.IOException
import java.net.SocketTimeoutException

/**
 * A custom exception to indicate that the API returned a non-successful response (e.g., 404, 500).
 * @param code The HTTP status code.
 */
class APIException(val code: Int) : IOException("API Error Response: $code")

/**
 * A custom exception to indicate a generic network failure, typically meaning no internet connection.
 */
class NoConnectivityException : IOException("No connectivity.")

/**
 * A custom exception for when a network request times out.
 */
class TimeoutException : SocketTimeoutException("Request timed out.")

/**
 * A custom exception for when the server response cannot be parsed into our data models.
 */
class DataParsingException : Exception("Error parsing data.")