# spring-security-turtorial
spring-security笔记

## day1 UserDetails入门

### UserDetailsServiceAutoConfiguration

新建一个SpringBoot项目，在pom.xml中引入Spring Security依赖后，启动项目。项目默认会弹出一个表单让你输入用户名和密码。默认的用户名是user，而密码是自动生成的。项目启动后，在SpringBoot控制台会打印出类似 `Using generated security password: e1f163be-ad18-4be1-977c-88a6bcee0d37` 的字样，后面的长串就是密码。而默认的密码是由UserDetailsServiceAutoConfiguration生成的。

UserDetailsServiceAutoConfiguration的全路径限定名是org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration。观察UserDetailsServiceAutoConfiguration会发现它只是向容器注入了一个InMemoryUserDetailsManager实例，到这里就会发现，注入的InMemoryUserDetailsManager实例中默认配置了一个用户，也就是上面说的默认的用户。进一步观察，UserDetailsServiceAutoConfiguration的源码还会发现，只有当容器中不存在UserDetailsService接口的实现类的实例时才会注册InMemoryUserDetailsManager实例。

InMemoryUserDetailsManager实例实现了UserDetailsManager接口，UserDetailsManager接口又是UserDetailsService接口的子接口。所以，如果我们在容器中提供一个UserDetailsManager接口的实现类，默认的InMemoryUserDetailsManager实例就不会起作用，这样我们就可以自定义用户管理逻辑。

## day2 自定义登录逻辑

## day3 自定义可支持多种登陆方式

## day4 自定义退出逻辑

通过实现LogoutHandler接口来自定义退出的逻辑。

通过实现LogoutSuccessHandler接口来自定义退出成功的逻辑。

## day5 实现JWT Token

### keytool

keytool是密码和证书的管理工具。它的位置位于\$JAVA_HOME\jre\bin\keytool.exe。Keytool将密钥（key）和证书（certificates）存在一个称为keystore的文件中，这个后缀为keystore的文件就是证书库。所有的数字证书是以一条一条(采用别名区别)的形式存入证书库的中，证书库中的每个证书包含该条证书的私钥，公钥和对应的数字证书的信息。

#### keytool的用法

查看keytool的使用方法的最好方式是直接在终端输入keytool命令并回车，会显示keytool的所有可用命令。

1. 创建证书

   ```shell
   keytool -genkeypair -alias test -keyalg RSA -keystore test.keystore
   ```

   

   ![](E:\IDEAWorkSpace\security-learn\images\keytool生成证书.png)

   

   其实上诉创建证书的方法也可以使用下面一行命令达到，效果是相同的。各个参数的含义是：

   - genkeypair 生成一对密钥，也就是公钥和私钥（非对称加密）。
   - alias 这个参数的作用就是给证书库中的一个证书起一个别名，这个别名不区分大小写但是要求唯一。如果不指定，默认值是 mykey。
   - keyalg 创建证书使用的加密算法。如果不指定，默认值是DSA。
   - keystore 证书库的位置，如果创建证书时证书库不存在，就创建它。如果不指定，默认值是当前用户的主目录。
   - storepass 这个密码是使用证书库时用的密码。对应到这里，就是使用test.keystore这个证书库时要用的密码。对应到上图1出的密码。
   - keypass 这个是使用某个具体的证书时用的密码。对应到这里，就是使用别名为test的证书时使用的密码。对应到上图出的密码。
   - dname 定义一些其他信息。
     - CN对应到上图中的“您的名字与姓氏”，但是并不是真正的要输入人的姓名。正确的用法是，在生产环境中要和服务器的域名相同，如果是在本地测试，应该使用localhost。
     - OU对应到上图中的“您的组织单位名称”。
     - O对应到上图中的“您的组织名称”。
     - L对应到上图中的“您所在的城市或区域”。
     - ST对应到上图中的“您所在的省/市/自治区”。
     - C对应到上图中的“该单位的双字母国家/地区代码”。

   ```shell
   keytool -genkeypair -alias test -keyalg RSA -keystore test.keystore -dname = "CN=localhost,OU=localhost,O=localhost,L=BEIJING,ST=BEIJING,C=CN" -keypass qlxazm -storepass 123456
   ```

   除了可以在命令行中指定上诉几个参数外，还有以下几个常用的参数：

   - keysize 指定生成的密钥的长度。默认值是1024。
   - validity 指定证书的有效期。默认值是90，单位是天。

2. 查看密钥库中的证书

   ```shell
   keytool -list -keystore test.keystore
   ```

   ![](E:\IDEAWorkSpace\security-learn\images\keytool查看密钥库中的证书.png)

3. 导出公钥文件

   ```shell
   keytool -export -alias test -file test.crt -keystore test.keystore
   ```

   ![](E:\IDEAWorkSpace\security-learn\images\keytool导出公钥成公钥文件.png)

4. 将公钥文件导入到另外一个证书库中

   ![](E:\IDEAWorkSpace\security-learn\images\keytool将公钥导入到另外一个证书库.png)

   ```shell
   keytool -import -keystore test2.keystore -file test.crt
   ```

   如果是为客户端的JVM导入密钥文件（密钥文件已经下载了下来），可以使用命令：

   ```shell
   keytool -import -trustcacerts -alias 公钥文件对应的证书的别名 -keystore "%JAVA_HOME%/jre/lib/security/cacerts" -file 公钥文件的位置 -storepass changeit
   ```

   这里要注意的是：

   - “-storepass changeit”，如果你没更改过JVM对应的证书库的密码，默认的密码就是changeit。

   - 在导入公钥文件时可能出现错误“keytool error: java.lang.Exception: Input not an X.509 certificate”异常。这时可用文本编辑器打开公钥文件查看，如是乱码，则应该是“DER 编码二进制 X.509（.CER）(D)”证书，如是字母数字，则应该是"Base64 编码 X.509（.CER）(D)”证书。

     这里以将“DER 编码二进制 X.509（.CER）(D)”证书转换成"Base64 编码 X.509（.CER）(D)”证书为例。![](E:\IDEAWorkSpace\security-learn\images\notAnX.509_step1.png)

     ![](E:\IDEAWorkSpace\security-learn\images\notAnX.509_step2.png)

     ![](E:\IDEAWorkSpace\security-learn\images\notAnX.509_step3.png)

     ![](E:\IDEAWorkSpace\security-learn\images\notAnX.509_step4.png)

5. 查看证书文件信息

   ```shell
   keytool -printcert -file test.crt
   ```

   ![](E:\IDEAWorkSpace\security-learn\images\keytool查看公钥文件的信息.png)

6. 删除证书库中的条目

   ```shell
   keytool -delete -keystore test2.keystore -alias test2
   ```

