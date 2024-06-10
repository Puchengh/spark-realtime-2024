package com.puchen.scala.gmall.bean

case class OrderDetail(
                        id: Long = 0L,
                        detail_id:Long,
                        order_id:Long,
                        sku_id:Long,
                        order_price:Double,
                        sku_num:Long,
                        sku_name:String,
                        create_time:String,
                        split_total_amount:Double=0D,
                        split_activity_amount:Double=0D,
                        split_coupon_amount:Double=0D

                      ){}
//CREATE TABLE `order_detail`  (
//  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
//`order_id` bigint(20) NULL DEFAULT NULL COMMENT '订单编号',
//`sku_id` bigint(20) NULL DEFAULT NULL COMMENT 'sku_id',
//`sku_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'sku名称（冗余)',
//`img_url` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片名称（冗余)',
//`order_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '购买价格(下单时sku价格）',
//`sku_num` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '购买个数',
//`create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
//`source_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '来源类型',
//`source_id` bigint(20) NULL DEFAULT NULL COMMENT '来源编号',
//`split_total_amount` decimal(16, 2) NULL DEFAULT NULL,
//`split_activity_amount` decimal(16, 2) NULL DEFAULT NULL,
//`split_coupon_amount` decimal(16, 2) NULL DEFAULT NULL,
//PRIMARY KEY (`id`) USING BTREE
//) ENGINE = InnoDB AUTO_INCREMENT = 13030 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单明细表' ROW_FORMAT = Dynamic;