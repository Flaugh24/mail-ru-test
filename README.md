# mail-ru-test

## Схема данных
https://github.com/Flaugh24/mail-ru-test/blob/master/src/main/resources/schema.sql

## Валидация входных данных
Для валидации json воспользовался библиотекой https://github.com/everit-org/json-schema
Библиотека позволяет описать схему json-файла - поля, обязательные поля, типы данных, возможные значения. 
Привел примеры тестов:
1) Все ок
2) Отстувует атрибут платформы
3) Неправильное название платформы
4) UserId - String вместо Long
5) Отрацательный Timestamp

https://github.com/Flaugh24/mail-ru-test/blob/master/src/test/java/ru/mail/feeds/JSONValidatorTest.java

## Задания
1) Взял за основу то, что одного и того же пользователя на разных платформах считаем за разных пользователей
Сделал на SQL, так как это показалось проще - https://github.com/Flaugh24/mail-ru-test/blob/master/src/main/resources/schema.sql
2) https://github.com/Flaugh24/mail-ru-test/blob/master/src/main/java/ru/mail/feeds/statistic/StatisticByAuthorsAndSources.java
3) Сессию определил как просмотренные подряд посты пользователям. 
Т.е если у нас есть информация, что пользователь просмотрел посты 1-5 - это одна сессия, 
потом пользователь вышел, лента обновилась, и он просмотрел 10-20 - другая сессия

https://github.com/Flaugh24/mail-ru-test/blob/master/src/main/java/ru/mail/feeds/statistic/StatisticBySessions.java

Можно сделать лучше - считая свертку и количество сессий за один проход, но ...

4) Переложил список юзеров в HashMap, в итоге фильтрация стала за O(1). 

https://github.com/Flaugh24/mail-ru-test/blob/master/src/main/java/ru/mail/feeds/statistic/StatisticByUesrs.java
