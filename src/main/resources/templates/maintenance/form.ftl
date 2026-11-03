<#include "../layout.ftl" />
<@layout title="新增维保" active="maintenance">
<div class="card mx-auto" style="max-width:640px">
  <div class="card-header">新增维保记录</div>
  <div class="card-body">
    <form id="form" enctype="multipart/form-data">
      <div class="mb-3">
        <label>设备</label>
        <select name="deviceId" class="form-select" id="deviceSel" required>
          <option value="">-- 请选择设备 --</option>
        </select>
      </div>
      <div class="mb-3"><label>维保日期</label>
        <input name="maintenanceDate" type="date" class="form-control" required></div>
      <div class="mb-3"><label>下次维保日期</label>
        <input name="nextMaintenanceDate" type="date" class="form-control"></div>
      <div class="mb-3"><label>维保内容</label>
        <textarea name="content" class="form-control" rows="4" required placeholder="如：更换空气滤芯、油滤，检查皮带张紧度"></textarea></div>
      <div class="mb-3"><label>附件</label>
        <input name="attachment" type="file" class="form-control"></div>
      <button class="btn btn-primary">保存</button>
      <a class="btn btn-secondary" href="/maintenance/list">取消</a>
    </form>
  </div>
</div>
<script>
// 加载所有设备
$.get("/api/devices?size=100", function(res){
  var sel = $("#deviceSel");
  if (res.data && res.data.list) {
    res.data.list.forEach(function(d){
      sel.append("<option value='"+d.id+"'>"+d.deviceNo+" - "+d.name+"</option>");
    });
  }
  var preId = "${deviceId!}".trim();
  if (preId) sel.val(preId);
});

$("#form").on("submit",function(e){
  e.preventDefault();
  var fd = new FormData(this);
  $.ajax({url:"/api/maintenance-records", method:"POST", data:fd, processData:false, contentType:false,
    success:function(r){
      if (r.code===200) { alert("保存成功"); location.href="/maintenance/list"; }
      else alert(r.message || "保存失败");
    }, error:function(xhr){ alert("保存失败："+xhr.responseText); }});
});
</script>
</@layout>
