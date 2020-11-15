package com.hippovio.child.helpers;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AsyncHelper<T extends Object> {

    public void asyncForSingle(Single<T> singleItem, CallBack<T> callBack){
        singleItem.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                response -> callBack.onSuccess(response),
                error -> callBack.onError(error));
    }

    public interface CallBack<K extends Object> {
        void onSuccess(K entity);
        void onError(Throwable error);
    }
}
