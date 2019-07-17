package com.ozakharchenko.placesearch

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

object AppExecutors {

    private val diskIO: Executor = Executors.newSingleThreadExecutor()
    private var networkIO: Executor = Executors.newFixedThreadPool(3)
    private var mainThread: MainThreadExecutor = MainThreadExecutor()

    fun diskIO(): Executor {
        return diskIO
    }

    fun networkIO(): Executor {
        return networkIO
    }

    fun mainThread(): Executor {
        return mainThread
    }

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }
}