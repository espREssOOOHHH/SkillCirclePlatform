<?php

namespace app\common\controller;

use think\Request;

class FileController 
{
    static public function UploadEvent($files,$size = '5442880',$ext = 'jpg,png,gif,jpeg',$path = 'uploads'){
        $info = $files->validate(['size'=>$size,'ext'=>$ext])->move($path);
        return [
            'data'=> $info ? $info->getPathname() : $files->getError(),
            'status'=> $info ? true :false
        ];
    }
}
