package project.iksandecade.activitymanager;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by
 * Name         : Ihksan Sukmawan
 * Email        : iksandecade@gmail.com
 * Company      : Meridian.Id
 * Date         : 03/11/16
 * Project      : ActivityManager
 */

public class Services extends Service {
    Timer timer = new Timer();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//       callAsynchronousTask();
//        Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
        List<ProcessManager.Process> processes = ProcessManager.getRunningApps();
        for (int i = 0; i < processes.size(); i++) {
            Log.d("Executed app", "Application executed : " + processes.get(i).name);
        }
        return START_STICKY;
    }

    public void callAsynchronousTask() {
        final Handler handler = new Handler();
        timer = new Timer();

        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                        final List<ActivityManager.RunningAppProcessInfo> recentTasks = activityManager.getRunningAppProcesses();

                        for (int i = 0; i < recentTasks.size(); i++) {
                            Log.d("Executed app", "Application executed : " + recentTasks.get(i).processName);
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 2000); //execute in every 50000 ms

    }
}
