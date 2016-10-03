package com.possible.architecturesample.data.controllers;

import android.app.Application;
import android.support.v4.util.SimpleArrayMap;

import com.possible.architecturesample.data.Subscriptor;
import com.possible.architecturesample.data.network.ControllerCallback;
import com.possible.architecturesample.data.network.NetworkDataSource;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class BaseController {
    private final String SEPARATOR = "::";

    private SimpleArrayMap<String, Observable> mapObservables = new SimpleArrayMap<>();
    private SimpleArrayMap<String, Subscription> mapSubscriptions = new SimpleArrayMap<>();
    protected Application application;
    protected NetworkDataSource networkDataSource;

    public BaseController(Application application, NetworkDataSource networkDataSource) {
        this.application = application;
        this.networkDataSource = networkDataSource;
    }

    /**
     * Make sure that subscriptor.getSubscriptorTag() returns an unique value per Subscriptor and
     * each methodTag to be unique per each Controller
     */
    @SuppressWarnings("unchecked")
    public void executeInBackground(Subscriptor subscriptor, String methodTag, boolean forceRefresh,
                                    Observable observable, final ControllerCallback callback) {
        String tag = subscriptor.getSubscriptorTag() + SEPARATOR + methodTag;
        subscriptor.addSubscriptedController(this);

        if (forceRefresh) {
            clearObserver(tag);
        }

        Observable observableWithSchedulers = mapObservables.get(tag);
        if (observableWithSchedulers == null) {
            observableWithSchedulers = observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .cache();
            mapObservables.put(tag, observableWithSchedulers);
        }

        mapSubscriptions.put(tag,
                observableWithSchedulers.subscribe(new Action1() {
                    @Override
                    public void call(Object o) {
                        if (callback != null) {
                            callback.onControllerNext(o);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (callback != null) {
                            callback.onControllerError(new Exception(throwable));
                        }
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        if (callback != null) {
                            callback.onControllerComplete();
                        }
                    }
                }));
    }

    public void unsubscribe(String subscriptorTag, boolean isPermanent) {
        if (isPermanent) {
            clearAllObserversFromSubscriptor(subscriptorTag);
        } else {
            unsubscribeAllSubscriptionsFromSubscriptor(subscriptorTag);
        }
    }

    private void unsubscribeSubscription(String tag) {
        Subscription subscription = mapSubscriptions.get(tag);
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        mapSubscriptions.remove(tag);
    }

    private void clearObserver(String tag) {
        unsubscribeSubscription(tag);
        mapObservables.remove(tag);
    }

    private void clearAllObserversFromSubscriptor(String subscriptorTag) {
        for (int i = mapObservables.size() - 1; i >= 0; i--) {
            String keyTag = mapObservables.keyAt(i);
            if (keyTag.contains(subscriptorTag + SEPARATOR)) {
                clearObserver(keyTag);
            }
        }
    }

    private void unsubscribeAllSubscriptionsFromSubscriptor(String subscriptorTag) {
        for (int i = mapSubscriptions.size() - 1; i >= 0; i--) {
            String keyTag = mapSubscriptions.keyAt(i);
            if (keyTag.contains(subscriptorTag + SEPARATOR)) {
                unsubscribeSubscription(keyTag);
            }
        }
    }
}
