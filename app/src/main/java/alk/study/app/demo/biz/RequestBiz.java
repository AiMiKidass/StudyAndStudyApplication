package alk.study.app.demo.biz;

public interface RequestBiz {
    /**
     * 请求数据
     * @param listener
     */
    void requestForData(OnRequestListener listener);

    void requestForUserFeedbackMessage(OnRequestListener listener);
}
