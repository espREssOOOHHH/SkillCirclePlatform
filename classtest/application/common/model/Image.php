<?php

namespace app\common\model;

use think\Model;
use \app\common\controller\FileController;

class Image extends Model
{
    protected $autoWriteTimestamp = true;
    protected $updateTime = false;
    public function uploadMore(){
        $image = $this->upload(request()->userId,'imglist');
        $imageCount = count($image);
        for ($i = 0; $i < $imageCount; $i++) { 
            //20160820/42a79759f284b767dfcb2a0197904287.jpg
            $image[$i]['url'] = getFileUrl($image[$i]['url']);
        }
        return $image;
    }
    
    public function upload($userid = '',$field = ''){
        $files = request()->file($field);
        if (is_array($files)) {
            $arr = [];
            foreach($files as $file){
                $res = FileController::UploadEvent($file);
                if ($res['status']) {
                    $arr[] = [
                        'url'=>$res['data'],
                        'user_id'=>$userid
                    ];
                }
            }
            return $this->saveAll($arr);
        }
        if(!$files) TApiException('请选择要上传的图片',10000,200);
        $file = FileController::UploadEvent($files);
        if(!$file['status']) TApiException($file['data'],10000,200);
        return self::create([
            'url'=>$file['data'],
            'user_id'=>$userid
        ]);
    }
}
