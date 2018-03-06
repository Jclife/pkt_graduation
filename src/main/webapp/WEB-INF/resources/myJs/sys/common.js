function quitLogin() {
    var userName = $("#sessionUserName").text();
    $.ajax({
        url:'/pkt/exit_login',
        type:'post',
        async:true,
        data:{
            userName:userName
        },
        beforeSend:function (xhr) {
            console.log(xhr+"   开始发送前");
        },
        success:function (data) {
            if (data=="LogOut"){
                layer.tips(userName+"已经登出");
                $(location).prop('href', '/pkt/index')
            }else if (data=="noUser"){
                layer.tips(userName+"账号没有登录");
                $(location).prop('href', '/pkt/index')
            }
        },
        error:function (data) {
            layer.tips("发生未知错误");
            console.log(data)
        },
        complete:function () {
            console.log("结束")
        }
    })
}