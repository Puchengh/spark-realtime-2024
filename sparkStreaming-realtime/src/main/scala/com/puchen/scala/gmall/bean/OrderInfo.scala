package com.puchen.scala.gmall.bean

case class OrderInfo(
                      id: Long = 0L,
                      province_id: Long = 0L,
                      order_status: String = null,
                      user_id: Long = 0L,
                      total_amount: Double = 0D,
                      activity_reduce_amount: Double = 0D,
                      coupon_reduce_amount: Double = 0D,
                      original_total_amount: Double = 0D,
                      feight_fee: Double = 0D,
                      feight_fee_reduce: Double = 0D,
                      expire_time: String = null,
                      refundable_time: String = null,
                      create_time: String = null,
                      operate_time: String = null,

                      var create_date: String = null,
                      var create_hour: String = null,

                      var province_name: String = null,
                      var province_iso_code: String = null,
                      var province_3166_2: String = null,
                      var province_area_code: String = null,
                      var user_age: Int = 0,
                      var user_gender: String = null

                    ) {}
//CREATE TABLE `order_info`  (
//  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
//`consignee` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货人',
//`consignee_tel` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收件人电话',
//`total_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '总金额',
//`order_status` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单状态',
//`user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
//`payment_way` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '付款方式',
//`delivery_address` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '送货地址',
//`order_comment` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单备注',
//`out_trade_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单交易编号（第三方支付用)',
//`trade_body` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单描述(第三方支付用)',
//`create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
//`operate_time` datetime(0) NULL DEFAULT NULL COMMENT '操作时间',
//`expire_time` datetime(0) NULL DEFAULT NULL COMMENT '失效时间',
//`process_status` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '进度状态',
//`tracking_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '物流单编号',
//`parent_order_id` bigint(20) NULL DEFAULT NULL COMMENT '父订单编号',
//`img_url` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片路径',
//`province_id` int(20) NULL DEFAULT NULL COMMENT '地区',
//`activity_reduce_amount` decimal(16, 2) NULL DEFAULT NULL COMMENT '促销金额',
//`coupon_reduce_amount` decimal(16, 2) NULL DEFAULT NULL COMMENT '优惠券',
//`original_total_amount` decimal(16, 2) NULL DEFAULT NULL COMMENT '原价金额',
//`feight_fee` decimal(16, 2) NULL DEFAULT NULL COMMENT '运费',
//`feight_fee_reduce` decimal(16, 2) NULL DEFAULT NULL COMMENT '运费减免',
//`refundable_time` datetime(0) NULL DEFAULT NULL COMMENT '可退款日期（签收后30天）',
//PRIMARY KEY (`id`) USING BTREE
//) ENGINE = InnoDB AUTO_INCREMENT = 4863 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单表 订单表' ROW_FORMAT = Dynamic;
