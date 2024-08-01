-- `blog-db`.article definition

CREATE TABLE `article` (
                           `id` varchar(36) NOT NULL,
                           `create_at` datetime(6) DEFAULT NULL,
                           `content` varchar(5000) NOT NULL,
                           `title` varchar(100) NOT NULL,
                           PRIMARY KEY (`id`),
                           UNIQUE KEY `UK571gx7oqo5xpmgocegaidlcu9` (`title`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- `blog-db`.comment definition

CREATE TABLE `comment` (
                           `id` varchar(36) NOT NULL,
                           `create_at` datetime(6) DEFAULT NULL,
                           `author` varchar(100) NOT NULL,
                           `content` varchar(5000) NOT NULL,
                           `parent_id` varchar(36) DEFAULT NULL,
                           `parent_type` enum('ARTICLE') DEFAULT NULL,
                           PRIMARY KEY (`id`)
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