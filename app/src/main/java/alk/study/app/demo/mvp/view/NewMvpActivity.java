package alk.study.app.demo.mvp.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import alk.study.app.R;
import alk.study.app.demo.mvp.base.BaseMvpActivty;
import alk.study.app.demo.mvp.biz.NewMvpPresenter;
import alk.study.app.demo.mvp.biz.NewMvpView;

/**
 * MVP Demp
 * MVP = Model + View + Prensenter(提出者，推荐者，赠送者)
 * 实际的开发中，请求的业务代码往往被丢到了Activity里面，
 * 大家都知道layout.xml的布局文件只能提供默认的UI设置，所以开发中视图层的变化也被丢到了Activity里面，
 * 再加上Activity本身承担着控制层的责任。所以Activity达成了MVC集合的成就，
 * 最终我们的Activity就变得越来越难看，从几百行变成了几千行。维护的成本也越来越高（非常同意）
 * Prensenter负责控制层的角色
 *
    全新封装后的写法,与MvpActivity做对比

 */
public class NewMvpActivity extends BaseMvpActivty<NewMvpPresenter> implements NewMvpView {

    private ListView mListView;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvp_demo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mListView = (ListView) findViewById(R.id.lv_list);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_loadingbar);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewMvpActivity.this.onItemClick(parent,view,position,id);
            }
        });

        super.presenter.onResume();
    }

    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // doSomething
        super.presenter.onItemClick(position);
    }

    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void setListItem(List<String> data) {
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(NewMvpActivity.this, android.R.layout.simple_list_item_1, data);
        mListView.setAdapter(adapter);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(NewMvpActivity.this, message, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 避免Activity内存泄漏
        super.presenter.dettach();
    }

    @Override
    public NewMvpPresenter initPresenter() {
        NewMvpPresenter presenter = new NewMvpPresenter();
        presenter.attach(NewMvpActivity.this);
        return presenter;
    }
}
