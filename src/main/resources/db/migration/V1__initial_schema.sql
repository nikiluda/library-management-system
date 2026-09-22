CREATE TABLE books (
                       id BIGSERIAL PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       author VARCHAR(255) NOT NULL,
                       publication_year INTEGER NOT NULL,
                       isbn VARCHAR(255) NOT NULL UNIQUE,
                       total_copies INTEGER NOT NULL,
                       available_copies INTEGER NOT NULL,
                       version BIGINT NOT NULL,


                       CONSTRAINT chk_books_copies_range
                           CHECK (
                               available_copies >= 0
                                   AND available_copies <= total_copies
                               )
);

CREATE TABLE readers (
                         id BIGSERIAL PRIMARY KEY,
                         first_name VARCHAR(255) NOT NULL,
                         last_name VARCHAR(255) NOT NULL,
                         email VARCHAR(255) NOT NULL UNIQUE,
                         phone VARCHAR(255) NOT NULL UNIQUE,
                         registration_date DATE
);

CREATE TABLE library_user (
                              id BIGSERIAL PRIMARY KEY,
                              email VARCHAR(255) NOT NULL UNIQUE,
                              password_hash VARCHAR(255) NOT NULL,
                              role VARCHAR(50) NOT NULL,
                              enabled BOOLEAN NOT NULL,
                              created_at TIMESTAMP NOT NULL,
                              reader_id BIGINT UNIQUE,

                              CONSTRAINT fk_library_user_reader
                                  FOREIGN KEY (reader_id)
                                      REFERENCES readers(id)
);

CREATE TABLE book_loans (
                            id BIGSERIAL PRIMARY KEY,
                            book_id BIGINT NOT NULL,
                            reader_id BIGINT NOT NULL,
                            loan_date DATE NOT NULL,
                            due_date DATE NOT NULL,
                            return_date DATE,
                            status VARCHAR(50) NOT NULL,

                            CONSTRAINT fk_book_loans_book
                                FOREIGN KEY (book_id)
                                    REFERENCES books(id),

                            CONSTRAINT fk_book_loans_reader
                                FOREIGN KEY (reader_id)
                                    REFERENCES readers(id)
);