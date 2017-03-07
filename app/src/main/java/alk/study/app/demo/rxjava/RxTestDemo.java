package alk.study.app.demo.rxjava;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import alk.study.app.demo.rxjava.bean.Student;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.AsyncSubject;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;

import static android.R.id.message;
import static android.content.ContentValues.TAG;

/**
 * Subject可以看成是一个桥梁或者代理，在某些ReactiveX实现中（如RxJava），它同时充当了Observer和Observable的角色。
 * Subject:AsyncSubject、BehaviorSubject、PublishSubject和ReplaySubject
 * 每个实现类都有特定的“技能”，下面结合代码来介绍一下它们各自的“技能”。
 * 注意，所有的实现类都由create()方法实例化，无需new,所有的实现类调用onCompleted()或onError(),它的Observer将不再接收数据；
 */
public class RxTestDemo {
    /**
     * AsyncSubject
     * Observer会接收AsyncSubject的`onComplete()之前的最后一个数据，如果因异常而终止，AsyncSubject将不会释放任何数据，
     * 但是会向Observer传递一个异常通知。
     */
    public void asyncSubjectDemo1() {
        AsyncSubject<String> asyncSubject = AsyncSubject.create();
        asyncSubject.onNext("asyncSubject1");
        asyncSubject.onNext("asyncSubject2");
        asyncSubject.onNext("asyncSubject3");
        asyncSubject.onCompleted();
        asyncSubject.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "asyncSubject onCompleted");  //输出 asyncSubject onCompleted
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "asyncSubject onError");  //不输出（异常才会输出）
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "asyncSubject:" + s);  //输出asyncSubject:asyncSubject3
            }
        });
    }

    /**
     * BehaviorSubject
     * Observer会接收到BehaviorSubject被订阅之前的最后一个数据，再接收其他发射过来的数据，
     * 如果BehaviorSubject被订阅之前没有发送任何数据，则会发送一个默认数据。
     * （注意跟AsyncSubject的区别，AsyncSubject要手动调用onCompleted()，且它的Observer会接收到onCompleted()前发送的最后一个数据
     * ，之后不会再接收数据，而BehaviorSubject不需手动调用onCompleted()，它的Observer接收的是BehaviorSubject被订阅前发送的最后一个数据
     * ，两个的分界点不一样，且之后还会继续接收数据。）
     */
    public void behaviorSubject() {
        BehaviorSubject<String> behaviorSubject = BehaviorSubject.create("default");
        behaviorSubject.onNext("behaviorSubject1");
        behaviorSubject.onNext("behaviorSubject2");
        behaviorSubject.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                log("behaviorSubject:complete");
            }

            @Override
            public void onError(Throwable e) {
                log("behaviorSubject:error");
            }

            @Override
            public void onNext(String s) {
                log("behaviorSubject:" + s);
            }
        });
        behaviorSubject.onNext("behaviorSubject3");
        behaviorSubject.onNext("behaviorSubject4");
    }

    /**
     * PublishSubject比较容易理解，相对比其他Subject常用，它的Observer只会接收到PublishSubject被订阅之后发送的数据。
     */
    public void publishSubject() {
        PublishSubject<String> publishSubject = PublishSubject.create();
        publishSubject.onNext("publishSubject1");
        publishSubject.onNext("publishSubject2");
        publishSubject.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                log("publishSubject observer1:" + s);
            }
        });
        publishSubject.onNext("publishSubject3");
        publishSubject.onNext("publishSubject4");

        /**
         * 结果:Observer只会接收到"behaviorSubject3"、"behaviorSubject4"
         */
    }

    /**
     * 测试buffer 缓存 使用缓存可返回特定对象的集合
     */
    public void bufferDemo() {
        Observable.just(1, 2, 3, 4, 5 ,6,7).map(new Func1<Integer, Student>() {
            @Override
            public Student call(Integer integer) {
                // 正常来说,应该根据参数实例化student类,因为是测试所以不写了
                log(""+ integer);
                return new Student();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .buffer(2)
                .subscribe(new Action1<List<Student>>() {
                    @Override
                    public void call(List<Student> students) {
                        log("姓名" + students.get(0).name);
                    }
                });
        // 上段代码的意思是,只有在buffer区填满了指定个数的元素后,才会执行Action1里面的函数
        // 共7个元素,执行了4次 call回调


    }

    /**
     * Distinct 去掉重复的元素
     */
    public void distinctDemo() {
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Student stu1 = new Student();
            stu1.name = "321sss";
            stu1.courses = new String[]{"aaa", "bb", "cc", "dd"};
            students.add(stu1);
        }
        // 去掉重复元素distinctUntilChanged
        Observable.from(students).distinctUntilChanged(new Func1<Student, String>() {
            @Override
            public String call(Student student) {
                return student.name;
            }
        }).map(new Func1<Student, String>() {
            @Override
            public String call(Student student) {
                return student.name;
            }
        })
        .subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d(TAG,"name=" +s);
            }
        });
    }

    private void log(String message) {
        Log.d(TAG, message);
    }
}
