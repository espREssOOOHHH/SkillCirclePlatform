<?php
namespace app\common\controller;

use think\Request;
use think\Controller;

class BaseController extends Controller
{
    static public function showResCode($msg = '未知' , $data = [] , $code = 200){
        $res = [
            'msg'=>$msg,
            'data'=>$data
        ];
        return json($res,$code);
    }

    static public function showResCodeWithOutData($msg = '未知' , $code = 200){
        return self::showResCode($msg,[],$code);
    }
}
