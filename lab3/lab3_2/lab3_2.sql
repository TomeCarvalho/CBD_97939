-- int IDs are used here to facilitate insertions, but uuid would be better in an actual system
drop keyspace if exists lab3_2;
create keyspace lab3_2 with replication = {'class': 'SimpleStrategy', 'replication_factor': 1};
use lab3_2;

drop table if exists users;
create table users
(
    username      text,
    name          text,
    email         text,
    register_date timestamp,
    primary key (username)
);

drop table if exists videos;
create table videos
(
    id              int,
    author_username text,
    name            text,
    description     text,
    tags            set<text>,
    upload_ts       timestamp,
    primary key (id, upload_ts)
) with clustering order by (upload_ts desc);

-- To find videos with a certain tag
drop table if exists videos_by_tag;
create table videos_by_tag
(
    tag      text,
    video_id int,
    primary key (tag, video_id)
);

-- To find videos by author, with filtering by upload timestamp
drop table if exists videos_by_author;
create table videos_by_author
(
    author_username text,
    video_id        int,
    upload_ts       timestamp,
    primary key (author_username, upload_ts, video_id)
);

-- 8. Permitir a pesquisa de comentários por utilizador, ordenado inversamente pela data
drop table if exists comments_by_username;
create table comments_by_username
(
    author_username text,
    video_id        int,
    comment         text,
    ts              timestamp,
    primary key (author_username, ts)
) with clustering order by (ts desc);

-- 9. Permitir a pesquisa de comentários por vídeos, ordenado inversamente pela data
drop table if exists comments_by_video;
create table comments_by_video
(
    video_id        int,
    author_username text,
    comment         text,
    ts              timestamp,
    primary key (video_id, ts)
) with clustering order by (ts desc);


-- Users who follow a video
drop table if exists video_followers;
create table video_followers
(
    video_id int,
    username text,
    primary key (video_id, username)
);

-- Videos followed by a user
drop table if exists user_followed_videos;
create table user_followed_videos
(
    username  text,
    video_ids set<int>,
    primary key (username)
);

drop table if exists events;
create table events
(
    id       int,
    video_id int,
    username text,
    type     text,
    ts       timestamp,
    video_ts int, -- in seconds
    primary key ((video_id, username), ts)
) with clustering order by (ts desc);

drop table if exists ratings;
create table ratings
(
    id       int,
    video_id int,
    score    int,
    primary key (video_id, id)
);

-- Insert users

insert into users(username, name, email, register_date)
values ('johndoe', 'John Doe', 'johndoe@cbd.com', '2019-01-14');

insert into users(username, name, email, register_date)
values ('janedoe', 'Jane Doe', 'janedoe@cbd.com', '2020-05-23');

insert into users(username, name, email, register_date)
values ('mikerotch', 'Mike Rotch', 'mikerotch@cbd.com', '2021-08-02');


-- Insert videos

insert into videos(id, author_username, name, description, tags, upload_ts)
values (1, 'johndoe', 'University of Aveiro Review', '5/7 with rice', {'Aveiro', 'University'}, '2020-11-26');
insert into videos_by_tag(tag, video_id)
values ('Aveiro', 1);
insert into videos_by_tag(tag, video_id)
values ('University', 1);
insert into videos_by_author(author_username, video_id, upload_ts)
values ('johndoe', 1, '2020-11-26');

insert into videos(id, author_username, name, description, tags, upload_ts)
values (2, 'johndoe', 'How to breathe', 'Inhale, exhale', {'Tutorial', 'Survival'}, '2021-03-12');
insert into videos_by_tag(tag, video_id)
values ('Tutorial', 2);
insert into videos_by_tag(tag, video_id)
values ('Survival', 2);
insert into videos_by_author(author_username, video_id, upload_ts)
values ('johndoe', 2, '2021-03-12');

insert into videos(id, author_username, name, description, tags, upload_ts)
values (3, 'janedoe', 'Best touristic spots in Aveiro', 'Make sure to visit them!', {'Aveiro', 'Tourism'},
        '2021-02-19');
insert into videos_by_tag(tag, video_id)
values ('Aveiro', 3);
insert into videos_by_tag(tag, video_id)
values ('Tourism', 3);
insert into videos_by_author(author_username, video_id, upload_ts)
values ('janedoe', 3, '2021-02-19');


-- Insert followers
insert into video_followers(video_id, username)
values (1, 'mikerotch');
insert into video_followers(video_id, username)
values (2, 'mikerotch');
insert into user_followed_videos(username, video_ids)
values ('mikerotch', {1, 2});


-- Insert comments
insert into comments_by_video(video_id, author_username, comment, ts)
values (2, 'mikerotch', 'Ty, really helpful, I almost died cuz I forgot how to breathe', '2021-04-01 16:21:12');
insert into comments_by_username(author_username, video_id, comment, ts)
values ('mikerotch', 2, 'Ty, really helpful, I almost died cuz I forgot how to breathe', '2021-04-01 16:21:12');

insert into comments_by_video(video_id, author_username, comment, ts)
values (1, 'mikerotch', '10/10 would suffer from project and test burn-out again', '2021-12-11 17:00:56');
insert into comments_by_username(author_username, video_id, comment, ts)
values ('mikerotch', 1, '10/10 would suffer from project and test burn-out again', '2021-12-11 17:00:56');

insert into comments_by_video(video_id, author_username, comment, ts)
values (2, 'janedoe', 'air go brrr', '2021-11-11 14:23:59');
insert into comments_by_username(author_username, video_id, comment, ts)
values ('janedoe', 2, 'air go brrr', '2021-11-11 14:23:59');

insert into comments_by_video(video_id, author_username, comment, ts)
values (2, 'janedoe', 'air air air', '2021-11-10 13:16:33');
insert into comments_by_username(author_username, video_id, comment, ts)
values ('janedoe', 2, 'air air air', '2021-11-10 13:16:33');

insert into comments_by_video(video_id, author_username, comment, ts)
values (2, 'janedoe', 'older comment about air probably', '2021-03-13 12:01:24');
insert into comments_by_username(author_username, video_id, comment, ts)
values ('janedoe', 2, 'older comment about air probably', '2021-03-13 12:01:24');


-- Insert ratings

insert into ratings(id, video_id, score)
values (1, 1, 4);

insert into ratings(id, video_id, score)
values (2, 1, 3);

insert into ratings(id, video_id, score)
values (3, 1, 5);

insert into ratings(id, video_id, score)
values (4, 2, 5);

insert into ratings(id, video_id, score)
values (5, 2, 5);

insert into ratings(id, video_id, score)
values (6, 3, 2);


-- Insert events
insert into events(id, video_id, username, type, ts, video_ts)
values (1, 1, 'mikerotch', 'play', '2021-12-11 16:51:02', 0);

insert into events(id, video_id, username, type, ts, video_ts)
values (2, 1, 'mikerotch', 'pause', '2021-12-11 16:55:02', 240);

insert into events(id, video_id, username, type, ts, video_ts)
values (3, 1, 'mikerotch', 'play', '2021-12-11 16:55:12', 240);

insert into events(id, video_id, username, type, ts, video_ts)
values (4, 1, 'mikerotch', 'pause', '2021-12-11 16:55:16', 244);

insert into events(id, video_id, username, type, ts, video_ts)
values (5, 1, 'mikerotch', 'play', '2021-12-11 16:55:30', 244);

insert into events(id, video_id, username, type, ts, video_ts)
values (6, 1, 'mikerotch', 'stop', '2021-12-11 16:55:40', 254);

-- b)
select json * from users;
select json * from videos;
select json * from videos_by_tag;
select json * from videos_by_author;
select json * from comments_by_username;
select json * from comments_by_video;
select json * from video_followers;
select json * from user_followed_videos;
select json * from events;

-- c)

-- 7. Permitir a pesquisa de todos os vídeos de determinado autor;
select *
from videos_by_author
where author_username = 'johndoe';

-- 8. Permitir a pesquisa de comentários por utilizador, ordenado inversamente pela data;
select *
from comments_by_username
where author_username = 'mikerotch';

-- 9. Permitir a pesquisa de comentários por vídeos, ordenado inversamente pela data;
select *
from comments_by_video
where video_id = 2;

-- 10. Permitir a pesquisa do rating médio de um vídeo e quantas vezes foi votado;
select avg(score) as avg_score, count(score) as num_ratings
from ratings
where video_id = 1;

-- d)
-- 1. Os últimos 3 comentários introduzidos para um vídeo;
select *
from comments_by_video
where video_id = 2
limit 3;

-- 2. Lista das tags de determinado vídeo;
select tags
from videos
where id = 2;

-- 3. Todos os vídeos com a tag Aveiro;
select *
from videos_by_tag
where tag = 'Aveiro';

-- 4. Os últimos 5 eventos de determinado vídeo realizados por um utilizador; TODO
/*
select *
from events
where video_id = 1
  and username = 'mikerotch'
  limit 5;*/

-- 5. Vídeos partilhados por determinado utilizador (maria1987, por exemplo) num
-- determinado período de tempo (Agosto de 2017, por exemplo);
-- Videos uploaded by johndoe in 2020
select *
from videos_by_author
where author_username = 'johndoe'
  and upload_ts >= '2020-01-01'
  and upload_ts < '2021-01-01';

-- 6. Os últimos 10 vídeos, ordenado inversamente pela data da partilhada;
-- Impossível a não ser que tivéssemos vídeos com o mesmo ID (o que nunca deve acontecer) e filtrássemos por ID

-- 7. Todos os seguidores (followers) de determinado vídeo;
select * from video_followers where video_id = 1;

-- 8. Todos os comentários (dos vídeos) que determinado utilizador está a seguir (following);
-- A seguinte query devolve o set de vídeos que o utilizador mikerotch segue
-- Seria necessário ir buscar os comments dos vídeos cujos IDs estão no set
select video_ids from user_followed_videos where username = 'mikerotch';

-- 9. Os 5 vídeos com maior rating;
-- Impossível?

-- 10. Uma query que retorne todos os vídeos e que mostre claramente a forma pela qual estão ordenados;
select * from videos;

-- 11. Lista com as Tags existentes e o número de vídeos catalogados com cada uma delas;
select tag, count(video_id) from videos_by_tag group by tag;