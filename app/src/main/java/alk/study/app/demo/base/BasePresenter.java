package alk.study.app.demo.base;


/**
 * 基础Presenter
 * 解决一些共用问题
 */
public abstract class BasePresenter<T> {

    public T mMvpView;

    public void attach(T view) {
        this.mMvpView = view;
    }

    public void dettach() {
        this.mMvpView = null;
    }
}
