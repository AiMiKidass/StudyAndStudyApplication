package alk.study.app.demo.okhttp;

import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import alk.study.app.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

import static android.R.attr.path;

/**
 * OkHttp用法Demo 注意要先引用okhttp和okio包
 */
public class OKHttpDemoActivity extends AppCompatActivity {

    private static final String TAG = OKHttpDemoActivity.class.getSimpleName();
    private OkHttpClient mOkHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okhttpdemo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendPostRequestTest();
            }
        });

        mOkHttpClient = new OkHttpClient();
    }

    /**
     * 测试call.excute方法是否同步
     */
    private void testHttpOKClient() {
        // Call.excute是执行在UI Thread中的,Android4.0中规定访问网络操作必须在thread中
        final OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url("http://www.baidu.com").build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mOkHttpClient.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void getHttpPageStr() {
        /**
         * 模拟如下参数
         conn.setConnectTimeout(CONNECTION_TIME_OUT);
         conn.setReadTimeout(READDATA_TIME_OUT);
         conn.setRequestProperty("Connection", "close");
         conn.setRequestMethod("POST");
         conn.setRequestProperty("User-Agent", "LetvBugClient Android;letv;1.0;");
         conn.setRequestProperty("Content-Type", "application/json");
         conn.setRequestProperty("Charset", "utf-8");
         */

        /**
         conn.setRequestProperty("User-Agent", "LetvBugClient Android;letv;1.0;");
         conn.setRequestProperty("Content-Type", "application/json");
         */

        /**
         *
         Headers.of();
         Headers headers = new Headers();
         */

        /*
        以上就是发送一个get请求的步骤，首先构造一个Request对象，参数最起码有个url，当然你可以通过Request.Builder设置更多的参数比如：header、method等。
        然后通过request的对象去构造得到一个Call对象，类似于将你的请求封装成了任务，既然是任务，就会有execute()和cancel()等方法。
        最后，我们希望以异步的方式去执行请求，所以我们调用的是call.enqueue，将call加入调度队列，然后等待任务执行完成，我们在Callback中即可得到结果。
         */
        final Request request = new Request.Builder().url("http://www.baidu.com").get()
                // .headers(headers)
                .build();
        // new call
        // 这里支持阻塞方式
        /**
         先说说什么叫做阻塞队列：阻塞队列是一个支持两个附加操作的队列。这两个附加的操作支持阻塞的插入和移除方法。
         支持阻塞的插入方法：意思是当队列满时，队列会阻塞插入元素的线程，知道队列不满。
         支持阻塞的移除方法：意思是当队列为空时，获取元素的线程会等待队列变为非空。
         阻塞队列常用于生产主管和消费者的场景。
         其插入和移除操作的4种处理方式有，add(e)/remove()，offer(e)/poll()，put(e)/take，offer(e,time,unit)/poll(time,unit)。
         其中第一种会抛出异常，第二种会返回特殊值，第三种会一直阻塞，第四种会超时退出。
         */
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "访问失败,错误原因:" + e.getMessage());
                        showMessage("访问失败,错误原因:" + e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String message = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showMessage("访问成功,内容:" + message);
                    }
                });
            }
        });
    }

    /**
     * 测试Post请求
     */
    private void sendPostRequestTest() {
        FormBody body = new FormBody.Builder()
                .add("your_param_1", "your_value_1")
                .add("your_param_2", "your_value_2")
                .build();

        Request request = new Request.Builder().url("http://www.baidu.com").post(body).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "success");
            }
        });

    }

    /**
     * 上传文件Demo
     */
    private void uploadFile() {
        File file = new File(Environment.getExternalStorageDirectory(), "trace.txt");

        Log.d(TAG, "file存在吗?结果=" + file.exists());
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);

        // 一个基础的文件上传的
        MultipartBody multipartBody = new MultipartBody.Builder("BbC04y").addPart(Headers.of("Content-Disposition", "form-data; filename=\"img.png\"")
                , RequestBody.create(MediaType.parse("image/png"), file)).build();
        new MultipartBody.Builder("AaB03x").setType(MultipartBody.FORM)
                .addFormDataPart("files", null, multipartBody);


        Headers.of("Content-Disposition", "form-data; filename=\"img.png\"");

        new MultipartBody.Builder("BbC04y")
                .addPart(Headers.of("Content-Disposition", "form-data; filename=\"img.png\""),
                        RequestBody.create(MediaType.parse("image/png"), new File("")));
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 一种标准的上传文件的POST请求方式
     */
    private void test() {
        MultipartBody body = new MultipartBody.Builder("AaB03x")
                .setType(MultipartBody.FORM)
                .addFormDataPart("files", null, new MultipartBody.Builder("BbC04y")
                        .addPart(Headers.of("Content-Disposition", "form-data; filename=\"img.png\""),
                                RequestBody.create(MediaType.parse("image/png"), new File("")))
                        .build())
                .build();
    }


}
