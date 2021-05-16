DROP TABLE IF EXISTS user_proxy.fetch_stats;
CREATE TABLE user_proxy.fetch_stats (
    id                SERIAL      PRIMARY KEY,
    login             TEXT        UNIQUE,
    request_count     BIGINT      DEFAULT 0
);