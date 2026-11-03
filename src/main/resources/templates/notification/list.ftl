<#include "../layout.ftl" />
<@layout title="通知中心" active="notification">
<div class="card">
  <div class="card-header d-flex justify-content-between">
    <span>通知中心</span>
    <button class="btn btn-sm btn-primary" onclick="readAll()">全部标为已读</button>
  </div>
  <div class="card-body">
    <table class="table table-hover">
      <thead><tr><th>时间</th><th>标题</th><th>内容</th><th>类型</th><th>状态</th><th>操作</th></tr></thead>
      <tbody id="tbody"></tbody>
    </table>
  </div>
</div>
<script>
function load(){
  $.get("/api/notifications?page=1&size=50", function(res){
    var html="";
    res.data.list.forEach(function(n){
      var badge = n.isRead===1?"bg-secondary":"bg-primary";
      html += "<tr><td>"+n.createTime+"</td><td>"+n.title+"</td><td>"+(n.content||"-")+"</td>"
            + "<td>"+(n.type||"-")+"</td>"
            + "<td><span class='badge "+badge+"'>"+(n.isRead===1?"已读":"未读")+"</span></td>"
            + "<td>" + (n.isRead===0?"<button class='btn btn-sm btn-success' onclick='readOne("+n.id+")'>标为已读</button>":"-")+"</td></tr>";
    });
    $("#tbody").html(html || "<tr><td colspan=6 class='text-center text-muted'>暂无通知</td></tr>");
  });
}
function readOne(id){ $.ajax({url:"/api/notifications/"+id+"/read", method:"PUT", success:load}); }
function readAll(){ $.ajax({url:"/api/notifications/read-all", method:"PUT", success:load}); }
load();
</script>
</@layout>
