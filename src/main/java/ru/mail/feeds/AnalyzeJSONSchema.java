package ru.mail.feeds;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class AnalyzeJSONSchema {
    public static void main(String[] args) {
        SparkSession spark = SparkSession
                .builder()
                .appName("analyze_json")
                .master("local[*]")
                .getOrCreate();

        JavaSparkContext javaSparkContext = JavaSparkContext.fromSparkContext(spark.sparkContext());

        InputStream resourceAsStream = AnalyzeJSONSchema.class.getClassLoader().getResourceAsStream("data/feeds_show.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(resourceAsStream));
        List<String> jsons = reader.lines().collect(Collectors.toList());

        Dataset<Row> dataset = spark.read().json(javaSparkContext.parallelize(jsons)).toDF();
        dataset.printSchema();
    }
}
