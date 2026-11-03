<#include "../layout.ftl" />
<@layout title="巡检计划" active="plan">
<div class="card">
  <div class="card-header d-flex justify-content-between">
    <span>巡检计划</span>
    <a class="btn btn-primary btn-sm" data-bs-toggle="modal" data-bs-target="#planModal">+ 新增计划</a>
  </div>
  <div class="card-body">
    <table class="table table-hover">
      <thead><tr><th>设备</th><th>巡检员</th><th>周期（天）</th><th>下次巡检</th><th>状态</th><th>操作</th></tr></thead>
      <tbody id="tbody"></tbody>
    </table>
  </div>
</div>

<div class="modal fade" id="planModal"><div class="modal-dialog"><div class="modal-content">
  <div class="modal-header"><h5 class="modal-title">新增巡检计划</h5>
    <button type="button" class="btn-close" data-bs-dismiss="modal"></button></div>
  <form id="planForm" class="modal-body">
    <div class="mb-3">
      <label>设备</label>
      <select name="deviceId" class="form-select" id="deviceSel" required>
        <option value="">-- 请选择设备 --</option>
      </select>
    </div>
    <div class="mb-3">
      <label>巡检员</label>
      <select name="inspectorId" class="form-select" id="inspectorSel" required>
        <option value="">-- 请选择巡检员 --</option>
      </select>
    </div>
    <div class="mb-3"><label>周期（天）</label><input name="cycleDays" type="number" class="form-control" required value="30"></div>
    <div class="mb-3"><label>下次巡检日期</label><input name="nextInspectionDate" type="date" class="form-control"></div>
    <button class="btn btn-primary">保存</button>
  </form>
</div></div></div>

<script>
// 加载设备下拉
$.get("/api/devices?size=100", function(res){
  var sel = $("#deviceSel");
  if (res.data && res.data.list) {
    res.data.list.forEach(function(d){
      sel.append("<option value='"+d.id+"'>"+d.deviceNo+" - "+d.name+"</option>");
    });
  }
});
// 加载巡检员下拉
$.get("/common/inspectors", function(list){
  var sel = $("#inspectorSel");
  if (list) {
    list.forEach(function(u){
      sel.append("<option value='"+u.id+"'>"+u.realName+" ("+u.username+")</option>");
    });
  }
});

function load(){
  $.get("/api/inspection-plans?page=1&size=50", function(res){
    var html="";
    if (res.data && res.data.list) {
      res.data.list.forEach(function(p){
        html += "<tr><td>"+p.deviceName+" ("+p.deviceNo+")</td>"
              + "<td>"+p.inspectorName+"</td><td>"+p.cycleDays+"</td>"
              + "<td>"+(p.nextInspectionDate||"-")+"</td>"
              + "<td><span class='badge "+(p.status===1?"bg-success":"bg-secondary")+"'>"+(p.status===1?"启用":"停用")+"</span></td>"
              + "<td><button class='btn btn-sm btn-danger' onclick='disablePlan("+p.id+")'>停用</button></td></tr>";
      });
    }
    $("#tbody").html(html || "<tr><td colspan=6 class='text-center text-muted'>暂无数据</td></tr>");
  });
}
function disablePlan(id){
  if (!confirm("确认停用该巡检计划？")) return;
  $.ajax({url:"/api/inspection-plans/"+id, method:"PUT",
    contentType:"application/json", data:JSON.stringify({status:0}),
    success:function(res){ if(res.code===200) load(); else alert(res.message); }});
}
$("#planForm").on("submit",function(e){
  e.preventDefault();
  var data = Object.fromEntries(new FormData(this));
  data.deviceId=+data.deviceId; data.inspectorId=+data.inspectorId; data.cycleDays=+data.cycleDays;
  // 空日期不能提交空串，否则 Jackson 无法反序列化成 LocalDate
  if (!data.nextInspectionDate) delete data.nextInspectionDate;
  $.ajax({url:"/api/inspection-plans", method:"POST", contentType:"application/json",
    data:JSON.stringify(data),
    success:function(res){
      if (res.code===200) {
        bootstrap.Modal.getOrCreateInstance($("#planModal")[0]).hide();
        $("#planForm")[0].reset();
        load();
      } else {
        alert(res.message || "保存失败");
      }
    }, error:function(xhr){ alert("保存失败："+(xhr.responseText||xhr.status)); }});
});
load();
</script>
</@layout>
