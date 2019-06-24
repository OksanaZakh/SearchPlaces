package com.ozakharchenko.placesearch.viewmodel

import com.ozakharchenko.placesearch.utils.DownloadException

class Resource<T> private constructor(var status: Status= Status.LOADING, val data: T?, val exception: DownloadException?) {
    enum class Status {
        SUCCESS, ERROR, LOADING
    }

    companion object {

        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(exception: DownloadException?): Resource<T> {
            return Resource(Status.ERROR, null, exception)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }
    }
}