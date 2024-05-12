package com.puchen.scala.gmall.bean

import com.alibaba.fastjson.JSONObject

case class PageDisPageLog(
                    mid: String,
                    user_id: String,
                    province_id: String,
                    channel: String,
                    is_new: String,
                    model: String,
                    operate_system: String,
                    version_code: String,
                    brand: String,
                    page_id: String,
                    last_page_id: String,
                    page_item: String,
                    page_item_type: String,
                    during_time: Long,
                    source_type: String,
                    ts: Long,
                    display_type: String,
                    item: String,
                    item_type: String,
                    pos_id: String,
                    order: String
                  ) {
}
