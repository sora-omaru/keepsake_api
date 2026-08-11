CREATE TABLE entry
(
    id           BIGSERIAL PRIMARY KEY,
    topic_id     BIGINT       NOT NULL,
    workspace_id BIGINT       NOT NULL,
    member_id    BIGINT       NOT NULL,
    title        VARCHAR(100) NOT NULL,
    content      TEXT,
    created_at   TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_entry_topic
        FOREIGN KEY (workspace_id, topic_id)
            REFERENCES topic (workspace_id, id)
            ON DELETE RESTRICT,

    CONSTRAINT fk_entry_member
        FOREIGN KEY (workspace_id, member_id)
            REFERENCES member (workspace_id, id)
            ON DELETE RESTRICT,

    CONSTRAINT uq_entry_workspace_id
        UNIQUE (workspace_id, id)

);

CREATE INDEX idx_entry_topic_id
    ON entry (topic_id);

CREATE INDEX idx_entry_member_id
    ON entry (member_id);
