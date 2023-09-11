<div align="center">



![MAIN로고](https://github.com/foxgeun/recipe/assets/121280503/d1c203f1-7002-420a-95aa-81a5971403b8)






  <h2>:fire:Intro</h2>

  <p>이젠 맛있게는 맛있는 음식을 만들고 공유하며 함께 먹는 푸드 커뮤니티 플랫폼입니다.<br> 각자 자신만의 특별한 레시피를 공유하고, 다른 요리사들의 레시피도 감상하며 직접 따라해볼 수 있는 공간을 제공합니다.

### 주요 기능


🍳 레시피 공유 및 검색: 자신만의 레시피를 공유하고, 다양한 카테고리에서 다른 사용자들의 레시피를 찾아볼 수 있습니다. <br>레시피 검색 필터를 활용하여 원하는 요리를 쉽게 찾을 수 있습니다.

👩‍🍳 커뮤니티 활동: 다른 사용자들과 커뮤니티 활동을 통해 음식과 요리에 대한 이야기를 나눌 수 있습니다.<br> 댓글, 좋아요, 공유 기능을 활용하여 다양한 사용자와 소통할 수 있습니다.

📸 사진 및 비디오 공유: 자신이 만든 요리를 사진과 비디오로 공유하여 더 생생한 경험을 전달할 수 있습니다.<br> 진행 과정과 팁을 함께 공유하여 다른 사용자들의 요리를 도와줄 수 있습니다.</p>

  
  <h2>:v:skills</h2>

  
![Html](https://img.shields.io/badge/HTML-239120?style=for-the-badge&logo=html5&logoColor=white)
![Css](https://img.shields.io/badge/CSS-239120?&style=for-the-badge&logo=css3&logoColor=white)
![Js](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=JavaScript&logoColor=white)
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)


![Bootstrap](https://img.shields.io/badge/Bootstrap-563D7C?style=for-the-badge&logo=bootstrap&logoColor=white)
![Spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![MySql](https://img.shields.io/badge/MySQL-00000F?style=for-the-badge&logo=mysql&logoColor=white)



  <h2>:raising_hand:Member</h2>

### 팀장: 박수근
![Anurag's GitHub stats](https://github-readme-stats.vercel.app/api?username=foxgeun&show_icons=true&theme=radical)
### 팀원: 천용규
![Anurag's GitHub stats](https://github-readme-stats.vercel.app/api?username=alive1020&show_icons=true&theme=radical)
### 팀원: 김민규
![Anurag's GitHub stats](https://github-readme-stats.vercel.app/api?username=kmingyuyu&show_icons=true&theme=radical)
### 팀원: 이현서
![Anurag's GitHub stats](https://github-readme-stats.vercel.app/api?username=eenql0529&show_icons=true&theme=radical)
### 팀원: 전민기
![Anurag's GitHub stats](https://github-readme-stats.vercel.app/api?username=mingi96&show_icons=true&theme=radical)



  <h2>:speech_balloon:Link</h2>

[**erdcloud**](https://www.erdcloud.com/d/QrJuwy2sJ9iWFPZNp)


[**갠트차트**](https://docs.google.com/spreadsheets/d/1rIzyAJdTPRRoDCJ99mbGD8wRouTvkVtLu_TsCAotIAM/edit#gid=1115838130)



[**요구사항정의서**](https://docs.google.com/spreadsheets/d/1yVZskRGVN5y52etl3DYE6SMND_y7NWA3oJBQd_XhPIM/edit#gid=0
)


[**Html템플릿**](https://themewagon.com/themes/free-bootstrap-4-html5-food-blog-website-template-yummy/
)


  <h2>:two_women_holding_hands:git</h2>

### 자신의 브랜치에만 push 합시다 (main에 하면 50만원)
</div>

##### 로컬 저장소(내 컴퓨터) 에서 브랜치 생성하는 법
```git
프로젝트 루트 폴더에서
git branch {브랜치명} // 브랜치 생성
git checkout {브랜치명} // 브랜치 이동
```
##### 개발한 브랜치를 push 하고 싶어요
```git
git branch // 현재 작업중인 브랜치 확인

  develope
  main
* feature/#1-member-login   // *이 붙어있는 곳이 현재 작업중인 브랜치


git add .                                 // 트래킹 중이지 않은 파일 추가
git commit -m ":cyclone:ing : 로그인 기능 완료 / redirect url 미완료"       // 커밋 메시지 작성
git push origin feature/#1-member-login   // 작업이 끝나지 않았다면
git push origin feature/develop           // 작업이 끝났다면
```
##### push 한 후 쓰지 않는 로컬 브랜치를 지우고 싶어요
```git
git branch -d {브랜치명}
```
##### 깃허브에 있는 브랜치를 내려받고 싶어요
```git
git branch // 현재 작업중인 브랜치 확인

  develope
  main
* feature/#1-member-login   // *이 붙어있는 곳이 현재 작업중인 브랜치

git pull origin {내려받을 브랜치명}
```
<hr>



### 코드 네이밍 룰

#### 모든 자바 메소드명, 변수명은 카멜 케이스를 따릅니다. 

#### 또한 누구나 알기 쉬운 단어를 사용합니다.

메소드명은 동사로 네이밍합니다.

:+1:
```java
private String personName; 

public void getUserId() {

}
```

:-1:
```java
private String PersonName;
private String personname; 

public void userid() {

}
```

#### 클래스 명은 파스칼 케이스를 따릅니다.

:+1:
```text
SampleCode.java
SampleCodeDto.java
```

:-1:
```text
samplecode.java
sampleCodeDto.java
```

#### HTML 파일 명, 패키지명 은 모두 소문자를 사용합니다.

:+1:
```text
samplecode.html
```

:-1:
```text
sample_code.html
sampleCode.html
```
#### 패키지명은 모두 소문자를 사용합니다.
#### ENUM이나 상수는 대문자로 네이밍합니다.








