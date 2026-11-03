<#include "../layout.ftl" />
<@layout title="维保记录" active="maintenance">
<div class="card">
  <div class="card-header d-flex justify-content-between">
    <span>维保记录</span>
    <a class="btn btn-primary btn-sm" href="/maintenance/form">+ 新增维保</a>
  </div>
  <div class="card-body">
    <table class="table table-hover">
      <thead><tr><th>日期</th><th>设备</th><th>操作人</th><th>内容</th><th>下次维保</th><th>附件</th></tr></thead>
      <tbody id="tbody"></tbody>
    </table>
  </div>
</div>
<script>
$.get("/api/maintenance-records?page=1&size=50", function(res){
  var html="";
  res.data.list.forEach(function(r){
    html += "<tr><td>"+r.maintenanceDate+"</td><td>"+r.deviceName+"</td>"
          + "<td>"+r.operatorName+"</td><td>"+r.content+"</td>"
          + "<td>"+(r.nextMaintenanceDate||"-")+"</td>"
          + "<td>"+(r.attachmentName?"<a href='/files/"+r.attachmentName+"' target='_blank'>查看</a>":"-")+"</td></tr>";
  });
  $("#tbody").html(html || "<tr><td colspan=6 class='text-center text-muted'>暂无数据</td></tr>");
});
</script>
</@layout>
