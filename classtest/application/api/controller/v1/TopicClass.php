<?php

namespace app\api\controller\v1;

use think\Controller;
use think\Request;
use app\common\validate\TopicClassValidate;
use app\common\controller\BaseController;
use app\common\model\TopicClass as TopicClassModel;

class TopicClass extends BaseController
{
    public function index()
    {
        $list=(new TopicClassModel())->getTopicClassList();
        return self::showResCode('获取成功',['list'=>$list]);
    }

    public function topic(){
        (new TopicClassValidate())->goCheck();
        $list=(new TopicClassModel)->getTopic();
        return self::showResCode('获取成功',['list'=>$list]);
    }

}
