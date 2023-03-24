package spark

import org.apache.spark.sql.functions.col
import org.apache.spark.sql.{SaveMode, SparkSession}
object incrementalkarpaCrypto {
  def main(args: Array[String]): Unit = {

    val url = "jdbc:postgresql://ec2-13-40-49-105.eu-west-2.compute.amazonaws.com:5432/testdb"
    val properties = new java.util.Properties()
    properties.setProperty("user", "consultants")
    properties.setProperty("password", "WelcomeItc@2022")
    properties.put("driver", "org.postgresql.Driver")

    import spark.implicits._
    import org.apache.spark.sql.functions._

    val spark = SparkSession.builder().appName("JDBCExample").getOrCreate()

    // *****************************************************************************************************

    val maxDF1 = spark.sql("select max(ethereum_id) from scalagroup.Ethereum_initialdataframe1").first()
    val cdc1 = maxDF1.get(0)
    val query1 = s"(select * from ethereum1 where cast(ethereum_id as int) > $cdc1) as tb1"

    val df_ethereum = spark.read.jdbc(url, query1, properties)
    df_ethereum.show(false)
    df_ethereum.write.mode(SaveMode.Append).saveAsTable("scalagroup.Ethereum_initialdataframe1")

  }
}
