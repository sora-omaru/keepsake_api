CREATE TABLE account
(
    id           BIGSERIAL PRIMARY KEY,

    google_sub   VARCHAR(255) NOT NULL UNIQUE,
    email        VARCHAR(320) NOT NULL,
    display_name VARCHAR(255),
    picture_url  TEXT,

    created_at   TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP
);
