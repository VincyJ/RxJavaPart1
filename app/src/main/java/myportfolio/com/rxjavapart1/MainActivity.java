package myportfolio.com.rxjavapart1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startSimpleActivity(View view) {

        demoObservableFrom();

        Observable<Integer> observable = Observable
                .just(1, 2, 3, 4, 5)
                .filter(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        //check if the number is odd? If the number is odd return true, to emmit that object.
                        return integer % 2 != 0;
                    }
                });

        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onCompleted() {
                System.out.println("All data emitted.");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("Error received: " + e.getMessage());
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("New data received: " + integer);
            }
        };

        Subscription subscription = observable
                .subscribeOn(Schedulers.io())       //observable will run on IO thread.
//                .observeOn(AndroidSchedulers.mainThread())      //Observer will run on main thread.
                .subscribe(observer);               //subscribe the observer

    }

    private void demoObservableCreate() {
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {

                subscriber.onNext(0);
                subscriber.onNext(1);
                subscriber.onNext(2);
                subscriber.onNext(3);
                subscriber.onCompleted();
            }
        }).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                Log.i("onCompleted", "-Completed-");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer integer) {
                Log.i("onNext", String.valueOf(integer));
            }
        });
    }

    private void demoObservableInterval() {
        Observable.interval(5, TimeUnit.SECONDS).subscribe(new Subscriber<Long>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Long aLong) {
                if (aLong == 5)
                    unsubscribe();
                Log.i("onNext", String.valueOf(aLong));

            }
        });
    }


    class Movie {
        public String name;

        public Movie(String name) {
            this.name = name;
        }
    }

    private void demoObservableJust() {
        Observable.just(1, 2, 3).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer integer) {
                Log.i("onNext", String.valueOf(integer));
            }
        });
    }

    private void demoObservableFrom() {

        Observable.from(new String[]{"A", "B", "C"}).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String integer) {
                Log.i("onNext", String.valueOf(integer));
            }
        });
    }

    private void demoObservableDefer() {

        /*Movie movie = new Movie("Fast & Furious 8");
        Observable<Movie> movieObservable = Observable.just(movie);
        movie = new Movie("Guardian of Galaxy");
        movieObservable.subscribe(new Subscriber<Movie>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Movie movie) {
                Log.i("onNext", movie.name);
            }
        });*/

        movie = new Movie("Fast & Furious 8");
        Observable<Movie> movieObservable = Observable.defer(new Func0<Observable<Movie>>() {
            @Override
            public Observable<Movie> call() {
                return Observable.just(movie);
            }
        });
        movie = new Movie("Guardian of Galaxy");
        movieObservable.subscribe(new Subscriber<Movie>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Movie movie) {

                Log.i("onNext", movie.name);
            }
        });
    }
}
