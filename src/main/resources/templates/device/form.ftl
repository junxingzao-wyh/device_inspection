<#include "../layout.ftl" />
<#-- id 为空表示新增，非空表示编辑；不能用 id?string 直接判断，缺失变量会抛异常 -->
<#assign editMode = (id??)>
<@layout title="${editMode?string('编辑设备','新增设备')}" active="device">
<div class="card mx-auto" style="max-width:720px">
  <div class="card-header">${editMode?string('编辑设备','新增设备')}</div>
  <div class="card-body">
    <form id="form">
      <div class="row g-3">
        <div class="col-6"><label>设备编号</label><input name="deviceNo" class="form-control" required></div>
        <div class="col-6"><label>设备名称</label><input name="name" class="form-control" required></div>
        <div class="col-6"><label>分类</label><input name="category" class="form-control" placeholder="如：加工设备"></div>
        <div class="col-6"><label>位置</label><input name="location" class="form-control"></div>
        <div class="col-6">
          <label>状态</label>
          <select name="status" class="form-select">
            <option value="NORMAL">正常</option>
            <option value="REPAIRING">维修中</option>
            <option value="DISABLED">停用</option>
          </select>
        </div>
        <div class="col-6">
          <label>责任人</label>
          <select name="responsibleUserId" class="form-select" id="responsibleSel">
            <option value="">-- 加载中 --</option>
          </select>
        </div>
        <div class="col-6"><label>购置日期</label><input name="purchaseDate" type="date" class="form-control"></div>
        <div class="col-6"><label>维保周期（天）</label><input name="maintenanceCycleDays" type="number" class="form-control"></div>
        <div class="col-12"><label>备注</label><textarea name="remark" class="form-control" rows="3"></textarea></div>
      </div>
      <div class="mt-3">
        <button type="submit" class="btn btn-primary">保存</button>
        <a class="btn btn-secondary" href="/device/list">取消</a>
      </div>
    </form>
  </div>
</div>

<script>
var editId = <#if editMode>${id}<#else>null</#if>;

// 一次性加载所有可选责任人（巡检员 + 维修员），并保留当前选中值
$.when($.get("/common/inspectors"), $.get("/common/repairers")).done(function(a, b){
  var users = (a[0] || []).concat(b[0] || []);
  var sel = $("#responsibleSel");
  sel.html("<option value=''>-- 请选择责任人 --</option>");
  users.forEach(function(u){
    sel.append("<option value='"+u.id+"'>"+u.realName+" ("+u.role+")</option>");
  });
  if (editId) loadDetail();
});

// 编辑模式：拉取设备详情回填表单
function loadDetail(){
  $.get("/api/devices/"+editId, function(res){
    if (res.code !== 200 || !res.data) return;
    var d = res.data, f = $("#form")[0];
    f.deviceNo.value = d.deviceNo || "";
    f.name.value = d.name || "";
    f.category.value = d.category || "";
    f.location.value = d.location || "";
    f.status.value = d.status || "NORMAL";
    $("#responsibleSel").val(d.responsibleUserId || "");
    f.purchaseDate.value = d.purchaseDate || "";
    f.maintenanceCycleDays.value = d.maintenanceCycleDays || "";
    f.remark.value = d.remark || "";
  });
}

$("#form").on("submit", function(e){
  e.preventDefault();
  var data = Object.fromEntries(new FormData(this));
  // 数值字段转换，空值删除避免类型转换报错
  ["responsibleUserId","maintenanceCycleDays"].forEach(function(k){
    if (data[k] === "" || data[k] === undefined) delete data[k];
    else data[k] = +data[k];
  });
  if (!data.purchaseDate) delete data.purchaseDate;
  var url = editId ? "/api/devices/"+editId : "/api/devices";
  $.ajax({
    url: url,
    method: editId ? "PUT" : "POST",
    contentType: "application/json",
    data: JSON.stringify(data),
    success: function(r){
      if (r.code === 200) { location.href = "/device/list"; }
      else alert(r.message || "保存失败");
    },
    error: function(xhr){ alert("保存失败："+(xhr.responseText || xhr.status)); }
  });
});
</script>
</@layout>
