<#include "../layout.ftl" />
<@layout title="巡检记录" active="record">
<div class="card">
  <div class="card-header">巡检记录</div>
  <div class="card-body">
    <form class="row g-2 mb-3" id="searchForm">
      <div class="col-2">
        <select name="result" class="form-select">
          <option value="">全部结果</option>
          <option value="NORMAL">正常</option>
          <option value="ABNORMAL">异常</option>
        </select>
      </div>
      <div class="col-auto"><button class="btn btn-primary">查询</button></div>
    </form>
    <table class="table table-hover">
      <thead><tr><th>时间</th><th>设备</th><th>巡检员</th><th>结果</th><th>描述</th><th>照片</th></tr></thead>
      <tbody id="tbody"></tbody>
    </table>
  </div>
</div>
<script>
$.get("/api/inspection-records?page=1&size=50", function(res){
  var html="";
  res.data.list.forEach(function(r){
    var st = r.result==="NORMAL"?"bg-success":"bg-danger";
    html += "<tr><td>"+r.inspectTime+"</td><td>"+r.deviceName+"</td>"
          + "<td>"+r.inspectorName+"</td>"
          + "<td><span class='badge "+st+"'>"+r.result+"</span></td>"
          + "<td>"+(r.description||"-")+"</td>"
          + "<td>"+(r.photoName?"<a href='/files/"+r.photoName+"' target='_blank'>查看</a>":"-")+"</td></tr>";
  });
  $("#tbody").html(html || "<tr><td colspan=6 class='text-center text-muted'>暂无数据</td></tr>");
});
</script>
</@layout>
