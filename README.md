# DropBookmarks

A simple Dropwizard 1.x project exposing REST API to store bookmarks.

How to start the DropBookmarks application
---

1. Run `mvn clean package` to build your application
1. Start application with `java -jar target/DropBookmarks-1.0-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`

The version of this project for DropWizard 0.8.2 is here `https://bitbucket.org/dnoranovich/dropbookmarks`