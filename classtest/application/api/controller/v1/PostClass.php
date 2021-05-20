<?php

namespace app\api\controller\v1;

use think\Controller;
use think\Request;
use app\common\controller\BaseController;
use app\common\model\PostClass as PostClassModel;

class postClass extends BaseController
{
    
    public function index()
    {
        //return 1111;
        $list=(new PostClassModel)->getPostClassList();
        return self::showResCode('获取成功',['list'=>$list]);
    }
}
