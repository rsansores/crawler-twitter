# Resources schema
 
# --- !Ups
 
CREATE TABLE tweets (
    id bigint(20) NOT NULL,
    text CHAR(200) CHARACTER SET utf8,
    retweet_count int(5) NOT NULL,
    retweeted bool NOT NULL,
    created_at timestamp NOT NULL,
    PRIMARY KEY (id)
)  CHARACTER SET utf8;
 
CREATE TABLE words (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    word CHAR(200) CHARACTER SET utf8,
    word_count int(5) NOT NULL,
    in_day DATE NOT NULL,
    PRIMARY KEY (id)
)  CHARACTER SET utf8; 
 
# --- !Downs
 
DROP TABLE tweets;
DROP TABLE words;