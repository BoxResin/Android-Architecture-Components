package com.ik.exploringviewmodel.sources.db

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.persistence.room.Room
import android.content.Context
import android.util.Log
import com.ik.exploringviewmodel.sources.db.AppDatabase.Companion.DATABASE_NAME
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Creates the [AppDatabase] asynchronously, exposing a LiveData object to notify of creation.
 */
object DatabaseCreator {

    private val mIsDatabaseCreated = MutableLiveData<Boolean>()

    lateinit var database: AppDatabase

    private val mInitializing = AtomicBoolean(true)

    val isDatabaseCreated: LiveData<Boolean>
        get() = mIsDatabaseCreated

    fun createDb(context: Context) {
        if (mInitializing.compareAndSet(true, false).not()) {
            return
        }
        mIsDatabaseCreated.value = false

        Completable.fromAction {
            database = Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()
        }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { mIsDatabaseCreated.value = true }
    }

}