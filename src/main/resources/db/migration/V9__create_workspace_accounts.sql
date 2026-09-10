CREATE TABLE workspace_account
(
    workspace_id BIGINT NOT NULL,
    account_id   BIGINT NOT NULL,

    CONSTRAINT pk_workspace_account
        PRIMARY KEY (workspace_id, account_id),

    CONSTRAINT fk_workspace_account_workspace
        FOREIGN KEY (workspace_id)
            REFERENCES workspace (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_workspace_account_account
        FOREIGN KEY (account_id)
            REFERENCES account (id)
            ON DELETE CASCADE
);

CREATE INDEX idx_workspace_account_account_id
    ON workspace_account (account_id);
