### 프로젝트 소개

이 프로젝트는 음식점들의 주문 관리를 효율적으로 지원하는 온라인 및 오프라인 통합 플랫폼입니다.
고객은 온라인에서 쉽게 음식을 주문하거나, 오프라인 매장에서 대면으로 주문할 수 있습니다.

가게 사장님은 온라인 주문과 대면 주문을 실시간으로 확인하고 처리할 수 있으며,
주문 상태 관리와 결제 확인까지 한 곳에서 통합적으로 관리할 수 있습니다.

또한, 관리자는 가게 및 메뉴 등록, 주문 현황 관리를 통해
플랫폼 전체를 통합적으로 운영할 수 있으며, 주문 데이터 분석을 통해 매출 관리가 가능합니다.

이 플랫폼은 주문부터 결제, 주문 내역 관리까지 모든 과정을 디지털화하여 온라인과 오프라인 주문을 모두 지원하며, 
외식업계의 운영 효율성을 극대화하고 고객에게 편리한 주문 경험을 제공합니다.

<br><br>

### 팀원 소개

| 강성준 | 김수빈 | 서현재 | 정다예 |
|:----:|:----:|:----: | :----: |
| <a href="https://github.com/Goldbar97"><img height="100px" width="100px" src="https://avatars.githubusercontent.com/u/100333239?v=4"/></a> | <a href="https://github.com/Soobinnni"><img height="100px" width="100px" src="https://avatars.githubusercontent.com/u/111328823?v=4"/></a> | <a href="https://github.com/seonow"><img height="100px" width="100px" src="https://avatars.githubusercontent.com/u/113659139?v=4"/></a> | <a href="https://github.com/Jungdaye89"><img height="100px" width="100px" src="https://avatars.githubusercontent.com/u/155501200?v=4"/></a> |
| [GitHub](https://github.com/Goldbar97) | [GitHub](https://github.com/Soobinnni) | [GitHub](https://github.com/seonow) | [GitHub](https://github.com/Jungdaye89) |
| Product, Store 담당 | Order, Payment 담당 | User, Security 담당 | AI, Review 담당 |

<br>

### 프로젝트 기간
2025.02.12 ~ 2025.02.25

<br>

### 배포 주소

<br>

## Stacks 🐈

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

<br>

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
1. **Git Repository 클론**  
    ```bash
    git clone [REPO_URL]
    cd [PROJECT_DIRECTORY]
    ```

2. **환경 변수 설정 (.env 파일 생성)**  
    ```env
    SPRING_DATASOURCE_URL=jdbc:postgresql://[RDS_ENDPOINT]/[DATABASE_NAME]
    SPRING_DATASOURCE_USERNAME=[DB_USERNAME]
    SPRING_DATASOURCE_PASSWORD=[DB_PASSWORD]
    AWS_ACCESS_KEY=[YOUR_AWS_ACCESS_KEY]
    AWS_SECRET_KEY=[YOUR_AWS_SECRET_KEY]
    JWT_SECRET=[YOUR_JWT_SECRET]
    ```

3. **의존성 설치 및 빌드**  
    ```bash
    ./gradlew clean build
    ```

4. **Docker 빌드 및 실행**  
    ```bash
    docker-compose up --build
    ```

5. **애플리케이션 접속**  
    - URL: `http://[EC2_IP_ADDRESS]:8080`  

<br><br>


## 서비스 아키텍처
![image](https://github.com/user-attachments/assets/44d1e0c9-11c5-4dd5-9506-1270decd62f2)

<br><br>

## ERD
![image](https://github.com/user-attachments/assets/6164a333-4ea8-4dd8-afe6-f13cc163e6d3)
