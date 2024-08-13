INSERT INTO `blog-db`.`member` (id, create_at, ip, login_id, login_pw, nickname, `type`)
VALUES ('1ef10cc2-fafd-6350-9ae7-efbd7470c2fe', NULL, NULL, 'admin', '$2a$10$Dgy.Uq8n3rBTLwwA2W2fqeYkSSQXuunLD3C0MrmWr2vXHcTtF7yZW', 'author',
        'MEMBER');


INSERT INTO `blog-db`.article (id, create_at, content, title)
VALUES ('4a280fa4-bdae-437d-9939-4d44c728fdf1', '2024-08-13 10:44:58.892864', '
## Application.class 에 불필요한 어노테이션 추가 금지

```java
@ComponentScan({"com.sample.product"})
@EnableJpaAuditing
@ConfigurationPropertiesScan
@SpringBootApplication
public class SampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
    }
}

```

메인 애플리케이션 클래스에 `@ComponentScan`과 같은 어노테이션을 추가하면, 애플리케이션이 로드될 때 불필요한 스캔이 이루어질 수 있습니다. 이는 특히 테스트 환경에서 문제가 될 수 있습니다.

예를 들어, `@WebMvcTest`를 사용하여 컨트롤러 단위 테스트를 수행할 때, 이 어노테이션은 기본적으로 컨트롤러와 관련된 빈만 스캔합니다. 이때 `@ComponentScan`이 애플리케이션 클래스에 있으면, 애플리케이션 로드시 불필요한 빈들이 스캔되면서 테스트에 불필요한 영향을 줄 수 있습니다.

마찬가지로, `@EnableJpaAuditing`도 문제를 일으킬 수 있습니다. 이 어노테이션은 JPA 관련 빈들을 스캔하게 만드는데, 테스트 환경에서 이러한 빈들이 존재하지 않으면 예외가 발생할 수 있습니다.

이러한 문제를 방지하기 위해, 애플리케이션 클래스와는 별도로 설정 클래스를 분리하는 것이 좋습니다:

```java
@Configuration
@ComponentScan({"com.sample.product"})
public class PackageComponentScan {}

@Configuration
@EnableJpaAuditing
public class JpaAuditingConfiguration {}

@ConfigurationPropertiesScan
@SpringBootApplication
public class SampleApplication {}

```

이렇게 분리함으로써, 테스트에서 필요하지 않은 빈들이 스캔되지 않도록 설정할 수 있습니다.

## `static` 메서드를 사용한 유틸리티 코드 사용 자제

```java
public class UuidGenerator {
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }
}

String id = UuidGenerator.generateUUID();

```

애플리케이션 코드에서는 `Utils`라는 이름으로 많은 `static` 메서드들이 사용되곤 합니다. 그러나 이러한 코드는 가짜 구현체로 대체하거나 모킹하기가 어려워, 테스트가 힘들어집니다.

`Mockito`에서는 `mockStatic`을 제공하여 `static` 메서드를 모킹할 수 있지만, 근본적인 해결책은 아닙니다. 대신, `LocalDateTime.now()`와 같은 메서드에서는 파라미터로 `Clock`을 받아 테스트 가능하도록 메서드를 오버로드하는 방식을 추천합니다.

예를 들어:

```java
@Configuration
public class ClockConfig {
    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}

@Service
@RequiredArgsConstructor
class TimeService {

    private final Clock clock;

    public void test() {
        LocalDateTime now = LocalDateTime.now(clock);
    }
}
```

이와 같이 의존성을 주입받아 사용하면, 테스트 환경에서 대체할 수 있는 구현체를 제공할 수 있어 테스트가 용이해집니다.

토비의 스프링에서는 의존하고 있는 오프젝트를 DI를 사용하여 바꿔치기하는 것을 서비스 추상화라고 부르며,

서비스 추상화는 다양한 장점이 있지만 원활한 테스트 만을 위해서도 충분히 가치가 있다고 합니다.

서비스 추상화는 테스트의 용이성뿐만 아니라 다양한 장점을 제공합니다. `static` 메서드를 사용하지 않는 것이 가장 좋지만, 불가피한 경우에는 해당 `static` 메서드를 호출하는 빈 객체를 만들어 사용하는 것을 권장합니다.

```java
public class UuidGenerator {
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }
}

@Component
public class IdGenerator {
    public String uuid(){
        return UuidGenerator.generateUUID();
    }
}
```

## 테스트하기 어려운 코드는 레이어의 가장 바깥에 위치시킬 것

테스트하기 쉬운 코드는 항상 같은 결과를 반환하며 외부 상태를 변경하지 않는 코드를 의미합니다. 반대로, 외부 상태를 변경하거나 외부에 의존하는 코드는 테스트하기 어렵습니다. (예: 데이터베이스 CRUD, 콘솔 출력 등)

테스트하기 어려운 코드가 레이어의 가장 안쪽에 위치하면, 그 레이어의 모든 코드가 테스트하기 어려워집니다. 예를 들어, 회원가입 로직을 작성할 때 이메일 검사나 비밀번호 검사와 같은 테스트하기 쉬운 코드와, 데이터베이스에 저장하는 테스트하기 어려운 코드를 분리해야 합니다.

테스트하기 쉬운 코드와 어려운 코드는 반드시 바운더리 레이어에서 만나야 합니다. 예를 들어, 레이어드 아키텍처에서는 이러한 코드들이 `facade` 레이어에서 만나야 하고, 헥사고날 아키텍처에서는 `service` 레이어에서 만날 수 있습니다. 이러한 레이어에서 통합 테스트를 수행하는 것이 바람직합니다.

## 서비스 간 합성은 인터페이스를 통해 구현할 것

A 서비스에서 B 서비스를 사용하는 경우, A 서비스가 B 서비스의 구현체를 직접 참조하면, B 서비스를 생성하는 것도 복잡해집니다. 이 경우, B를 인터페이스로 참조하면, 테스트에서 B를 쉽게 모킹하거나 테스트 더블 객체로 대체할 수 있습니다.

```java
// Bad
class UserService {
	private final MessageHandler handler;
}
class MessageHandler {}

---

// Good
class UserService {
	private final MessageHandler handler;
}

class MessageResolver implements MessageHandler{}

interface MessageHandler{}
```

## 리포지토리에서 엔티티를 찾지 못할 경우 예외를 던질 것

서비스 레이어에서 리포지토리에 엔티티를 요청했을 때, 엔티티를 찾지 못하는 경우 리포지토리에서 `Optional`을 반환하면, 서비스 레이어에서는 `Optional`을 처리해야 할 추가적인 로직과 테스트가 필요해집니다. 대신, 리포지토리에서 엔티티를 찾지 못할 경우 예외를 던지게 하여, 서비스 레이어에서 이를 처리하도록 하는 것이 좋습니다. 이렇게 하면, 테스트의 범위가 좁아져 테스트 코드 작성이 더 쉬워집니다.

## assertj 와 junit 중에 assertj를 사용하자

스프링 팀은 테스트 도구로 AssertJ를 사용하고 있습니다. JUnit 팀도 기본적인 테스트는 JUnit으로 충분하지만, 더 강력한 기능이 필요하다면 AssertJ를 사용하라고 권장합니다. [JUnit 공식 문서](https://junit.org/junit5/docs/current/user-guide/#writing-tests-assertions-third-party)에서도 이를 확인할 수 있습니다.

JUnit의 `assertEquals`와 같은 메서드는 기대값과 실제값을 두 개의 파라미터로 받는 반면, AssertJ는 단일 파라미터를 체인 형식으로 사용하여 가독성이 뛰어나고 의미를 이해하기 쉽습니다.

예를 들어, 다음과 같은 코드로 유연하고 명확하게 테스트할 수 있습니다:

```java
assertThat("str")
	.isEqualTo("str")
	.withFailMessage("not equals str")

assertThat(2)
        .isNotZero()
        .isGreaterThan(1)
        .isGreaterThanOrEqualTo(1);

assertThat("hello")
        .as("해당 테스트 실패 시 출력할 설명 메시지")
        .isNotNull()
        .startsWith("h")
        .endsWith("o");

assertThatThrownBy(() -> {
    throw new RuntimeException("error message");
}).hasMessage("error message");

assertThatExceptionOfType(RuntimeException.class)
        .isThrownBy(() -> {
            throw new RuntimeException("error message");
        })
        .withMessage("error message");

List<String> list = List.of("first", "second");
assertThat(list)
        .isNotNull()
        .contains("first")
        .containsAll(list)
        .isEqualTo(list);

```

## 테스트 메서드 이름에 대상의 이름을 포함하지 말자

테스트 메서드의 명명 규칙은 다양하지만, 테스트 대상의 이름을 메서드 이름에 포함시키는 것은 좋지 않은 방법입니다. 대상의 이름이 변경되면 테스트 메서드의 이름도 함께 변경해야 하기 때문입니다.

또한, 기능을 기준으로 메서드 이름을 작성하면 일관성이 떨어질 수 있습니다. 대신, `Given`, `Should`, `When`, `Then`과 같은 단어를 활용하여 메서드 이름을 작성하는 것이 더 좋은 접근입니다.

예: `Should_ThrowException_When_AgeLessThan18`

## 테스트 코드도 코드다: 상속 대신 합성을 사용하자

테스트 코드도 코드이므로, 중복을 줄이기 위해 상속을 사용하는 것은 바람직하지 않습니다. 상속은 테스트 코드에서도 불필요한 복잡성과 의존성을 유발할 수 있습니다. 따라서 중복 코드를 합성을 통해 적절히 분리하여 관리하는 것이 좋습니다.

### 반복 테스트에는 `@ParameterizedTest`를 활용하자

반복적인 테스트를 위해 `@ParameterizedTest`를 사용하는 것이 좋습니다. 예를 들어, 다음과 같은 방식으로 여러 케이스를 테스트할 수 있습니다:

```java
public static Stream<Arguments> getInstanceParameters() {
    return Stream.of(
            arguments(null, "ABC", "DEF"),
            arguments(UUID.randomUUID(), null, "DEF"),
            arguments(UUID.randomUUID(), "ABC", null)
    );
}

@ParameterizedTest(name = "인스턴스 생성 시 Null 허용 안함: id = {0}, loginId = {1}, loginPwd = {2}")
@MethodSource("getInstanceParameters")
void failCreateInstance(UUID id, String loginId, String loginPwd) {
    Assertions.assertThatThrownBy(() -> Member.withId(id, loginId, loginPwd))
            .isInstanceOf(IllegalArgumentException.class);
}

```

## 테스트 메서드에는 `public`을 사용하지 말자

JUnit 5에서는 테스트 메서드에 `public` 접근 제한자를 붙이지 말고, 기본 접근 제한자(default)를 사용하는 것이 권장됩니다. [SonarLint](https://junit.org/junit5/docs/current/user-guide/#writing-tests-classes-and-methods)와 같은 정적 분석 도구에서도 이를 권장하고 있습니다.

## 테스트 코드는 쉬운 테스트부터 작성하자

테스트를 작성할 때는 예외 처리나 실패 테스트처럼 간단한 테스트부터 시작하는 것이 좋습니다. 성공 테스트는 상대적으로 복잡한 로직을 테스트해야 하기 때문에 피드백이 느려질 수 있으며, 성공 테스트를 먼저 작성하고 나서 실패 테스트를 추가하면 기존 로직에 불필요한 분기를 추가하게 됩니다.

테스트 작성이 용이한 코드는 좋은 코드이며, 피드백이 빠른 테스트 코드는 좋은 테스트 코드입니다.

> 💡 단, 테스트 대상이 올바른지 항상 확인하는 것이 중요합니다.
>

## 하나의 클래스 파일에 하나의 테스트 클래스 파일을 매핑하자

서비스 클래스에 두 개의 public 메서드가 있다고 가정해봅시다. 이 두 메서드를 각각 하나의 테스트 클래스에 넣을지, 아니면 하나의 클래스에 모두 포함시킬지 고민될 수 있습니다.

하나의 클래스에 모두 포함시키면 파일의 크기가 커지지만, 두 개의 클래스로 나누면 테스트 클래스와 대상 클래스 간의 매핑이 복잡해질 수 있습니다. 대부분의 프로젝트에서는 테스트 클래스를 하나로 처리하고 파일 크기에 크게 신경 쓰지 않습니다. 이 방식을 따라가는 것이 좋습니다.

## Final 클래스는 Mockito로 Mocking할 수 없으니 MockMaker를 사용하자

Final 클래스는 기본적으로 Mocking할 수 없습니다. Final 키워드를 제거하는 대신, `MockMaker`를 통해 바이트 코드 조작으로 Mocking을 처리하는 것이 더 좋은 접근입니다.

다음과 같은 경로와 파일을 생성한 후, 파일에 `mock-maker-inline` 내용을 추가합니다:

```bash
vi /test/resources/mockito-extensions/org.mockito.plugins.MockMaker
```

이 설정을 통해 Final 클래스도 Mockito로 Mocking할 수 있습니다.

## BDD 스타일로 테스트할 때는 **BDDMockito**를 사용하자

```java
void test() {
    // given
    String loginId = "";
    Mockito.when(memberRepository.findById(loginId))
            .thenReturn(Optional.of(new Member()));

    // when
    Token token = memberService.login(loginId);

    // then
    Assertions.assertThat(token).isNotNull();
    Mockito.verify(memberRepository, Mockito.times(1)).findById(loginId);
}

```

위와 같은 given-when-then 구조의 테스트에서, `Mockito.when()` 메서드는 given 영역에 있어 부적절해 보일 수 있습니다. 이런 경우 `BDDMockito`를 사용하면 더 자연스럽고 읽기 쉬운 테스트 코드를 작성할 수 있습니다.

```java
void test() {
    // given
    String loginId = "";
    BDDMockito.given(memberRepository.findById(loginId))
            .willReturn(Optional.of(new Member()));

    // when
    Token token = memberService.login(loginId);

    // then
    Assertions.assertThat(token).isNotNull();
    BDDMockito.then(memberRepository).should().findById(loginId);
}
```', '테스트팁 - 테스트 하기 좋은 코드')
