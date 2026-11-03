<#include "../layout.ftl" />
<@layout title="设备列表" active="device">
<div class="card">
  <div class="card-header d-flex justify-content-between">
    <span>设备台账</span>
    <a class="btn btn-primary btn-sm" href="/device/form">+ 新增设备</a>
  </div>
  <div class="card-body">
    <form class="row g-2 mb-3" id="searchForm">
      <div class="col-3"><input name="keyword" class="form-control" placeholder="编号或名称"></div>
      <div class="col-2">
        <select name="status" class="form-select">
          <option value="">全部状态</option>
          <option value="NORMAL">正常</option>
          <option value="REPAIRING">维修中</option>
          <option value="DISABLED">已停用</option>
        </select>
      </div>
      <div class="col-auto"><button class="btn btn-primary">查询</button></div>
    </form>
    <table class="table table-hover" id="tbl">
      <thead><tr><th>编号</th><th>名称</th><th>分类</th><th>位置</th><th>状态</th><th>责任人</th><th>操作</th></tr></thead>
      <tbody></tbody>
    </table>
    <nav><ul class="pagination" id="pager"></ul></nav>
  </div>
</div>

<script>
var page=1,size=10;
function load(){
  var q = $("#searchForm").serialize() + "&page="+page+"&size="+size;
  $.get("/api/devices", q, function(res){
    var rows = res.data.list, html="";
    rows.forEach(function(r){
      var st = {NORMAL:"bg-success",REPAIRING:"bg-warning text-dark",DISABLED:"bg-secondary"}[r.status]||"bg-light";
      html += "<tr><td>"+r.deviceNo+"</td><td>"+r.name+"</td><td>"+(r.category||"-")+"</td>"
            + "<td>"+(r.location||"-")+"</td>"
            + "<td><span class='badge "+st+"'>"+r.status+"</span></td>"
            + "<td>"+(r.responsibleUserName||"-")+"</td>"
            + "<td><a class='btn btn-sm btn-info' href='/device/detail?id="+r.id+"'>详情</a> "
            + "<a class='btn btn-sm btn-secondary' href='/device/form?id="+r.id+"'>编辑</a> ";
      if(r.status!="DISABLED") html += "<button class='btn btn-sm btn-danger' onclick='disableDev("+r.id+")'>停用</button>";
      html += "</td></tr>";
    });
    $("#tbl tbody").html(html || "<tr><td colspan=7 class='text-center text-muted'>暂无数据</td></tr>");
    renderPager(res.data.total, res.data.page, res.data.size);
  });
}
function renderPager(total,cur,sz){
  var pages = Math.max(1, Math.ceil(total/sz)), html="";
  for(var i=1;i<=pages;i++){
    html += "<li class='page-item "+(i===cur?"active":"")+"'><a class='page-link' href='#' data-p="+i+">"+i+"</a></li>";
  }
  $("#pager").html(html).find("a").click(function(e){ e.preventDefault(); page=+$(this).data("p"); load(); });
}
function disableDev(id){
  if(!confirm("确认停用？")) return;
  $.ajax({url:"/api/devices/"+id+"/disable", method:"PUT", success:function(){ load(); }});
}
$("#searchForm").on("submit",function(e){ e.preventDefault(); page=1; load(); });
load();
</script>
</@layout>
