package alk.study.app.demo.mvp.biz;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;

/**
 * Created by Aleak on 2017/2/17.
 * MVP模式中的Presenter,负责View的控制,提供,操作
 */
public class MvpPresenter {

    private MvpView mvpView;
    private RequestBiz requestBiz;
    private Handler mHandler;

    public MvpPresenter(MvpView mvpView) {
        this.mvpView = mvpView;
        this.requestBiz = new RequestBizImpl();
        this.mHandler = new Handler(Looper.getMainLooper());
    }

    public void onResume() {
        mvpView.showLoading();
        requestBiz.requestForData(new OnRequestListener() {
            @Override
            public void onSuccess(final ArrayList<String> data) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mvpView.hideLoading();
                        mvpView.setListItem(data);
                    }
                });
            }

            @Override
            public void onFailed() {
                mvpView.showMessage("请求失败");
            }
        });
    }

    public void onItemClick(int position) {
        mvpView.showMessage("点击了Item" + position);
    }

    /**
     * mvpView = null 将Activity的对象实例=null 避免Activity内存泄漏
     */
    public void destoryActivity() {
        this.mvpView = null;
    }
}
