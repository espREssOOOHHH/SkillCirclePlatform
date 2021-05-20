<?php

namespace app\common\validate;

use think\Validate;

class TopicClassValidate extends BaseValidate
{	
	protected $rule = [
        'id'=>'require|integer|>:0',
        'page'=>'require|integer|>:0',
    ];
    protected $message = [
        'id.require' => 'id不能为空',
        'id.integer' => 'id格式错误',
        'page.require' => 'page不能为空',
        'page.integer' => 'page格式错误',
    ];
}
