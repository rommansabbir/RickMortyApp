package com.rommansabbir.rickmortyapp.base.failure.manager

import com.rommansabbir.rickmortyapp.base.failure.Failure

/**
 * Is responsible to manage failure and return the proper error message.
 */
interface FailureManager {
    fun handleFailure(failure: Failure): String
}