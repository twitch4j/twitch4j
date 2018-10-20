# Camunda Project Template

This is a tempate for camunda projects.

## Details

The project contains 4 subprojects:

- Process Application (Your Process)
- Package WAR (builds the tomcat war deployment)
- Package Boot (Spring Boot embedded process engine)
- Package Boot AllInOne (Spring Boot embedded process engine, with WebApp and REST-API)

## Deployment

Supports the following deployment methods from the same code base:

- deployment into a shared engine (tomcat-war)
- spring boot standalone
- spring boot standalone with webapps and rest api

## License

Released under the [MIT License](LICENSE).
