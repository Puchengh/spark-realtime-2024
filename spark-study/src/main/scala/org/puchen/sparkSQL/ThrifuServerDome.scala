package org.puchen.sparkSQL

import java.sql.{Connection, DriverManager, PreparedStatement, ResultSet}

object ThrifuServerDome {

  def main(args: Array[String]): Unit = {
    Class.forName("org.apache.hive,jdbc.HiveDriver")

    val conn: Connection = DriverManager.getConnection(
      "jdbc:hive2://node2:1000/default",
      "root",
      "123456"
    )

    val sql:String = """select id from t_person""".stripMargin
    val ps: PreparedStatement = conn.prepareStatement(sql)

    val rs: ResultSet = ps.executeQuery()

    //处理结果
    while (rs.next()){
      val id: Int = rs.getInt("id")
      println(id)
    }
    if(conn != null) conn.close()
    if(ps != null) ps.close()
    if(rs != null) rs.close()

  }
}
