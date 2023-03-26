package spark

import org.apache.spark.sql.{SaveMode, SparkSession}

object Officestransformation {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession
      .builder()
      .appName("Spark SQL basic example")
      .config("spark.some.config.option", "some-value")
      .getOrCreate()

    val url = "jdbc:postgresql://ec2-13-40-49-105.eu-west-2.compute.amazonaws.com:5432/testdb"
    val properties = new java.util.Properties()
    properties.setProperty("user", "consultants")
    properties.setProperty("password", "WelcomeItc@2022")
    properties.put("driver", "org.postgresql.Driver")
    //**********************************************************************************************************
    import spark.implicits._
    import org.apache.spark.sql.functions._
    import spark.implicits._
    println("Offices dry run")
    val df1 = spark.read.jdbc(url, "offices9", properties)
    df1.show(false)
    // Create Hive Internal table
    df1.write.mode(SaveMode.Overwrite).saveAsTable("mytestdb.offices_Jenkins")

    //****************************************************************************************************
    val df2 = df1.map(row => {
      val address = row.getString(3) + " " + row.getString(4)
      (row.getInt(0), row.getString(1), row.getString(2), address, row.getString(5), row.getString(6), row.getString(7))
    })
    val df2Map = df2.toDF("id", "city", "phone", "address", "state_or_region", "country", "post_code")

    df2Map.printSchema()
    df2Map.show(false)
    df2Map.write.mode(SaveMode.Overwrite).saveAsTable("mytestdb.officesJenkins_trans")

  }
}
