# DevNote Board API

Spring Boot 3, JPA, PostgreSQL, Spring Security JWT로 만든 게시판 API입니다.

## 실행

프로젝트 루트에 `.env` 파일을 만들고 Base64URL 형식의 32바이트 이상 키를 설정합니다.

```env
JWT_SECRET=openssl_rand_base64_32로_생성한_값
```

다음 한 명령으로 API와 PostgreSQL을 실행합니다.

```bash
docker compose up --build
```

API는 `http://localhost:8080`에서 실행됩니다. 개발 DB와 컨테이너를 모두 초기화하려면 `docker compose down -v`를 사용합니다.

## 인증 방식

세션 대신 JWT Access Token을 사용합니다. REST API 서버가 세션 상태를 보관하지 않아도 되고, 클라이언트는 로그인 후 받은 토큰을 요청 헤더에 담으면 됩니다.

```http
Authorization: Bearer {accessToken}
```

토큰은 1시간 뒤 만료됩니다. 비밀번호와 BCrypt 해시는 응답이나 JWT claim에 포함하지 않습니다.

## API

| 기능 | 메서드 | URL | 인증 |
| --- | --- | --- | --- |
| 회원가입 | POST | `/api/v1/members/signup` | 불필요 |
| 로그인 | POST | `/api/v1/members/login` | 불필요 |
| 게시글 작성 | POST | `/api/v1/posts` | 필요 |
| 게시글 목록 | GET | `/api/v1/posts?page=0&size=20` | 불필요 |
| 게시글 상세 | GET | `/api/v1/posts/{postId}` | 불필요 |
| 게시글 수정 | PUT | `/api/v1/posts/{postId}` | 작성자 |
| 게시글 삭제 | DELETE | `/api/v1/posts/{postId}` | 작성자 |
| 댓글 작성 | POST | `/api/v1/posts/{postId}/comments` | 필요 |
| 댓글 목록 | GET | `/api/v1/posts/{postId}/comments` | 불필요 |
| 댓글 수정 | PUT | `/api/v1/comments/{commentId}` | 작성자 |
| 댓글 삭제 | DELETE | `/api/v1/comments/{commentId}` | 작성자 |

회원가입 요청 예시입니다.

```json
{"email":"user@example.com","password":"password12","nickname":"user"}
```

로그인 성공 응답은 Access Token만 반환합니다.

```json
{"accessToken":"...","tokenType":"Bearer"}
```

## 설계 결정

- 회원 이메일에는 DB unique 제약과 서비스 중복 검사를 모두 적용했습니다. 비밀번호는 BCrypt 해시만 저장합니다.
- 게시글 목록은 `Post`·작성자·댓글을 조인하고 `count(comment)`로 집계하는 페이지 쿼리를 사용합니다. 글마다 작성자와 댓글 수를 다시 조회하지 않으므로 N+1이 발생하지 않습니다.
- 게시글 삭제 시 `Post.comments`의 `CascadeType.REMOVE`와 `orphanRemoval`로 연결 댓글도 함께 물리 삭제합니다.
- 요청과 응답에는 DTO만 사용합니다. 엔티티와 비밀번호는 API에 노출하지 않습니다.
- 인증되지 않은 쓰기 요청은 401, 타인의 글·댓글 변경은 403, 입력 오류는 400, 없는 글·댓글은 404로 동일한 오류 응답 구조를 사용합니다.

오류 응답 예시입니다.

```json
{"status":404,"code":"RESOURCE_NOT_FOUND","message":"게시글을(를) 찾을 수 없습니다.","path":"/api/v1/posts/999"}
```
