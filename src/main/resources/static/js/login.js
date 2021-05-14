function getAESKey(){
    var result = "";
    for(var i=0;i<16;++i){
        var charOrNum;
        var randomNumber = Math.round(Math.random()*10);
        if(randomNumber%2==0){
            charOrNum = "char";
        }else{
            charOrNum = "num";
        }
        if(charOrNum=="char"){
            result += String.fromCharCode(Math.round(Math.random()*25)+65);
        }else{
            result += Math.round(Math.random()*9).toString();
        }
    }
    return result;
}
function encryptByAES(data,k,i){
    var key = CryptoJS.enc.Latin1.parse(k);
    var iv = CryptoJS.enc.Latin1.parse(i);
    return CryptoJS.AES.encrypt(data,key,{
        iv:iv,
        mode:CryptoJS.mode.CBC,
        padding:CryptoJS.pad.ZeroPadding
    }).toString();
}
function decryptByAES(data,k,i) {
    var key = CryptoJS.enc.Latin1.parse(k);
    var iv = CryptoJS.enc.Latin1.parse(i);
    var decrypt = CryptoJS.AES.decrypt(data, key, { iv: iv, mode: CryptoJS.mode.CBC, padding:CryptoJS.pad.ZeroPadding});
    var decryptedStr = decrypt.toString(CryptoJS.enc.Utf8);
    return decryptedStr.toString();
}
function phoneCheck(phones) {
    var myreg = /^[1][3,4,5,7,8,9][0-9]{9}$/;
    if (!myreg.test(phones)) {
        console.log('手机号格式不正确');
        return false;
    } else {
        console.log('手机号格式正确');
        return true;
    }
}
$(function(){
    var $button = $("#login");
    $button.prop("disabled",true);
    var AES_KEY;
    var AES_IV;
    var encrypt_key;
    var encrypt_iv;
    $("#input_password").focus(function(){
        var n_timestamp = new Date().getTime();
        $.ajax({
            type:'post',
            contentType:'application/json;charset=UTF-8',
            url:'/getKey',
            dataType:'json',
            success:function (data,status) {
                console.log(data.nonce); //展示 nonce
                console.log(data.rsaPub); //展示 rsaPub
                console.log(data.timestamp);
                var bg_timestamp = data.timestamp;
                //检查 后台发送的timestamp是否过期
                var sub = bg_timestamp-n_timestamp;
                if(sub/1000>5){
                    //下发的timestamp过期
                    console.log("下发内容过期，请刷新页面后重试");
                }
                var rsapub = "-----BEGIN PUBLIC KEY-----"+data.rsaPub+"-----END PUBLIC KEY-----";
                var encrypt = new JSEncrypt();
                encrypt.setPublicKey(rsapub);
                AES_KEY = getAESKey();
                AES_IV = getAESKey();
                console.log(AES_KEY);
                console.log(AES_IV);
                encrypt_key = encrypt.encrypt(AES_KEY); //rsa公钥加密AESkey
                encrypt_iv = encrypt.encrypt(AES_IV); //rsa公钥加密偏移量
                $("#encrypt_aes_key").val(encrypt_key);
                $("#encrypt_aes_iv").val(encrypt_iv);
                $button.prop("disabled",false);
            },
            error:function(status){
                alert(status);
            }
        });
    });
    $("#login").click(function(){
        var submit_time = new Date().getTime();
        var token_ori= $("#input_username").val()+"|"+$("#input_password").val()+"|"+submit_time+"|"+$("#input_remember").prop("checked");
        var token_en = encryptByAES(token_ori,AES_KEY,AES_IV);
        console.log(token_en);
        $("#encrypt_token").val(token_en);
        $("#input_username").attr("disabled",true);
        $("#input_password").attr("disabled",true);
        //进行加密并隐藏输入信息
        return true;
    });
    //弃用ajax登录请求，原因登录失败消息不回显 虽然可以在发送请求后 在success中对页面进行修改
    //但改用隐藏输入框 失去焦点加密登录的方法
    var $resetbutton = $("#reset");
    var $sendcodebutton = $("#sendCodeButton");
    $resetbutton.prop("disabled",true);
    $sendcodebutton.prop("disabled",true);
    $("#reset-phonenumber").focus(function(){
        $.ajax({
            type:'post',
            contentType:'application/json;charset=UTF-8',
            url:'/getKey',
            dataType:'json',
            success:function(data,status){
                    //TODO 重置密码功能全程加密，目前仅使用nonce验证防止发送短信功能被滥用
                    $("#reset-nonce").val(data.nonce);
                    console.log("接收到nonce:"+data.nonce);
                    //本应实时监听手机号输入情况 在此直接放行按钮，等用户点击时在检查
                    //接收到密钥 nonce，发送验证码按钮可用
                    $sendcodebutton.prop("disabled",false);
            },
            error:function(status){
                alert(status);
            }
        });
    });
    $("#sendCodeButton").click(function(){
        var $nonce = $("#reset-nonce").val();
        var $username = $("#reset-username").val();
        var $phonenumber = $("#reset-phonenumber").val();
        if($username.indexOf(" ")>=0){
            console.log("错误的用户名");
            return false;
        }
        if(phoneCheck($phonenumber)==false){
            console.log("错误的手机号");
            return false;
        }
        $.ajax({
            type:'post',
            url:'/sendCode',
            contentType: 'application/json;charset=UTF-8',
            data: JSON.stringify({"nonce":$nonce,"identity":$("#reset-identity option:selected").val(),"username": $username,"phonenumber":$phonenumber}),
            dataType:'json',
            success: function(data,status){
                if(data.msg=="success"){
                    console.log("发送验证码成功");
                    //避免重复发送，发送成功后按钮禁用
                    $sendcodebutton.prop("disabled",true);
                    //启用提交按钮
                    $resetbutton.prop("disabled",false);
                }else{
                    console.log(data.msg);
                }
            },
            error:function(status){
                alert(status);
            }
        });
    });
});