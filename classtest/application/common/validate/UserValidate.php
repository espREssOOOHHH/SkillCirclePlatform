<?php

namespace app\common\validate;

use think\Validate;

class UserValidate extends BaseValidate
{
    protected $regex = [ 'phoneformat' => '/^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/'];
    protected $rule = [
        'phone' => 'require|max:11|regex:phoneformat',
        'code' => 'require|number|length:4|isPefectCode',
        
        'username' => 'require',
        'password' => 'require|alphaDash',

        'provider'=>'require',
        'openid'=>'require',
        'nickName'=>'require',
        'avatarUrl'=>'require',
        'expires_in'=>'require',
    ];

    protected $message = [
        'phone.require' => '请填写手机号码',
        'phone.max' => '手机号码位数过长',
        'phone.regex' => '手机号码格式不正确',
        'username.require' => '请填写用户名',
        'password.require' => '请填写密码',
        'password.alphaDash' => '密码格式不正确',

    ];
    
    protected $scene = [
        'sendcode' => ['phone'],
        'phonelogin' => ['phone','code'],
        'login' => ['username','password'],
        'otherlogin'=>['provider','openid','nickName','avatarUrl','expires_in'],
    ];
}
