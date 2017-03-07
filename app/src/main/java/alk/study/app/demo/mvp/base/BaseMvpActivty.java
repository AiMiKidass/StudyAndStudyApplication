package alk.study.app.demo.mvp.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


/**
 * Created by Aleak on 2017/2/19.
 * 公用baseActivity
 */
public abstract class BaseMvpActivty<T extends BasePresenter> extends AppCompatActivity {

    public T presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.presenter = initPresenter();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // 实例化presenter
    public abstract T initPresenter();

}
