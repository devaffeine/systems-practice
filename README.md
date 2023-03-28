# sb-auth-poc

Spring Boot Auth PoC project

## Setup

### Environment variables:

```
JWT_SECRET_KEY=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970;
DB_HOSTNAME=localhost; DB_PORT=3306; DB_USERNAME=auth_poc; DB_PASSWORD=auth_poc_pass; DB_NAME=auth_poc
```

## Monitoring

- Check health status at http://localhost:8080/actuator/health
- Check metrics status at http://localhost:8080/actuator/metrics

## OpenAPI

- Check OpenAPI json at http://localhost:8080/api-docs
- Check OpenAPI UI at http://localhost:8080/api-ui.html
