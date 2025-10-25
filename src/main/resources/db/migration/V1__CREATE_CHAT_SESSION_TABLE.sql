CREATE TABLE users (
user_id bigint AUTO_INCREMENT PRIMARY KEY,
user_name varchar(255) NOT NULL,
user_email varchar(255) NOT NULL UNIQUE,
created_on datetime(6) DEFAULT CURRENT_TIMESTAMP(6)
);

CREATE TABLE chat_session (
  id bigint AUTO_INCREMENT PRIMARY KEY,
  created_on datetime(6) DEFAULT CURRENT_TIMESTAMP(6),
  is_favorite BOOLEAN DEFAULT FALSE,
  session_name varchar(255) NOT NULL,
  user_id bigint NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users (user_id)
) ;
