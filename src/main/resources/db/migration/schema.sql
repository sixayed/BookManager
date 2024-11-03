create schema if not exists library;

create table library.book (
    id bigserial primary key,
    title varchar(100) not null check (length(trim(title)) >= 3),
    author varchar(50) not null check (length(trim(author)) >= 5),
    published_date date
);