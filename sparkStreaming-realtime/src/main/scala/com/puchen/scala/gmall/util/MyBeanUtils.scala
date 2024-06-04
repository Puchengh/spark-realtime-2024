package com.puchen.scala.gmall.util
import com.puchen.scala.gmall.bean.{DauInfo, PageLog}

import java.lang.reflect.{Field, Method, Modifier}
import scala.util.control.Breaks
/**
 * 实现对象属性拷贝
 */
object MyBeanUtils {
//  def main(args: Array[String]): Unit = {
//    val pageLoig: PageLog = new PageLog("mid1001", "uid1001", "prov1001", null, null, null, null, null, null, null, null, null, null, 0L, null, 123456)
//    val dauInfo: DauInfo = new DauInfo()
//
//    println("pre:" +dauInfo)
//    copyProperties(pageLoig,dauInfo)
//    println("after:" +dauInfo)
//  }
  /**
   * 将 srcObj 中属性的值拷贝到 destObj 对应的属性上
   */
  def copyProperties(srcObj : AnyRef , destObj: AnyRef): Unit ={
    if(srcObj == null || destObj == null ){
      return
    }
    //获取到 srcObj 中所有的属性
    val   srcFields:Array[Field] =   srcObj.getClass.getDeclaredFields
    //处理每个属性的拷贝
    for (srcField <- srcFields) {
      Breaks.breakable{
        //get / set
        // Scala 会自动为类中的属性提供 get、 set 方法
        // get : fieldname()
        // set : fieldname_$eq(参数类型)
        //getMethodName
        var getMethodName:String = srcField.getName
        //setMethodName
        var setMethodName:String = srcField.getName+"_$eq"
        //从 srcObj 中获取 get 方法对象，
        val getMethod:Method = srcObj.getClass.getDeclaredMethod(getMethodName)
        //从 destObj 中获取set方法对象
        // String name;
        // getName()
        // setName(String name ){ this.name = name }
        val setMethod: Method =
          try{
            destObj.getClass.getDeclaredMethod(setMethodName, srcField.getType)
          }catch{
            // NoSuchMethodException
            case ex : Exception =>  Breaks.break()
          }
        //忽略val属性
        val destField:Field = destObj.getClass.getDeclaredField(srcField.getName)
        if(destField.getModifiers.equals(Modifier.FINAL)){
          Breaks.break()
        }
        //调用get方法获取到srcObj属性的值，再调用set方法将获取到的属性值赋值给destObj的属性
        setMethod.invoke(destObj, getMethod.invoke(srcObj))
      }
    }
  }

}
