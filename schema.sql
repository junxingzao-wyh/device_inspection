-- ================================================================
-- 小微企业设备巡检与维保工单系统 数据库脚本
-- 数据库：device_inspection  MySQL 8.x  utf8mb4
-- ================================================================

DROP DATABASE IF EXISTS device_inspection;
CREATE DATABASE device_inspection
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_general_ci;
USE device_inspection;

-- ========== 1. 用户表 ==========
DROP TABLE IF EXISTS user;
CREATE TABLE user (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    username    VARCHAR(50)  NOT NULL COMMENT '登录名',
    password    VARCHAR(100) NOT NULL COMMENT 'BCrypt 密文',
    real_name   VARCHAR(50)           COMMENT '真实姓名',
    phone       VARCHAR(20)           COMMENT '手机号',
    role        VARCHAR(20)  NOT NULL COMMENT 'ADMIN / INSPECTOR / REPAIRER',
    avatar      VARCHAR(200)          COMMENT '头像文件名',
    status      TINYINT      NOT NULL DEFAULT 1 COMMENT '1 启用 0 停用',
    create_time DATETIME              COMMENT '创建时间',
    update_time DATETIME              COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username)
) ENGINE=InnoDB COMMENT='用户表';

-- ========== 2. 设备表 ==========
DROP TABLE IF EXISTS device;
CREATE TABLE device (
    id                      BIGINT        NOT NULL AUTO_INCREMENT COMMENT '主键',
    device_no               VARCHAR(50)   NOT NULL COMMENT '设备编号',
    name                    VARCHAR(100)  NOT NULL COMMENT '设备名称',
    category                VARCHAR(50)            COMMENT '分类',
    location                VARCHAR(100)           COMMENT '位置',
    responsible_user_id     BIGINT                 COMMENT '责任人，关联 user.id',
    status                  VARCHAR(20)   NOT NULL DEFAULT 'NORMAL' COMMENT 'NORMAL / REPAIRING / DISABLED',
    purchase_date           DATE                   COMMENT '购置日期',
    maintenance_cycle_days  INT                    COMMENT '维保周期（天）',
    last_maintenance_date   DATE                   COMMENT '上次维保日期',
    remark                  VARCHAR(255)           COMMENT '备注',
    create_time             DATETIME               COMMENT '创建时间',
    update_time             DATETIME               COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_device_no (device_no),
    KEY idx_responsible_user_id (responsible_user_id),
    KEY idx_status (status)
) ENGINE=InnoDB COMMENT='设备表';

-- ========== 3. 巡检计划表 ==========
DROP TABLE IF EXISTS inspection_plan;
CREATE TABLE inspection_plan (
    id                    BIGINT      NOT NULL AUTO_INCREMENT COMMENT '主键',
    device_id             BIGINT      NOT NULL COMMENT '关联 device.id',
    inspector_id          BIGINT      NOT NULL COMMENT '巡检员，关联 user.id',
    cycle_days            INT         NOT NULL COMMENT '巡检周期（天）',
    next_inspection_date  DATE                 COMMENT '下次巡检日期',
    status                TINYINT     NOT NULL DEFAULT 1 COMMENT '1 启用 0 停用',
    create_time           DATETIME             COMMENT '创建时间',
    PRIMARY KEY (id),
    KEY idx_device_id (device_id),
    KEY idx_inspector_id (inspector_id)
) ENGINE=InnoDB COMMENT='巡检计划表';

-- ========== 4. 巡检记录表 ==========
DROP TABLE IF EXISTS inspection_record;
CREATE TABLE inspection_record (
    id            BIGINT      NOT NULL AUTO_INCREMENT COMMENT '主键',
    plan_id       BIGINT               COMMENT '关联巡检计划，可空（手动巡检时为空）',
    device_id     BIGINT      NOT NULL COMMENT '关联 device.id',
    inspector_id  BIGINT      NOT NULL COMMENT '巡检员',
    result        VARCHAR(20) NOT NULL COMMENT 'NORMAL / ABNORMAL',
    description   TEXT                 COMMENT '巡检描述',
    photo_name    VARCHAR(200)         COMMENT '照片文件名',
    inspect_time  DATETIME    NOT NULL COMMENT '巡检时间',
    create_time   DATETIME             COMMENT '创建时间',
    PRIMARY KEY (id),
    KEY idx_plan_id (plan_id),
    KEY idx_device_id (device_id, inspect_time),
    KEY idx_inspector_id (inspector_id),
    KEY idx_result (result)
) ENGINE=InnoDB COMMENT='巡检记录表';

-- ========== 5. 报修工单表 ==========
DROP TABLE IF EXISTS repair_order;
CREATE TABLE repair_order (
    id             BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    order_no       VARCHAR(50)  NOT NULL COMMENT '工单编号，如 RO20250101001',
    device_id      BIGINT       NOT NULL COMMENT '关联 device.id',
    reporter_id    BIGINT       NOT NULL COMMENT '报修人',
    handler_id     BIGINT                COMMENT '处理人（维修员），可空',
    title          VARCHAR(100) NOT NULL COMMENT '故障标题',
    description    TEXT                  COMMENT '故障描述',
    photo_name     VARCHAR(200)          COMMENT '故障照片文件名',
    status         VARCHAR(20)  NOT NULL COMMENT 'PENDING_ASSIGN / PROCESSING / PENDING_ACCEPT / COMPLETED / CLOSED',
    handle_remark  VARCHAR(500)          COMMENT '处理说明',
    assign_time    DATETIME              COMMENT '派单时间',
    finish_time    DATETIME              COMMENT '处理完成时间',
    accept_time    DATETIME              COMMENT '验收时间',
    create_time    DATETIME              COMMENT '创建时间',
    update_time    DATETIME              COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_order_no (order_no),
    KEY idx_device_id (device_id),
    KEY idx_reporter_id (reporter_id),
    KEY idx_handler_id (handler_id),
    KEY idx_status (status)
) ENGINE=InnoDB COMMENT='报修工单表';

-- ========== 6. 维保记录表 ==========
DROP TABLE IF EXISTS maintenance_record;
CREATE TABLE maintenance_record (
    id                  BIGINT      NOT NULL AUTO_INCREMENT COMMENT '主键',
    device_id           BIGINT      NOT NULL COMMENT '关联 device.id',
    operator_id         BIGINT      NOT NULL COMMENT '操作人',
    content             TEXT                 COMMENT '维保内容',
    attachment_name     VARCHAR(200)         COMMENT '附件文件名',
    maintenance_date    DATE        NOT NULL COMMENT '维保日期',
    next_maintenance_date DATE              COMMENT '下次维保日期',
    create_time         DATETIME             COMMENT '创建时间',
    PRIMARY KEY (id),
    KEY idx_device_id (device_id),
    KEY idx_operator_id (operator_id)
) ENGINE=InnoDB COMMENT='维保记录表';

-- ========== 7. 通知表 ==========
DROP TABLE IF EXISTS notification;
CREATE TABLE notification (
    id          BIGINT      NOT NULL AUTO_INCREMENT COMMENT '主键',
    receiver_id BIGINT      NOT NULL COMMENT '接收人',
    title       VARCHAR(100) NOT NULL COMMENT '通知标题',
    content     VARCHAR(500)         COMMENT '通知内容',
    type        VARCHAR(30)          COMMENT 'ORDER_ASSIGN / ORDER_ACCEPT / INSPECTION_ABNORMAL / MAINTENANCE_DUE',
    biz_id      BIGINT               COMMENT '关联业务 id',
    is_read     TINYINT     NOT NULL DEFAULT 0 COMMENT '0 未读 1 已读',
    create_time DATETIME             COMMENT '创建时间',
    PRIMARY KEY (id),
    KEY idx_receiver_id_is_read (receiver_id, is_read)
) ENGINE=InnoDB COMMENT='通知表';


-- ================================================================
-- 初始化测试数据
-- BCrypt 密文对应明文 123456
-- ================================================================

-- 3 个测试账号
INSERT INTO user (username, password, real_name, phone, role, status, create_time, update_time) VALUES
('admin',     '$2a$10$ZKEHB5IjGNOgwieR/ExZlu8mN7kqm1fzzQpz9t3Bbt6Zl3AQmQ/bi', '系统管理员', '13800000001', 'ADMIN',     1, NOW(), NOW()),
('inspector', '$2a$10$ZKEHB5IjGNOgwieR/ExZlu8mN7kqm1fzzQpz9t3Bbt6Zl3AQmQ/bi', '张三',       '13800000002', 'INSPECTOR', 1, NOW(), NOW()),
('repairer',  '$2a$10$ZKEHB5IjGNOgwieR/ExZlu8mN7kqm1fzzQpz9t3Bbt6Zl3AQmQ/bi', '李四',       '13800000003', 'REPAIRER',  1, NOW(), NOW());

-- 5 台设备
INSERT INTO device (device_no, name, category, location, responsible_user_id, status, purchase_date, maintenance_cycle_days, last_maintenance_date, remark, create_time, update_time) VALUES
('DEV-001', '空压机 1 号',     '压缩设备', '车间 A-01', 1, 'NORMAL',   '2023-03-15', 90,  '2026-08-01', '功率 15kW',       NOW(), NOW()),
('DEV-002', '数控车床 1 号',   '加工设备', '车间 B-02', 1, 'NORMAL',   '2022-07-20', 120, '2026-09-10', '型号 CK6140',     NOW(), NOW()),
('DEV-003', '液压泵站',        '液压设备', '车间 A-03', 1, 'REPAIRING','2023-11-05', 60,  '2026-07-20', '近期频繁漏油',     NOW(), NOW()),
('DEV-004', '配电柜 1 号',     '电气设备', '配电室-01', 1, 'NORMAL',   '2024-02-18', 180, '2026-06-15', '主配电柜',        NOW(), NOW()),
('DEV-005', '通风机',          '通风设备', '车间 C-01', 1, 'DISABLED', '2021-09-10', 90,  '2026-03-01', '已报废停用',       NOW(), NOW());

-- 2 条巡检计划（巡检员为 inspector，设备为 001 / 002）
INSERT INTO inspection_plan (device_id, inspector_id, cycle_days, next_inspection_date, status, create_time) VALUES
(1, 2, 30, '2026-11-15', 1, NOW()),
(2, 2, 45, '2026-12-01', 1, NOW());
