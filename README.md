# Azure Java Libraries 1.6 Monitor Metrics APIs Test

This is a test to try out Azure Libraries for Java 1.6 on the management monitor APIs

### Project configured to compatable w/ Java 1.7

### Transitive JAR dependencies other than Azure's own from Azure 1.6.0.
- retrofit-2.1.0.jar
- okhttp-3.4.2.jar
- okio-1.9.0.jar
- logging-interceptor-3.4.2.jar
- okhttp-urlconnection-3.4.2.jar
- converter-jackson-2.1.0.jar
- jackson-databind-2.7.2.jar
- jackson-datatype-joda-2.7.2.jar
- jackson-annotation-2.7.0.jar
- commons-lang3-3.4.jar
- adapter-rxjava-2.1.0.jar
- slf4j-api-1.7.22.jar
- oauth2-oidc-sdk-4.5.jar
- mail-1.4.7.jar
- activation-1.1.jar
- jcip-annotations-1.0.jar
- json-smart-1.1.1.jar
- lang-tag-1.4.jar
- nimbus-jose-jwt-3.1.2.jar
- bcprov-jdk15on-1.5.1.jar
- gson-2.2.4.jar
- commons-codec-1.10.jar
- slf4j-simple-1.7.5.jar
- rxjava-1.2.4.jar
- httpcore-4.4.5.jar
- jackson-core-2.6.0.jar
- joda-time-2.1.jar
- commons-net-3.3.jar

- <font color="red">guava-20.0.jar included from Azure-Libraries-SDK 1.6 </font>
- <font color="red">jackson*.jar included from Azure-Libraries-SDK 1.6 </font>

Possible issue to address the following conflicts per David.
May yield to smaller depending jars collection when picking subset of Azure API libraries. 
Then, still need to deal w/ potential jars conflicts.  

