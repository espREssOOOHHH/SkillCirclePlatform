<?php
namespace app\common\validate;

use think\Validate;
use app\lib\exception\BaseException;

class BaseValidate extends Validate{
    public function goCheck($scene = ''){
        $params = request()->param();
       // halt(request());
       //halt($params);
        $check = empty($scene)?
            $this->check($params):
            $this->scene($scene)->check($params);
        if(!$check){
            throw new BaseException(['msg'=>$this->getError(),'errorCode'=>10000,'code'=>400]);
        }
    }

    protected function isPefectCode($value, $rule='', $data='', $field='')
    {
        $beforeCode = cache($data['phone']);
        if(!$beforeCode) return "请重新获取验证码";
        if($value != $beforeCode) return "验证码错误";
        return true;
    }

}