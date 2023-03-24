package spark

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.functions.{col, spark_partition_id}
import org.apache.spark.sql.{SaveMode, SparkSession}

object dryrunkarpaCrypto {

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

    import spark.implicits._
    import org.apache.spark.sql.functions._

    // *****************************************************************************************************
    // Ethereum table Transformations
    println("Ethereum Initial DataFrame")
    val df_ethereum = spark.read.jdbc(url, "ethereum1", properties)
    df_ethereum.show(false)
    // Create Hive Internal table
    df_ethereum.write.mode(SaveMode.Overwrite).saveAsTable("scalagroup.Ethereum_InitialDataFrame1")

    // *****************************************************************************************************
    println("Ethereum DataFrame filtered by price > '1.3'")
    // filter() Transformation = filter the records in an RDD. filtering price > "1.3".
    val filtered_df_ethereum = df_ethereum.filter($"ethereum_price" > "1.3")
    filtered_df_ethereum.show(false)
    df_ethereum.write.mode(SaveMode.Overwrite).saveAsTable("scalagroup.Ethereum_Filteredbyprice1")
    //*****************************************************************************************************
    println("Ethereum DataFrame sortByKey() descending order by price")
    // sortByKey() Transformation
    val sorted_df_ethereum = filtered_df_ethereum.orderBy(desc("ethereum_price"))
    sorted_df_ethereum.show(false)
    // Create Hive Internal table
    sorted_df_ethereum.write.mode(SaveMode.Overwrite).saveAsTable("scalagroup.Ethereum_SortedByKeyByPrice1")
    //*******************************************************************************************************
    // Bitcoin table Transformations
    println("Bitcoin Initial DataFrame")
    val df_bitcoin = spark.read.jdbc(url, "bitcoin1", properties)
    df_bitcoin.show(false)
    // Create Hive Internal table
    df_bitcoin.write.mode(SaveMode.Overwrite).saveAsTable("scalagroup.Bitcoin_InitialDataFrame1")
    // *****************************************************************************************************

    println("Bitcoin DataFrame filtered by 'bitcoin_price < 250'")
    // filter() Transformation = filter the records in an RDD. filtering 'price < 250'.
    val filtered_df_bitcoin = df_bitcoin.filter($"bitcoin_price" < "250")
    filtered_df_bitcoin.show(false)
    // Create Hive Internal table
    filtered_df_bitcoin.write.mode(SaveMode.Overwrite).saveAsTable("scalagroup.Bitcoin_FilteredByPrice1")
    // *****************************************************************************************************

    println("Bitcoin DataFrame sortByKey() descending order by bitcoin_price")
    // sortByKey() Transformation
    val sorted_df_bitcoin = filtered_df_bitcoin.orderBy(desc("bitcoin_price"))
    sorted_df_bitcoin.show(false)
    // Create Hive Internal table
    sorted_df_bitcoin.write.mode(SaveMode.Overwrite).saveAsTable("scalagroup.Bitcoin_SortedByKeyByPrice1")

    // *****************************************************************************************************

    println("Drop the bitcoin_NULL column and Add a new column bitcoin_mean_price")
    val dropped_bitcoin_null_column = df_bitcoin.drop("bitcoin_NULL")
    // Compute the bitcoin_mean_price of the "bitcoin_price" column
    val bitcoin_mean_price = df_bitcoin.select(mean(col("bitcoin_price"))).first().getDouble(0)
    val mean_price_df_bitcoin = dropped_bitcoin_null_column.withColumn("bitcoin_mean_price", lit(bitcoin_mean_price))
    mean_price_df_bitcoin.show(false)
    mean_price_df_bitcoin.write.mode(SaveMode.Overwrite).saveAsTable("scalagroup.Bitcoin_Mean_Price1")
  }
}
