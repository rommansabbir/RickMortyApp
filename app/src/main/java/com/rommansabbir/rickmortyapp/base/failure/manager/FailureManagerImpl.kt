package com.rommansabbir.rickmortyapp.base.failure.manager

import android.content.Context
import com.rommansabbir.rickmortyapp.R
import com.rommansabbir.rickmortyapp.base.failure.Failure
import javax.inject.Inject

class FailureManagerImpl @Inject constructor(private val context: Context) : FailureManager {
    override fun handleFailure(failure: Failure): String = when (failure) {
        is Failure.ActualException -> failure.exception.message.toString()
        is Failure.HTTP.BadRequest -> context.getString(R.string.failure_bad_request)
        is Failure.HTTP.CanNotConnectToTheServer -> context.getString(R.string.faliure_can_not_connect_to_the_server)
        is Failure.LocalCache.FailedToCache -> context.getString(R.string.failure_failed_to_cache)
        is Failure.HTTP.Forbidden -> context.getString(R.string.failure_forbidden_request)
        is Failure.HTTP.InternalServerError -> context.getString(R.string.failure_internal_server_error)
        is Failure.HTTP.MethodNotAllowed -> context.getString(R.string.failure_method_not_allowed)
        is Failure.HTTP.NetworkConnection -> context.getString(R.string.failure_no_internet_available)
        is Failure.LocalCache.NotExistInCache -> context.getString(R.string.failure_not_exist_in_cache)
        is Failure.HTTP.NotFound -> context.getString(R.string.failure_not_found)
        is Failure.HTTP.TooManyRequest -> context.getString(R.string.failure_too_many_request)
        is Failure.HTTP.UnauthorizedError -> context.getString(R.string.failure_unauthorized_error)
    }
}