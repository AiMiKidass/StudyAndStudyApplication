package alk.study.app.demo.mvp.biz;


/**
 * Created by Aleak on 2017/2/20.
 * 注意构造函数的参数类型需要为MVPView类型,而不是Activity,虽然实际上是一个Activity
 */
public class TaskDetailPresenter implements TaskDetailContract.Presenter {

    private TaskDetailContract.View mMvpView;

    public TaskDetailPresenter(TaskDetailContract.View mvpView) {
        this.mMvpView = mvpView;
        this.mMvpView.setPresenter(this);
    }

    @Override
    public void editTask() {

    }

    @Override
    public void deleteTask() {

    }

    @Override
    public void start() {

    }
}
