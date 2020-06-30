package com.kittendevelop.kittenappscollage.helpers.rx;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ThreadTransformers {

    public static class NewThread<T> implements ObservableTransformer<T,T> {
        /*Этот планировщик создает совершенно новый поток при каждом вызове.
        В данном случае использование пула потоков не принесет никакой выгоды.
        Потоки очень затратно создавать и уничтожать.
        Вы должны быть осторожны и не злоупотреблять чрезмерным созданием потоков,
        так как это может привести в замедлению работы системы и переполнению памяти.
        Новый поток будет создаваться для обработки каждого элемента, полученного
        из observable-источника.
        В идеале вы должны использовать этот планировщик довольно редко,
        в основном для выведения в отдельный поток долго работающих частей программы.*/
        @Override
        public ObservableSource<T> apply(Observable<T> upstream) {
            return upstream
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread());

        }
    }

       public static class Processor<T> implements ObservableTransformer<T,T> {
           /*Этот планировщик используется для выполнения работы, высоко нагружающей ЦП,
           такой как обработка больших объемов данных, изображений и так далее.
           Планировщик основывается на ограниченном пуле потоков с размером в
           количество доступных процессоров.
           Так как этот планировщик подходит только для интенсивной работы с
           ЦП — количество его потоков ограничено.
           Сделано это для того, чтобы потоки не конкурировали за процессорное время и не простаивали.*/
        @Override
        public ObservableSource<T> apply(Observable<T> upstream) {
            return upstream
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    }



    public static class OnlySingle<T> implements ObservableTransformer<T,T> {
        /*Этот планировщик основывается на единственном потоке,
        который используется для последовательного выполнения задач.
        Он может быть очень полезен, когда у вас есть набор фоновых заданий в разных местах вашего приложения,
        но нельзя допустить одновременного выполнения более чем одного из этих заданий.*/
        @Override
        public ObservableSource<T> apply(Observable<T> upstream) {
            return upstream
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    }


    public static class InputOutput <T> implements ObservableTransformer<T,T> {
        /*Этот планировщик основывается на неограниченном пуле потоков
        и используется для интенсивной работы с вводом-выводом без использования ЦП,
        например, доступ к файловой системе, выполнение сетевых вызовов,
        доступ к базе данных и так далее.
        Количество потоков в этом планировщике неограничено и может расти по мере необходимости.*/
        @Override
        public ObservableSource<T> apply(Observable<T> upstream) {
            return upstream
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    }
}
