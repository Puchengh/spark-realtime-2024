package study.pre.modematching

import scala.util.Random

object CaseClassDome {
  def main(args: Array[String]): Unit = {

    var arr = Array(CheckTimeOutTask,SummiTask("1000","task-00001"),HeartBeat(3000L))

    arr(Random.nextInt(arr.length)) match {
        //不需要写case _  因为他一定会匹配到
      case CheckTimeOutTask => println("CheckTimeOutTask")
      case SummiTask(port,task) => println("SummiTask")
      case HeartBeat(time) => println("HeartBeat")
    }
  }
}

/**
  * 多例的
  * @param time
  */
case class HeartBeat(time: Long)
case class SummiTask(id: String,taskName: String)

/**
  * 单例的
  */
case object CheckTimeOutTask