package ru.mail.feeds.statistic;

import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.api.java.function.MapPartitionsFunction;
import org.apache.spark.api.java.function.ReduceFunction;
import org.apache.spark.sql.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/*
Количество сессий, средняя глубина просмотра (по позиции фида) и
средняя продолжительность пользовательской сессии в ленте за день.
Определение сессии остаётся на ваше усмотрение;

Сложность алгоритма будет зависеть от сортировки. Не знаю точно какой алгоритм там используется,
но думаю что это сортировка слиянием - O(N logN)
 * */
public class StatisticBySessions {
    public static void main(String[] args) {

        SparkSession spark = SparkSession
                .builder()
                .appName("analyze_json")
                .master("local[*]")
                .enableHiveSupport()
                .getOrCreate();

        Dataset<Session> usersSessions = spark.sql("select * from feed")
                // Сортируем по позиции фида и разбиваем на партиции по user_id
                // чтобы найти сессии пользователя
                // сессия - подряд просмотренные посты
                .sort("position")
                .repartition(new Column("user_id"))
                .map(new MapFunction<Row, Feed>() {
                    @Override
                    public Feed call(Row value) throws Exception {
                        return new Feed();
                    }
                }, Encoders.bean(Feed.class))
                .mapPartitions(new MapPartitionsFunction<Feed, Session>() {
                    @Override
                    public Iterator<Session> call(Iterator<Feed> input) throws Exception {
                        long prevPosition = 0;
                        long durationCurrentSession = 0;
                        int currentCountFeedsViewed = 0;
                        List<Session> sessions = new ArrayList<>();
                        while (input.hasNext()) {
                            Feed currentFeed = input.next();
                            long currentPosition = currentFeed.getPosition();
                            // Если просмотренные посты пользователям идут друг за другом без разрывов, значит это было сделано в рамках одной сессии
                            // Пока условие выполняется, ведем подсчет длительности просмотра постов и колиество просмотренных постов
                            // Если произошел разрыв, то добавляем текущие счетчики в коллекции
                            if (currentPosition - prevPosition == 1) {
                                durationCurrentSession = durationCurrentSession + currentFeed.getDurationMs();
                                prevPosition = currentFeed.getPosition();
                                currentCountFeedsViewed++;
                            } else {
                                sessions.add(new Session(currentCountFeedsViewed, durationCurrentSession));
                                durationCurrentSession = currentFeed.getDurationMs();
                                prevPosition = currentFeed.getPosition();
                                currentCountFeedsViewed = 1;
                            }
                        }

                        return sessions.iterator();
                    }
                }, Encoders.bean(Session.class))
                .persist();
        long count = usersSessions.count();
        Session reduce = usersSessions.reduce(new ReduceFunction<Session>() {
            @Override
            public Session call(Session v1, Session v2) throws Exception {
                return new Session(v1.getCountViewedFeeds() + v2.getCountViewedFeeds(),
                        v1.getSessionDuration() + v2.getSessionDuration());
            }
        });

        System.out.println("Количетсво сессий: " + count);
        System.out.println("Средняя длительность сессии: " + reduce.getSessionDuration() / count);
        System.out.println("Средняя глубина просмотра: " + reduce.getCountViewedFeeds() / count);
    }
}
