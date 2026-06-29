# BeautyScope (Spring 버전) — 설명용 학습 문서

이 문서는 "내가 뭘 만들었고, 왜 이렇게 만들었는지"를 직접 설명할 수 있도록
정리한 것이다. 발표/질의응답 전에 한 번 읽어보면 된다.

## 1. 한 줄 요약

Python(pandas)으로 이미 분석을 끝낸 화장품 리뷰 데이터(쿠팡+무신사+
올리브영, 538,774건)를 **Oracle DB에 옮겨 담고**, 그 위에 **Spring MVC +
MyBatis + JSP**로 "제품 리뷰 분석 사이트"를 만들었다. 분석(ML/통계)은
이미 Python에서 끝낸 결과를 가져다 쓰는 것이고, 이번에 한 작업은 **그
결과를 실제 웹 서비스로 보여주는 부분**이다.

## 2. 왜 이렇게 설계했는가

### 2-1. 왜 Java로 ML을 다시 만들지 않았나

`D:\crolling` 프로젝트에서 이미 Python으로 감성분석 모델을 학습하고,
속성기반 분석(ABSA)까지 끝내서 CSV로 결과를 뽑아놓은 상태였다. Spring
미션의 목적은 "Spring/MyBatis/Oracle을 배우는 것"이지 "ML을 Java로
재구현하는 것"이 아니라서, **분석은 Python에서 끝낸 걸 그대로 가져오고,
이번엔 그 데이터를 DB에 넣고 웹으로 보여주는 부분만 새로 만들었다.**

### 2-2. 왜 기존 `board`/`reply` 패턴을 그대로 따라갔나

이미 배운 구조(Controller → Service(+Impl) → DAO(+Impl) → VO, MyBatis
namespace 호출 방식)를 그대로 재사용했다. 새로운 패턴을 배우는 게
아니라 **같은 패턴으로 다른 도메인(제품/리뷰)을 만들 수 있다는 걸
보여주는 것**이 이번 과제의 핵심이라고 판단했다.

```
kr.ac.kopo.product.controller.ProductController
kr.ac.kopo.product.service.ProductService / ProductServiceImpl
kr.ac.kopo.product.dao.ProductDAO / ProductDAOImpl
kr.ac.kopo.product.vo.ProductVO / ProductCriteria / RatingDistVO / BrandStatVO

kr.ac.kopo.review.controller.ReviewController  (JSON, fetch() 대상)
kr.ac.kopo.review.service.ReviewService / ReviewServiceImpl
kr.ac.kopo.review.dao.ReviewDAO / ReviewDAOImpl
kr.ac.kopo.review.vo.ReviewVO / ReviewCriteria / ReviewPageResult
```

`board`는 글 하나 = VO 하나로 충분했지만, 제품은 "검색 조건 + 페이징"이
같이 필요해서 `ProductCriteria`라는 별도 VO를 만들어 그 안에 카테고리,
키워드, 정렬, 페이지 번호를 다 담고 그대로 MyBatis에 넘겼다.

## 3. DB 스키마

```sql
T_PRODUCT (platform, product_id, product_name, brand_name, category,
           review_count, avg_rating, n_negative, n_neutral, n_positive)
  PK (platform, product_id)   -- 같은 product_id가 플랫폼마다 따로 존재해서 복합키 사용

T_REVIEW (review_no, platform, product_id, rating, sentiment,
          review_content(CLOB), review_date, nickname)
  PK review_no (시퀀스)
  FK (platform, product_id) -> T_PRODUCT
  INDEX (platform, product_id)  -- 상세페이지에서 "이 제품의 리뷰"를 계속 조회하므로
```

**왜 review_content를 CLOB으로 했나**: 리뷰 본문 중 가장 긴 게 4,311자였다.
한글은 UTF-8에서 글자당 최대 3바이트라 VARCHAR2(4000 byte 제한)로는
넘칠 수 있어서 CLOB을 썼다.

**왜 review_id를 PK로 안 쓰고 새 시퀀스(review_no)를 만들었나**: 원본
CSV의 review_id가 무신사 데이터는 전부 비어있어서(NULL) PK로 쓸 수 없었다.
그래서 Oracle 시퀀스로 새 PK를 만들었다.

## 4. 데이터 적재 — `DataLoader.java`

`src/test/java/kr/ac/kopo/loader/DataLoader.java`를 1회 실행해서
`merged_reviews_all.csv`(538,774행)를 두 번 읽는다.

1. **1차 패스**: `platform+product_id` 기준으로 리뷰를 집계해서
   (리뷰 수, 평균 평점, 감성별 건수) → `T_PRODUCT`에 적재 (2,405개 상품)
2. **2차 패스**: 같은 CSV를 다시 읽으면서 리뷰 원본을 그대로
   `T_REVIEW`에 배치(1,000건 단위) insert (538,774건)

MyBatis를 안 쓰고 **순수 JDBC**로 적재한 이유: 이건 한 번만 실행하는
배치 작업이라, 매퍼 XML을 만드는 것보다 `PreparedStatement.addBatch()`로
직접 넣는 게 더 빠르고 간단하다. (반면 웹 화면에서 쓰는 조회/검색은
전부 MyBatis로 만들었다 — 반복적으로 쓰는 쿼리라서 매퍼로 관리하는 게
맞다.)

**겪은 문제**:
- CSV가 `utf-8-sig`(BOM 포함)로 저장돼 있어서, 헤더를 자동 인식하면
  첫 번째 컬럼명 앞에 보이지 않는 BOM 문자가 붙어 매칭이 깨졌다. →
  헤더를 코드에 직접 명시(`setHeader(...)`)하고 첫 줄은 무조건 건너뛰게
  (`setSkipHeaderRecord(true)`) 해서 우회했다.
- 일부 쿠팡 리뷰는 `product_name`이 비어있어서(2,895건) `NOT NULL` 제약에
  걸려 insert가 실패했다. → `null`이면 "(상품명 미확인)"으로 대체.

## 5. MyBatis 쿼리에서 신경 쓴 부분

### 5-1. 페이징 — `OFFSET ... FETCH NEXT`

```sql
SELECT ...
FROM t_product
ORDER BY avg_rating DESC
OFFSET #{offset} ROWS FETCH NEXT #{pageSize} ROWS ONLY
```

`board` 미션에는 페이징이 없어서 직접 찾아본 부분이다. Oracle 12c부터
지원하는 최신 문법이고, 옛날 방식(`ROWNUM` + 서브쿼리 중첩)보다 훨씬
간단하다.

### 5-2. 동적 SQL — `<where>`, `<if>`, `<choose>`

카테고리/키워드 필터가 있을 때만 조건을 붙이고, 정렬 기준도 파라미터에
따라 바뀌어야 해서 MyBatis의 동적 SQL 태그를 썼다.

```xml
<where>
    <if test="category != null and category != ''">AND category = #{category}</if>
    <if test="keyword != null and keyword != ''">AND product_name LIKE '%' || #{keyword} || '%'</if>
</where>
ORDER BY
<choose>
    <when test="sort == 'rating_asc'">avg_rating ASC</when>
    <when test="sort == 'latest'">latest_review_date DESC</when>
    <otherwise>avg_rating DESC</otherwise>
</choose>
```

### 5-3. 컬럼명 매핑은 `AS`로 직접 alias

`board` 매퍼가 `view_cnt AS viewCnt`처럼 직접 alias를 붙이는 방식을 쓰길래
그 컨벤션을 그대로 따라갔다 (MyBatis의 `mapUnderscoreToCamelCase` 전역
설정을 새로 켜는 대신).

## 6. 비동기 리뷰 로딩 (V1-303) — 왜 fetch()로 따로 뺐나

한 제품에 리뷰가 수만 건까지 있을 수 있어서, 상세 페이지를 열 때마다
전부 한 번에 내려주면 느리다. 그래서:

1. `/product/{platform}/{productId}` 페이지는 **제품 정보 + 별점분포
   차트만** 서버에서 바로 렌더링
2. 리뷰 목록은 `<script>`에서 `fetch('/api/reviews?...')`로 따로
   요청해서 비동기로 채운다 (10건씩 페이징)
3. `ReviewController`는 `@RestController`라서 JSON으로 응답하고,
   `ReviewPageResult`라는 작은 응답용 클래스(reviews/totalCount/
   totalPages/page)로 묶어서 내려준다

리뷰 내용 검색(V1-402)도 같은 API에 `keyword` 파라미터를 추가해서
처리한다 — 검색창에 입력하고 검색 버튼을 누르면 같은 fetch 함수를
1페이지로 다시 호출한다.

**XSS 주의**: 리뷰 본문은 사용자가 쓴 텍스트라서, `innerHTML`에 그대로
넣으면 `<script>` 같은 태그가 실행될 위험이 있다. 그래서 JS에 작은
`escapeHtml()` 함수를 만들어 리뷰 내용/닉네임/감성 텍스트를 항상
이스케이프해서 넣는다.

## 7. 실제로 겪은 버그 — 한글 GET 파라미터가 안 먹힌 문제

`/product?category=토너`로 카테고리 필터를 보냈는데 결과가 0건이
나오는 버그가 있었다. 원인을 추적해보니:

- DB에는 데이터가 정확히 UTF-8로 잘 들어가 있었다 (`LENGTHB`로 직접
  확인: "클렌징폼" 4글자가 12바이트 = 글자당 3바이트, 정상적인 UTF-8)
- `CharacterEncodingFilter`(web.xml에 이미 있던 것)는 **POST 바디**
  인코딩만 처리하고, **GET 쿼리스트링은 컨테이너(Tomcat)가 자체적으로
  먼저 디코딩**하기 때문에 필터가 관여할 수 없었다
- Tomcat의 `Connector`에 `URIEncoding="UTF-8"`이 명시돼 있지 않으면
  쿼리스트링을 다른 인코딩으로 해석할 수 있다 → `server.xml`의
  `<Connector port="8080" ...>`에 `URIEncoding="UTF-8"`을 추가해서 해결

**배포할 Tomcat(Eclipse의 Tomcat v10.1 서버 등)에도 이 설정을 꼭
추가해야 한다** — 이건 코드가 아니라 서버 설정이라 WAR 안에 들어가지
않는다.

## 8. 기능 ID ↔ 구현 매핑 (기능명세서 기준)

| ID | 기능 | 구현 |
|---|---|---|
| V1-101 | TOP5 랭킹 | `MainController` → `index.jsp` (`평점 ≥ 20건` 상품 중 평점순) |
| V1-102 | 카테고리 바로가기 | `index.jsp` |
| V1-201~204 | 목록(필터/정렬/페이징) | `GET /product` |
| V1-301~302 | 상세 + 별점분포 차트 | `GET /product/{platform}/{productId}` |
| V1-303 | 리뷰 비동기 로딩 | `GET /api/reviews` (fetch) |
| V1-401 | 제품명 검색 | `/product?keyword=` |
| V1-402 | 리뷰내용 검색 | `/api/reviews?keyword=` |
| V1-501 | 카테고리 랭킹 | `GET /ranking` |
| V1-502 | 브랜드 비교 차트 | `GET /ranking` (Chart.js) |

## 9. 예상 질문 Q&A

**Q. MyBatis 대신 JPA를 쓰지 않은 이유는?**
이 미션 프로젝트 자체가 MyBatis 기반으로 시작됐고(board/reply가 이미
MyBatis), 같은 컨벤션을 유지하는 게 일관성 있다고 판단했다. SQL을
직접 다루다 보니 페이징/동적 정렬처럼 복잡한 쿼리를 세밀하게 제어하기도
편했다.

**Q. 제품 목록을 평점 TOP5로 보여줄 때, 리뷰 1건짜리 5점 제품이 끼면
어떻게 하나?**
`review_count >= 20`인 제품만 랭킹/TOP5 대상으로 삼았다 — Python
분석 쪽에서도 같은 기준(리뷰 20건 미만은 신뢢도가 낮다고 판단)을 썼다.

**Q. 538,774건을 다 넣었는데 조회 성능은 어떤가?**
`(platform, product_id)` 복합 인덱스를 리뷰 테이블에 걸어놔서, 상세
페이지처럼 "특정 제품의 리뷰만" 조회하는 쿼리는 빠르다. 다만 카테고리
전체를 다 훑는 식의 쿼리는 아직 인덱스를 추가로 고민해볼 부분이다.

**Q. 브랜드 비교 차트는 왜 쿠팡 제품이 안 나오나?**
쿠팡 원본 데이터엔 브랜드 정보 자체가 없다(크롤링 시점에 수집 안 됨).
그래서 `brand_name IS NOT NULL`로 걸러지는 건 의도된 동작이고, 이건
원천 데이터의 한계다.

**Q. 이 다음엔 뭘 할 건가?**
Python 쪽에서 이미 만들어둔 속성기반 분석(ABSA), 리뷰 신뢰도(모델
기반), 대안상품 추천, 피부타입 세그먼트를 2차로 추가할 계획이다.
이미 만든 CSV를 같은 방식(테이블 추가 → DAO/Service/Controller →
JSP)으로 가져오면 된다.

## 10. 한계 / 1차 범위에서 일부러 안 한 것

- 회원/로그인 기능 없음 (게시판 미션의 회원 기능과 분리해서, 이번엔
  "리뷰 데이터를 보여주는" 부분에만 집중)
- 리뷰 작성/수정/삭제(CUD) 없음 — 크롤링한 데이터를 "보여주기"만 하는
  서비스라 board 미션과 달리 쓰기 기능이 필요 없다고 판단
- ABSA/리뷰신뢰도/대안추천/피부세그먼트는 2차 범위 (Python 결과는
  이미 있고, DB 적재 + 화면 추가만 남음)
