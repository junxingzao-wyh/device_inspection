<#include "../layout.ftl" />
<@layout title="发起报修" active="repair">
<div class="card mx-auto" style="max-width:640px">
  <div class="card-header">发起报修</div>
  <div class="card-body">
    <form id="form" enctype="multipart/form-data">
      <div class="mb-3">
        <label>设备</label>
        <select name="deviceId" class="form-select" id="deviceSel" required>
          <option value="">-- 请选择设备 --</option>
        </select>
      </div>
      <div class="mb-3"><label>故障标题</label>
        <input name="title" class="form-control" required placeholder="如：空压机压力异常"></div>
      <div class="mb-3"><label>故障描述</label>
        <textarea name="description" class="form-control" rows="4" placeholder="详细描述故障现象..."></textarea></div>
      <div class="mb-3"><label>故障照片</label>
        <input name="photo" type="file" class="form-control" accept="image/*"></div>
      <button class="btn btn-primary">提交</button>
      <a class="btn btn-secondary" href="/repair/list">取消</a>
    </form>
  </div>
</div>
<script>
// 加载所有设备
$.get("/api/devices?size=100", function(res){
  var sel = $("#deviceSel");
  if (res.data && res.data.list) {
    res.data.list.forEach(function(d){
      sel.append("<option value='"+d.id+"'>"+d.deviceNo+" - "+d.name+" ("+d.status+")</option>");
    });
  }
  var preId = "${deviceId!}".trim();
  if (preId) sel.val(preId);
});

$("#form").on("submit",function(e){
  e.preventDefault();
  var fd = new FormData(this);
  $.ajax({url:"/api/repair-orders", method:"POST", data:fd, processData:false, contentType:false,
    success:function(r){
      if (r.code===200) { alert("提交成功"); location.href="/repair/list"; }
      else alert(r.message || "提交失败");
    }, error:function(xhr){ alert("提交失败："+xhr.responseText); }});
});
</script>
</@layout>
