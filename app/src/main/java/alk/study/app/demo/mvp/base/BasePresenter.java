package alk.study.app.demo.mvp.base;


/**
 * 基础Presenter
 * 解决一些共用问题
 */
public abstract class BasePresenter<T extends BaseMvpView> {

    public T mMvpView;

    public void attach(T view) {
        this.mMvpView = view;
    }

    public void dettach() {
        this.mMvpView = null;
    }
}
