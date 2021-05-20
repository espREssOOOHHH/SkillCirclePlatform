<?php

namespace app\api\controller\v1;

use think\Request;
use app\common\controller\BaseController;
use app\common\validate\UserValidate;
use app\common\model\User as UserModel;
use think\facade\Config;

class User extends BaseController
{
    
    public function sendCode(){
        (new UserValidate())->goCheck('sendcode');
        (new UserModel())->sendCode();
        return self::showResCodeWithOutData('发送成功！');
        //return '1111';
    }

  //  \think\Config::get('aliSMS.expire')
    public function phoneLogin(){
        (new UserValidate())->goCheck('phonelogin');
        $token = (new UserModel())->phoneLogin();
        return self::showResCode('登录成功',['token'=>$token]);
        //return 'shoujisenglu';
    }

    public function login(){
        (new UserValidate())->goCheck('login');
        $token = (new UserModel())->login();
        return self::showResCode('登录成功',['token'=>$token]);
        //return 11111;
    }

    public function otherLogin(){
        (new UserValidate())->goCheck('otherlogin');
        $token = (new UserModel())->otherlogin();
        return self::showResCode('登录成功',['token'=>$token]);
    }

    public function logout(){
        //halt(request());
        (new UserModel())->logout();
        return self::showResCodeWithOutData('退出成功');
       // return '退出登陆';
    }
}
