package com.possible.architecturesample.data.controllers

import android.app.Application
import android.support.v4.util.SimpleArrayMap
import com.possible.architecturesample.data.Subscriptor
import com.possible.architecturesample.data.network.ControllerCallback
import com.possible.architecturesample.data.network.NetworkDataSource
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

open class BaseController(protected var application: Application, protected var networkDataSource: NetworkDataSource) {
    private val SEPARATOR = "::"

    private val mapObservables = SimpleArrayMap<String, Observable<*>>()
    private val mapSubscriptions = SimpleArrayMap<String, Subscription>()

    /**
     * Make sure that subscriptor.getSubscriptorTag() returns an unique value per Subscriptor and
     * each methodTag to be unique per each Controller
     */
    fun executeInBackground(subscriptor: Subscriptor, methodTag: String, forceRefresh: Boolean,
                            observable: Observable<*>, callback: ControllerCallback<Any>?) {
        val tag = subscriptor.subscriptorTag + SEPARATOR + methodTag
        subscriptor.addSubscriptedController(this)

        if (forceRefresh) {
            clearObserver(tag)
        }

        var observableWithSchedulers: Observable<out Any>? = mapObservables.get(tag)
        if (observableWithSchedulers == null) {
            observableWithSchedulers = observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .cache()
            mapObservables.put(tag, observableWithSchedulers)
        }

        mapSubscriptions.put(tag,
                observableWithSchedulers?.subscribe( { o -> callback?.onControllerNext(o) },
                        { throwable -> callback?.onControllerError(Exception(throwable)) },
                        { callback?.onControllerComplete() }))
    }

    fun unsubscribe(subscriptorTag: String, isPermanent: Boolean) {
        if (isPermanent) {
            clearAllObserversFromSubscriptor(subscriptorTag)
        } else {
            unsubscribeAllSubscriptionsFromSubscriptor(subscriptorTag)
        }
    }

    private fun unsubscribeSubscription(tag: String) {
        val subscription = mapSubscriptions.get(tag)
        if (subscription != null && !subscription.isUnsubscribed) {
            subscription.unsubscribe()
        }
        mapSubscriptions.remove(tag)
    }

    private fun clearObserver(tag: String) {
        unsubscribeSubscription(tag)
        mapObservables.remove(tag)
    }

    private fun clearAllObserversFromSubscriptor(subscriptorTag: String) {
        for (i in mapObservables.size() - 1 downTo 0) {
            val keyTag = mapObservables.keyAt(i)
            if (keyTag.contains(subscriptorTag + SEPARATOR)) {
                clearObserver(keyTag)
            }
        }
    }

    private fun unsubscribeAllSubscriptionsFromSubscriptor(subscriptorTag: String) {
        for (i in mapSubscriptions.size() - 1 downTo 0) {
            val keyTag = mapSubscriptions.keyAt(i)
            if (keyTag.contains(subscriptorTag + SEPARATOR)) {
                unsubscribeSubscription(keyTag)
            }
        }
    }
}
