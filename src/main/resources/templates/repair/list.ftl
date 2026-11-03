<#include "../layout.ftl" />
<@layout title="报修工单" active="repair">
<div class="card">
  <div class="card-header d-flex justify-content-between">
    <span>报修工单</span>
    <a class="btn btn-primary btn-sm" href="/repair/form">+ 发起报修</a>
  </div>
  <div class="card-body">
    <form class="row g-2 mb-3" id="searchForm">
      <div class="col-3"><input name="keyword" class="form-control" placeholder="工单号或设备名"></div>
      <div class="col-2">
        <select name="status" class="form-select">
          <option value="">全部状态</option>
          <option value="PENDING_ASSIGN">待派单</option>
          <option value="PROCESSING">处理中</option>
          <option value="PENDING_ACCEPT">待验收</option>
          <option value="COMPLETED">已完成</option>
          <option value="CLOSED">已关闭</option>
        </select>
      </div>
      <div class="col-auto"><button class="btn btn-primary">查询</button></div>
    </form>
    <table class="table table-hover">
      <thead><tr><th>工单号</th><th>设备</th><th>标题</th><th>状态</th><th>维修员</th><th>创建时间</th><th>操作</th></tr></thead>
      <tbody id="tbody"></tbody>
    </table>
    <nav><ul class="pagination" id="pager"></ul></nav>
  </div>
</div>
<script>
var page=1,size=10;
function load(){
  var q = $("#searchForm").serialize() + "&page="+page+"&size="+size;
  $.get("/api/repair-orders", q, function(res){
    var html="";
    if (res.data && res.data.list) {
      res.data.list.forEach(function(o){
        var map = {PENDING_ASSIGN:"bg-warning",PROCESSING:"bg-info",PENDING_ACCEPT:"bg-primary",COMPLETED:"bg-success",CLOSED:"bg-secondary"};
        html += "<tr><td>"+o.orderNo+"</td><td>"+o.deviceName+"</td><td>"+o.title+"</td>"
              + "<td><span class='badge "+map[o.status]+"'>"+o.status+"</span></td>"
              + "<td>"+(o.handlerName||"-")+"</td><td>"+o.createTime+"</td>"
              + "<td><a class='btn btn-sm btn-info' href='/repair/detail?id="+o.id+"'>详情</a></td></tr>";
      });
    }
    $("#tbody").html(html || "<tr><td colspan=7 class='text-center text-muted'>暂无数据</td></tr>");
    // 渲染分页
    renderPager(res.data ? res.data.total : 0, res.data ? res.data.page : 1, res.data ? res.data.size : 10);
  });
}
function renderPager(total,cur,sz){
  var pages = Math.max(1, Math.ceil(total/sz)), html="";
  html += "<li class='page-item "+(cur<=1?"disabled":"")+"'><a class='page-link' href='#' data-p='"+(cur-1)+"'>上一页</a></li>";
  for(var i=1;i<=pages;i++){
    html += "<li class='page-item "+(i===cur?"active":"")+"'><a class='page-link' href='#' data-p="+i+">"+i+"</a></li>";
  }
  html += "<li class='page-item "+(cur>=pages?"disabled":"")+"'><a class='page-link' href='#' data-p='"+(cur+1)+"'>下一页</a></li>";
  html += "<li class='page-item disabled'><span class='page-link'>共 "+total+" 条</span></li>";
  $("#pager").html(html).find("a").click(function(e){ e.preventDefault(); page=+$(this).data("p"); load(); });
}
$("#searchForm").on("submit",function(e){ e.preventDefault(); page=1; load(); });
load();
</script>
</@layout>
