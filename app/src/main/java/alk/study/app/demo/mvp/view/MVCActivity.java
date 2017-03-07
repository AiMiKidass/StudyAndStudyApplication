package alk.study.app.demo.mvp.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import alk.study.app.demo.mvp.biz.OnRequestListener;
import alk.study.app.demo.mvp.biz.RequestBizImpl;

/**
 * 用于区别MVP模式
 */
public class MVCActivity extends AppCompatActivity {

    private RequestBizImpl requestBiz;
    private Handler handler;
    private ListView listview;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvc);
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

        handler = new Handler(Looper.getMainLooper());
        listview = (ListView) findViewById(R.id.lv_main_container);
        progressBar = (ProgressBar) findViewById(R.id.pb_loadingbar);
        progressBar.setVisibility(View.VISIBLE);
        requestBiz = new RequestBizImpl();

        requestData();


    }

    public void requestData() {
        requestBiz.requestForData(new OnRequestListener() {
            @Override
            public void onSuccess(final ArrayList<String> data) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MVCActivity.this, android.R.layout.simple_list_item_1, data);
                        listview.setAdapter(adapter);
                        listview.setOnItemClickListener(itemClickListener);
                    }
                });
            }

            @Override
            public void onFailed() {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MVCActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(MVCActivity.this, "点击了Item", Toast.LENGTH_SHORT).show();
        }
    };
}
