# Whydah-HelidonProvider

## Purpose
Authorization provider for Helidon-based services that integrates with the Whydah IAM system. Validates application tokens and user tokens from Whydah SecurityTokenService, enabling Helidon microservices to participate in the Whydah SSO ecosystem.

## Tech Stack
- Language: Java 11+
- Framework: Helidon
- Build: Maven
- Key dependencies: Whydah-Java-SDK, Helidon Security

## Architecture
Library that plugs into Helidon's security provider framework. Intercepts incoming requests and validates Bearer tokens against Whydah SecurityTokenService. Supports application-level authentication via ApplicationTokenId and will support user-level authentication via JWT/UserToken in future versions.

## Key Entry Points
- Helidon security provider implementation
- `pom.xml` - Maven coordinates: `net.whydah.sso:Whydah-HelidonProvider`

## Development
```bash
# Build
mvn clean install

# Test
mvn test
```

## Domain Context
Whydah IAM integration for Helidon microservices. Enables services built on the Helidon framework to authenticate and authorize requests using Whydah tokens, extending SSO coverage to the Helidon ecosystem.
