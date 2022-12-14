create table news
(
    id                bigserial,
    name              varchar unique,
    width             smallint,
    height            smallint,
    dpi               smallint,
    original_filename varchar,
    file_link         varchar,
    upload            timestamp default CURRENT_TIMESTAMP
);

