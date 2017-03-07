package alk.study.app.demo.mvp.biz;

import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;

/**
 * MVP
 */
public interface MvpView {
    /**
     * 显示“正在读取中”
     */
    void showLoading();

    /**
     * 隐藏loading progress
     */
    void hideLoading();

    /**
     * ListView的初始化
     *
     * @param data
     */
    void setListItem(ArrayList<String> data);

    /**
     * Toast消息
     *
     * @param message
     */
    void showMessage(String message);

    /**
     * ListViewItem click事件
     */
    void onItemClick(AdapterView<?> parent, View view, int position, long id);
}
