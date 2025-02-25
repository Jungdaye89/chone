[프로젝트 소개](프로젝트-소개)     |     [팀원 소개](팀원-소개) | [프로젝트 기간](프로젝트-기간) | [프로젝트 목적 및 상세](프로젝트-목적-및-상세)
<br>
[배포 주소](배포-주소) | [프로젝트 구조](프로젝트-구조) | [서비스 구성 및 실행 방법](서비스-구성-및-실행-방법)
<br>
[Stacks](Stacks) | [기술 도입 이유](기술-도입-이유) | [ERD](ERD) | [서비스 아키텍처](서비스-아키텍처) | [Swagger](Swagger)
<br><br><br>

## 프로젝트 소개

이 프로젝트는 음식점들의 주문 관리를 효율적으로 지원하는 온라인 및 오프라인 통합 플랫폼입니다.
고객은 온라인에서 쉽게 음식을 주문하거나, 오프라인 매장에서 대면으로 주문할 수 있습니다.

가게 사장님은 온라인 주문과 대면 주문을 실시간으로 확인하고 처리할 수 있으며,
주문 상태 관리와 결제 확인까지 한 곳에서 통합적으로 관리할 수 있습니다.

또한, 관리자는 가게 및 메뉴 등록, 주문 현황 관리를 통해
플랫폼 전체를 통합적으로 운영할 수 있으며, 주문 데이터 분석을 통해 매출 관리가 가능합니다.

이 플랫폼은 주문부터 결제, 주문 내역 관리까지 모든 과정을 디지털화하여 온라인과 오프라인 주문을 모두 지원하며,
외식업계의 운영 효율성을 극대화하고 고객에게 편리한 주문 경험을 제공합니다.

<br><br>

## 팀원 소개

| 강성준 | 김수빈 | 서현재 | 정다예 |
| --- | --- | --- | --- |
| <a href="https://github.com/Goldbar97"><img height="100px" width="100px" src="https://avatars.githubusercontent.com/u/100333239?v=4"/></a> | <a href="https://github.com/Soobinnni"><img height="100px" width="100px" src="https://avatars.githubusercontent.com/u/111328823?v=4"/></a> | <a href="https://github.com/seonow"><img height="100px" width="100px" src="https://avatars.githubusercontent.com/u/113659139?v=4"/></a> | <a href="https://github.com/Jungdaye89"><img height="100px" width="100px" src="https://avatars.githubusercontent.com/u/155501200?v=4"/></a> |
| [GitHub](https://github.com/Goldbar97) | [GitHub](https://github.com/Soobinnni) | [GitHub](https://github.com/seonow) | [GitHub](https://github.com/Jungdaye89) |
| Product, Store 담당 | Order, Payment 담당 | User, Security 담당 | AI, Review 담당 |

<br>

## 프로젝트 기간

2025.02.12 ~ 2025.02.25

<br>

## 배포 주소

[http://43.202.62.253:8080](http://3.34.46.173:8080/)

<br><br>

## 프로젝트 구조

```jsx
src
├── main
│   ├── java
│   │   └── com.chone.server
│   │       ├── commons                 # 공통 모듈 및 유틸리티
│   │       │   ├── config               # 설정 파일
│   │       │   ├── exception            # 예외 처리
│   │       │   ├── facade               # 복합 비즈니스 로직
│   │       │   ├── handler              # 예외 핸들러
│   │       │   ├── jpa                  # JPA 공통 모듈
│   │       │   ├── jwt                  # JWT 인증/인가
│   │       │   ├── lock                 # 락 처리
│   │       │   └── util                 # 공통 유틸리티
│   │       │
│   │       └── domains                 # 도메인 모듈
│   │           ├── ai                   # AI 생성
│   │           ├── auth                 # 사용자 인증/인가
│   │           ├── delivery             # 배송 관리
│   │           ├── order                # 주문 관리
│   │           ├── payment              # 결제 관리
│   │           ├── product              # 상품 관리
│   │           ├── review               # 리뷰 및 평점 관리
│   │           ├── s3                   # AWS S3 연동
│   │           ├── store                # 가게 관리
│   │           └── user                 # 사용자 정보 관리
│   │
│   └── resources
│       ├── application.yml             # 환경 설정 파일
│       └── static                      # 정적 리소스 (이미지, CSS, JS)
│
└── test                               # 테스트 코드

```
<br><br>

## 서비스 구성 및 실행 방법

### 서비스 구성

이 프로젝트는 Spring Boot와 PostgreSQL을 기반으로 한 백엔드 애플리케이션으로, AWS EC2에 배포되며 S3를 통해 이미지 및 정적 자원을 관리합니다.
GitHub Actions를 활용한 CI/CD 파이프라인으로 자동화된 배포 및 테스트가 이루어집니다.

- Backend: Spring Boot, Spring Security, JPA, QueryDSL
- Database: PostgreSQL (AWS RDS 사용)
- Deploy: AWS EC2, S3, GitHub Actions
- Communication: Discord, Notion

<br>

### **실행 방법**

<aside>

**필수 요구 사항**

- Java 17
- Gradle
- PostgreSQL
</aside>

1. **Git Repository 클론**
    
    ```bash
    git clone https://github.com/Jungdaye89/chone
    cd chone
    ```
    
2. **환경 변수 설정 (.env 파일 생성)**
    
    ```
    # Database 설정
    SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/chone
    SPRING_DATASOURCE_USERNAME=db_username
    SPRING_DATASOURCE_PASSWORD=db_password
    
    # JWT Secret
    JWT_SECRET=jwt_secret
    
    # AWS 설정
    AWS_ACCESS_KEY=aws_access_key
    AWS_SECRET_KEY=aws_secret_key
    ```
    
3. **의존성 설치 및 빌드**
    
    ```bash
    ./gradlew clean build -x test
    
    ```
    
4. **EC2 접속 명령어**
    
    ```bash
    ssh -i "sparta_web.pem" ubuntu@ec2-43-202-62-253.ap-northeast-2.compute.amazonaws.com
    ```
    
<br><br>

## 프로젝트 목적 및 상세

이 프로젝트는 음식점의 주문 관리 및 운영 효율성을 높이기 위해 개발된 통합 주문 관리 플랫폼입니다.

- **온라인 및 오프라인 주문 통합 관리**
    - 고객은 온라인으로 주문하고, 사장님은 매장 및 온라인 주문을 한 곳에서 실시간으로 확인 및 관리
- **주문 상태 및 결제 확인**
    - 주문 접수, 준비 중, 완료 등 상태 관리
    - 결제 확인 및 주문 내역 관리
- **매장 및 메뉴 관리**
    - 사장님은 메뉴 등록 및 수정
    - 관리자는 플랫폼 전체의 매장 및 주문 현황 관리
- **리뷰 및 평점**
    - 고객은 주문 완료 후 리뷰와 평점을 남길 수 있음
- **AI API 연동**
    - 상품 설명 자동 생성 기능을 통해 가게 사장님이 쉽게 상품 설명 작성

<br><br>

## Stacks

### Environment
[![My Skills](https://skillicons.dev/icons?i=git,github)](https://skillicons.dev)

### Build Tool
[![My Skills](https://skillicons.dev/icons?i=gradle)](https://skillicons.dev)       

### Development
[![My Skills](https://skillicons.dev/icons?i=java,spring)](https://skillicons.dev)

### Database
[![My Skills](https://skillicons.dev/icons?i=postgres)](https://skillicons.dev)

### Testing & API Tool
[![My Skills](https://skillicons.dev/icons?i=postman)](https://skillicons.dev)

### Communication
[![My Skills](https://skillicons.dev/icons?i=discord,notion)](https://skillicons.dev)


### Deploy
[![My Skills](https://skillicons.dev/icons?i=githubactions,aws)](https://skillicons.dev)   

<br><br>

## 기술 도입 이유

- **Spring Boot**
    - 빠른 개발 속도와 생산성: 내장된 톰캣 서버와 기본 설정으로 빠르게 애플리케이션을 실행하고 배포할 수 있음
    - 확장성과 유지보수성: 모듈화된 아키텍처와 레이어드 아키텍처를 통한 유지보수 용이성 및 확장성 확보
- **PostgreSQL**
    - 복잡한 쿼리 처리 능력: 고급 SQL 기능과 JSON 데이터 타입을 지원하여 복잡한 검색 및 필터링 로직 구현에 유리
    - 안정성 및 ACID 보장: 트랜잭션의 안정성과 일관성을 유지하며, 데이터 무결성을 보장
- **QueryDSL**
    - **타입 안전한 동적 쿼리**: 빌더 패턴을 사용하여 컴파일 타임에 SQL 구문 오류를 방지하고 유지보수성을 높임
    - **복잡한 검색 조건 처리**: 다중 필터링 및 정렬 기능 구현 시 깔끔한 코드 작성 가능
- **AWS EC2 / RDS**
    - **확장성과 가용성**: 필요한 만큼의 컴퓨팅 리소스를 동적으로 할당하고 확장할 수 있음
    - **보안 및 안정성**: IAM(Role 기반 접근 제어)을 통해 보안 수준을 높이고 안정성 있는 서버 운영 가능


<br><br>
## ERD
![chone](https://github.com/user-attachments/assets/5eb4f918-d493-485e-bad4-37f39c0f2cb4)

<br><br>

## 서비스 아키텍처

![시스템 아키텍처](https://github.com/user-attachments/assets/14aefeef-5368-42b0-b370-d007d1765e5a)


<br><br>

## Swagger

http://43.202.62.253:8080/swagger-ui/index.html
