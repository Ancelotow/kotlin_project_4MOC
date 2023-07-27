package com.oye.moviepedia.data.repositories

import android.util.Log
import com.oye.moviepedia.data.data_sources.LogoutDataSource
import com.oye.moviepedia.domain.repositories.LogoutRepository
import javax.inject.Inject

class RemoteLogoutRepository @Inject constructor(
    private val dataSource: LogoutDataSource
) : LogoutRepository {

    override fun logout(access_token: String): Boolean {
        val response = dataSource.logout(access_token)
        return response;
    }

}