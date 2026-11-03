<#include "../layout.ftl" />
<@layout title="设备详情" active="device">
<div id="content" class="card"><div class="card-body">加载中...</div></div>
<script>
$.get("/api/devices/${id}", function(res){
  if (res.code!==200) { $("#content").html("<div class='alert alert-warning'>"+res.message+"</div>"); return; }
  var d = res.data;
  $("#content").html(
    "<table class='table'><tbody>" +
    "<tr><th style='width:120px'>设备编号</th><td>"+d.deviceNo+"</td></tr>" +
    "<tr><th>名称</th><td>"+d.name+"</td></tr>" +
    "<tr><th>分类</th><td>"+(d.category||"-")+"</td></tr>" +
    "<tr><th>位置</th><td>"+(d.location||"-")+"</td></tr>" +
    "<tr><th>责任人</th><td>"+(d.responsibleUserName||"-")+"</td></tr>" +
    "<tr><th>状态</th><td>"+d.status+"</td></tr>" +
    "<tr><th>购置日期</th><td>"+(d.purchaseDate||"-")+"</td></tr>" +
    "<tr><th>维保周期</th><td>"+(d.maintenanceCycleDays||"-")+" 天</td></tr>" +
    "<tr><th>上次维保</th><td>"+(d.lastMaintenanceDate||"-")+"</td></tr>" +
    "<tr><th>备注</th><td>"+(d.remark||"-")+"</td></tr>" +
    "</tbody></table>" +
    "<a class='btn btn-secondary' href='/device/form?id="+d.id+"'>编辑</a> " +
    "<a class='btn btn-primary' href='/inspection/execute?deviceId="+d.id+"'>执行巡检</a> " +
    "<a class='btn btn-outline-secondary' href='/device/list'>返回列表</a>"
  );
});
</script>
</@layout>
