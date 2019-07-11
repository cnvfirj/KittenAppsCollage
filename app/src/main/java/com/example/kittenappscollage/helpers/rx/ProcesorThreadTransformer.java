package com.example.kittenappscollage.helpers.rx;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/*Этот планировщик используется для выполнения работы, высоко нагружающей ЦП,
 такой как обработка больших объемов данных, изображений и так далее.
 Планировщик основывается на ограниченном пуле потоков с размером в количество доступных процессоров.
Так как этот планировщик подходит только для интенсивной работы с
 ЦП — количество его потоков ограничено.
 Сделано это для того, чтобы потоки не конкурировали за процессорное время и не простаивали.*/
public class ProcesorThreadTransformer<T> implements ObservableTransformer<T,T> {

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
