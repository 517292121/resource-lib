# MCProject（RISS2 多语种）运行与部署

项目按 `D:\HLSProject` 的服务目录方式部署：Java 8、Spring Boot 2.7、Windows 服务包装器，应用 JAR 与依赖 JAR 分离。因页面使用 JSP，运行目录还需要保留独立的 `WebRoot` 文件夹。

## 构建

要求 JDK 8 和 Maven 3.6+：

```powershell
$env:JAVA_HOME = 'C:\Program Files\Java\jdk1.8.0_161'
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
mvn clean package -DskipTests
```

构建结果在 `target`：

- `MCProject.jar`：应用代码
- `lib\`：运行依赖，首次部署时复制；依赖未变化时后续更新不必重复传输
- `config\`：外置 `application.properties` 和 `config.properties`；其中数据库配置从项目根目录的 `MCProject\config\config.properties` 复制
- `WebRoot\`：JSP、图片、CSS 和其他站点资源；其中旧的 `WEB-INF\lib` 不再复制，统一从 `lib\` 加载
- `MCProject.exe`、`MCProject.xml` 和 `MCProjectServer-*.bat`：Windows 服务文件

## 运行

在 `target` 目录双击 `MCProjectstart.bat`，或在命令行运行：

```powershell
java -Dspring.config.location=file:./config/ -cp "config;MCProject.jar;lib/*" cn.com.adtech.riss2.Riss2Application
```

默认端口是 8080。Apache HTTP Server 可反向代理到该端口；Apache 本身不运行 Java/JSP 应用。`MCProject.xml` 的服务 ID/名称为 `MCProject`，安装、启动、停止和卸载可使用对应的 `MCProjectServer-*.bat`。安装 Windows 服务时请以管理员身份运行安装脚本。

如果只修改 Java 代码，增量更新通常只需要替换 `MCProject.jar`；修改 JSP/图片/CSS 时同步更新 `WebRoot` 中对应文件。只有依赖变动时才需要更新 `lib`。

数据库和业务配置以项目根目录的 `MCProject\config\config.properties` 为准，运行时优先读取该文件；服务部署目录下的 `config\config.properties` 作为打包副本/回退路径。首次部署前检查数据库、文件根目录、文件服务和 PDF 工具路径。数据库初始化脚本为 `WebRoot\db\database-riss2-mysql.sql`，之后执行 `riss2-dyz-mysql.sql` 与 `create-trigger-mysql.sql`。不要将生产密码提交到代码库。

Java 源码命名空间仍为 `cn.com.adtech.riss2`，这里只将应用产物名和 Windows 服务名统一为 `MCProject`，避免无必要地改动大量旧代码引用。
