# Spring-Toy-Project

Java + Spring Boot + AWS S3를 사용하여 다양한 기능을 구현한 토이 프로젝트

Spring Security + JWT Boilerplate를 활용
Spring Rest Docs를 활용한 테스트 코드 개발(AsciiDoctor)


# 📚 STACKS

### Tech Stack

+ Java
+ Spring Boot 3.1.8
+ Gradle 8.5
+ MySQL
+ Spring Data JPA
+ Spring Security
+ JWT
+ AWS S3
+ Redis
+ Docker + Github actions CI/CD
+ Spring Rest Docs

# ERD
<https://dbdiagram.io/d/ToyProject-65bc90f1ac844320ae493fa3>

![ToyProject](https://github.com/HanYoonSoo/Spring-Toy-Project/assets/114587653/9f3fe04d-25f1-4a00-b055-6391c83fe779)

# Postman
<https://documenter.getpostman.com/view/24415208/2sA2rFRKMi#ee46b019-b7e1-4344-bbed-1bff32f630b7>

![Postman](https://github.com/HanYoonSoo/Spring-Toy-Project/assets/114587653/438361b0-0529-4f1e-af86-7c1cc9f7ab83)

# AsciiDoctor

테스트 성공 후 빌드 및 실행시 자동으로 작성된 API 명세
![스크린샷 2024-03-01 오후 12 59 07](https://github.com/HanYoonSoo/Spring-Toy-Project/assets/114587653/f00536bc-cfee-4ecc-8d79-467dd0b3d5a0)
![스크린샷 2024-03-01 오후 12 58 29](https://github.com/HanYoonSoo/Spring-Toy-Project/assets/114587653/9927d098-e9d0-465e-9f19-56648a852a16)

# Dockerhub
<https://hub.docker.com/repositories/hanyoonsoo>

![image](https://github.com/HanYoonSoo/Spring-Toy-Project/assets/114587653/bd6f789c-96b2-4d77-92a9-8cb622961077)

# 구현내용

+ Spring Security + JWT를 활용한 stateless 방식 사용, Redis를 통한 RefreshToken 구현
+ 이메일 인증을 통한 사용자 인증 기능
+ 인증된 사용자에 따른 주문 권한 확인
+ Spring Rest Docs를 활용한 테스트 코드 개발
+ 테스트 성공시 관련 API 문서가 자동으로 작성될 폼 구현
+ AWS S3 활용 프로필 이미지 CRUD
+ VirtualBox 활용 Docker + Github actions CI/CD
+ 아이템 등록 및 아이템 주문 기능

# 트러블슈팅

<details>
  <summary>오류 발견시 원인 찾기 어려움</summary>
  Spring Security를 비롯한 다양한 오류들에서 발생하는 문제의 원일을 찾는 것이 어려움. 따라서, Spring Rest Docs를 활용한 테스트 코드 작성 및 오류 발생시 적절한 예외처리를 하는 예외처리 핸들러 사용.
</details>

<details>
  <summary>Spring Security 오류시 아무런 값도 반환하지 않음</summary>
  Spring Security Filter는 일반 Filter로 DispatchServlet전에 동작하여 Controller까지 전달되지 않음. 따라서, Spring Security 필터 및 검증 단계에서 에러 발생시 response에 관련 정보를 담음으로 해결.
</details>

<details>
  <summary>객체지향과 성능 문제에 관한 트레이드 오프</summary>
  성능을 고려하지 않고 객체지향적으로만 설계할 경우 삭제 및 업데이트 시 리스트의 반복문으로 시간 낭비가 발생가능. 반면에 성능에 이점을 두어 설계를 진행할 경우 한번의 쿼리만 나가지만 객체지향적 설계가 아님. 프로젝트 특성상 리스트에 큰 값이 없기 때문에 CRD의 경우는 객체지향적 설계를 하였고 Update의 경우에만 쿼리가 한번만 나가도록 설계.
</details>

<details>
  <summary>가상머신을 외부에서 접속하는 것이 어려움. CI/CD 불가.</summary>
  공유기 환경 내부에 있는 컴퓨터 환경 내부에 있기 때문에 접근하는 것이 어려움. 따라서, 가상머신의 네트워크 환경을 어댑터에 브릿지로 설정하여 일반 데스크탑처럼 호스트로 인식. 그 후, 공유기에 VPN 환경 구축 후 ssh로 접속 가능. 또한, 가상머신 내부의 포트에 접속하려면 가상머신에 접속할 수 있는 외부 IP가 필요함. Ngrok 구성을 통하여 문제 해결.
</details>

<details>
  <summary>EC2가 아닌 VirtualBox 사용으로 인한 네트워크 오류</summary>
  EC2와는 달리 VirtualBox는 직접 네트워크 구성을 해줘야 함. 방화벽을 작동시키지 않으면 openssh-server를 적용한 22 포트 이외에는 전부 막혀있음. 따라서, ufw를 활성화하고 적절한 포트를 열어줌으로 문제 해결.
</details>









