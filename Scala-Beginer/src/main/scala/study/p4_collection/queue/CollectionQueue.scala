package study.p4_collection.queue

import scala.collection.immutable.Queue
import scala.collection.mutable

object CollectionQueue {

  def main(args: Array[String]): Unit = {
    val que = new mutable.Queue[String]()
    que.enqueue("a", "b", "c")
    println(que.dequeue())
    println(que.dequeue())
    println(que.dequeue())

    val queue = Queue("c","d","e")
    val queue1 = queue.enqueue("f")
    println(queue)
    println(queue1)
  }
}
