<?php
namespace app\common\model;

use think\Model;
use think\facade\Cache;
use app\lib\exception\BaseException;
use think\facade\Config;

class User extends Model
{
    protected $autoWriteTimestamp = true;
    protected $updateTime = false;
    public function sendCode(){
        $phone = request()->param('phone');
        // Cache::set('19912454150','2342',60)
        if(Cache::get($phone)) TApiException('你操作得太快了',30001,200);
        $code = random_int(1000,9999);
        //dump(Config::get('config.aliSMS.isopen'));
        if(!Config::get('config.aliSMS.isopen')){
            Cache::set($phone,$code,Config::get('config.aliSMS.expire'));
            Cache::set($phone,$code,60);
            TApiException('验证码：'.$code,30005,200);
    }
        // $res = AlismsController::SendSMS($phone,$code);
        if($res['Code']=='OK') return Cache::set($phone,$code,config('api.aliSMS.expire'));
        if($res['Code']=='isv.MOBILE_NUMBER_ILLEGAL')  TApiException('无效号码',30002,200);
        if($res['Code']=='isv.DAY_LIMIT_CONTROL') TApiException('今日你已经发送超过限制，改日再来',30003,200);
        throw TApiException('发送失败',30004,200);
    }

    public function userinfo(){
        return $this->hasOne('UserInfo');
    }

    public function userbind(){
        return $this->hasMany('UserBind');
    }

    public function isExist($arr = []){
        if(!is_array($arr)) return false;
        if (array_key_exists('phone',$arr)){
            return $this->where('phone',$arr['phone'])->find();
        }

        if (array_key_exists('id',$arr)) { 
            return $this->where('id',$arr['id'])->find();
        }
        if (array_key_exists('email',$arr)) { 
            return $this->where('email',$arr['email'])->find();
        }
        if (array_key_exists('username',$arr)) {
            return $this->where('username',$arr['username'])->find();
        }

        if (array_key_exists('provider',$arr)) {
            $where = [
                'type'=>$arr['provider'],
                'openid'=>$arr['openid']
            ];
            return $this->userbind()->where($where)->find();
        }

        return false;
    }

    public function phoneLogin(){
        $param = request()->param();
        $user = $this->isExist(['phone' => $param['phone']]);
        if(!$user){
            $user = self::create([
                'username'=> $param['phone'],
                'phone'=> $param['phone'],
                 'password'=>password_hash($param['phone'],PASSWORD_DEFAULT)
            ]);
            $user->userinfo()->save([ 'user_id' => $user->id ]);
            //halt($user);
            //dump($user);
            return $this->CreateSaveToken($user->toArray());
        }
        $this->checkStatus($user->toArray());
        return $this->CreateSaveToken($user->toArray());
    }
    
    public function CreateSaveToken($arr = []){
        $token = sha1(md5(uniqid(md5(microtime(true)),true)));
        $arr['token'] = $token;
        $expire = array_key_exists('expires_in',$arr) ? $arr['expires_in'] : Config::get('config.token_expire');
        if (!Cache::set($token,$arr,$expire)) TApiException('缓存函数异常',10001,200);
        return $token;
    }
    
    public function checkStatus($arr,$isReget = false){
        $status = 1;
        if ($isReget) {
            $userid = array_key_exists('user_id',$arr)?$arr['user_id']:$arr['id'];
            if ($userid < 1) return $arr; //初次第三方登陆，未绑定手机号默认值user_id=0
            $user = $this->find($userid)->toArray();
            $status = $user['status'];
        }else{
            $status = $arr['status'];
        }
        if($status == 0)TApiException('该用户已被禁用',20001,200);
        return $arr;
    }

    public function login(){
        $param = request()->param();
        $user = $this->isExist($this->filterUserData($param['username']));
        if(!$user) TApiException('昵称/邮箱/手机号错误',20000,200);
        $this->checkStatus($user->toArray());
        $this->checkPassword($param['password'],$user->password);
        return $this->CreateSaveToken($user->toArray());
    }

    public function filterUserData($data){
        $arr=[];
        if(preg_match('/^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/', $data)){
            $arr['phone']=$data; 
            return $arr;
        }
        if(preg_match('/^[_a-z0-9-]+(\.[_a-z0-9-]+)*@[a-z0-9-]+(\.[a-z0-9-]+)*(\.[a-z]{2,})$/', $data)){
            $arr['email']=$data; 
            return $arr;
        }
        $arr['username']=$data; 
        return $arr;
    }

    public function checkPassword($password,$hash){
        if (!$hash) TApiException('密码错误',20002,200);
        if(!password_verify($password,$hash)) TApiException('密码错误',20002,200);
        return true;
    }

    public function otherlogin(){
        $param = request()->param();
        // 解密过程（待添加）
        $user = $this->isExist(['provider'=>$param['provider'],'openid'=>$param['openid']]);
        $arr = [];
        if (!$user) {
            //$user = model('UserBind');
            $user = new userbind([
                'type'=>$param['provider'],
                'openid'=>$param['openid'],
                'nickname'=>$param['nickName'],
                'avatarurl'=>$param['avatarUrl'],
            ]);
            $user->save();
            $arr = $user->toArray();
           // dump($arr);
            $arr['expires_in'] = $param['expires_in']; 
            return $this->CreateSaveToken($arr);
        }
        $arr = $this->checkStatus($user->toArray(),true);
        $arr['expires_in'] = $param['expires_in']; 
        return $this->CreateSaveToken($arr);
    }
    //暂未使用requestparam中已有数据
    public function OtherLoginIsBindPhone($user){
        if(array_key_exists('type',$user)){
            if($user['user_id']<1){
                TApiException('请先绑定手机！',20005,200);
            }
            return $user['user_id'];
        }
        return $user['id'];
    }

    public function logout(){
        if (!Cache::pull(request()->userToken)) TApiException('你已经退出了',30006,200);
        return true;
    }

}