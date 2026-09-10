-- Legacy entries retain their member attribution; new entries use the authenticated account.
ALTER TABLE entry ALTER COLUMN member_id DROP NOT NULL;
ALTER TABLE entry ADD COLUMN creator_account_id BIGINT REFERENCES account(id) ON DELETE RESTRICT;
ALTER TABLE entry ADD CONSTRAINT ck_entry_author CHECK (member_id IS NOT NULL OR creator_account_id IS NOT NULL);
CREATE INDEX idx_entry_creator_account_id ON entry(creator_account_id);
