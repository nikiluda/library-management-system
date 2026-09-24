MERGE INTO books (
    title,
    author,
    publication_year,
    isbn,
    total_copies,
    available_copies,
    version
)
KEY (isbn)
VALUES (
    'The Hobbit',
    'J.R.R. Tolkien',
    1937,
    '9780261102217',
    3,
    2,
    0
);

MERGE INTO books (
    title,
    author,
    publication_year,
    isbn,
    total_copies,
    available_copies,
    version
)
KEY (isbn)
VALUES (
    'Clean Code',
    'Robert C. Martin',
    2008,
    '9780132350884',
    2,
    2,
    0
);

MERGE INTO readers (
    first_name,
    last_name,
    email,
    phone,
    registration_date
)
KEY (email)
VALUES (
    'Ivan',
    'Petrov',
    'ivan.petrov@test.com',
    '+79990000001',
    CURRENT_DATE
);

MERGE INTO readers (
    first_name,
    last_name,
    email,
    phone,
    registration_date
)
KEY (email)
VALUES (
    'Anna',
    'Smirnova',
    'anna.smirnova@test.com',
    '+79990000002',
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
SELECT
    book.id,
    reader.id,
    CURRENT_DATE,
    DATEADD('DAY', 14, CURRENT_DATE),
    NULL,
    'ACTIVE'
FROM books book
CROSS JOIN readers reader
WHERE book.isbn = '9780261102217'
  AND reader.email = 'ivan.petrov@test.com'
  AND NOT EXISTS (
      SELECT 1
      FROM book_loans loan
      WHERE loan.book_id = book.id
        AND loan.reader_id = reader.id
        AND loan.status = 'ACTIVE'
  );
