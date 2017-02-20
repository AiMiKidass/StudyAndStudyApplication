package alk.study.app.demo.biz;

import java.util.ArrayList;

/**
 * Created by Aleak on 2017/2/17.
 */
public interface OnRequestListener {
    void onSuccess(ArrayList<String> data);
    void onFailed();
}
