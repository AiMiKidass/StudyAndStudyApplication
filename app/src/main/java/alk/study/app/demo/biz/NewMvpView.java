package alk.study.app.demo.biz;

import java.util.List;

import alk.study.app.demo.base.BaseMvpView;

/**
 * Created by Aleak on 2017/2/19.
 * NewMvpView共有4个行为(方法),父接口两个,自己两个,一共4个
 */
public interface NewMvpView extends BaseMvpView {
    void setListItem(List<String> data);
    void showMessage(String message);
}
