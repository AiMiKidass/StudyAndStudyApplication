package alk.study.app.demo.mvp.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import alk.study.app.R;
import alk.study.app.demo.mvp.base.BaseGoogleDemoPresenter;
import alk.study.app.demo.mvp.base.BaseGoogleDemoView;
import alk.study.app.demo.mvp.biz.GoogleDemoPresenter;
import alk.study.app.demo.mvp.biz.TaskDetailContract;
import alk.study.app.demo.mvp.biz.TaskDetailPresenter;

/**
 * 使用Google推荐的MVP写法
 * Activity在我们看来是View(MVPView),所以给Presenter传入该对象
 */
public class GoogleDemoMVPActivity extends AppCompatActivity implements TaskDetailContract.View {

    private TaskDetailPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_demo_mvp);
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


        // MVP M = model V = View(Activity) P = GoogleDemoPresenter
        GoogleDemoPresenter presenter;

        mPresenter = new TaskDetailPresenter(GoogleDemoMVPActivity.this);

        BaseGoogleDemoPresenter s;
        BaseGoogleDemoView ss;
    }

    @Override
    public void showTitle() {

    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void setPresenter(BaseGoogleDemoPresenter presenter) {

    }
}
