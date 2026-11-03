<#-- 公共布局宏：左侧导航 + 顶栏 + 内容区 -->
<#macro layout title="" active="">
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>${title} - 设备巡检系统</title>
<link rel="stylesheet" href="/lib/bootstrap.min.css">
<#-- 必须在 head 中引入，保证各业务页面内联脚本执行时 $ / bootstrap 已可用 -->
<script src="/lib/jquery.min.js"></script>
<script src="/lib/bootstrap.bundle.min.js"></script>
<style>
  body { background:#f5f7fa; }
  .sidebar { width:220px; background:#2c3e50; min-height:100vh;
             position:fixed; left:0; top:0; padding-top:20px; }
  .sidebar a { color:#ecf0f1; padding:12px 20px; display:block;
               text-decoration:none; border-left:3px solid transparent; }
  .sidebar a:hover, .sidebar a.active { background:#34495e; border-left-color:#1a73e8; }
  .main { margin-left:220px; padding:20px; }
  .topbar { background:#fff; padding:12px 24px; border-bottom:1px solid #e0e0e0;
            display:flex; justify-content:space-between; align-items:center; }
  .stat-card { background:#fff; border-radius:8px; padding:20px; text-align:center;
               box-shadow:0 2px 8px rgba(0,0,0,0.05); }
  .stat-card .num { font-size:28px; font-weight:600; color:#1a73e8; }
</style>
</head>
<body>
<aside class="sidebar">
  <h6 style="color:#ecf0f1;padding:0 20px 16px">设备巡检系统</h6>
  <a href="/" class="${active?contains('index')?string('active','')}">首页看板</a>
  <a href="/device/list" class="${active?contains('device')?string('active','')}">设备台账</a>
  <a href="/repair/list" class="${active?contains('repair')?string('active','')}">报修工单</a>
  <a href="/inspection/plan" class="${active?contains('plan')?string('active','')}">巡检计划</a>
  <a href="/inspection/record" class="${active?contains('record')?string('active','')}">巡检记录</a>
  <a href="/maintenance/list" class="${active?contains('maintenance')?string('active','')}">维保记录</a>
  <a href="/notification/list" class="${active?contains('notification')?string('active','')}">通知中心</a>
  <a href="/user/profile" class="${active?contains('profile')?string('active','')}">个人中心</a>
</aside>
<div class="main">
  <div class="topbar">
    <strong>${title}</strong>
    <div>
      <span id="userInfo">加载中...</span>
      <button class="btn btn-outline-danger btn-sm ms-2" onclick="logout()">退出</button>
    </div>
  </div>
  <div style="padding:20px">
    <#nested/>
  </div>
</div>
<script>
$(function(){
  $.get("/api/auth/me", function(res){
    if (res.code !== 200) { window.location.href = "/login"; return; }
    var r = res.data;
    $("#userInfo").text(r.realName + " (" + r.role + ")");
  });
});
function logout(){
  if (!confirm("确认退出登录？")) return;
  $.post("/api/auth/logout", function(){ window.location.href = "/login"; });
}
</script>
</body>
</html>
</#macro>
