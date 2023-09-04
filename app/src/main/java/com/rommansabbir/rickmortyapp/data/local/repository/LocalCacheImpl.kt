package com.rommansabbir.rickmortyapp.data.local.repository

import android.content.Context
import com.rommansabbir.rickmortyapp.base.appresult.AppResult
import com.rommansabbir.rickmortyapp.base.failure.Failure
import com.rommansabbir.rickmortyapp.data.local.models.CacheCharactersListRequestModel
import com.rommansabbir.rickmortyapp.data.remote.models.RickMortyCharactersListAPIResponse
import com.rommansabbir.rickmortyapp.utils.extensions.executeBodyOrReturnNull
import com.rommansabbir.storex.v2.config.StoreXSmartConfig
import com.rommansabbir.storex.v2.smartstorex.SmartStoreX
import com.rommansabbir.storex.v2.strategy.StoreXCachingStrategy
import java.lang.ref.WeakReference
import javax.inject.Inject


class LocalCacheImpl @Inject constructor(private val context: Context) :
    LocalCache {
    override fun cacheCharactersListLocally(requestModel: CacheCharactersListRequestModel): AppResult<Boolean> {
        return executeBodyOrReturnNull {
            val config = getCacheCharactersListConfig(context = context, requestModel)
            SmartStoreX.getInstance.write(config = config)
            AppResult.Success(true)
        } ?: kotlin.run { AppResult.Error(Failure.LocalCache.FailedToCache("Failed to cache.")) }
    }

    override fun getCharactersListFromLocal(): AppResult<RickMortyCharactersListAPIResponse> {
        return executeBodyOrReturnNull {
            val cachedData = SmartStoreX.getInstance.read<CacheCharactersListRequestModel>(
                getCacheCharactersListConfig(
                    context = context, CacheCharactersListRequestModel(null, mutableListOf())
                ), CacheCharactersListRequestModel::class.java
            ) ?: return@executeBodyOrReturnNull AppResult.Error(Failure.LocalCache.NotExistInCache)
            return@executeBodyOrReturnNull AppResult.Success<RickMortyCharactersListAPIResponse>(
                RickMortyCharactersListAPIResponse().apply {
                    this.results.addAll(cachedData.list)
                    this.paginationInfo.next = cachedData.paginatedURL
                })
        } ?: kotlin.run { AppResult.Error(Failure.LocalCache.NotExistInCache) }
    }

    /**
     * Create a return a new instance of [StoreXSmartConfig] for local caching.
     *
     * @param context Context.
     * @param requestModel Model to be cached to the local.
     */
    private fun getCacheCharactersListConfig(
        context: Context, requestModel: CacheCharactersListRequestModel
    ): StoreXSmartConfig<CacheCharactersListRequestModel> {
        return StoreXSmartConfig(
            WeakReference(context),
            "characters_list",
            requestModel,
            StoreXCachingStrategy.CacheDir,
            overwriteExistingFile = true
        )
    }
}