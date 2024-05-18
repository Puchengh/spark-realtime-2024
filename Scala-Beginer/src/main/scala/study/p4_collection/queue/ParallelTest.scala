package study.p4_collection.queue

import scala.collection.immutable
import scala.collection.parallel.immutable.ParSeq

object ParallelTest {

  def main(args: Array[String]): Unit = {
    val strings: immutable.IndexedSeq[Long] = (1 to 100).map(
      x => Thread.currentThread().getId
    )
    println(strings)

    val longs: ParSeq[Long] = (1 to 100).par.map(
      x => Thread.currentThread().getId
    )
    println(longs)
  }
}
