# SmallChat
스몰챗 프로젝트

---
### 목차
1. [Project Spec](#project-spec)
2. [환경설정파일 암/복호화 방법](#환경-설정파일-암호화-복호화-방법)
3. [공통 응답 및 에러 처리 Advice](#공통-응답-및-에러-처리-Advice)
<br><br><br>


---
## Project Spec
- Java : [Amazon Corretto 17 (LTS)](https://aws.amazon.com/ko/about-aws/whats-new/2021/09/amazon-corretto-17-now-available/)
- Spring Boot : [3.2.5](https://spring.io/projects/spring-boot#support)
    - Spring Framework: 6.0.10
    - Spring Security: 6.1 (어째서인지 Spring Security가 최근부터 버전 번호를 달리가고있음)
    - Tomcat 10.0 (Servlet 5.0)
- Gradle : 7.x (7.5 or later)



<br><br><br><br>
## 환경 설정파일 암호화, 복호화 방법

<br><br>
### 1. 암호화
로컬 환경에서 서버 실행 후 `[POST] /demos/encrypt` 경로로 하기 데이터 요청

```json
{
    "text": "암호화할 내용"
}
```
*로컬 전용 api*<br><br>

응답
```json
{
    "data": {
        "text": "GKYhnjKEMf87il4jlOHMsruEjX9g4x/1gOsaIZENcswZYt6WjLvZxA=="
    },
    "meta": {
        "code": 0,
        "message": "",
        "result": "success"
    }
}
```
<br><br>

### 2. 복호화
잘 암호화 됬는지 확인은 `[POST] /demos/decript`로 하기 암호화된 문자열 데이터 요청
```json
{
    "text": "GKYhnjKEMf87il4jlOHMsruEjX9g4x/1gOsaIZENcswZYt6WjLvZxA=="
}
```
*로컬 전용 api*<br><br>

응답
```json
{
    "data": {
        "text": "암호화할 내용"
    },
    "meta": {
        "code": 0,
        "message": "",
        "result": "success"
    }
}
```

<br><br><br>



---
## 공통 응답 및 에러 처리 Advice
`SmallChatResponseAdvice`: `ResponseBodyAdvice` 상속 구현체
`BasicResponseDTO` 를 이용한 공통응답을 처리합니다.

<br>

#### 작동 시점
HttpMessageConverter 선택된 후 ~ write method 호출 직전

<br><br>
#### BasicResponseDTO
- companion object로 성공응답 제작함수와, 실패 응답 제작함수를 제공합니다.
- 각 함수는 편의성을 위해(parameter의 생략 등) 필요한 형태를 오버로딩하여 사용합니다.

응답을 아래와같은 일관된 형식으로 제작해줍니다.
```json
{
  "meta": {
    "code": 0,
    "result": "success",
    "message": "OK",
    "httpStatus": 200
  },
  "data": {
    "id": 1,
    "name": "테스트"
  }
}
```

<br><br>
#### 공통 응답 처리
응답 body 객체를 일정한 형식으로 내려주도록 조정
- 기본 응답 객체인 BasicResponseDTO가 전달된 경우 그대로 리턴
- Page 상속 객체인 경우 PaginationDTO와 함께 success 응답 리턴
- List 형 데이터인 경우 BasicResponseDTO로 wrapping하여 success 응답 리턴


<br><br>
#### 공통 에러 처리
- 자체 제작 커스텀 Exception(SmallChatHttpException)을 BasicResponseDTO error 응답 리턴
- @valid 에러(MethodArgumentNotValidException)를 BasicResponseDTO error 응답 리턴
- 다양한 종류의 400 에러, 404에러, 500에러를 BasicResponseDTO error 응답 리턴


<br><br>
#### [관련파일]
- `SmallChatResponseAdvice.kt`
- `BasicResponseDTO가.kt`
- `SmallChatHttpException.kt`


