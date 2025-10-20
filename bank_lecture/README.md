
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
