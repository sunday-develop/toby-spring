ALTER TABLE users
ADD COLUMN level tinyint not null,
ADD COLUMN login int not null,
ADD COLUMN recommend int not null
;