### ⭐folder 구조
```
─ 기능 상위 도메인
 ├─ entity
 ├─ dto 
 ├─ repository(interface)
 ├─ api
 ├─ controller
 ├─ service
 └─ util (필요시)
```

### 🔖Commit message
|헤더|내용|
|----|------|
|feat|새로운 기능 추가|
|fix|버그 수정|
|docs|문서 관련|
|style|스타일 변경|
|refact|코드 리팩토링|
|build|빌드 관련 파일 수정|
|chore|그 외 자잘한 수정|

형식: `헤더: 내용`

<br/>

### 🔖Branch
| feat | refact | hotfix |
|------|---------|--------|


형식: `헤더/#이슈번호-내용`

> * feat, refact branch -> `dev`
> * hotfix -> `main, dev`
> * `dev` -> `main`
