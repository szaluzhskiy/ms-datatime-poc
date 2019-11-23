create user tus password 'tus_psw';

create schema tus authorization tus;

alter default privileges for user tus in schema tus grant select, insert, update, delete on tables to tus;
alter default privileges for user tus in schema tus grant usage, select on sequences to tus;
grant usage on schema tus to tus;

GRANT ALL PRIVILEGES ON TABLE tus.test_datatime_table TO tus;
ALTER TABLE tus.test_datatime_table OWNER TO tus;

revoke usage on schema tus from public;

alter user tus_ms set search_path = 'tus';




