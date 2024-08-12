# 1세대 포켓몬 도감

Android 관련 기술을 적용해보고 공부하기 위한 프로젝트입니다.  
Compose Migratrion 진행중입니다.(~ing)

# 아키텍처

안드로이드 권장 아키텍처 기반으로 작성하였습니다.
Layer에 맞게 모듈을 분리하고 MVVM Pattern을 적용하였습니다.

![권장 아키텍처](https://github.com/user-attachments/assets/22737d5b-5d3c-4c97-afab-c630904b884f)


![MVVM](https://github.com/user-attachments/assets/7395af0b-90c8-44a9-8100-1832d3cbdcd8)


### 기능 소개

* 1세대 포켓몬의 간단한 정보를 리스트로 보여줍니다.
* 상세 화면에서 각 포켓몬의 능력치를 확인할 수 있습니다.
* 각 포켓몬에 좋아요 클릭이 가능하며 해당 사항을 로컬 데이터베이스에 저장합니다.
* 좋아요를 누른 포켓몬만 모아보기가 가능합니다.

### 사용 기술

* Kotlin
* Coroutine + Flow
* Room
* Retrofit2
* Gson
* AAC ViewModel
* Hilt
* Glide
