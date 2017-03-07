package alk.study.app.demo.rxjava;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import alk.study.app.R;
import alk.study.app.demo.rxjava.bean.BaiDuHtmlCode;
import alk.study.app.demo.rxjava.bean.Student;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * RxJava是一个在 Java VM 上使用可观测的序列来组成异步的、基于事件的程序的库
 * 关键字:异步
 * 比起Handler和AsyncTask的优点是特别简洁,随着程序逻辑变得越来越复杂，它依然能够保持简洁
 * 是为了解决
 * 1.异步任务导致的代码和逻辑上的优化,方便理清思维;
 * <p>
 * <p>
 * <p>
 * RxJava是基于观察者模式的;
 */
public class RxJavaDemoActivity extends AppCompatActivity {

    private static final String TAG = RxJavaDemoActivity.class.getSimpleName();
    private ImageView mImageView;
    private OkHttpClient mOkHttpClient;
    private RxTestDemo rxTestDemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java_demo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        File file ;
        file.exists();


        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().build());

        // Observable的另一种写法
        rxTestDemo = new RxTestDemo();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  demo1();
                // rxJavaDemo2();
                // rxJavaDemo3();

                //  newRxJavaDemo1();
                //newRxJavaDemo2();
                // newRxJavaDemo3();
//                newRxJavaDemo4();
                newRxJavaDemo5();

                //rxTestDemo.distinctDemo();
            }
        });


    }


    /**
     * flatMap用法
     */
    private void newRxJavaDemo5() {
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Student stu1 = new Student();
            stu1.name = "321sss" + (i + 1);
            stu1.courses = new String[]{"aaa", "bb", "cc", "dd"};
            students.add(stu1);
        }
        /*


        // Observable.from 适用于泛型集合,在多个元素时可以使用,如果是基本数据类型可以使用Observable.just
        Observable.from(students).map(new Func1<Student, String>() {
            @Override
            public String call(Student student) {
                return student.name;
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d(TAG, "student.name=" + s);
            }
        });
        */

        Observable.from(students).flatMap(new Func1<Student, Observable<String>>() {
            @Override
            public Observable<String> call(Student student) {
                Log.d(TAG, "---------");
                Log.d(TAG, "student.name=" + student.name);
                return Observable.from(student.courses);
            }
        }).filter(new Func1<String, Boolean>() {
            // filter作为过滤器,过滤的是最初的Arraylist,而不是订阅事件subscribe中的元素,在这个例子中,过滤的是
            // Student 而不是Student.courses
            // 注意boolean表达式以true作为返回的条件 进行后续的计算 也符合逻辑
            @Override
            public Boolean call(String s) {
                Log.d(TAG, "in filter,student.course=" + s);
                boolean result = "aaa".equals(s);
                Log.d(TAG, "result=" + result);
                return result;
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d(TAG, "student.course=" + s);
                Log.d(TAG, "---------");
            }
        });


    }

    /**
     * 变换 (.map)  可以指定在subscribe中
     */
    private void newRxJavaDemo4() {
        Observable.just("images/logo.png").map(new Func1<String, Bitmap>() {
            @Override
            public Bitmap call(String s) {  // Func1<String, Bitmap> 前者为参数类型,后者为返回类型
                return null;
            }
        }).subscribe(new Action1<Bitmap>() {
            @Override
            public void call(Bitmap bitmap) {
                // showBitmap with imageview
            }
        });


        mOkHttpClient = new OkHttpClient();

        // 一个融合OKHttp的例子,去网络访问数据并使用RxJava回调
        Observable.just("http://www.baidu.com").map(new Func1<String, BaiDuHtmlCode>() {
            @Override
            public BaiDuHtmlCode call(String url) {
                final Request request = new Request.Builder().url(url).build();
                BaiDuHtmlCode ss = new BaiDuHtmlCode();
                try {
                    Response response = mOkHttpClient.newCall(request).execute();
                    ss.jsonStr = response.body().string();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return ss;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaiDuHtmlCode>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "completed!");

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "error=" + e.getMessage());
                    }

                    @Override
                    public void onNext(BaiDuHtmlCode message) {
                        Toast.makeText(RxJavaDemoActivity.this, "访问成功,内容:" + message.jsonStr, Toast.LENGTH_SHORT).show();
                    }
                });


    }

    /**
     * 一个由OKHttp和Observable组合的例子
     */
    private void newRxJavaDemo3() {
        mOkHttpClient = new OkHttpClient();

        // 一个融合OKHttp的例子,去网络访问数据并使用RxJava回调
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                final Request request = new Request.Builder().url("http://www.baidu.com").build();
                try {
                    Response response = mOkHttpClient.newCall(request).execute();
                    final String message = response.body().string();
                    subscriber.onNext(message);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "completed!");

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "error=" + e.getMessage());
                    }

                    @Override
                    public void onNext(String message) {
                        Toast.makeText(RxJavaDemoActivity.this, "访问成功,内容:" + message, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    /**
     * Demo2 熟悉RxJava的Observerable模式
     */
    private void newRxJavaDemo2() {
        Log.i(TAG, "running");
        // 一个基本的Observer调用方式,注意 onNext , onCompleted, onError的调用
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "completed!");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "error");
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "Item:" + s);
            }
        };

        // 常用又优化过的类:Subscriber
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onCompleted() {
                Log.d(TAG, "completed!");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "error");
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "Item:" + s);
            }
        };

        // subscribe(Action与Subscriber没有关系,只是在observable.subscribe时,作为参数传入的Action会被自动封装为ActionSubscriber类,这个类是继承于Subscriber)
        Observable observable = Observable.from(new String[]{"1", "2"});
        observable.subscribe(subscriber);
        observable.subscribe(new Action1() {
            @Override
            public void call(Object o) {
                // do something
            }
        });

        // 遍历数组
        final String[] names = {"11", "22"};
        Observable tempobservable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onStart();
                // 定义subscriber中的onNext onError onCompleted 如何调用
                for (String name : names) {
                    subscriber.onNext(name);
                }
                subscriber.onCompleted();
            }
        });
        // 关联Observable和Subscribe,从而执行函数
        tempobservable.subscribe(subscriber);

        // 以Button => OnClickListener为例
        // OnClickListener对应 Subscriber  onclick(View) 对应 onNext,onCompleted等等


    }

    /**
     * Demo1 为了避免无限制的循环写法导致的多次递进结构,我们需要RxJava特有的API来解决这个问题;
     */
    private void newRxJavaDemo1() {
        final File[] folders = new File[]{};

        // 原本的例子:
        new Thread() {
            @Override
            public void run() {
                super.run();

                for (File folder : folders) {
                    File[] files = folder.listFiles();
                    for (File file : files) {
                        if (file.getName().endsWith(".png")) {
                            RxJavaDemoActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(RxJavaDemoActivity.this, "find .png image!", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                }
            }
        }.start();

        //可以看到上面的例子,可以看到foreach结构套用了很多层,有多层代码上的递进结构,不利于逻辑上的梳理;
        // 如果使用RxJava,可以用如下方式;
        Observable.from(folders)
                .flatMap(new Func1<File, Observable<File>>() {
                    @Override
                    public Observable<File> call(File file) {
                        return Observable.from(file.listFiles());
                    }
                })
                .filter(new Func1<File, Boolean>() {
                    @Override
                    public Boolean call(File file) {
                        return null;
                    }
                }).map(new Func1<File, Bitmap>() {
            @Override
            public Bitmap call(File file) {
                return null;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        Toast.makeText(RxJavaDemoActivity.this, "find .png image!", Toast.LENGTH_LONG).show();
                    }
                });

    }


    /**
     * 基于调度器(Scheduler)的RxJava Demo
     * 在不指定线程的情况下， RxJava 遵循的是线程不变的原则，即：在哪个线程调用 subscribe()，就在哪个线程生产事件；
     * 在哪个线程生产事件，就在哪个线程消费事件。如果需要切换线程，就需要用到 Scheduler （调度器）。
     * <p>
     * <p>
     * 在RxJava 中，Scheduler ——调度器，相当于线程控制器，RxJava 通过它来指定每一段代码应该运行在什么样的线程。RxJava 已经内置了几个 Scheduler ，它们已经适合大多数的使用场景：
     * <p>
     * Schedulers.immediate(): 直接在当前线程运行，相当于不指定线程。这是默认的 Scheduler。
     * Schedulers.newThread(): 总是启用新线程，并在新线程执行操作。
     * Schedulers.io(): I/O 操作（读写文件、读写数据库、网络信息交互等）所使用的 Scheduler。行为模式和 newThread() 差不多，区别在于 io() 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率。不要把计算工作放在 io() 中，可以避免创建不必要的线程。
     * Schedulers.computation(): 计算所使用的 Scheduler。这个计算指的是 CPU 密集型计算，即不会被 I/O 等操作限制性能的操作，例如图形的计算。这个 Scheduler 使用的固定的线程池，大小为 CPU 核数。不要把 I/O 操作放在 computation() 中，否则 I/O 操作的等待时间会浪费 CPU。
     * 另外， Android 还有一个专用的 AndroidSchedulers.mainThread()，它指定的操作将在 Android 主线程运行。
     * 有了这几个 Scheduler ，就可以使用 subscribeOn() 和 observeOn() 两个方法来对线程进行控制了。 * subscribeOn(): 指定 subscribe() 所发生的线程，即 Observable.OnSubscribe 被激活时所处的线程。或者叫做事件产生的线程。 * observeOn(): 指定 Subscriber 所运行在的线程。或者叫做事件消费的线程。
     */
    private void rxJavaDemo3() {
        // Demo 1
        Observable.just(1, 2, 3, 4)
                .subscribeOn(Schedulers.io())   // 指定 subscribe() 发生在IO线程
                .observeOn(AndroidSchedulers.mainThread())  // 指定Subscriber的回调发生在主线程
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer number) {
                        Log.d(TAG, "number:" + number);
                    }
                });

        // Demo 2
        // 由ID取得图片并显示
        final int drawableres = R.mipmap.ic_launcher;
        mImageView = new ImageView(this);
        Observable.create(new Observable.OnSubscribe<Drawable>() {
            @Override
            public void call(Subscriber<? super Drawable> subscriber) {
                Drawable drawable = getResources().getDrawable(drawableres);
                Log.d(TAG, "call drawable=" + drawable);
                subscriber.onNext(drawable);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())   // 指定subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread())  // 指定Subscriber 的回调发生在主线程
                .subscribe(new Observer<Drawable>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(RxJavaDemoActivity.this, "error=" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Drawable drawable) {
                        mImageView.setImageDrawable(drawable);
                        Log.d(TAG, "setImageDrawable onNext() drawable=" + drawable);
                    }
                });

    }

    /**
     * RxJavaDemo
     * 基于同步,线性前提下的RXJava使用
     */
    private void rxJavaDemo2() {
        // 打印字符串数组
        String[] names = {"1", "2", "3"};
        Observable.from(names).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d(TAG, "结果是:" + s);
            }
        });

        // 由ID取得图片并显示
        final int drawableres = R.mipmap.ic_launcher;
        mImageView = new ImageView(this);
        Observable.create(new Observable.OnSubscribe<Drawable>() {
            @Override
            public void call(Subscriber<? super Drawable> subscriber) {
                Drawable drawable = getResources().getDrawable(drawableres);
                Log.d(TAG, "call drawable=" + drawable);
                subscriber.onNext(drawable);
                subscriber.onCompleted();
            }
        }).subscribe(new Observer<Drawable>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(RxJavaDemoActivity.this, "error=" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(Drawable drawable) {
                mImageView.setImageDrawable(drawable);
                Log.d(TAG, "setImageDrawable onNext() drawable=" + drawable);
            }
        });


    }

    private void demo1() {
        // 该例子中,模拟了一个从N个文件夹中,每个文件夹中有N张图片,将这些图片全部加载到内存中转化为bitmap对象的伪代码逻辑
        /*
        new Thread() {
            @Override
            public void run() {
                super.run();
                for (File folder : folders) {
                    File[] files = folder.listFiles();
                    for (File file : files) {
                        if (file.getName().endsWith(".png")) {
                            final Bitmap bitmap = getBitmapFromFile(file);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    imageCollectorView.addImage(bitmap);
                                }
                            });
                        }
                    }
                }
            }
          }.start();
         */


        // 而使用RxJava的方式去做会使用如下代码
        /*
        Observable.from(folders)
            .flatMap(new Func1<File, Observable<File>>() {
                @Override
                public Observable<File> call(File file) {
                    return Observable.from(file.listFiles());
                }
            })
            .filter(new Func1<File, Boolean>() {
                @Override
                public Boolean call(File file) {
                    return file.getName().endsWith(".png");
                }
            })
            .map(new Func1<File, Bitmap>() {
                @Override
                public Bitmap call(File file) {
                    return getBitmapFromFile(file);
                }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<Bitmap>() {
                @Override
                public void call(Bitmap bitmap) {
                    imageCollectorView.addImage(bitmap);
                }
            });
         */

        new Managersss().getA().getB();
    }

    private class XXX extends ViewGroup {
        public XXX(Context context) {
            super(context);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {

        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }


    private class Managersss {
        Managersss getA() {
            return null;
        }

        Managersss getB() {
            return null;
        }
    }

}
