package gr.jvoyatz.foodrecipes;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class AppExecutors {

    private static AppExecutors instance;

    public static AppExecutors getInstance(){
        if(instance == null){
            instance = new AppExecutors();
        }
        return instance;
    }

    private AppExecutors(){}

    //an executor is a class used to execute runnable tasks
    //a scheduled executor service is a class that can schedule commands to run after a certain delay
    // pool of 3 threads
    private final ScheduledExecutorService mNetworkIO = Executors.newScheduledThreadPool(3);
    private Executor mBackgroundExecutor = Executors.newSingleThreadExecutor();

    public ScheduledExecutorService networkIO(){
        return mNetworkIO;
    }

}
