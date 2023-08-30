package com.rommansabbir.rickmortyapp.utils.extensions

val stringBuilderLazyInstance by lazy { StringBuilder() }

/**
 * Write a string with [StringBuilder]
 *
 * @param body Body to be executed which return an instance of [StringBuilder]
 */
inline fun Any.writeString(crossinline body: (builder: StringBuilder) -> Unit): String {
    return stringBuilderLazyInstance.apply {
        clear()
        body.invoke(this)
    }.toString()
}
