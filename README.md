# WriteThis-API

---

## Run for FE

***!!! If you are Windows user, you need first install [wsl2][5]*** and [maven][6] ([how to install][7]).

If you are an FE developer, and you want just to run the back-end part,
then you'll be enough to install docker (see [step 1](README.md/#1.-preparing)), and after that just execute command :

_... with build (for first run):_
```sh
docker-compose up --build
```

_... without build:_
```sh
docker-compose up
```



## 1. Preparing

- **Install Java**. Application is running on Java JDK 17.
  To make sure that you are using the correct version - `java -version`. Install for [Windows][1]. For Linux is
  convenient to use [sdkman][2].
- **Install Docker**. For [Windows][3]. For [Linux][4].

<hr/>

## 2. Build project

Run `./mvnw clean install`
or without tests `./mvnw clean install -DskipTests`

...or for Windows:

Run `mvn clean install`
or without tests `mvn clean install -DskipTests`
<hr/>

## 3. Run locally

Before running, you have to up the database:

``` sh
docker-compose up db
```

***!!! The database data will be available on the path $HOME/Documents/pgdata***

To run an application you can configure your IDE,
or you can just execute the command:

``` sh
./mvnw spring-boot:run
```

...or for Windows:

``` commandline
mvn spring-boot:run
```

# Other documentation

## Swagger

To access to swagger page go to `{root_path}/swagger-ui/`
F.e. `http://localhost:8080/v1/swagger-ui/`

[1]: https://download.oracle.com/java/17/archive/jdk-17.0.4.1_windows-x64_bin.exe

[2]: https://sdkman.io/install

[3]: https://desktop.docker.com/win/main/amd64/Docker%20Desktop%20Installer.exe

[4]: https://docs.docker.com/engine/install/ubuntu/#installation-methods

[5]: https://docs.microsoft.com/uk-ua/windows/wsl/install

[6]: https://maven.apache.org/download.cgi

[7]: https://maven.apache.org/install.html