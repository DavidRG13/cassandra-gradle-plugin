CREATE KEYSPACE cas WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'}  AND durable_writes = true;

CREATE TABLE cas.ticketgrantingticket (
    id text PRIMARY KEY,
    ticket text,
    ticket_granting_ticket_id text,
    date_bucket int
) WITH default_time_to_live = 5184000;

CREATE MATERIALIZED VIEW cas.ticket_cleaner
AS SELECT date_bucket, ticket
FROM cas.ticketgrantingticket
WHERE date_bucket IS NOT NULL AND ticket IS NOT NULL
PRIMARY KEY((date_bucket), ticket)
WITH CLUSTERING ORDER BY (date_bucket ASC);

CREATE MATERIALIZED VIEW cas.proxygrantingticket
AS SELECT ticket_granting_ticket_id
FROM cas.ticketgrantingticket
WHERE ticket_granting_ticket_id IS NOT NULL
PRIMARY KEY ((ticket_granting_ticket_id), id);


CREATE TABLE cas.serviceticket (
    id text PRIMARY KEY,
    ticket text
) WITH default_time_to_live = 60;

CREATE TABLE cas.ticket_cleaner_lastrun (
    id text PRIMARY KEY,
    last_run int
);
