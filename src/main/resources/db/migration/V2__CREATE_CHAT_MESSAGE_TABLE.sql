
CREATE TABLE chat_message (
  id bigint AUTO_INCREMENT PRIMARY KEY,
  created_on datetime(6) DEFAULT CURRENT_TIMESTAMP(6),
  message_content varchar(255) NOT NULL,
  message_sender varchar(255) NOT NULL,
  session_id bigint NOT NULL,
  FOREIGN KEY (session_id) REFERENCES chat_session (id)
) ;
