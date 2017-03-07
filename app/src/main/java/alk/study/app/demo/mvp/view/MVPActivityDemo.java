package alk.study.app.demo.mvp.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import alk.study.app.R;
import alk.study.app.demo.mvp.biz.MvpPresenter;
import alk.study.app.demo.mvp.biz.MvpView;

/**
 * MVP Demp
 * MVP = Model + View + Prensenter(提出者，推荐者，赠送者)
 * 实际的开发中，请求的业务代码往往被丢到了Activity里面，
 * 大家都知道layout.xml的布局文件只能提供默认的UI设置，所以开发中视图层的变化也被丢到了Activity里面，
 * 再加上Activity本身承担着控制层的责任。所以Activity达成了MVC集合的成就，
 * 最终我们的Activity就变得越来越难看，从几百行变成了几千行。维护的成本也越来越高（非常同意）
 * Prensenter负责控制层的角色
 *
 * Prensenter包含MvpPresenter和MvpView,
 * MvpView表示这个Activity对View的行为处理,里面定义了View的各种行为,如隐藏View,填充ListView等等;
 * MvpPresenter包含Activity的数据处理,以及部分MvpView的调用
 * Activity不包含任何数据的获取和处理,只负责数据显示,MvpPrensenter的调用,以及MvpView的具体实现,这些实现是指View的实现代码,如View的隐藏,填充ListView等;
 * 这样写虽然一开始比较麻烦,但是熟悉以后可以在项目维护到中后期代码臃肿的情况下,有助于新开发人员迅速理清项目架构,并便于小组其他人员维护有着很重要的作用
 */
public class MVPActivityDemo extends AppCompatActivity implements MvpView {

    private ListView mListView;
    private ProgressBar mProgressBar;
    private MvpPresenter mvpPresenter;

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
        mvpPresenter = new MvpPresenter(MVPActivityDemo.this);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MVPActivityDemo.this.onItemClick(parent,view,position,id);
            }
        });
        mvpPresenter.onResume();

        NewMvpActivity sss;
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
    public void setListItem(ArrayList<String> data) {
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(MVPActivityDemo.this, android.R.layout.simple_list_item_1, data);
        mListView.setAdapter(adapter);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(MVPActivityDemo.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mvpPresenter.onItemClick(position);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 避免Activity内存泄漏
        mvpPresenter.destoryActivity();
    }
}
