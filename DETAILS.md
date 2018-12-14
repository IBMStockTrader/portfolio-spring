# Portfolio-Spring details

## Spring OAuth2 (JWT) Authentication

Spring provides JWT functionality within the OAuth2 part of it's Security framework.

This would normally be used to verify an `access_token` issued by 
a remote OAuth2 server. The framework allows for the possibility
of the token being a JWT, and expects it to be sent as a Bearer token in the Authorization header of a request.

Conveniently, this matches the way MP-JWT expects it's credentials, 
enabling Spring to act as an MP-JWT protected service.

To enable this functionality you add an `@EnableResourceServer` annotation (requires an exsiting `@EnableWebSecurity`). This expects
configuration via an `ResourceServerConfigurerAdapter` bean that can configure details such as:
 - JWT PublicKey for verifying signing.
 - Issuer/Audience/Custom claim verification.
 - Mapping claims to GrantedAuthorities (Roles)
 - Error handling.

This project has been configured to enforce that MP-JWT required claims are present, and to map from the MP-JWT groups to Roles.

## Spring Method Level Security

Spring provides method level security, allowing annotation with 
`@Secured`, `@RoleAllowed` (JSR-250 version of `@Secured`), and `@PreAuthorize` / `@PostAuthorize`

The first two just allow restriction of method invocation if the
user holds a given Role. The latter two however accept Spring Expression Language (SpEL), and are really quite powerful, enabling 
arbitrary expressions to be crafted that can even access the method arguments/return values respectively. 

Spring Method Level Security is enabled by adding the `@EnableGlobalMethodSecurity` annotation to the application. The annotation can be customised to indivually enable use
of `@Secured`, `@RoleAllowed`, and `@PreAuthorize`/`@PostAuthorize`.

For this project, the `@Secured` annotation is used to restrict 
access to the rest endpoints to users identified as having the 
appropriate roles.

## Spring Data JDBC

Spring Data offers two approaches to talk to a relational database, 
JDBC and JPA. Both are similar, in that you provide the interface 
and data transfer objects (DTOs), and Spring creates the implementation
that backs the interface. The main difference between them is Spring Data JDBC
is intended to be a lighter weight interface to the database, and doesn't 
do as much for you as Spring Data JPA does.

The original Portfolio project used inline JDBC statements, so this
implementation has chosen Spring Data JDBC to illustrate Springs capabilty.

Spring Data JDBC is enabled by adding the `@EnableJdbcRepositories("package")` annotation
indicating the package, or packages that contain the interfaces to be implemented by Spring Data.

## Spring MVC

Although this microservice doesn't need most of Spring MVC, the Controller part
is genuinely useful. 

Springs `@RestController` provides the simple hosting of REST endpoints, 
including JSON binding for arguments & results.

While `@RestController` handles the inbound requests, `RestTemplate` is its
counterpart for making outbound requests. It also handles the JSON binding, and provides
a robust framework for adding functionality such as interceptors to add headers
to outgoing requests, and `@ExceptionHandlers` to map application exceptions to REST responses.

## Spring JMS (IBM MQ Spring Boot Starter)

Spring supports JMS via it's Spring JMS api, and IBM has released a Spring Starter
that makes it really simple to setup and use Spring JMS with MQ.

In keeping with the Spring MVC `RestTemplate`, Spring JMS has its own `JMsTemplate`
that can be used to send JMS messages. The IBM Starter makes one available based
on the configuration that can be `@Autowired` into application code. 

Although this project doesn't use it, Spring JMS also offers a `@JmsListener` annotation
that can be used to declare methods to be invoked when messages are received.

Spring JMS is enabled by adding the `@EnableJms` annotation to the application.



 
