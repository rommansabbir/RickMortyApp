package com.rommansabbir.rickmortyapp.base.api

/**
 * Define all endpoints here. If might be expensive for this demo project
 * in larger project, when we are using mock response to continue the development,
 * we can utilize this tool.
 *
 * [Ex: Let's say we are using Interceptor (OkHttp) to return an JSON as a response from the
 * asset folder. In the interceptor we need to check the target endpoint, so by listing all
 * endpoints here can makes it easier to check the endpoint with the defined constant value]
 *
 */
object APIEndpoints {
    object Characters {
        const val CharacterEndpoint = "character"
    }
}