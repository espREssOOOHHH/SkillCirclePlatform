<?php
namespace app\http\middleware;

use app\common\model\User;

class ApiUserBindPhone
{
    public function handle($request, \Closure $next)
    {
        //$param = $request->userTokenUserInfo;
        //(new User()) -> OtherLoginIsBindPhone($param); 验证token合法时将用户数据存入requestpara 此方法暂时未使用
        if($request->userId < 1)TApiException('请先绑定手机！',20005,200);
        return $next($request);
    }
}