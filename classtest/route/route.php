<?php
use think\facade\Route;
use think\facade\Config;

// +----------------------------------------------------------------------
// | ThinkPHP [ WE CAN DO IT JUST THINK ]
// +----------------------------------------------------------------------
// | Copyright (c) 2006~2018 http://thinkphp.cn All rights reserved.
// +----------------------------------------------------------------------
// | Licensed ( http://www.apache.org/licenses/LICENSE-2.0 )
// +----------------------------------------------------------------------
// | Author: liu21st <liu21st@gmail.com>
// +----------------------------------------------------------------------
Route::group('api/:version',function(){
	Route::post('user/sendcode','api/:version.User/sendCode');
    Route::post('user/phonelogin','api/:version.User/phoneLogin');
    Route::post('user/login','api/:version.User/login');
    Route::post('user/otherlogin','api/:version.User/otherLogin');
    Route::get('postclass', 'api/:version.PostClass/index');
    Route::get('topicclass','api/:version.TopicClass/index');
    Route::get('hottopic','api/:version.Topic/index');
    Route::get('topicclass/:id/topic/:page', 'api/:version.TopicClass/topic');
});
//dump(\think\Config::get('ApiUserAuth'));
Route::group('api/:version/',function(){
    Route::post('user/logout','api/:version.User/logout');
})->middleware([Config::get('middlewareconf.ApiUserAuth')]);

// 验证token\是否绑定手机\用户是否禁用
Route::group('api/:version/',function(){
    //Route::post('user/logout','api/:version.User/logout');
    Route::post('image/uploadmore','api/:version.Image/uploadMore');
})->middleware([Config::get('middlewareconf.ApiUserAuth'),Config::get('middlewareconf.ApiUserBindPhone'),Config::get('middlewareconf.ApiUserStatus')]);

