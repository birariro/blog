-- `blog-db`.article definition

CREATE TABLE `article` (
                           `id` varchar(36) NOT NULL,
                           `create_at` datetime(6) DEFAULT NULL,
                           `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                           `title` varchar(100) NOT NULL,
                           PRIMARY KEY (`id`),
                           UNIQUE KEY `UK571gx7oqo5xpmgocegaidlcu9` (`title`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- `blog-db`.`member` definition

CREATE TABLE `member` (
                          `id` varchar(36) NOT NULL,
                          `create_at` datetime(6) DEFAULT NULL,
                          `ip` varchar(255) DEFAULT NULL,
                          `login_id` varchar(255) DEFAULT NULL,
                          `login_pw` varchar(255) DEFAULT NULL,
                          `nickname` varchar(100) NOT NULL,
                          `type` enum('GUEST','MEMBER') DEFAULT NULL,
                          PRIMARY KEY (`id`),
                          UNIQUE KEY `UKhh9kg6jti4n1eoiertn2k6qsc` (`nickname`),
                          UNIQUE KEY `UKnlyn40hq7vv0mejoshpeb1a6e` (`ip`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- `blog-db`.comment definition

CREATE TABLE `comment` (
                           `id` varchar(36) NOT NULL,
                           `create_at` datetime(6) DEFAULT NULL,
                           `content` varchar(5000) NOT NULL,
                           `parent_id` varchar(36) DEFAULT NULL,
                           `parent_type` enum('ARTICLE','COMMENT') DEFAULT NULL,
                           `author_id` varchar(36) DEFAULT NULL,
                           PRIMARY KEY (`id`),
                           KEY `FK2bam3knj13ijq6eiskx55xtqh` (`author_id`),
                           CONSTRAINT `FK2bam3knj13ijq6eiskx55xtqh` FOREIGN KEY (`author_id`) REFERENCES `member` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- `blog-db`.tag definition

CREATE TABLE `tag` (
                       `id` varchar(36) NOT NULL,
                       `create_at` datetime(6) DEFAULT NULL,
                       `name` varchar(255) NOT NULL,
                       `article_id` varchar(36) DEFAULT NULL,
                       PRIMARY KEY (`id`),
                       KEY `FKhutavxkklg0b3kj7rmcnc9hvf` (`article_id`),
                       CONSTRAINT `FKhutavxkklg0b3kj7rmcnc9hvf` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- `blog-db`.aggregate_count definition

CREATE TABLE `aggregate_count` (
                                   `id` varchar(36) NOT NULL,
                                   `count` bigint DEFAULT NULL,
                                   `parent_id` varchar(36) DEFAULT NULL,
                                   `type` enum('READ','VISIT') DEFAULT NULL,
                                   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;