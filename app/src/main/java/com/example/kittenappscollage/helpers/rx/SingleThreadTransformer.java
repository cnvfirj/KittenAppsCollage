package com.example.kittenappscollage.helpers.rx;

/*Этот планировщик основывается на единственном потоке,
который используется для последовательного выполнения задач.
Он может быть очень полезен, когда у вас есть набор фоновых заданий в разных местах вашего приложения,
но нельзя допустить одновременного выполнения более чем одного из этих заданий.*/

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SingleThreadTransformer<T> implements ObservableTransformer<T,T> {

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
