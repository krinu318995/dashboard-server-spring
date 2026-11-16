# Grid Dashboard (`dashboard-server-spring`)

위젯 기반 개인 일정(Task)·메모(Memo) 대시보드 웹 애플리케이션입니다.  
React 기반 프론트엔드와 Spring Boot REST API 백엔드로 구성되어 있으며, 직관적인 그리드 대시보드 UI와 도메인 중심 백엔드 아키텍처를 구현했습니다.

---

## 🛠 Tech Stack

### Backend
* **Language & Framework**: Java 21 (OpenJDK 21 LTS), Spring Boot 4.1.2 (Spring Web MVC, Spring Data JPA, Validation)
* **Database & Security**: MySQL, Hibernate, JPQL, Jasypt (설정 정보 암호화)
* **Build & Library**: Gradle, Lombok, Jackson

### Frontend
* **Language & Framework**: React, TypeScript, Vite
* **HTTP Client**: Axios

---

## 📂 Project Structure

```text
dashboard-server-spring
 ├── frontend/                                 # React + TypeScript 대시보드 UI
 └── backend/                                  # Spring Boot REST API 서버
      └── src/main/java/com/dashboard/backend
           ├── task/                           # 일정 및 일자별 완료 기록 도메인
           │    ├── Task.java
           │    ├── TaskCompletion.java
           │    ├── TaskRepository.java
           │    ├── TaskCompletionRepository.java
           │    ├── TaskDto.java
           │    ├── TaskService.java
           │    └── TaskController.java
           ├── widget/                         # 대시보드 위젯 배치 및 관리 도메인
           │    ├── Widget.java
           │    ├── WidgetRepository.java
           │    ├── WidgetDto.java
           │    ├── WidgetService.java
           │    └── WidgetController.java
           └── memo/                           # 메모 CRUD 도메인
                ├── Memo.java
                ├── MemoRepository.java
                ├── MemoDto.java
                ├── MemoService.java
                └── MemoController.java
