<#include "../layout.ftl" />
<@layout title="执行巡检" active="record">
<div class="card mx-auto" style="max-width:640px">
  <div class="card-header">执行巡检</div>
  <div class="card-body">
    <form id="form" enctype="multipart/form-data">
      <div class="mb-3">
        <label>设备</label>
        <select name="deviceId" class="form-select" id="deviceSel" required>
          <option value="">-- 请选择设备 --</option>
        </select>
      </div>
      <div class="mb-3">
        <label>巡检结果</label>
        <select name="result" class="form-select">
          <option value="NORMAL">正常</option>
          <option value="ABNORMAL">异常</option>
        </select>
      </div>
      <div class="mb-3"><label>巡检描述</label>
        <textarea name="description" class="form-control" rows="3" placeholder="如：压力表读数正常，油位正常"></textarea></div>
      <div class="mb-3"><label>现场照片</label>
        <input name="photo" type="file" class="form-control" accept="image/*"></div>
      <button class="btn btn-primary">提交</button>
      <a class="btn btn-secondary" href="/inspection/record">取消</a>
    </form>
  </div>
</div>
<script>
// 加载所有设备作为下拉
$.get("/api/devices?size=100&status=NORMAL", function(res){
  var sel = $("#deviceSel");
  if (res.data && res.data.list) {
    res.data.list.forEach(function(d){
      sel.append("<option value='"+d.id+"'>"+d.deviceNo+" - "+d.name+"</option>");
    });
  }
  // 预选 deviceId 参数（如果有）
  var preId = "${deviceId!}".trim();
  if (preId) sel.val(preId);
});

$("#form").on("submit",function(e){
  e.preventDefault();
  var fd = new FormData(this);
  $.ajax({url:"/api/inspection-records", method:"POST", data:fd, processData:false, contentType:false,
    success:function(r){
      if (r.code===200) { alert("提交成功"); location.href="/inspection/record"; }
      else alert(r.message || "提交失败");
    }, error:function(xhr){ alert("提交失败："+xhr.responseText); }});
});
</script>
</@layout>
