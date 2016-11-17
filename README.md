# DropBookmarks

A simple Dropwizard 1.0.x project exposing REST API to store bookmarks. The 
application is using MySql as a DBMS.

Database settings are in the config.yml file. To switch to another RDBMS one 
should modify the driver and connection URL. By default it is necessary to create 
DropBookmarks database and it will be populated using migrations.  
  
A brief [video introduction to DropWizard](https://youtu.be/l1TD2sYEgc8).  
  
How to start the DropBookmarks application
---

0. Check out project using `git clone https://github.com/javaeeeee/DropBookmarks.git`
1. Create a key store in the project's folder using Java 8 *keytool* 
`keytool -genkeypair -keyalg RSA -dname "CN=localhost" \
-keystore dropbookmarks.keystore  -keypass p@ssw0rd -storepass p@ssw0rd`
2. Run `mvn clean package` to build the application
3. To populate the database run `java -jar target/DropBookmarks-1.0-SNAPSHOT.jar db migrate -i TEST config.yml` 
4. Start application with `java -jar target/DropBookmarks-1.0-SNAPSHOT.jar server config.yml`
5. To check that your application is running, enter URL `http://localhost:8081` in the browser 
  
How to try the DropBookmarks application
---
  
The API is secured with Basic Authentication. One can find a user *javaeeeee*
whose password is *p@ssw0rd* along with several bookmarks in the database after 
executing the migrations.

To get the list of all bookmarks stored by *javaeeeee* enter

~~~~
curl -w "\n" 2>/dev/null -k https://localhost:8443/bookmarks -u javaeeeee:p@ssw0rd
~~~~

To get the data stored for the bookmark with id == 1 type

~~~~
 curl -w "\n" 2>/dev/null -k https://localhost:8443/bookmarks/1 -u javaeeeee:p@ssw0rd
~~~~

To add a bookmark it is necessary to key in 

~~~~
curl -X POST -w "\n" 2>/dev/null -k https://localhost:8443/bookmarks \
 -u javaeeeee:p@ssw0rd -H "Content-Type: application/json" \
 -d '{"url":"http://github.com", "description":"A lot of great projects"}'
~~~~

To modify a bookmark the API offers PUT method

~~~~
curl -X PUT -w "\n" 2>/dev/null -k https://localhost:8443/bookmarks/1 -u javaeeeee:p@ssw0rd \
 -H "Content-Type: application/json" -d '{"url":"https://github.com/javaeeeee/SpringBootBookmarks"}'
~~~~

To delete a bookmark use 

~~~~
curl -X DELETE -w "\n" 2>/dev/null -k https://localhost:8443/bookmarks/1 -u javaeeeee:p@ssw0rd
~~~~


The version of this project for DropWizard 0.8.2 can be found here 
[https://bitbucket.org/dnoranovich/dropbookmarks](https://bitbucket.org/dnoranovich/dropbookmarks)