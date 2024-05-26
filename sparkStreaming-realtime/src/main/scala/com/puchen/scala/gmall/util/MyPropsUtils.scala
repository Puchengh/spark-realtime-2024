package com.puchen.scala.gmall.util

import java.util.ResourceBundle

/**
 * 配置文件读取配置信息
 */
object MyPropsUtils {

  private val bundle: ResourceBundle = ResourceBundle.getBundle("config")
  def apply(prosKey:String):String = {
    bundle.getString(prosKey)
  }

}
