package alk.study.app.demo.mvp.biz;

import android.os.SystemClock;

import java.util.ArrayList;

/**
 * Created by Aleak on 2017/2/17.
 */

public class RequestBizImpl implements RequestBiz {
    @Override
    public void requestForData(final OnRequestListener listener) {
        new Thread() {
            @Override
            public void run() {
                SystemClock.sleep(2000);
                ArrayList<String> data = new ArrayList<String>();
                for (int i = 0; i < 8; i++) {
                    data.add("Item" + i);
                }
                if(null != listener){
                    listener.onSuccess(data);
                }
            }
        }.start();
    }

    @Override
    public void requestForUserFeedbackMessage(final OnRequestListener listener) {
        new Thread() {
            @Override
            public void run() {
                SystemClock.sleep(2000);
                ArrayList<String> data = new ArrayList<String>();
                for (int i = 0; i < 8; i++) {
                    data.add("Item" + i);
                }
                if(null != listener){
                    listener.onSuccess(data);
                }
            }
        }.start();
    }
}
