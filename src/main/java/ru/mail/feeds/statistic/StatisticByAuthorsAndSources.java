package ru.mail.feeds.statistic;

import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.api.java.function.ReduceFunction;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

/*
* Количество за день уникальных авторов и уникального контента, показанного в ленте;
* */
public class StatisticByAuthorsAndSources {

    public static void main(String[] args) {
        SparkSession spark = SparkSession
                .builder()
                .appName("analyze_json")
                .master("local[*]")
                .enableHiveSupport()
                .getOrCreate();

        UniqueAuthorsAndContents uniqueAuthorsAndContents = spark.sql("select * from feed")
                // Из каждой записи в БД создам объект UniqueAuthorsAndContents,
                // перекладывая авторов и контент из листов в сет, таким образом убираем дубли на уровне строк
                .map(new MapFunction<Row, UniqueAuthorsAndContents>() {
                    @Override
                    public UniqueAuthorsAndContents call(Row value) throws Exception {
                        return new UniqueAuthorsAndContents();
                    }
                }, Encoders.bean(UniqueAuthorsAndContents.class))
                // Аггрегируем промежуточные результаты
                .reduce(new ReduceFunction<UniqueAuthorsAndContents>() {
                    @Override
                    public UniqueAuthorsAndContents call(UniqueAuthorsAndContents v1, UniqueAuthorsAndContents v2) throws Exception {
                        v1.getUsers().addAll(v2.getUsers());
                        v1.getGroups().addAll(v2.getGroups());
                        v1.getUserPhotos().addAll(v2.getUserPhotos());
                        v1.getGroupPhotos().addAll(v2.getGroupPhotos());
                        v1.getMovies().addAll(v2.getMovies());
                        v1.getPosts().addAll(v2.getPosts());
                        return v1;
                    }
                });

        System.out.println("Количетсво уникальных авторов-пользователей: " + uniqueAuthorsAndContents.getUsers().size());
        System.out.println("Количетсво уникальных авторов-групп: " + uniqueAuthorsAndContents.getGroups().size());
        System.out.println("Количетсво уникального фоток пользователй: " + uniqueAuthorsAndContents.getUserPhotos().size());
        System.out.println("Количетсво уникального фоток групп: " + uniqueAuthorsAndContents.getGroupPhotos().size());
        System.out.println("Количетсво уникального видосиков: " + uniqueAuthorsAndContents.getMovies().size());
        System.out.println("Количетсво уникального постов: " + uniqueAuthorsAndContents.getPosts().size());
    }

}
