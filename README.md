# MZting - LLM 기반 MBTI 가상 인물 소개팅 시뮬레이션

## 소개

**MZting**은 MBTI 성격 유형을 기반으로 한 가상 인물 소개팅 시뮬레이션 서비스입니다. 이 서비스는 사용자가 다양한 MBTI 유형의 가상 인물들과 대화를 나누며 소개팅을 체험할 수 있도록 설계되었습니다. 특히, 연애 경험이 적은 사용자들이 이성과의 대화를 자연스럽게 이끌어나가는 연습을 할 수 있도록 돕는 것을 목표로 합니다.

## 주요 기능

- **MBTI 기반 가상 캐릭터 생성**: 사용자의 MBTI를 기반으로 가상 캐릭터가 생성됩니다.
- **LLM 기반 대화**: 대형 언어 모델을 활용해 자연스러운 대화 경험을 제공합니다.
- **다양한 시나리오**: 여러 상황에서 가상 소개팅을 체험할 수 있습니다.
- **호감도 확인**: 가상 소개팅 결과에 따라 캐릭터의 호감도를 확인할 수 있습니다.

## 기술 스택

- **프론트엔드**: React
  - 사용자 인터페이스를 제공하며, Flask 및 Spring Boot 백엔드와 상호작용합니다.
  
- **백엔드**:
  - **Flask (Python)**: 특정 기능(예: LLM 기반 텍스트 생성) 담당
  - **Spring Boot (Java)**: 데이터베이스 연동 및 인증/인가 로직 담당
  
- **데이터베이스**: MySQL 또는 H2 (Spring Boot 설정에 따라 다름)