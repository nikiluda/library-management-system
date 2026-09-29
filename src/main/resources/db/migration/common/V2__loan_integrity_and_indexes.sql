CREATE INDEX idx_book_loans_book_status
    ON book_loans (book_id, status);

CREATE INDEX idx_book_loans_reader_loan_date
    ON book_loans (reader_id, loan_date);

CREATE INDEX idx_book_loans_status_due_date
    ON book_loans (status, due_date);

ALTER TABLE book_loans
    ADD CONSTRAINT chk_book_loans_due_date
        CHECK (due_date >= loan_date);

ALTER TABLE book_loans
    ADD CONSTRAINT chk_book_loans_return_state
        CHECK (
            (status = 'ACTIVE' AND return_date IS NULL)
            OR (status = 'RETURNED' AND return_date IS NOT NULL AND return_date >= loan_date)
        );
