package com.oye.moviepedia.data.exceptions

data class RemoteException(val code: Int, override val message: String) : Exception(message)