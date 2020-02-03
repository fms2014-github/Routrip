# Sub PJT 1 저장소 설명

## back-sk : 백엔드 서버

### 사용한 개발 도구

- Spring Boot
    - Spring Web
    - Spring Bood DevTools
- Maven
- Java

## front-sk : 프론트 서버

### 사용한 개발 도구

- Vue.js CLI 3
    - Vue-Router
    - esLint
    - email-validator
    - babel
    - password-validator
- CSS-Preprocessor(node-sass)
    - scss
- JavaScript

---

# 프론트/백엔드 프로젝트 사용방법(Based by Windows 10)

## 프론트 사용 방법

1. Node.js LTS버전 설치
2. 명령 프롬프트 창에서 npm install -g  @vue/cli를 입력해서 vue.js를 설치
3. cd 명령어를 사용해서 프로젝트 생성할 곳을 찾아간 후 vue create [프로젝트 이름] 입력 후 enter
4. vue add 명령어를 사용해서 vue-router, eslint, babel, email-validator, password-validator, sass-loader, sass 를 추가

## 백엔드 사용 방법

JAVA 8 설치 필수

### VSCode

1. VSCode 설치
2. 키보드에서 'Ctrl' + 'Shift' + 'X' 조합키를 누른 후 아래 목록을 전부 설치한다.
    - Spring Boot Extension Pack
    - Java Extension Pack
    - Maven for Java

### STS

Spring 홈페이지([https://spring.io/](https://spring.io/))에서 개발도구(STS)를 설치한다. [3.9.7 버전 혹은 이상의 안정된 버전 권장]



### DB ###

maria DB 사용

데이터베이스 이름은 routripdb, 포트번호는 3306

테이블 생성은 wireframe 폴더의 create table sql query문.txt 참고