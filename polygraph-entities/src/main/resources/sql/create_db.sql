-- Role: polygraph

-- DROP ROLE polygraph;

CREATE ROLE polygraph LOGIN
  ENCRYPTED PASSWORD 'md58de521fa2a705ef4d14ccc8af74fddf5'
  NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE NOREPLICATION VALID UNTIL 
  	'2099-05-19 00:00:00';


-- Database: polygraph

-- DROP DATABASE polygraph;

CREATE DATABASE polygraph
  WITH OWNER = polygraph
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       LC_COLLATE = 'Russian, Russia'
       LC_CTYPE = 'Russian, Russia'
       CONNECTION LIMIT = -1;
       

