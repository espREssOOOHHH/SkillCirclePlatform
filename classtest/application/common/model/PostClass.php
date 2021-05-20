<?php

namespace app\common\model;

use think\Model;

class PostClass extends Model
{
    public function getPostClassList(){
        return $this->field('id,classname')->where('status',1)->select();
    }
}
