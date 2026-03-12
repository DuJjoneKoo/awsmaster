# ☁️ Spring Boot + AWS 클라우드 아키텍처 설계 & 배포

> Spring Boot 애플리케이션을 AWS 인프라 위에 직접 설계하고 배포한 프로젝트예요!
> VPC, EC2, RDS, S3, Parameter Store, IAM Role까지 직접 구성했어요 😊

---

## 🛠️ 기술 스택

### Backend
![Java](https://img.shields.io/badge/Java_17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot_3.5-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring_Data_JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white)

### AWS
![EC2](https://img.shields.io/badge/EC2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white)
![RDS](https://img.shields.io/badge/RDS_MySQL-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white)
![S3](https://img.shields.io/badge/S3-569A31?style=for-the-badge&logo=amazons3&logoColor=white)
![Parameter Store](https://img.shields.io/badge/Parameter_Store-FF9900?style=for-the-badge&logo=amazonssmparameter&logoColor=white)

### Database
![MySQL](https://img.shields.io/badge/MySQL_8.4-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![H2](https://img.shields.io/badge/H2_(local)-09476B?style=for-the-badge&logo=h2&logoColor=white)

---

## 🏗️ 인프라 아키텍처

```
                         ┌─────────────────────────────────────────┐
                         │              bogum-vpc                   │
                         │           (10.0.0.0/16)                  │
                         │                                          │
                         │  ┌──────────────┐  ┌──────────────────┐ │
  클라이언트  ──────────▶ │  │  public 서브넷 │  │  private 서브넷   │ │
  (Postman)              │  │              │  │                  │ │
                         │  │    EC2 🖥️    │──▶│    RDS 🗄️       │ │
                         │  │  (8080 포트)  │  │  (MySQL 8.4.7)  │ │
                         │  └──────────────┘  └──────────────────┘ │
                         └─────────────────────────────────────────┘
                                    │
                          ┌─────────┴──────────┐
                          │   AWS Parameter     │
                          │   Store 🔑          │
                          │  /bogum/DB_URL      │
                          │  /bogum/DB_USERNAME │
                          │  /bogum/DB_PASSWORD │
                          └────────────────────┘
                                    │
                          ┌─────────┴──────────┐
                          │      S3 🪣          │
                          │   bogum-s3         │
                          │  (프로필 이미지)     │
                          └────────────────────┘
```

---

## 🔐 보안 그룹 구성

| 보안 그룹 | 인바운드 규칙 | 설명 |
|-----------|--------------|------|
| `bogum-ec2-sg` | 8080 (전체), 22 (SSH) | EC2 웹 서버 |
| `bogum-rds-sg` | 3306 (EC2 보안 그룹만 허용) | RDS 보안 그룹 체이닝 |

> 💡 RDS는 EC2 보안 그룹 ID만 허용해서 외부에서 직접 접근이 불가능해요!

---

## 📁 프로젝트 구조

```
src/main/java/com/example/aws/
├── controller/
│   └── MemberController.java     # REST API 엔드포인트
├── service/
│   ├── MemberService.java        # 비즈니스 로직
│   └── S3Service.java            # S3 파일 업로드
├── entity/
│   └── Member.java               # JPA 엔티티
├── dto/
│   ├── MemberRequest.java        # 요청 DTO
│   └── MemberResponse.java       # 응답 DTO
├── repository/
│   └── MemberRepository.java     # JPA Repository
├── exception/
│   └── GlobalExceptionHandler.java
└── AwsApplication.java

src/main/resources/
├── application.yml               # 공통 설정 (local 프로필 활성화)
├── application-local.yml         # 로컬 환경 (H2 인메모리 DB)
└── application-prod.yml          # 운영 환경 (RDS + Parameter Store)
```
LEVEL 0
![예산결제](https://github.com/user-attachments/assets/02c57cae-1843-4ef6-9175-d1b77496abcb)
13.125.97.116 퍼블릭IP
---

## 🌐 API 명세

### Member API

| Method | URL | 설명 |
|--------|-----|------|
| `POST` | `/api/members` | 멤버 저장 |
| `GET` | `/api/members/{id}` | 멤버 단건 조회 |
| `PATCH` | `/api/members/{id}/profile-image` | 프로필 이미지 업로드 |
| `GET` | `/actuator/health` | 서버 상태 확인 |

### 요청 / 응답 예시

**POST** `/api/members`
```json
// Request Body
{
    "name": "정민교",
    "age": 25,
    "mbti": "ISTJ"
}

// Response 200 OK
{
    "id": 1,
    "name": "정민교",
    "age": 25,
    "mbti": "ISTJ"
}
```

**PATCH** `/api/members/{id}/profile-image`
```
Content-Type: multipart/form-data
Key: file
Value: (이미지 파일)
```

---

## 📸 API 테스트 결과

### ✅ POST /api/members - 멤버 저장 성공

![Image](https://github.com/user-attachments/assets/69a535f0-40b8-4522-9276-62caebe24eeb)

---

### ✅ GET /api/members/1 - 멤버 조회 성공

![Image](https://github.com/user-attachments/assets/6a7a1cf9-ee43-40c1-abc0-3f2d41ed0b27)

---

### ✅ S3 이미지 업로드 성공

![Image](https://github.com/user-attachments/assets/e6a5ff63-ed07-40c8-8801-568353c4952c)

---

## ⚙️ 환경 설정

### 프로필 구분

| 프로필 | DB | 사용 환경 |
|--------|----|----------|
| `local` | H2 인메모리 | 로컬 개발 |
| `prod` | RDS MySQL | EC2 운영 서버 |

### Parameter Store 구성

| 키 | 타입 |
|----|------|
| `/bogum/DB_URL` | String |
| `/bogum/DB_USERNAME` | String |
| `/bogum/DB_PASSWORD` | SecureString 🔒 |

---

## 🚀 배포 방법

```bash
# 1. 빌드
JAVA_HOME="C:/Program Files/Java/jdk-17" ./gradlew build

# 2. EC2로 전송
scp -i "bogum-key.pem" build/libs/aws-0.0.1-SNAPSHOT.jar ec2-user@{EC2_IP}:/home/ec2-user/

# 3. EC2 접속
ssh -i "bogum-key.pem" ec2-user@{EC2_IP}

# 4. 운영 프로필로 실행
java -jar aws-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

---

## 🔑 IAM Role 구성

EC2에 연결된 IAM Role: `bogum-ec2-role`

| 정책 | 용도 |
|------|------|
| `AmazonSSMReadOnlyAccess` | Parameter Store 읽기 |
| `AmazonS3FullAccess` | S3 업로드/조회 |

> 💡 액세스 키를 코드에 직접 넣지 않고 IAM Role로 권한을 부여했어요!

---

## 📝 트러블슈팅

| # | 문제 | 원인 | 해결 |
|---|------|------|------|
| 1 | `spring-cloud-aws-starter-ssm` 의존성 실패 | 3.x에서 아티팩트 이름 변경 | `spring-cloud-aws-starter-parameter-store` + BOM 사용 |
| 2 | `'url' must start with "jdbc"` | 2.x Parameter Store 설정 방식 사용 | `spring.config.import` 방식으로 변경 |
| 3 | 로컬 빌드 테스트 실패 (SdkClientException) | 로컬 AWS 자격증명 없음 | `@MockitoBean(types = S3Client.class)` 추가 |
| 4 | Presigned URL 1시간 만료 | IAM Role 임시 자격증명 최대 1시간 | 접근 성공 스크린샷으로 대체 |
| 5 | 파일 업로드 용량 초과 | Spring Boot 기본 1MB 제한 | `max-file-size: 80MB` 설정 추가 |
