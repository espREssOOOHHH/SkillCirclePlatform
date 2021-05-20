<?php

namespace app\api\controller\v1;

use think\Controller;
use think\Request;
use app\common\model\Topic as TopicModel;
use app\common\controller\BaseController;

class Topic extends BaseController
{
    public function index()
    {
        $list = (new TopicModel())->gethotlist();
        return self::showResCode('获取成功',['list'=>$list]);
    }

}