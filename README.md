 블로그 플랫폼
설명: 사용자가 글을 작성, 수정, 삭제하고, 다른 사용자의 글을 읽고 댓글을 달 수 있는 블로그 플랫폼.
기능: 사용자 프로필, 게시물 작성 및 관리, 댓글 시스템, 태그 및 카테고리 관리, 게시물 검색.
기술 스택: Spring Boot, JPA, React/Vue.js, Elasticsearch (검색 기능), MySQL,Redis

1. Users 테이블
사용자 정보를 저장하는 테이블입니다.
```sql
CREATE TABLE Users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(255) NOT NULL UNIQUE,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  profile_picture_url VARCHAR(255),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  role varchar(255) DEFAULT NULL
);
```
3. Posts 테이블
게시물 정보를 저장하는 테이블입니다.
```sql
CREATE TABLE Posts (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  content TEXT NOT NULL,
  user_id BIGINT NOT NULL,
  category_id BIGINT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE,
  FOREIGN KEY (category_id) REFERENCES Categories(id) ON DELETE SET NULL
);
```
5. Comments 테이블
댓글 정보를 저장하는 테이블입니다.
```sql
CREATE TABLE Comments (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  content TEXT NOT NULL,
  user_id BIGINT NOT NULL,
  post_id BIGINT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE,
  FOREIGN KEY (post_id) REFERENCES Posts(id) ON DELETE CASCADE
);
```
7. Categories 테이블
카테고리 정보를 저장하는 테이블입니다.
```sql
CREATE TABLE Categories (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL UNIQUE,
  description TEXT
);
```
9. Tags 테이블
태그 정보를 저장하는 테이블입니다.
```sql
CREATE TABLE Tags (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL UNIQUE
);
```
11. Post_Tags 테이블
게시물과 태그 간의 다대다 관계를 관리하는 조인 테이블입니다.
```sql
CREATE TABLE Post_Tags (
  post_id BIGINT NOT NULL,
  tag_id BIGINT NOT NULL,
  PRIMARY KEY (post_id, tag_id),
  FOREIGN KEY (post_id) REFERENCES Posts(id) ON DELETE CASCADE,
  FOREIGN KEY (tag_id) REFERENCES Tags(id) ON DELETE CASCADE
);
```
13. Likes 테이블
사용자가 게시물을 좋아요한 정보를 저장하는 테이블입니다.
```sql
CREATE TABLE Likes (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  post_id BIGINT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE,
  FOREIGN KEY (post_id) REFERENCES Posts(id) ON DELETE CASCADE
);
```
Users 테이블: 사용자의 기본 정보(이름, 이메일, 비밀번호, 프로필 사진 URL 등)를 저장합니다. 이 테이블은 MySQL에서 사용자 계정을 관리하는 기본적인 테이블로 활용됩니다.

Posts 테이블: 사용자들이 작성한 게시물의 제목과 내용을 저장합니다. 게시물은 작성자(user_id)와 카테고리(category_id)를 참조합니다.

Comments 테이블: 게시물에 달린 댓글을 저장합니다. 댓글은 작성자(user_id)와 게시물(post_id)을 참조합니다.

Categories 테이블: 게시물에 대한 카테고리를 관리합니다. 카테고리는 각 게시물에 할당될 수 있으며, 카테고리별로 게시물을 분류하는 데 사용됩니다.

Tags 테이블: 게시물에 태그를 달기 위한 태그 정보를 저장합니다. 각 태그는 고유해야 하며, 여러 게시물에 동일한 태그를 할당할 수 있습니다.

Post_Tags 테이블: 게시물과 태그 간의 다대다 관계를 관리합니다. 이 테이블은 각각의 게시물과 태그 간의 관계를 매핑합니다.

Likes 테이블: 사용자들이 좋아요한 게시물 정보를 저장합니다. 특정 사용자가 어떤 게시물을 좋아요했는지를 관리합니다.
