CREATE TABLE IF NOT EXISTS normal_book(
                                          id int unsigned not null auto_increment,
                                          author varchar(255) not null,
                                          title varchar(255) not null,
                                          language varchar(255) not null,
                                          number_of_hard_copies int not null,
                                          loan_period int not null,
                                          PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS audio_book(
                                         id int unsigned not null auto_increment,
                                         author varchar(255) not null,
                                         title varchar(255) not null,
                                         language varchar(255) not null,
                                         free_trial_period int not null,
                                         PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS loan_record(
                                          id int unsigned not null auto_increment,
                                          book_id int unsigned not null,
                                          user_name varchar(255) not null,
                                          loan_type varchar(255) not null,
                                          PRIMARY KEY (id),
                                          FOREIGN KEY (book_id) REFERENCES normal_book(id)
);