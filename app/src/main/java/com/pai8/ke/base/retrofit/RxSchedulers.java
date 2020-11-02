package com.pai8.ke.base.retrofit;


import com.pai8.ke.R;
import com.pai8.ke.app.MyApp;
import com.pai8.ke.exception.NetConnException;
import com.pai8.ke.utils.NetUtils;
import com.pai8.ke.utils.ResUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 线程切换
 * Created by gh on 2020/11/2.
 */
public class RxSchedulers {

    public static ObservableTransformer io_main() {

        return new ObservableTransformer() {
            @Override
            public ObservableSource apply(Observable observable) {
                return observable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {

                                if (!NetUtils.isCon(MyApp.getMyApp())) {
                                    if (!disposable.isDisposed()) {
                                        disposable.dispose();
                                    }
                                    throw new NetConnException(ResUtils.getText(R.string.str_net_error));
                                }
                            }
                        });
            }
        };

    }
}
