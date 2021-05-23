package ttpicshk.tk.SkillCirlce

object Account {
    private var userName = String()
    private var passWord=String()
    private var phoneNumber= String()
    private var isLogin=false
    private var photoId:Int = 0

    fun userName():String= userName
    fun userPhoto():Int= photoId

    fun LogIn(username:String,password:String,phone:String) :Boolean{
        isLogin=true
        userName=username
        passWord=password
        phoneNumber=phone
        photoId=R.drawable.grape
        return true
    }
    fun LogOut():Boolean{
        isLogin=false
        return true
    }

    fun IsOnLine():Boolean{
        return isLogin
    }
}