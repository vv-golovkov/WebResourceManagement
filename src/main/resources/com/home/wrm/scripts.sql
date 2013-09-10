-- *************************************************************** --
create database wrmdb character set cp1251 collate cp1251_general_ci;
-- *************************************************************** --
create table if not exists directory (
	id int not null auto_increment primary key,
	name varchar(255) not null,
	full_path varchar(255) not null,
	par_dir_id int (11) default null
) character set cp1251 collate cp1251_general_ci;

create table if not exists resource (
	id int not null auto_increment primary key,
	name varchar(255) not null,
	link text not null,
	saving_date varchar(50) not null,
	saving_time varchar(50) not null,
	dir_id int (11) default null
) character set cp1251 collate cp1251_general_ci;

create table if not exists archived_resource (
	id int not null auto_increment primary key,
	name varchar(255) not null,
	link text not null,
	dir_path varchar(255) not null,
	saving_date varchar(50) not null,
	deleting_date date not null
) character set cp1251 collate cp1251_general_ci;
 *************************************************************** --
insert into directory(name, par_dir_id) values('aaa', null);
insert into directory(name, par_dir_id) values('bbb', 1);
insert into directory(name, par_dir_id) values('ccc', null);
insert into directory(name, par_dir_id) values('ddd', 2);
insert into directory(name, par_dir_id) values('eee', 2);
 *************************************************************** --
update directory d set name = 'ddd' where d.id = 4;
 *************************************************************** --
delete from directory where id = 3;
 *************************************************************** --
select * from directory;