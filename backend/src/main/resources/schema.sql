-- Travel App Database Schema
-- 旅游分享平台数据库初始化脚本

CREATE DATABASE IF NOT EXISTS travel_app CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE travel_app;

-- 用户表
CREATE TABLE IF NOT EXISTS `users` (
  `id`          BIGINT       NOT NULL AUTO_INCREMENT     COMMENT '用户 ID，主键自增',
  `username`    VARCHAR(50)  NOT NULL UNIQUE             COMMENT '用户名，唯一',
  `password`    VARCHAR(255) NOT NULL                    COMMENT '登录密码，BCrypt 加密存储',
  `email`       VARCHAR(100) NOT NULL UNIQUE             COMMENT '邮箱地址，唯一',
  `avatar`      VARCHAR(500) DEFAULT NULL                COMMENT '头像图片 URL',
  `bio`         VARCHAR(500) DEFAULT NULL                COMMENT '个人简介',
  `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `deleted`     TINYINT(1)   NOT NULL DEFAULT 0          COMMENT '逻辑删除标志：0=正常，1=已删除',
  PRIMARY KEY (`id`),
  INDEX `idx_username` (`username`),
  INDEX `idx_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户基本信息表';

-- 角色表
CREATE TABLE IF NOT EXISTS `roles` (
  `id`          BIGINT      NOT NULL AUTO_INCREMENT      COMMENT '角色 ID，主键自增',
  `name`        VARCHAR(50) NOT NULL UNIQUE              COMMENT '角色名称，如 ROLE_USER / ROLE_MODERATOR / ROLE_ADMIN',
  `description` VARCHAR(200) DEFAULT NULL                COMMENT '角色描述',
  `create_time` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统角色表';

-- 用户角色关联表
CREATE TABLE IF NOT EXISTS `user_roles` (
  `id`          BIGINT   NOT NULL AUTO_INCREMENT         COMMENT '关联记录 ID，主键自增',
  `user_id`     BIGINT   NOT NULL                        COMMENT '用户 ID',
  `role_id`     BIGINT   NOT NULL                        COMMENT '角色 ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '关联创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_role` (`user_id`, `role_id`),
  INDEX `idx_user_id` (`user_id`),
  INDEX `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

-- 板块表（旅游目的地/话题板块）
CREATE TABLE IF NOT EXISTS `sections` (
  `id`          BIGINT       NOT NULL AUTO_INCREMENT     COMMENT '板块 ID，主键自增',
  `name`        VARCHAR(100) NOT NULL                    COMMENT '板块名称',
  `province`    VARCHAR(100) DEFAULT NULL                COMMENT '所属省份',
  `description` TEXT         DEFAULT NULL                COMMENT '板块简介',
  `cover_image` VARCHAR(500) DEFAULT NULL                COMMENT '板块封面图 URL',
  `post_count`  INT          NOT NULL DEFAULT 0          COMMENT '板块下帖子数量（冗余字段）',
  `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `deleted`     TINYINT(1)   NOT NULL DEFAULT 0          COMMENT '逻辑删除标志：0=正常，1=已删除',
  PRIMARY KEY (`id`),
  INDEX `idx_name` (`name`),
  INDEX `idx_province` (`province`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='旅游板块表';

-- 帖子表
CREATE TABLE IF NOT EXISTS `posts` (
  `id`             BIGINT       NOT NULL AUTO_INCREMENT   COMMENT '帖子 ID，主键自增',
  `user_id`        BIGINT       NOT NULL                  COMMENT '发布者用户 ID',
  `title`          VARCHAR(200) NOT NULL                  COMMENT '帖子标题',
  `content`        LONGTEXT     NOT NULL                  COMMENT '帖子正文内容',
  `images`         TEXT         DEFAULT NULL              COMMENT '图片 URL 列表，多个以英文逗号分隔',
  `section_id`     BIGINT       DEFAULT NULL              COMMENT '所属板块 ID',
  `latitude`       DOUBLE       DEFAULT NULL              COMMENT '发布地点纬度（高德坐标系）',
  `longitude`      DOUBLE       DEFAULT NULL              COMMENT '发布地点经度（高德坐标系）',
  `view_count`     INT          NOT NULL DEFAULT 0        COMMENT '浏览次数',
  `like_count`     INT          NOT NULL DEFAULT 0        COMMENT '点赞次数',
  `favorite_count` INT          NOT NULL DEFAULT 0        COMMENT '收藏次数',
  `comment_count`  INT          NOT NULL DEFAULT 0        COMMENT '评论次数',
  `status`         TINYINT(1)   NOT NULL DEFAULT 0        COMMENT '帖子状态：0=正常，1=被版主删除',
  `create_time`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  `update_time`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `deleted`        TINYINT(1)   NOT NULL DEFAULT 0        COMMENT '逻辑删除标志：0=正常，1=已删除',
  PRIMARY KEY (`id`),
  INDEX `idx_user_id` (`user_id`),
  INDEX `idx_section_id` (`section_id`),
  INDEX `idx_create_time` (`create_time`),
  INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='旅游帖子表';

-- 评论表
CREATE TABLE IF NOT EXISTS `comments` (
  `id`          BIGINT   NOT NULL AUTO_INCREMENT         COMMENT '评论 ID，主键自增',
  `post_id`     BIGINT   NOT NULL                        COMMENT '所属帖子 ID',
  `user_id`     BIGINT   NOT NULL                        COMMENT '评论用户 ID',
  `parent_id`   BIGINT   DEFAULT NULL                    COMMENT '父评论 ID，顶级评论为 null',
  `content`     TEXT     NOT NULL                        COMMENT '评论内容',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `deleted`     TINYINT(1) NOT NULL DEFAULT 0            COMMENT '逻辑删除标志：0=正常，1=已删除',
  PRIMARY KEY (`id`),
  INDEX `idx_post_id` (`post_id`),
  INDEX `idx_user_id` (`user_id`),
  INDEX `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='帖子评论表';

-- 点赞表
CREATE TABLE IF NOT EXISTS `likes` (
  `id`          BIGINT   NOT NULL AUTO_INCREMENT         COMMENT '点赞记录 ID，主键自增',
  `user_id`     BIGINT   NOT NULL                        COMMENT '点赞用户 ID',
  `post_id`     BIGINT   NOT NULL                        COMMENT '被点赞的帖子 ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '点赞时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_post` (`user_id`, `post_id`),
  INDEX `idx_post_id` (`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='帖子点赞记录表';

-- 收藏表
CREATE TABLE IF NOT EXISTS `favorites` (
  `id`          BIGINT   NOT NULL AUTO_INCREMENT         COMMENT '收藏记录 ID，主键自增',
  `user_id`     BIGINT   NOT NULL                        COMMENT '收藏用户 ID',
  `post_id`     BIGINT   NOT NULL                        COMMENT '被收藏的帖子 ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_post` (`user_id`, `post_id`),
  INDEX `idx_user_id` (`user_id`),
  INDEX `idx_post_id` (`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='帖子收藏记录表';

-- 关注表
CREATE TABLE IF NOT EXISTS `follows` (
  `id`          BIGINT   NOT NULL AUTO_INCREMENT         COMMENT '关注关系 ID，主键自增',
  `follower_id` BIGINT   NOT NULL                        COMMENT '关注者（粉丝）用户 ID',
  `followee_id` BIGINT   NOT NULL                        COMMENT '被关注者用户 ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '关注时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_follower_followee` (`follower_id`, `followee_id`),
  INDEX `idx_follower_id` (`follower_id`),
  INDEX `idx_followee_id` (`followee_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户关注关系表';

-- 初始化默认角色
INSERT IGNORE INTO `roles` (`name`, `description`) VALUES
  ('ROLE_USER',      '普通用户'),
  ('ROLE_MODERATOR', '内容版主'),
  ('ROLE_ADMIN',     '系统管理员');
