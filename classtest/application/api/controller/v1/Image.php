<?php

namespace app\api\controller\v1;

use think\Controller;
use think\Request;
use app\common\controller\BaseController;
use app\common\model\Image as ImageModel;

class Image extends BaseController
{
    public function uploadMore(){
        $list = (new ImageModel())->uploadMore();
        return self::showResCode('上传成功',['list' => $list]);
        //return '上传成功';
    }
    

}
