-- Опишите схему данных в любой удобной вам нотации.
create table feed
(
    user_id     integer,
    platform    string,
    duration_Ms bigint,
    position    bigint,
    `timestamp` bigint,
    owners      struct<`group` :array<bigint>, `user` :array<bigint>>,
    resources   struct<group_photo : array<bigint>, movie :array<bigint>, post :array<bigint>, user_photo
                       :array<bigint>>
)
    stored as orc;


-- Количество показов и уникальных пользователей за день в разрезе по платформам, в том числе по всем платформам суммарно;

with statistic_by_platform as (
    select platform,
           count(1)                as count_shows,
           count(distinct user_id) as count_distinct_users
    from feed
    group by platform
),
     statistic_all as (
         select "all_platforms"           as platform,
                sum(count_shows)          as count_shows,
                sum(count_distinct_users) as count_distinct_users
         from statistic_by_platform
     )
select platform,
       count_shows,
       count_distinct_users
from statistic_by_platform
union
select platform,
       count_shows,
       count_distinct_users
from statistic_all;