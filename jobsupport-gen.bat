@echo off
setlocal

cd target

REM java -cp .;daogen-1.0.1-SNAPSHOT.jar;lib/*; net.rothsmith.dao.runner.DaoGenRunner ^
REM --jdbcurl jdbc:oracle:thin:@ldap://oradsqa.fcci-group.com:10389/dv01,cn=OracleContext,dc=fcci-group,dc=com ^
REM --dbuser shareduser ^
REM --dbpassword shareduser ^
REM --driverclass oracle.jdbc.driver.OracleDriver ^
REM --types TABLE ^
REM --names APPLICATION* ^
REM --outputdir /workspaces/daogen/wb-web-security/src/main/java ^
REM --dtopkg com.fcci.genericdao ^
REM --deftmpt /daodefs.vm ^
REM --deffile /workspaces/daogen/wb-web-security/src/main/resources/daodefs.xml ^
REM --springdaofile /workspaces/daogen/wb-web-security/src/main/resources/dao.xml ^
REM --springappctx /workspaces/daogen/wb-web-security/src/main/resources/applicationContext.xml ^
REM --testoutputdir /workspaces/daogen/wb-web-security/src/test/java ^
REM --testpkg com.fcci.wb-web-security ^
REM --schema SHAREDUSER ^
REM --junittmpt spring/junit.vm 

REM --springAppCtxFile=<null>
REM --springAppCtxTemplate=<null>
REM --springDaoCtxFile=<null>
REM --springDaoCtxTemplate=<null>
REM --sqlStmts=select a.useridcode, c.USERID, d.FIRSTNAME, d.LASTNAME, a.GROUPID, b.GROUPDESC from GROUPUSERS a join groups b on a.GROUPID = b.GROUPID join users c on a.USERIDCODE = c.USERIDCODE join usersgeneral d on d.USERIDCODE = c.USERIDCODE where a.USERIDCODE = 1

if /I "%1" EQU "DEBUG" set DEBUG=-agentlib:jdwp=transport=dt_socket,server=n,suspend=y,address=localhost:8000

java -cp .;daogen.jar;lib/*; ^
-XX:+UnlockCommercialFeatures ^
-XX:+FlightRecorder ^
-XX:FlightRecorderOptions=defaultrecording=true,dumponexit=true,dumponexitpath=daogen.jfr ^
%DEBUG% ^
net.rothsmith.dao.runner.DaoGenRunner ^
--dbpassword=jobsched ^
--dbuser=jobsched ^
--deffile=/workspaces/daogen/jobsupport/src/main/resources/daodefs.xml ^
--deftpl=/daodefs.vm ^
--dtopkg=com.fcci.jobsupport.job ^
--dtotpl=/dto.vm ^
--driverclass=oracle.jdbc.driver.OracleDriver ^
--jdbcurl=jdbc:oracle:thin:@ldap://oradsqa.fcci-group.com:10389/dv01,cn=oraclecontext,dc=fcci-group,dc=com ^
--junittpl=dbutils/junit.vm ^
--outputdir=/workspaces/daogen/jobsupport/src/main/java ^
--schema=JOBSCHED ^
--names=JOB ^
--types=TABLE ^
--testoutputdir=/workspaces/daogen/jobsupport/src/test/java ^
--testpkg=com.fcci.jobsupport.job ^
--dbupropstpl=dbutils/propsfile.vm ^
--propsfile=classpath:unitils.properties ^
--jndiname=java:/comp/env/jdbc/jobsched ^
--dbupropsdir=/workspaces/daogen/jobsupport/src/main/resources 
 
endlocal
