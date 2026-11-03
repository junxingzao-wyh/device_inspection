<#include "layout.ftl" />
<@layout title="首页看板" active="index">
<div class="row">
  <div class="col-md-4"><div class="stat-card">
    <div>待派单工单</div><div class="num">${stats.pendingOrderCount}</div>
  </div></div>
  <div class="col-md-4"><div class="stat-card">
    <div>处理中工单</div><div class="num">${stats.processingOrderCount}</div>
  </div></div>
  <div class="col-md-4"><div class="stat-card">
    <div>巡检计划总数</div><div class="num">${stats.planCount}</div>
  </div></div>
</div>

<div class="row mt-4">
  <div class="col-12">
    <div class="card"><div class="card-body">
      <h5>快捷入口</h5>
      <a class="btn btn-primary me-2" href="/device/form">新增设备</a>
      <a class="btn btn-info me-2" href="/inspection/execute">执行巡检</a>
      <a class="btn btn-warning me-2" href="/repair/form">发起报修</a>
      <a class="btn btn-success" href="/maintenance/form">新增维保</a>
    </div></div>
  </div>
</div>
</@layout>
