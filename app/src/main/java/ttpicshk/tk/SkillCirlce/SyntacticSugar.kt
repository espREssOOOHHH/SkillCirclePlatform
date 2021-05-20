package ttpicshk.tk.SkillCirlce

import android.content.Context
import android.content.Intent
import android.widget.Toast

fun String.showToast(context: Context, duration:Int= Toast.LENGTH_SHORT){
    Toast.makeText(context,this,duration).show()
}
infix fun <T>Collection<T>.has(element:T)=contains(element)
infix fun Int.division(divisor:Int):Float=this.toFloat().div(divisor.toFloat())
infix fun Int.multiply(multiplier:Int):Float=this.toFloat().times(multiplier.toFloat())

inline fun <reified T>startActivity(context:Context){
    val intent= Intent(context,T::class.java)
    context.startActivity(intent)
}

inline fun <reified T>startActivity(context: Context,block:Intent.()->Unit){
    val intent=Intent(context,T::class.java)
    intent.block()
    context.startActivity(intent)
}