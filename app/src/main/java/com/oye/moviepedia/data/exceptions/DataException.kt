package com.oye.moviepedia.data.exceptions

data class DataException constructor(override val message: String) : Exception(message) {
    companion object {
        fun fromRemoteException(ex: RemoteException): DataException {
            val message = "HTTP ${ex.code}: ${ex.message}"
            return DataException(message)
        }
    }
}