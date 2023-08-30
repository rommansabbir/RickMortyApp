package com.rommansabbir.rickmortyapp.base.failure

/**
 * Class that represent any kind of managed error that occurs during the application runtime.
 */
sealed class Failure {
    data class ActualException(val exception: Exception) : Failure()
    object NetworkConnection : Failure()
    object UnauthorizedError : Failure()
    object CanNotConnectToTheServer : Failure()
    object TooManyRequest : Failure()

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