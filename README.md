# course-builder
http://app.vanhack.com/Challenges/Details?id=28

# Deploy

GlassFish 4.1 has a bug... Deploy to WildFly:

1. Download WildFly:
http://wildfly.org/downloads/
2. Download MySQL conector:
https://dev.mysql.com/downloads/connector/j/
3. Copy `mysql` folder to `/modules/system/layers/base/com/`
4. Copy `mysql-connector-java-5.1.41-bin.jar` to `/modules/system/layers/base/com/mysql/main/`
5. Open `configuration/standalone.xml` and find "datasources" and "drivers", and add this content:

```xml
    <datasources ...>

    {...}
        
        <datasource jndi-name="java:/jdbc/CourseBuilderDS" pool-name="CourseBuilderDS" enabled="true" use-java-context="true">
            <connection-url>jdbc:mysql://localhost:3306/coursebuilder</connection-url>
            <driver>mysql</driver>
            <pool>
                <min-pool-size>5</min-pool-size>
                <max-pool-size>10</max-pool-size>
                <prefill>true</prefill>
            </pool>
            <security>
                <user-name>root</user-name>
                <password>root</password>
            </security>
            <validation>
                <valid-connection-checker class-name="org.jboss.jca.adapters.jdbc.extensions.mysql.MySQLValidConnectionChecker"/>
                <validate-on-match>true</validate-on-match>
                <exception-sorter class-name="org.jboss.jca.adapters.jdbc.extensions.mysql.MySQLExceptionSorter"/>
            </validation>
        </datasource>

        <drivers>

            {...}

            <driver name="mysql" module="com.mysql">
                <driver-class>com.mysql.jdbc.Driver</driver-class>
            </driver>
        </drivers>
    </datasources>
```
(change username/password/mysql-conector version if necessary)
6. Place the .war file in `/standalone/deployments/`
7. Start MySQL and create schema with `create schema if not exists coursebuilder;`
8. start WildFly: `standalone.bat` (Windows) or `standalone.sh` (Other)