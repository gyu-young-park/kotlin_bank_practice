
## Kotlin에서 JPA 설정

```gradle
implementation("org.jetbrains.kotlin:kotlin-reflect")
implementation("org.springframework.boot:spring-boot-starter-data-jpa")
implementation("mysql:mysql-connector-java:8.0.33")
implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
```
1. `org.jetbrains.kotlin:kotlin-reflect`: kotlin의 reflection은 java와 다르기 때문에 호환을 위해 필요하다.
2. `org.springframework.boot:spring-boot-starter-data-jpa`: JPA 등 다양한 data layer 기능의 도움을 스프링을 통해 받기 위해 사용한다.
3. `mysql:mysql-connector-java:8.0.33`: mysql-connector이다. `com.mysql:mysql-connector-j`을 사용해도 무방하다.
4. `org.jetbrains.kotlin:kotlin-stdlib-jdk8`: java8 이상에서만 제공되는 기능들을 사용하기 위해서 쓴다. 

## Kotlin에서 AOP 극복
`@Transactional`의 self-invokation 문제가 있는데, A 메서드에서 B 메서드를 호출하는데 두 메서드 모두 `@Transactional`이라면 내부에서 호출되는 메서드는 `@Transactional`이 보장되지 않는다. 왜냐하면 B 메서드는 A 메서드의 내부에서 실행되기 때문이다. 즉, 외부에서 A 메서드를 실행할 때는 런타임에 transactional 객체가 대신 A 메서드를 호출해주지만, B 메서드는 외부에서 실행하는 것이 아니라, 인스턴스 내부에서 실행하기 때문에 `@Transactional` 영향을 못 받는것이다.

또한, Spring AOP의 `@PointCut` 사용 시에 컴파일 단계에서는 문제가 발생하지 않으나 런타임 시에 문제가 발생하는 경우가 있다. 또한, 문구도 너무 복잡한데 이러한 문제들을 해결할 수 있는 메서드를 kotlin에서 만들 수 있다.

java에서 주로 AOP를 쓰는 가장 큰 이유는 다른 언어처럼 함수를 일급 객체로 다를 수 없기 때문이다. go, python, c 언어 모두 함수를 일급 객체로 다루기 때문에 함수를 파라미터로 받아서 프록시 패턴을 적용하는 코드가 spring AOP에 비해서 훨씬 간단하고 쉽다. 이에 비해 java는 함수 개념이 없고 메서드만이 존재하며 메서드를 공식적으로 하나의 일급 객체로 취급하지 않는다. 이러한 문제 때문에 프록시 패턴 구현을 위해서는 객체를 다른 객체로 뒤덮는 패턴으로만 구현이 가능한데, Spring AOP는 이러한 구현을 쉽게 만들어주는 것일 뿐이다.

그런데 kotlin은 함수, 메서드 모두 일급 객체로 취급이 가능하기 때문에 Spring AOP를 보다 더 쉬운 방식으로 제공이 가능한 것이다. logging하는 방식 조차도 Spring AOP를 직접쓰지 않고 다음처럼 함수를 받아서 프록시 처리하는 메서드를 직접 만들면 된다.

```kotlin
object Logging {
    fun <T: Any> getLogger(clazz: Class<T>): Logger = LoggerFactory.getLogger(clazz)

    fun <T> logFor(log: Logger, function: (MutableMap<String, Any>) -> T?) : T {
        val logInfo = mutableMapOf<String, Any>()
        logInfo["start_at"] = now()
        val result = function.invoke(logInfo)
        logInfo["end_at"] = now()

        log.info(logInfo.toString())

        return result ?: throw CustomException(ErrorCode.FAILED_TO_INVOKE_IN_LOGGER)
    }

    private fun now() = System.currentTimeMillis()
}
```

`function`을 받아서 `invoke`로 실행하는데 앞 뒤로 원하는 로직들을 실행할 수 있다. 다만 한 가지 주의할 것은 kotlin의 경우 non local return과 local return 개념이 있는데, 프록싱한 함수에서 return을 하게되면 프록시 함수에서도 return이 되는 non local return 현상이 발생할 수 있다. 이 때문에 local return으로 현재의 람다에서만 탈풀할 수 있도록 만들어야 한다.

```kotlin
@Service
class AuthService(
    private val oAuth2Services: Map<String, OAuthServiceInterface>,
    private val jwtProvider: JwtProvider,
    private val logger: Logger = Logging.getLogger(AuthService::class.java)
) {
    fun handleAuth(state: String, code: String): String = Logging.logFor(logger) {
        val provider = state.lowercase()
        val callService = oAuth2Services[provider] ?: throw CustomException(ErrorCode.PROVIDER_NOT_FOUND, provider)

        val accessToken = callService.getToken(code)
        val userInfo = callService.getUserInfo(accessToken.accessToken)
        val token = jwtProvider.createToken(provider, userInfo.email, userInfo.name, userInfo.id)
        return token
    }
}
```
`handleAuth`에 `Logging.logFor(logger)`을 적용한 것을 볼 수 있다. 함수 매개변수로 람다식이 들어가는데, 여기서 실행된 `return`은 non-local return이라서 `Logging.logFor` 메서드에서도 return되어 버린다. 이러한 문제를 해결하기 위해서 `return`에 `return@logFor`을 써주어 local return으로 만들어주면 된다. 더 코틀린스러운 방법은 `return`을 안쓰고 `token`만 쓰면 된다.

`@Transactional`의 self-invoke 문제도 다음과 같이 메서드(함수)를 매개변수로 받아서 `@Transactional`을 프록시로 덮어주면 된다.
```kotlin
@Component
private class Advice: Runner {
    @Transactional
    override fun <T> run(function: () -> T?): T? = function()

    @Transactional
    override fun <T> readOnly(function: () -> T?): T? = function()
}

@Component
class Transactional(
    private val advice: Runner
) {
    fun <T> run(function: () -> T?) : T? = advice.run(function)
    fun <T> readOnly(function: () -> T?): T? = advice.run(function)
}
```
`Transactional` 클래스는 함수를 매개변수로 받아서 `run`을 시켜주는데 `run`은 `advice.run`을 사용하고 있다. `advice.run`은 `@Transactional`의 영향을 받고 있다. 코드에서 `@Transactional`이 있는 `run`을 항상 `Transactional` 클래스의 `run` 메서드에서 호출하기 때문에 `Advice` 클래스에 있는 `run` 메서드는 항상 외부에서 호출되므로 `@Transactional`이 유효하게 된다.