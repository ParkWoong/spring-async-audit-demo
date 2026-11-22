CREATE TABLE api_request_log (
    t_ymdhms VARCHAR(17),
    random_id VARCHAR(2),
    user_id VARCHAR(80),
    client_id VARCHAR(80),
    token_key_id VARCHAR(80),
    request_uri VARCHAR(1000),
    request_header VARCHAR(4000),
    request_body VARCHAR(4000),
    response_code VARCHAR(3)
);
