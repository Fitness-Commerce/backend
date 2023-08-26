# Fitness-Commerce

## Web 기능정의서

## API Spec

|Function|Method|End Point|Page|
|---|---|---|---|
|회원가입|POST|/api/members/signup||
|로그인|POST|/api/members/login||
|로그아웃|POST|/api/members/logout||
|회원정보 수정|PUT|/api/members/{member_id}||
|회원 비밀번호 수정|PUT|/api/members/{member_id}/password||
|회원 단건 조회(상대방)|GET|/api/members/{member_id}||
|회원 단건 조회(본인)|GET|/api/members/mypage||
|회원 전체 조회|GET|/api/members||
|회원탈퇴|DELETE|/api/members/{member_id}||
|상품 등록|POST|/api/items||
|상품 수정|PUT|/api/items/{itemId}||
|상품 단건 조회|GET|/api/items/{itemId}||
|상품 전체 조회|GET|/api/items||
|상품 삭제|DELETE|/api/items/{itemId}||
|상품 검색|GET|/api/items/search||
|상품 댓글 작성|POST|/api/items/{itemId}/comments||
|상품 댓글 조회|GET|/api/items/{itemId}/comments||
|상품 댓글 수정|PUT|/api/items/{itemId}/comments/{commentId)||
|상품 댓글 삭제|DELETE|/api/items/{itemId}/comments/{commentId)||
|상품 카테고리 생성|POST|/api/categories||
|카테고리별 상품 조회|GET|/api/categories/{category_id}/items||
|카테고리 목록 조회|GET|/api/categories||
|카테고리 수정|PUT|/api/categories/{category_id}||
|카테고리 삭제|DELETE|/api/categories/{category_id}||
|게시글 작성|POST|/api/posts||
|게시글 단건 조회|GET|/api/posts/{postId}||
|게시글 전체 조회|GET|/api/posts||
|게시글 수정|PUT|/api/posts/{postId}||
|게시글 삭제|DELETE|/api/posts/{postId}||
|게시글 검색|GET|/api/posts/search||
|게시판 생성|POST|/api/boards||
|게시판별 게시글 조회|GET|/api/boards/{boardId}/posts||
|게시판 목록 조회|GET|/api/boards||
|게시판 수정|GET|/api/boards/{boardId}||
|게시판 삭제|DELETE|/api/boards/{boardId}||
|채팅방 생성|POST|/api/chatrooms||
|채팅방 상세 조회|GET|/api/chatrooms/{chatroomId}||
|채팅방 삭제|DELETE|/api/chatrooms/{chatroomId}||
|채팅방 메시지 전송|POST|/api/chatrooms/{chatroomId}/messages||

## ERD

<img src="https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fcp4Hm0%2Fbtsrf44uvKe%2Fkhx1Q7YEYfN8gUGfNI5o21%2Fimg.png" width="500" height="500"/>

## Commit Convention

> feat: 사용자 등록 기능 추가
>
> 사용자 등록 기능을 추가하였습니다. 사용자의 정보를 입력받아 데이터베이스에 저장합니다.
>
> 변경 내용:
> 
> - 사용자 정보 입력 폼 디자인
> - 데이터베이스 연동 및 저장 로직 구현


<br>
위의 예시처럼 <유형>에는 다양한 타입을 사용할 수 있습니다. 주로 사용되는 커밋 유형은 아래와 같습니다:

- feat: 새로운 기능 추가
- design: css 및 사용자 UI 디자인 변경
- fix: 버그 수정
- chore: 빌드 및 기타 자잘한 작업
- docs: 문서 수정
- style: 코드 스타일 변경 (공백, 세미콜론 등)
- refactor: 코드 리팩토링
- test: 테스트 코드 추가 또는 수정
- perf: 성능 개선 관련 작업
- revert: 이전 커밋으로 되돌리는 작업
