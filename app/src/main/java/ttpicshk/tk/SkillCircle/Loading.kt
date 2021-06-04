package ttpicshk.tk.SkillCircle

import android.app.Dialog
import android.content.Context
import android.view.Window

object Loading {
    lateinit var dialog: Dialog
    fun start(context:Context){
        dialog= Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_loading)
        dialog.show()
    }
    fun stop(){
        dialog.dismiss()
    }
}