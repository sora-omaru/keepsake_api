CREATE TABLE topic
(
    id           BIGSERIAL PRIMARY KEY,
    workspace_id BIGINT       NOT NULL,
    name         VARCHAR(100) NOT NULL,
    description  TEXT,
    created_at   TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_topic_workspace
        FOREIGN KEY (workspace_id)
            REFERENCES workspace (id)
            ON DELETE RESTRICT,

    CONSTRAINT uq_topic_workspace_id
        UNIQUE (workspace_id, id),

    CONSTRAINT uq_topic_workspace_name
        UNIQUE (workspace_id, name)
);
