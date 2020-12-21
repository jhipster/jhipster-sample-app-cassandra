INSERT INTO user (id,login,password,firstname,lastname,email,activated,lang_key,activation_key,authorities)
VALUES('1','admin','$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC','Administrator','Administrator','admin@localhost',true,'en','',{'ROLE_USER','ROLE_ADMIN'});

INSERT INTO user_by_login (login, id)
VALUES('admin','1');

INSERT INTO user_by_email (email, id)
VALUES('admin@localhost','1');

INSERT INTO user (id,login,password,firstname,lastname,email,activated,lang_key,activation_key,authorities)
VALUES('2','user','$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K','User','User','user@localhost',true,'en','',{'ROLE_USER'});

INSERT INTO user_by_login (login, id)
VALUES('user','2');

INSERT INTO user_by_email (email, id)
VALUES('user@localhost','2');
