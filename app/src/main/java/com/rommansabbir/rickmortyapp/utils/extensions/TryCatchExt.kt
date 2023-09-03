package com.rommansabbir.rickmortyapp.utils.extensions

/**
 * A generic method to execute codes from client and return an object [T] type.
 * If exception occur return null else the object [T].
 *
 * @param T Object type.
 * @param body Code block to be executed under try catch and should return [T].
 *
 * @return [T] object can be nullable.
 */
inline fun <T> executeBodyOrReturnNull(crossinline body: () -> T): T? {
    return try {
        body.invoke()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

/**
 * Represent a null value by returning "---"
 */
fun nullString() = "---"




