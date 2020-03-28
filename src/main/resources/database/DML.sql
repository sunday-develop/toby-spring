alter table users
    ADD COLUMN level tinyint not null;
alter table users
    ADD COLUMN login int not null;
alter table users
    ADD COLUMN recommend int not null;