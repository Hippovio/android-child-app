package com.hippovio.child.helpers;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import lombok.Builder;

public class AsyncHelper<T extends Object> {

    public static AsyncHelper initialise(){
        return new AsyncHelper<>();
    }

    public void asyncForSingle(Single<T> singleItem, Consumer<T> onSuccessCallback){
        singleItem.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(onSuccessCallback);
    }

    public void asyncForSingle(Single<T> singleItem, Consumer<T> onSuccessCallback, Consumer<Throwable> onErrorCallback){
        singleItem.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(onSuccessCallback, onErrorCallback);
    }
}
