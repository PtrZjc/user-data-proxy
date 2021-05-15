DROP
  FUNCTION IF EXISTS increment_fetch_counter(TEXT);

CREATE
OR REPLACE FUNCTION increment_fetch_counter(user_login TEXT) RETURNS BIGINT
AS
$$
WITH initial_value AS (
  SELECT
    COALESCE(
      (
        SELECT
          request_count
        FROM
          user_proxy.fetch_stats
        WHERE
          login = user_login
      ),
      0
    ) AS counter
)

INSERT INTO user_proxy.fetch_stats (login, request_count)
VALUES (user_login, 1)
ON CONFLICT (login) DO
    UPDATE
    SET
      request_count = (
        SELECT
          counter
        FROM
          initial_value
      ) + 1 RETURNING request_count
$$ LANGUAGE SQL;
