<?php
declare (strict_types = 1);

namespace app\common\validate;

use think\Validate;

class CeshiValidate extends BaseValidate
{
    protected $rule = [
        'username'=>'require',
        'email'=>'email|require' 
    ];

    protected $message = [
        'username.require'=>'用户名不能为空',
        'email.email'=>'邮箱格式不正确',
        'email,require'=>'邮箱不能为空'
    ];
    
    protected $scene = [
        'login'=> ['username']
    ];
}
