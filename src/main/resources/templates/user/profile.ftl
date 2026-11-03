<#include "../layout.ftl" />
<@layout title="个人中心" active="profile">
<div class="card mx-auto" style="max-width:560px">
  <div class="card-header">个人资料</div>
  <div class="card-body" id="content">加载中...</div>
</div>
<script>
$.get("/api/auth/me", function(res){
  var r = res.data;
  $("#content").html(
    "<table class='table'><tbody>" +
    "<tr><th style='width:120px'>用户名</th><td>"+r.username+"</td></tr>" +
    "<tr><th>姓名</th><td>"+(r.realName||"-")+"</td></tr>" +
    "<tr><th>手机号</th><td>"+(r.phone||"-")+"</td></tr>" +
    "<tr><th>角色</th><td>"+r.role+"</td></tr>" +
    "<tr><th>状态</th><td>"+(r.status===1?"正常":"停用")+"</td></tr>" +
    "</tbody></table>"
  );
});
</script>
</@layout>
