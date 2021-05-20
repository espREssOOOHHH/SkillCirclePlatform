<?php

namespace app\lib\exception;

use Exception;
use think\exception\Handle;
use think\Response;
use Throwable;
use think\facade\Config;

class ExceptionHandler extends Handle{
    public $code;
    public $msg;
    public $errorCode;
    public function render(exception $e)
    {
        if ($e instanceof BaseException) {
            $this ->code = $e->code;
            $this ->msg = $e->msg;
            $this ->errorCode = $e->errorCode;
        }else{
            if(Config::get('app.app_debug'))return parent::render($e);
          //  $ac=config(env('APP_DEBUG',''));
            
            $this ->code = 500;
            $this ->msg = '服务器异常';
            $this ->errorCode = '999';
        }

        $res = [
            'msg'=>$this->msg,
            'errorCode'=>$this->errorCode
        ];

        return json($res,$this->code);
    }

}

