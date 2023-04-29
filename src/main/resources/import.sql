INSERT INTO member (username, password, nickname) VALUES ('ks', '{bcrypt}$2a$10$oVbP3wYPdpZ/eG1AsY5gsOnoAQSL7i67xVmXgL1ySdDo39kzvWd5u', 'mt_kim');
INSERT INTO member (username, password, nickname) VALUES ('ks2', '{bcrypt}$2a$10$oVbP3wYPdpZ/eG1AsY5gsOnoAQSL7i67xVmXgL1ySdDo39kzvWd5u', 'mt_kim2');
INSERT INTO member (username, password, nickname) VALUES ('ks3', '{bcrypt}$2a$10$oVbP3wYPdpZ/eG1AsY5gsOnoAQSL7i67xVmXgL1ySdDo39kzvWd5u', 'mt_kim3');
INSERT INTO member (username, password, nickname) VALUES ('ks4', '{bcrypt}$2a$10$oVbP3wYPdpZ/eG1AsY5gsOnoAQSL7i67xVmXgL1ySdDo39kzvWd5u', 'mt_kim4');
INSERT INTO member (username, password, nickname) VALUES ('ks5', '{bcrypt}$2a$10$oVbP3wYPdpZ/eG1AsY5gsOnoAQSL7i67xVmXgL1ySdDo39kzvWd5u', 'mt_kim5');
INSERT INTO member (username, password, nickname) VALUES ('ks6', '{bcrypt}$2a$10$oVbP3wYPdpZ/eG1AsY5gsOnoAQSL7i67xVmXgL1ySdDo39kzvWd5u', 'mt_kim6');

INSERT INTO post (content, member_id, depth) VALUES ('글 입니다.', 1, 1);
INSERT INTO post_receiver (member_id, post_id) VALUES (6, 1);
