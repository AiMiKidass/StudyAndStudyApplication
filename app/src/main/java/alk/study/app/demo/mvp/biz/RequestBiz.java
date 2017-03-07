package alk.study.app.demo.mvp.biz;

public interface RequestBiz {
    /**
     * 请求数据
     * @param listener
     */
    void requestForData(OnRequestListener listener);

    void requestForUserFeedbackMessage(OnRequestListener listener);
}
