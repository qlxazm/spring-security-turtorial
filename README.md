# spring-security-turtorial
spring-security笔记

## day1 UserDetails入门

### UserDetailsServiceAutoConfiguration

新建一个SpringBoot项目，在pom.xml中引入Spring Security依赖后，启动项目。项目默认会弹出一个表单让你输入用户名和密码。默认的用户名是user，而密码是自动生成的。项目启动后，在SpringBoot控制台会打印出类似 `Using generated security password: e1f163be-ad18-4be1-977c-88a6bcee0d37` 的字样，后面的长串就是密码。而默认的密码是由UserDetailsServiceAutoConfiguration生成的。

UserDetailsServiceAutoConfiguration的全路径限定名是org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration。观察UserDetailsServiceAutoConfiguration会发现它只是向容器注入了一个InMemoryUserDetailsManager实例，到这里就会发现，注入的InMemoryUserDetailsManager实例中默认配置了一个用户，也就是上面说的默认的用户。进一步观察，UserDetailsServiceAutoConfiguration的源码还会发现，只有当容器中不存在UserDetailsService接口的实现类的实例时才会注册InMemoryUserDetailsManager实例。

InMemoryUserDetailsManager实例实现了UserDetailsManager接口，UserDetailsManager接口又是UserDetailsService接口的子接口。所以，如果我们在容器中提供一个UserDetailsManager接口的实现类，默认的InMemoryUserDetailsManager实例就不会起作用，这样我们就可以自定义用户管理逻辑。