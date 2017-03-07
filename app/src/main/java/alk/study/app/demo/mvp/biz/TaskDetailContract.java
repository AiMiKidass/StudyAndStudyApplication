package alk.study.app.demo.mvp.biz;

import alk.study.app.demo.mvp.base.BaseGoogleDemoPresenter;
import alk.study.app.demo.mvp.base.BaseGoogleDemoView;

/**
 * Created by Aleak on 2017/2/20.
 * Google biz Demo
 * 学习google写法
 * 优点是把同一个业务逻辑(Activity)的MvpView和presenter放在一起,如果不这样做,那么就得专门新建class文件去实现,多出一倍的类文件.
 * 并且这样非常直观
 */

public interface TaskDetailContract {
    interface View extends BaseGoogleDemoView<BaseGoogleDemoPresenter>{
        void showTitle();
        boolean isActive();
    }

    interface Presenter extends BaseGoogleDemoPresenter{
        void editTask();
        void deleteTask();
    }
}
