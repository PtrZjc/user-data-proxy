CREATE SCHEMA IF NOT EXISTS user_proxy;

DROP TABLE IF EXISTS user_proxy.character;
CREATE TABLE user_proxy.fetch_stats (
    id                BIGINT      PRIMARY KEY,
    login             TEXT,
    request_count     BIGINT      DEFAULT 0
);