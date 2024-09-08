# 1세대 포켓몬 도감

Android 관련 기술을 적용해보고 공부하기 위한 토이 프로젝트입니다.

# 아키텍처

안드로이드 권장 아키텍처 기반으로 작성하였습니다.  
Layer에 맞게 모듈을 분리하고 MVVM Pattern을 적용하였습니다.

<p align="center"> <img src="https://github.com/user-attachments/assets/22737d5b-5d3c-4c97-afab-c630904b884f" align="center" width="45%">  
  <img src="https://github.com/user-attachments/assets/7395af0b-90c8-44a9-8100-1832d3cbdcd8" align="center" width="45%">  
</p>

# 스크린샷

<p align="center">  <img src="https://github.com/user-attachments/assets/98266cc2-1d67-4b88-8a79-0e85e81b40a0" align="center" width="32%">  
  <img src="https://github.com/user-attachments/assets/8f5d3552-f9e0-4da9-bcd0-958e2fb6e450" align="center" width="32%">  
  <img src="https://github.com/user-attachments/assets/89886465-bf33-4123-a1c9-df4607c655d8" align="center" width="32%">  
</p>


# 기능 소개

* 1세대 포켓몬의 간단한 정보를 리스트로 보여줍니다.
* 상세 화면에서 각 포켓몬의 능력치를 확인할 수 있습니다.
* 각 포켓몬에 좋아요 클릭이 가능하며 해당 사항을 로컬 데이터베이스에 저장합니다.
* 좋아요를 누른 포켓몬만 모아보기가 가능합니다.

# 사용 기술

* Kotlin
* Coroutine + Flow
* Room
* Retrofit2
* Gson
* Compose
* AAC ViewModel
* Hilt
* Glide
