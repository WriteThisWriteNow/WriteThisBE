# WriteThis-API

---

## Run for FE

***!!! If you are Windows user, you need first install [wsl2][5]*** and [maven][6] ([how to install][7]).

If you are an FE developer, and you want just to run the back-end part,
then you'll be enough to install docker (see [step 1](README.md/#1.-preparing)) and run it.

If you are going to register with a local application and get a confirmation email
then you need to replace the password in the line `SPRING_MAIL_PASSWORD: <password>` (in file `docker-compose.yml`)
with required data. Ask VanSisto (`vansisto@writethis.com.ua`) about it.

And after that just execute command in project root directory:

_... with build (for first run after code update):_

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

If you are going to register with a local application and get a confirmation email
then you need to replace the password in the line `SPRING_MAIL_PASSWORD: <password>` (in file `docker-compose.yml`)
with required data. Ask VanSisto (`vansisto@writethis.com.ua`) about it.

Before running, you have to up the database:

``` sh
docker-compose up db
```

***!!! The database data will be available on the path $HOME/Documents/pgdata***

To run an application you can configure your IDE,
or you can just execute the command:

``` sh
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

...or for Windows:

``` commandline
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

# Other documentation
## Swagger
To access to swagger page go to `{root_path}/swagger-ui/`
F.e. `http://localhost:8080/v1/swagger-ui/`

## Authentication
### Basic authentication
For the first time, you must send a simple POST request to the `/login` URL (`http://localhost:8080/v1/login`).
You'll get the 403 response status. And you will get a cookie with XSRF-TOKEN in it.
To log in, you have to send a POST request to the `/login` URL with a form-data body (not json) like this:
```json
{
  "email": "your@email.com",
  "password": "Your strong enough password"
}
```
Also, you have to set the XSRF token to header - `"X-XSRF-TOKEN" : "<token copied from cookies>"`

After successfully logging you'll get the response with status 200. And in headers, you'll get the `Authorization`
header with an authentication token that looks like `Bearer <someLongEnoughToken>`.
Notice that it expires in 1 week by default.

For the following requests to API, you have to set that header (`"Authorization" : "Bearer <token>"`)

In summary - you have to have two headers with each request:

| Header name   | Header value                    |
|---------------|---------------------------------|
| X-XSRF-TOKEN  | `<some-generated-token>`        |
| Authorization | `Bearer <some-generated-token>` |

### Local authentication

A local environment has five basic users already:

| **Email**  | **Password** |
|------------|--------------|
| superadmin | superadmin   |
| admin      | admin        |
| manager    | manager      |
| author     | author       |
| reader     | reader       |

## Email client configuration

To configure sending emails (f.e. registration-verification letters) you need to add a few environment variables:

1. **SPRING_MAIL_HOST**=_<email-server-name>_
2. **SPRING_MAIL_PASSWORD**=<email-server-password>
3. **SPRING_MAIL_PORT**=<smtp-port>

Ask VanSisto (`vansisto@writethis.com.ua`) to get actual data for the dev environment.

[1]: https://download.oracle.com/java/17/archive/jdk-17.0.4.1_windows-x64_bin.exe

[2]: https://sdkman.io/install

[3]: https://desktop.docker.com/win/main/amd64/Docker%20Desktop%20Installer.exe

[4]: https://docs.docker.com/engine/install/ubuntu/#installation-methods

[5]: https://docs.microsoft.com/uk-ua/windows/wsl/install

[6]: https://maven.apache.org/download.cgi

[7]: https://maven.apache.org/install.html