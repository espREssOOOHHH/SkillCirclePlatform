<?php

namespace app\common\model;

use think\Model;

class TopicClass extends Model
{
    public function getTopicClassList(){
        return $this->field('id,classname')->where('status',1)->select();
    }
    public function topic(){
        return $this->hasMany('Topic');
    }
    public function getTopic(){
        $param = request()->param();
        //halt($param);
        return self::get($param['id'])->topic()->page($param['page'],1)->select();
    }
}
