# hhplus01_tdd

## [항해 플러스 백엔드 1주차 ]
## TDD 시작하기

### 작업 내용
- 3월 19일 화요일
    - TDD 기본 지식 학습
    - PointService TDD 기반으로 작성
        - {포인트 조회}, {포인트 충전}, {포인트 사용}, {이용 내역} 단위 테스트 및 개발
    - PointController API 작성
    - {포인트 충전}, {포인트 사용} 동시성 제어 코드 추가
    - *~Controller 통합 테스트 작성 중*


### 생각의 흐름

#### TDD 방식으로 개발하며
개발 흐름 : ((단위 테스트 → 로직 개발) *반복) 서비스 로직 개발 완료 → 통합 테스트

#### 동시성 제어 방식 고찰
- 순차적으로 제어
- 대규모 : 분산 락, 트랜잭션
- 소규모 : ReentrantLock, Syncronized

#### 예외처리
- custom exception의 효용성

#### 단위테스트 모의 객체 Mock
- 모의 객체를 이용한 테스트의 실효성
- 런던파 vs 고전파

#### 테스트 단위
- controller 통합테스트
- service 단위테스트
- 이외에 테스트를 작성하면 좋은 단위가 무엇인가
