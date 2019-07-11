package com.example.kittenappscollage.helpers.rx;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/*Этот планировщик создает совершенно новый поток при каждом вызове.
В данном случае использование пула потоков не принесет никакой выгоды.
Потоки очень затратно создавать и уничтожать.
Вы должны быть осторожны и не злоупотреблять чрезмерным созданием потоков,
так как это может привести в замедлению работы системы и переполнению памяти.
Новый поток будет создаваться для обработки каждого элемента, полученного из observable-источника.
В идеале вы должны использовать этот планировщик довольно редко,
в основном для выведения в отдельный поток долго работающих частей программы.*/


public class NewThreadTransformer<T> implements ObservableTransformer<T,T>  {




    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

    }
}
