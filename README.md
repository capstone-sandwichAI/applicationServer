# "sandwich AI" Project [23.09 ~ 23.12]

차량 외관 불량 검수 플랫폼
<br>

## summary
- AI 모델을 활용해 차량의 외관 불량 정보와 날짜별 검수 요약 데이터를 제공
  - parts: 도어, 루프사이드, 휀더, 범퍼, 라디에이터 그릴, 테일 램프, 헤드 램프
  - defect types: 스크래치, 외관 손상, 단차, 장착 불량
    
- 세종대학교 2023-2학기 capstone 프로젝트 참여
  - 세종대학교 공학인증 창의설계 경진대회 대상 수상
<br>

## 기술 스택
<img width="811" alt="스크린샷 2023-11-14 오후 11 25 41" src="https://github.com/capstone-sandwichAI/applicationServer/assets/96655921/00baa1a8-e293-47de-a1ce-c7996680dda3">

<img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=Spring&logoColor=white">  <img src="https://img.shields.io/badge/Springboot-6DB33F?style=for-the-badge&logo=Springboot&logoColor=white">  <img src="https://img.shields.io/badge/mariaDB-003545?style=for-the-badge&logo=mariaDB&logoColor=white"> <img src="https://img.shields.io/badge/amazonaws-232F3E?style=for-the-badge&logo=amazonaws&logoColor=yellow"> <img src="https://img.shields.io/badge/ec2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white"> <img src="https://img.shields.io/badge/s3-569A31?style=for-the-badge&logo=amazons3&logoColor=white"> <img src="https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white">

<br>

## 이렇게 프로젝트를 진행했어요
개발과 동시에 아이템 제안과 보고서 등 문서 작업 병행
- 아이템 제안
  - [제안서](https://github.com/capstone-sandwichAI/applicationServer/blob/main/src/main/resources/4%EC%A3%BC%EC%B0%A8_%EC%A0%9C%EC%95%88%EC%84%9C.pdf)

- 차량 불량 검수 use case 설명
  - [use case 명세서](https://github.com/capstone-sandwichAI/applicationServer/blob/main/src/main/resources/6%EC%A3%BC%EC%B0%A8_%EC%9C%A0%EC%8A%A4%EC%BC%80%EC%9D%B4%EC%8A%A4_%EB%8B%A4%EC%9D%B4%EC%96%B4%EA%B7%B8%EB%9E%A8.pdf)
  - [use case 발표 자료](https://github.com/capstone-sandwichAI/applicationServer/blob/main/src/main/resources/6%EC%A3%BC%EC%B0%A8_%EC%9C%A0%EC%8A%A4%EC%BC%80%EC%9D%B4%EC%8A%A4_%EB%B0%9C%ED%91%9C%EC%9E%90%EB%A3%8C.pdf)

- 중간 보고
  - [중간 보고서](https://github.com/capstone-sandwichAI/applicationServer/blob/main/src/main/resources/9%EC%A3%BC%EC%B0%A8_%EC%A4%91%EA%B0%84_%EB%B3%B4%EA%B3%A0%EC%84%9C.pdf)

- 최종 보고
  - [최종 보고서](https://github.com/capstone-sandwichAI/applicationServer/blob/main/src/main/resources/14%EC%A3%BC%EC%B0%A8_%EC%B5%9C%EC%A2%85_%EB%B3%B4%EA%B3%A0%EC%84%9C.pdf)

- 창의설계 경진대회
  - [대회 발표 자료](https://github.com/capstone-sandwichAI/applicationServer/blob/main/src/main/resources/13%EC%A3%BC%EC%B0%A8_%EC%B0%BD%EC%9D%98%EC%84%A4%EA%B3%84_%EA%B2%BD%EC%A7%84%EB%8C%80%ED%9A%8C.pdf)
<br>

## 이렇게 개발했어요
- backend server, AI server 각각 배포
  - 각 ec2 인스턴스에 서버를 올려 docker를 활용해 백엔드는 spring과 mariadb, AI는 flask를 실행시켰다.
  - 클라이언트의 요청에 대해 백엔드는 AI 서버에 요청을 한다. 이 때 사용되는 2번의 request dto를 같게 하여 코드 재사용을 고려했다.
 
    <br>
- 검수 시간 소요 개선 고민
  - 첫째, 영상을 input으로 한 검수 플랫폼을 계획했으나, 전송 테스트 과정에서 시간이 많이 걸리는 문제를 발견했다.
  - 둘째, AI에서 영상을 프레임 단위로 쪼개고 각 프레임별 output을 이어 붙여 벡엔드에 전송해야 하므로 작업의 간결함이 필요했다.
  - 셋째, 영상보다 이미지가 고화질이다.
  - 위와 같은 이유로 영상을 이미지로 대체하였고 플랫폼이 실제 사용될 제조 공장의 환경에도 이미지로 하지 않을 이유가 없었다.
  - db 적재 시점을 검수 이후로 하여 처음 들어온 요청 이미지들에 대해서는 db와 묶이지 않고 AI 서버로 검수 요청만 하도록 했다.
  - ![image](https://github.com/capstone-sandwichAI/applicationServer/assets/96655921/6159b371-da00-479d-b70d-b8782680b246)
  - 15MB 정도의 자동차 외관 영상(좌)과 테스트 이미지 8장(우)을 통신하였을 때,영상은 205ms, 사진은 29ms가 나왔다.
 
    <br>
- MVC 패턴 개발
  - controller, service, repository 계층을 분리해 각 계층이 단일한 책임을 갖도록 구조화했다.
  - entity와 dto 계층을 나눠 데이터 무결성을 강화했고 클라이언트와의 통신에서 효율적인 데이터 전송을 생각했다.


## Screen shots
|메인 화면|
|---|
|<img src = "https://github.com/capstone-sandwichAI/applicationServer/blob/main/src/main/resources/KakaoTalk_Photo_2023-12-16-23-30-28.png" width=800>|

|차량 검수 결과 상세 조회|
|---|
|<img src = "https://github.com/capstone-sandwichAI/applicationServer/blob/main/src/main/resources/KakaoTalk_Photo_2023-12-16-23-30-34.png" width=800>|

|날짜별 차량 검수 결과 조회|
|---|
|<img src = "https://github.com/capstone-sandwichAI/applicationServer/blob/main/src/main/resources/KakaoTalk_Photo_2023-12-16-23-30-40.png" width=800>|


<br>

## 팀원
<table>
  <tbody>
    <tr>
      <td align="center"><a href="https://github.com/justinkim12"><img src=""width="200px;" alt=""/><br /><sub><b>BE : 김정민</b></sub></a><br /></td>
      <td align="center"><a href="https://github.com/ABCganada"><img src="" width="200px;" alt=""/><br /><sub><b>BE : 신민기</b></sub></a><br /></td>
     <tr/>
  </tbody>
</table>
