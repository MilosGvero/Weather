
CREATE TABLE IF NOT EXISTS weather_data (
    time TIMESTAMPTZ NOT NULL,
    location TEXT NOT NULL,
    current_temperature FLOAT NOT NULL,
    PRIMARY KEY (time, location)
);

SELECT create_hypertable('weather_data', 'time');