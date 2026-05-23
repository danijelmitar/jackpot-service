CREATE TABLE IF NOT EXISTS bet
(
    bet_id     VARCHAR(36)    NOT NULL PRIMARY KEY,
    user_id    VARCHAR(255)   NOT NULL,
    jackpot_id VARCHAR(255)   NOT NULL,
    bet_amount DECIMAL(19, 4) NOT NULL
);

CREATE TABLE IF NOT EXISTS jackpot
(
    jackpot_id                 VARCHAR(36)    NOT NULL PRIMARY KEY,
    pool_amount                DECIMAL(19, 4) NOT NULL,
    initial_pool_amount        DECIMAL(19, 4) NOT NULL,
    contribution_strategy_type VARCHAR(50)    NOT NULL,
    reward_strategy_type       VARCHAR(50)    NOT NULL
);

CREATE TABLE IF NOT EXISTS jackpot_contribution
(
    bet_id                 VARCHAR(36)    NOT NULL,
    jackpot_id             VARCHAR(36)    NOT NULL REFERENCES jackpot (jackpot_id),
    user_id                VARCHAR(255)   NOT NULL,
    stake_amount           DECIMAL(19, 4) NOT NULL,
    contribution_amount    DECIMAL(19, 4) NOT NULL,
    current_jackpot_amount DECIMAL(19, 4) NOT NULL,
    created_at             TIMESTAMP      NOT NULL,
    PRIMARY KEY (bet_id, jackpot_id)
);

CREATE TABLE IF NOT EXISTS jackpot_reward
(
    bet_id        VARCHAR(36)    NOT NULL,
    jackpot_id    VARCHAR(36)    NOT NULL REFERENCES jackpot (jackpot_id),
    user_id       VARCHAR(255)   NOT NULL,
    reward_amount DECIMAL(19, 4) NOT NULL,
    created_at    TIMESTAMP      NOT NULL,
    PRIMARY KEY (bet_id, jackpot_id)
);
