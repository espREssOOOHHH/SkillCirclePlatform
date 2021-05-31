package ttpicshk.tk.SkillCirlce

object Account {
    private var userName ="user"
    private var passWord=String()
    private var phoneNumber= String()
    private var isLogin=false
    private var photoId:Int = R.drawable.apple
    private var signature="fuck U asshole"
    private var birthday="2013-02-10"
    private var gender=2
    private var id="0"
    private var email="xxyyy@ss.com"
    private var phone="0000000000000"

    fun userName():String= userName
    fun userPhoto():Int= photoId
    fun signature():String= signature
    fun birthday():String= birthday
    fun id():String=id
    fun email():String=email
    fun phone():String=phone

    fun gender():String{
        var backValue=""
        when(gender){
            0->backValue="男"
            1->backValue= "女"
            2->backValue="保密"
        }
        return backValue
    }

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