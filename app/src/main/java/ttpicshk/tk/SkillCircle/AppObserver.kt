package ttpicshk.tk.SkillCircle

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class AppObserver(val lifecycle: Lifecycle):LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun activityStart(){
        Log.d("AppObserver","activity Start")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun activityPause(){
        Log.d("AppObserver","activity Pause")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun activityStop(){
        Log.d("AppObserver","activity Stop")
    }

}