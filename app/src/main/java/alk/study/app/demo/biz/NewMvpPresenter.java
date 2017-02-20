package alk.study.app.demo.biz;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;

import alk.study.app.demo.base.BasePresenter;

/**
 * Created by Aleak on 2017/2/19.
 * Demo
 */
public class NewMvpPresenter extends BasePresenter<NewMvpView> {

    private RequestBiz requestBiz;
    private Handler mHandler;

    public NewMvpPresenter() {
        this.mHandler = new Handler(Looper.getMainLooper());
        this.requestBiz = new RequestBizImpl();
    }

    public void onResume() {
        this.requestBiz.requestForData(new OnRequestListener() {
            @Override
            public void onSuccess(final ArrayList<String> data) {
                if(NewMvpPresenter.super.mMvpView == null){
                    // dettach回收了对象实例
                    return;
                }

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        NewMvpPresenter.super.mMvpView.hideLoading();
                        NewMvpPresenter.super.mMvpView.setListItem(data);
                    }
                });
            }

            @Override
            public void onFailed() {
                mMvpView.showMessage("请求失败");
            }
        });
    }

    public void onItemClick(int position) {
        mMvpView.showMessage("点击了item" + position);
    }

}
