drop keyspace if exists lab3_4;
create keyspace lab3_4 with replication = {'class': 'SimpleStrategy', 'replication_factor': 1};
use lab3_4;

drop table if exists users;
create table users
(
    handle        text,
    username      text,
    followers     set<text>,
    following     set<text>,
    info          map<text, text>,
    notifications list<text>,
    primary key (handle)
);

drop table if exists dweeds;
create table dweeds
(
    id      int,
    poster  text,
    content text,
    date    timestamp,
    primary key (id)
);

-- A user's feed is composed of the dweeds posted by the users they follow, sorted by newest first.
drop table if exists feeds;
create table feeds
(
    user_handle text,
    dweed_id    int,
    dweed_date  timestamp,
    primary key (user_handle, dweed_date)
) with clustering order by (dweed_date desc);

-- List of dweeds posted by a user, sorted by newest first.
drop table if exists user_dweeds;
create table user_dweeds
(
    user_handle text,
    dweed_id    int,
    dweed_date  timestamp,
    primary key (user_handle, dweed_date)
) with clustering order by (dweed_date desc);

drop table if exists dweed_likes;
create table dweed_likes
(
    dweed_id    int,
    user_handle text,
    like_date   timestamp,
    primary key ((dweed_id, user_handle), like_date)
) with clustering order by (like_date desc);

-- Index that allows us to obtain the users who liked a dweed
drop index if exists dweed_likes_id;
create index dweed_likes_id on dweed_likes (dweed_id);

-- Index that allows us to obtain the dweeds liked by a user
drop index if exists dweed_likes_user_handle;
create index dweed_likes_user_handle on dweed_likes (user_handle);

-- Index that allows us to query based on info
drop index if exists user_info;
create index user_info on users (entries(info));

insert into users(handle, username, followers, following, info, notifications)
values ('z_praxz', 'praxz',
        {'ViniciusLucius', 'diomont01'},
        {'ViniciusLucius', 'diomont01', 'quietglitch'},
        {'location': 'Portugal', 'birthdate': 'March 25'},
        ['Welcome to Dweeder!']);

insert into users(handle, username, followers, following, info, notifications)
values ('ViniciusLucius', 'Lucius Vinicius',
        {'z_praxz', 'quietglitch', 'rospuye'},
        {'z_praxz', 'quietglitch', 'rospuye'},
        {'birthdate': 'April 7'},
        ['Welcome to Dweeder', 'Follow your contacts!']);

insert into users(handle, username, followers, following, info, notifications)
values ('quietglitch', 'that one',
        {'z_praxz', 'ViniciusLucius', 'diomont01'},
        {'diomont01', 'ViniciusLucius', 'rospuye'},
        {'location': 'Imperium of Man'},
        []);

insert into users(handle, username, followers, following, info, notifications)
values ('diomont01', 'diogo',
        {'z_praxz', 'quietglitch', 'rospuye'},
        {'quietglitch', 'rospuye', 'z_praxz'},
        {'location': 'Aveiro', 'birthdate': 'April 13'},
        []);

insert into users(handle, username, followers, following, info, notifications)
values ('rospuye', 'isabel',
        {'ViniciusLucius', 'quietglitch', 'diomont01'},
        {'diomont01', 'ViniciusLucius'},
        {'link': 'Instagram', 'birthdate': 'June 12'},
        []);


insert into dweeds(id, poster, content, date)
values (1, 'ViniciusLucius', 'com o miranha lancado tenho medo de usar o twitter ent n me julguem se eu sumir',
        '2021-12-14 19:52:26');

insert into user_dweeds(user_handle, dweed_id, dweed_date)
values ('ViniciusLucius', 1, '2021-12-14 19:52:26');

insert into feeds(user_handle, dweed_id, dweed_date)
values ('z_praxz', 1, '2021-12-14 19:52:26');

insert into feeds(user_handle, dweed_id, dweed_date)
values ('quietglitch', 1, '2021-12-14 19:52:26');

insert into feeds(user_handle, dweed_id, dweed_date)
values ('rospuye', 1, '2021-12-14 19:52:26');


insert into dweeds(id, poster, content, date)
values (2, 'z_praxz', 'ngl, cassandra esta a ser muito penoso', '2021-12-16 16:49:07');

insert into user_dweeds(user_handle, dweed_id, dweed_date)
values ('z_praxz', 2, '2021-12-16 16:49:07');

insert into feeds(user_handle, dweed_id, dweed_date)
values ('ViniciusLucius', 2, '2021-12-16 16:49:07');

insert into feeds(user_handle, dweed_id, dweed_date)
values ('diomont01', 2, '2021-12-16 16:49:07');


insert into dweeds(id, poster, content, date)
values (3, 'quietglitch', 'STEAM GAMES FINALLY COME UP IN THE START MENU BLESS WHOEVER DID THIS',
        '2021-12-12 15:46:58');

insert into user_dweeds(user_handle, dweed_id, dweed_date)
values ('quietglitch', 3, '2021-12-12 15:46:58');

insert into feeds(user_handle, dweed_id, dweed_date)
values ('z_praxz', 3, '2021-12-12 15:46:58');

insert into feeds(user_handle, dweed_id, dweed_date)
values ('ViniciusLucius', 3, '2021-12-12 15:46:58');

insert into feeds(user_handle, dweed_id, dweed_date)
values ('diomont01', 3, '2021-12-12 15:46:58');


insert into dweeds(id, poster, content, date)
values (4, 'diomont01',
        'Just inhaled a lethal dose of carbon monoxide to better understand what goes on inside the heads of my brain-dead opponents.',
        '2021-11-22 17:58:02');

insert into user_dweeds(user_handle, dweed_id, dweed_date)
values ('diomont01', 4, '2021-11-22 17:58:02');

insert into feeds(user_handle, dweed_id, dweed_date)
values ('z_praxz', 4, '2021-11-22 17:58:02');

insert into feeds(user_handle, dweed_id, dweed_date)
values ('quietglitch', 4, '2021-11-22 17:58:02');

insert into feeds(user_handle, dweed_id, dweed_date)
values ('rospuye', 4, '2021-11-22 17:58:02');


insert into dweeds(id, poster, content, date)
values (5, 'rospuye', 'vou ter que ver o @ViniciusLucius dnv', '2021-12-12 18:35:26');

insert into user_dweeds(user_handle, dweed_id, dweed_date)
values ('rospuye', 5, '2021-12-12 18:35:26');

insert into feeds(user_handle, dweed_id, dweed_date)
values ('ViniciusLucius', 5, '2021-12-12 18:35:26');

insert into feeds(user_handle, dweed_id, dweed_date)
values ('quietglitch', 5, '2021-12-12 18:35:26');

insert into feeds(user_handle, dweed_id, dweed_date)
values ('diomont01', 5, '2021-12-12 18:35:26');

insert into dweeds(id, poster, content, date)
values (6, 'quietglitch', 'Today I found out that fried pickles are a thing, now must buy pickles.',
        '2021-12-18 12:30:03');

insert into user_dweeds(user_handle, dweed_id, dweed_date)
values ('quietglitch', 6, '2021-12-18 12:30:03');

insert into feeds(user_handle, dweed_id, dweed_date)
values ('z_praxz', 6, '2021-12-18 12:30:03');

insert into feeds(user_handle, dweed_id, dweed_date)
values ('ViniciusLucius', 6, '2021-12-18 12:30:03');

insert into feeds(user_handle, dweed_id, dweed_date)
values ('diomont01', 6, '2021-12-18 12:30:03');


insert into dweeds(id, poster, content, date)
values (7, 'quietglitch', 'a camila n fez este tweet mas quero mais', '2021-12-19 16:57:59');

insert into user_dweeds(user_handle, dweed_id, dweed_date)
values ('quietglitch', 7, '2021-12-19 16:57:59');

insert into feeds(user_handle, dweed_id, dweed_date)
values ('z_praxz', 7, '2021-12-19 16:57:59');

insert into feeds(user_handle, dweed_id, dweed_date)
values ('ViniciusLucius', 7, '2021-12-19 16:57:59');

insert into feeds(user_handle, dweed_id, dweed_date)
values ('diomont01', 7, '2021-12-19 16:57:59');


insert into dweed_likes(dweed_id, user_handle, like_date)
values (4, 'z_praxz', '2021-12-19 16:45:12');

insert into dweed_likes(dweed_id, user_handle, like_date)
values (4, 'ViniciusLucius', '2021-12-19 15:23:52');

insert into dweed_likes(dweed_id, user_handle, like_date)
values (5, 'z_praxz', '2021-12-17 13:02:27');


-- Updates and deletes
insert into users(handle, username, followers, following, info, notifications)
values ('del1', '', {}, {}, {}, []);
insert into users(handle, username, followers, following, info, notifications)
values ('del2', '', {}, {}, {}, []);
insert into users(handle, username, followers, following, info, notifications)
values ('del3', '', {}, {}, {}, []);
insert into users(handle, username, followers, following, info, notifications)
values ('del4', '', {}, {}, {}, []);
insert into users(handle, username, followers, following, info, notifications)
values ('del5', '', {}, {}, {}, []);

update users
set username = 'del1'
where handle = 'del1';
update users
set username = 'del2'
where handle = 'del2';
update users
set username = 'del3'
where handle = 'del3';
update users
set username = 'del4'
where handle = 'del4';
update users
set username = 'del5'
where handle = 'del5';

delete
from users
where handle in ('del1', 'del2', 'del3', 'del4', 'del5');

update users
set notifications = notifications + ['diogo, your 2020 Dweeder recap has arrived!']
where handle = 'diomont01';

delete notifications[0]
from users
where handle = 'ViniciusLucius';

update users
set info = info + {'location': 'Portugal'}
where handle = 'rospuye';

select *
from users
where handle = 'rospuye';

-- Queries

-- 1. Select a user's liked dweeds (uses secondary index)
select *
from dweed_likes
where user_handle = 'z_praxz';

-- 2. Select a dweed's likes (uses secondary index)
select *
from dweed_likes
where dweed_id = 4;

-- 3. Select the 2 most recent dweeds in a user's feed
select *
from feeds
where user_handle = 'diomont01'
limit 2;

-- 4. Select a user's dweeds, from oldest to newest
select *
from user_dweeds
where user_handle = 'quietglitch'
order by dweed_date;
-- asc is the default

-- 5. Count the number of dweeds posted by each user
select user_handle, count(dweed_id) as num_dweeds
from user_dweeds
group by user_handle;

-- 6. Count the total number of likes given by a user
select user_handle, count(dweed_id) as num_dweeds_liked
from dweed_likes
where user_handle = 'z_praxz';

-- 7. Count the number of dweeds in each user's feed
select user_handle, count(dweed_id) as feed_num_dweeds
from feeds
group by user_handle;

-- 8. Show the dweeds posted on 2021-12-19 on a user's feed
select *
from feeds
where user_handle = 'z_praxz'
  and dweed_date > '2021-12-19'
  and dweed_date < '2021-12-20';

-- 9. List users whose location is Portugal (uses secondary index)
select *
from users
where info['location'] = 'Portugal';

-- 10. List each user's most recent dweed
select *
from user_dweeds
group by user_handle;
