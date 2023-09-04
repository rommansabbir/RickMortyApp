package com.rommansabbir.rickmortyapp.base.failure

/**
 * Class that represent any kind of managed error that occurs during the application runtime.
 * Here we can define the HTTP Errors, Caching Error, Feature Error and so on.
 */
sealed class Failure {
    data class ActualException(val exception: Throwable) : Failure()

    /**
     * Define all HTTP error here.
     */
    object HTTP {
        object Forbidden : Failure()
        object NotFound : Failure()
        object MethodNotAllowed : Failure()
        object NetworkConnection : Failure()
        object UnauthorizedError : Failure()
        object BadRequest : Failure()
        object CanNotConnectToTheServer : Failure()
        object TooManyRequest : Failure()
        object InternalServerError : Failure()
    }

    object LocalCache {
        object NotExistInCache : Failure()
        data class FailedToCache(val message: String) : Failure()
    }

    /*
    We can also manage feature specific errors here.
    The main goal is to have a central error channel where all manged errors are defined.
     */
    /*object FeatureAFailure{
        object FeatureAFailure1 : Failure()
        object FeatureAFailure2 : Failure()
    }

    object FeatureBFailure{
        object FeatureBFailure1 : Failure()
        object FeatureBFailure2 : Failure()
    }*/
}