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
$(function () {
    var $button = $("button");
    $button.prop("disabled", true);
    $("#phonenumber").focus(function () {
        if($("#phonenumber").val().length==0){
            $("#getphonenumber").attr("disabled",true);
        }
        $("body").delegate("#phonenumber", "propertychange input", function () {
            var $phonenumber = $("#phonenumber").val();
            if (phoneCheck($phonenumber)) {
                $(".numbercheckwarning").html("");
                $("#getphonenumber").prop("disabled", false);
            } else {
                $(".numbercheckwarning").html("手机号码格式不正确");
                $("#getphonenumber").prop("disabled", true);
            }
        });
    });
    $("#getphonenumber").click(function () {
        var curCount = 60;//倒计时时间10秒
        var intervalId = setInterval(SetRemainTime,"1000");
        $("#getphonenumber").attr("disabled",true);
        function SetRemainTime() {
            if (curCount == 0) {
                clearInterval(intervalId);//停止计时器
                $("#getphonenumber").removeAttr("disabled");//启用按钮
                $("#getphonenumber").text("重新发送验证码");
            } else {
                curCount--;
                $("#getphonenumber").text(curCount + "秒后重新获取验证码");
            }
        }
        var $phonenumber =$("#phonenumber").val();
        $.ajax({
            type: 'post',
            contentType: 'application/json;charset=UTF-8',
            data: JSON.stringify({"nonce": $nonce,"phonenumber":$phonenumber}),
            url: '/getPhoneNumber',
            dataTye: "json",
            success: function (data, status) {
                if (data.msg == 'success') {
                    console.log("发送短信请求成功");
                    //发送成功 注册按钮变为可用
                    $("#registersubmit").attr("disabled",false);
                } else {
                    console.log("发送短信请求失败");
                    clearInterval(intervalId);
                    $("#getphonenumber").text("发送验证码");
                    $("#getphonenumber").attr("disabled",false);
                }
            },
            error: function (data, status) {
                alert("发送短信ajax请求错误");
            }
        });

    });
});
var $nonce = "";
var $rsapub = "";
var flag = false;
$(function () {
    //检查用户名是否可用
    $("#input_username").blur(function () {
        var $username = $("#input_username").val();
        if ($username.indexOf(" ") >= 0) {
            $(".verfUne").html("用户名不能包含空格");
            $(".submit-btn").prop("disabled", true);
        } else {
            $(".verfUne").html("");
            if ($username == "") {
                console.log("用户名为空");
            } else {
                $.ajax({
                    type: 'post',
                    contentType: "application/json;charset=UTF-8",
                    data: JSON.stringify({"identity":$("#identity option:selected").val(),"username": $username}),
                    url: "/registerUsernameCheck",
                    dataType: "json",
                    success: function (data, status) {
                        if (data.result == "yes") {
                            $(".verfUne").html("用户名可使用");
                            $(".verfUne").css("color", "green");
                        } else {
                            $(".verfUne").html("用户名已被注册");
                            $(".submit-btn").prop("disabled", true);
                        }
                    },
                    error: function (data, status) {
                        console.log(status);
                        alert("ajax请求错误");
                    }
                });
            }
        }
    });
    //检查前后密码是否一致
    $("body").delegate("#twice_password", "propertychange input", function () {
        var $once = $("#once_password").val();
        var $twice = $("#twice_password").val();
        if ($once != $twice) {
            $(".passwordcheckwarning").html("前后密码不一致");
            $(".submit-btn").prop("disabled", true);
        } else {
            var n_timestamp = new Date().getTime();
            $(".passwordcheckwarning").html("");
            $.ajax({
                type: 'post',
                contentType: 'application/json;charset=UTF-8',
                url: '/getKey',
                dataType: 'json',
                success: function (data, status) {
                    console.log(data.nonce); //展示 nonce
                    $nonce = data.nonce;
                    console.log(data.rsaPub); //展示 rsaPub
                    console.log(data.timestamp);
                    var bg_timestamp = data.timestamp;
                    //检查 后台发送的timestamp是否过期
                    var sub = bg_timestamp - n_timestamp;
                    if (sub / 1000 > 5) {
                        //下发的timestamp过期
                        alert("下发内容过期，请刷新页面后重试");
                    }
                    $rsapub = "-----BEGIN PUBLIC KEY-----" + data.rsaPub + "-----END PUBLIC KEY-----";
                    //$sendmessage.prop("disabled", false);
                },
                error: function (status) {
                    alert(status);
                }
            });
        }
    });
    //提交注册请求
    $("#registersubmit").click(function () {
        if($rsapub==""){
            console.log("未获得公钥");
            return false;
        }else{
            var submit_time = new Date().getTime();
            var content_ori = $("#input_username").val() + "|" + $("#once_password").val() + "|" + $("#phonenumber").val() + "|" + submit_time + "|" + $("#verificationCode").val();
            console.log(content_ori);
            var encrypt = new JSEncrypt();
            encrypt.setPublicKey($rsapub);
            var content_encrypt = encrypt.encrypt(content_ori);
            $("#input_username").attr("disabled", true);
            $("#once_password").attr("disabled", true);
            $("#twice_password").attr("disabled", true);
            $("#phonenumber").attr("disabled", true);
            $("#verificationCode").attr("disabled", true);
            $("#encrypt_content").val(content_encrypt);
            console.log(content_encrypt);
        }
    });
});
//检查密码是否前后一致
//获取rsa公钥
//获取nounce 防止滥用发送验证码
//请求发送手机验证码