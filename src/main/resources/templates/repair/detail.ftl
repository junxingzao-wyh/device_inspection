<#include "../layout.ftl" />
<@layout title="工单详情" active="repair">
<div class="row">
<div class="col-md-8">
<div class="card"><div class="card-body" id="content">加载中...</div></div>
</div>
<div class="col-md-4">
<div class="card"><div class="card-header">状态流转</div>
<div class="card-body" id="actions"></div></div>
</div>
</div>
<script>
$.get("/api/repair-orders/${id}", function(res){
  if (res.code!==200) { $("#content").html("<div class='alert alert-warning'>"+res.message+"</div>"); return; }
  var o = res.data;
  $("#content").html(
    "<table class='table'><tbody>" +
    "<tr><th style='width:120px'>工单号</th><td>"+o.orderNo+"</td></tr>" +
    "<tr><th>设备</th><td>"+o.deviceName+"</td></tr>" +
    "<tr><th>标题</th><td>"+o.title+"</td></tr>" +
    "<tr><th>描述</th><td>"+(o.description||"-")+"</td></tr>" +
    "<tr><th>状态</th><td>"+o.status+"</td></tr>" +
    "<tr><th>报修人</th><td>"+o.reporterName+"</td></tr>" +
    "<tr><th>处理人</th><td>"+(o.handlerName||"-")+"</td></tr>" +
    "<tr><th>处理说明</th><td>"+(o.handleRemark||"-")+"</td></tr>" +
    "<tr><th>派单时间</th><td>"+(o.assignTime||"-")+"</td></tr>" +
    "<tr><th>完成时间</th><td>"+(o.finishTime||"-")+"</td></tr>" +
    "<tr><th>验收时间</th><td>"+(o.acceptTime||"-")+"</td></tr>" +
    "</tbody></table>" +
    "<a class='btn btn-outline-secondary' href='/repair/list'>返回工单列表</a>"
  );
  renderActions(o);
});
function renderActions(o){
  var html="";
  if (o.status==="PENDING_ASSIGN") {
    html = "<label>派给维修员</label><select id='handlerSel' class='form-select'></select>"
         + "<button class='btn btn-primary mt-2' onclick='assignOrder("+o.id+")'>派单</button>";
    $.get("/common/repairers", function(rs){
      rs.forEach(function(u){ $("#handlerSel").append("<option value='"+u.id+"'>"+u.realName+"</option>"); });
    });
  } else if (o.status==="PROCESSING") {
    html = "<button class='btn btn-info' onclick='processOrder("+o.id+")'>开始处理</button> "
         + "<button class='btn btn-warning' onclick='finishOrder("+o.id+")'>处理完成</button>";
  } else if (o.status==="PENDING_ACCEPT") {
    html = "<button class='btn btn-success' onclick='acceptOrder("+o.id+")'>验收通过</button>";
  }
  html += "<hr><button class='btn btn-secondary' onclick='closeOrder("+o.id+")'>关闭工单</button>";
  $("#actions").html(html);
}
function ajaxPut(url,body,cb){
  $.ajax({url:url, method:"PUT", contentType:"application/json",
    data: JSON.stringify(body||{}), success:function(){ cb&&cb(); }});
}
function assignOrder(id){ var h={handlerId:+$("#handlerSel").val()}; ajaxPut("/api/repair-orders/"+id+"/assign",h,function(){ location.reload(); }); }
function processOrder(id){ ajaxPut("/api/repair-orders/"+id+"/process",null,function(){ location.reload(); }); }
function finishOrder(id){ var r=prompt("请输入处理说明"); if(r!==null) ajaxPut("/api/repair-orders/"+id+"/finish",{handleRemark:r},function(){ location.reload(); }); }
function acceptOrder(id){ if(confirm("确认验收通过？")) ajaxPut("/api/repair-orders/"+id+"/accept",null,function(){ location.reload(); }); }
function closeOrder(id){ if(confirm("确认关闭？")) ajaxPut("/api/repair-orders/"+id+"/close",null,function(){ location.reload(); }); }
</script>
</@layout>
