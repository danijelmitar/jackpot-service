CREATE TABLE IF NOT EXISTS bet
(
    bet_id     VARCHAR(36)    NOT NULL PRIMARY KEY,
    user_id    VARCHAR(255)   NOT NULL,
    jackpot_id VARCHAR(255)   NOT NULL,
    bet_amount DECIMAL(19, 4) NOT NULL
);
