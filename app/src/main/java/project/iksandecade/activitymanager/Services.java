package project.iksandecade.activitymanager;

import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

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
//        callAsynchronousTask();
        Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
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
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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

    private String getProcessName() {
        String foregroundProcess = "";
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(ACTIVITY_SERVICE);
        // Process running
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager mUsageStatsManager = (UsageStatsManager) getSystemService(USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            // We get usage stats for the last 10 seconds
            List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 10, time);
            // Sort the stats by the last time used
            if (stats != null) {
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                for (UsageStats usageStats : stats) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (mySortedMap != null && !mySortedMap.isEmpty()) {
                    String topPackageName = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                    foregroundProcess = topPackageName;
                }
            }
        } else {
            @SuppressWarnings("deprecation") ActivityManager.RunningTaskInfo foregroundTaskInfo = activityManager.getRunningTasks(1).get(0);
            foregroundProcess = foregroundTaskInfo.topActivity.getPackageName();

        }
        return foregroundProcess;
    }

}
