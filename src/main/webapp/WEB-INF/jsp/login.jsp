<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>登录 - 设备巡检系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/lib/bootstrap.min.css">
    <style>
        body { background: linear-gradient(135deg, #1a73e8, #0b3d91); min-height: 100vh;
               display: flex; align-items: center; justify-content: center; }
        .login-box { width: 380px; padding: 36px; background: rgba(255,255,255,0.96);
                     border-radius: 12px; box-shadow: 0 20px 40px rgba(0,0,0,0.2); }
        .login-box h2 { text-align: center; margin-bottom: 24px; color: #1a73e8; }
        .tip { text-align: center; font-size: 12px; color: #888; margin-top: 16px; }
    </style>
</head>
<body>
<div class="login-box">
    <h2>设备巡检系统</h2>
    <% if (request.getParameter("error") != null) { %>
        <div class="alert alert-danger" role="alert">用户名或密码错误</div>
    <% } %>
    <form id="loginForm">
        <div class="mb-3">
            <label class="form-label">用户名</label>
            <input type="text" class="form-control" name="username" value="admin" required autofocus>
        </div>
        <div class="mb-3">
            <label class="form-label">密码</label>
            <input type="password" class="form-control" name="password" value="123456" required>
        </div>
        <button type="submit" class="btn btn-primary w-100">登录</button>
    </form>
    <div class="tip">admin / 123456 &nbsp;|&nbsp; inspector / 123456 &nbsp;|&nbsp; repairer / 123456</div>
</div>
<script src="${pageContext.request.contextPath}/lib/jquery.min.js"></script>
<script>
$("#loginForm").on("submit", function(e){
    e.preventDefault();
    $.ajax({
        url: "${pageContext.request.contextPath}/api/auth/login",
        type: "POST",
        data: $(this).serialize(),
        dataType: "json",
        success: function(res){
            if (res.code === 200) {
                window.location.href = "${pageContext.request.contextPath}/";
            } else {
                alert(res.message || "登录失败");
            }
        },
        error: function(){ alert("网络错误"); }
    });
});
</script>
</body>
</html>
