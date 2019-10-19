package ru.mail.feeds.statistic;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/*
 * Дополнительно дан список пользователей в формате <userId: Long>,
 * включающий в себе несколько тысяч пользователей.
 * Нужно посчитать количество показов фидов в ленте по этим пользователям.
 *
 * Сложность алгоритма O(N)
 */
public class StatisticByUesrs {
    public static void main(String[] args) {

        SparkSession spark = SparkSession
                .builder()
                .appName("analyze_json")
                .master("local[*]")
                .enableHiveSupport()
                .getOrCreate();

        List<Long> usersForComputeStatistic = new ArrayList<>();
        HashSet<Long> usersId = new HashSet<>(usersForComputeStatistic);

        JavaPairRDD<Long, Long> viewedByUsers = spark
                .sql("select * from feed")
                .toJavaRDD()
                .mapToPair(new PairFunction<Row, Long, Long>() {
                    @Override
                    public Tuple2<Long, Long> call(Row row) throws Exception {
                        return new Tuple2<>(row.getAs("user_id"), 1L);
                    }
                })
                .filter(x -> usersId.contains(x._1))
                .reduceByKey(Long::sum);
    }
}
