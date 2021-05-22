package ttpicshk.tk.SkillCirlce

class Account {
    private var userName = String()
    private var passWord=String()
    private var phoneNumber= String()
    private var isLogin=false

    fun LogIn(username:String,password:String,phone:String) :Boolean{
        isLogin=true
        userName=username
        passWord=password
        phoneNumber=phone

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