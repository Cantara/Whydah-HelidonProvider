# Whydah-HelidonProvider
Authorize services, clients and users in Helidon
## What is implemented

* Validate Application using ApplicationTokenId in Authorization header with pattern "Bearer 1234.56789"

## What will be imlpemented later
* Validate Application using:
  * Whydah-App-TokenId
  * Whydah-User-TokenId
  * Authorization AppTokenId ....
  * Authorization UserTokenId ....
  * Authorization Bearer JWT token representing the user and service requesting access.

* Validating User based on JWT and UserToken from SecurityTokenService


## Getting Started

## Create Application and "Client" in Whydah UserAdminWeb
* Create an application with name, secret and id.
* Create another applicatio with <name>_client, secret and id.

### Config

In pom.xml
```
<dependency>
  <groupId>net.whydah.sso</groupId>
  <artifactId>Whydah-HelidonProvider</artifactId>
  <version>0.1.0-SNAPSHOT</version>
</dependency>
```
In src/main/resources/application.yaml file
```
security:
  providers:
    - atn:
      class: "net.whydah.sso.helidon.WhydahProvider"
```

In src/main/resources/META-INF/microprofile-config.properties
```
whydah_enabled=true
whydah_uri=<https://whydah.example.com/>
whydah_application_name=<name of application>
whydah_application_id=<tobeset>
whydah_application_secret=<tobeset>
```

In local_override.properties
```
whydah_application_id=<id from whydah useradmin>
whydah_application_secret=<secret from whydah useradmin>
```

 ### REST endpoints

 ```
@Path("...")
@Authenticated
@RoleValidator.Roles(value = "service_verified", subjectType = SubjectType.SERVICE)
public class WhydahSecuredResource() {
....
}
 ```

## Validate

1. Logon your client with curl:
Replace ***insert*** as apropiate
```
curl -i -X POST \
   -H "Content-Type:application/x-www-form-urlencoded" \
   -d "applicationcredential=<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>
    <applicationcredential>
        <params>
            <applicationID>***insert***</applicationID>
            <applicationName>***insert***</applicationName>
            <applicationSecret>***insert***</applicationSecret>
        </params>
    </applicationcredential>" \
 'https://hos:port/tokenservice/logon/'
 ```
2. Access your application with curl
Replace with "applicationtokenID" from result in 1.
```
curl -i -X GET \
   -H "Authorization:Bearer ***insert***" \
 'http://host:port/whydah/services'
```

