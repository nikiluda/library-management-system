INSERT INTO books (
    title,
    author,
    publication_year,
    isbn,
    total_copies,
    available_copies,
    version
)
VALUES (
           'The Hobbit',
           'J.R.R. Tolkien',
           1937,
           '9780261102217',
           3,
           2,
           0
       );

INSERT INTO readers (
    first_name,
    last_name,
    email,
    phone,
    registration_date
)
VALUES (
           'Ivan',
           'Petrov',
           'ivan.petrov@test.com',
           '+79990000001',
           CURRENT_DATE
       );

INSERT INTO book_loans (
    book_id,
    reader_id,
    loan_date,
    due_date,
    return_date,
    status
)
VALUES (
           (SELECT id FROM books WHERE isbn = '9780261102217'),
           (SELECT id FROM readers WHERE email = 'ivan.petrov@test.com'),
           CURRENT_DATE,
           CURRENT_DATE + INTERVAL '14 days',
           NULL,
           'ACTIVE'
       );