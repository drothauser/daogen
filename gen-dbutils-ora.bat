@echo off
setlocal

cd target

REM java -cp .;daogen-1.0.1-SNAPSHOT.jar;lib/*; com.rothsmith.dao.runner.DaoGenRunner ^
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
com.rothsmith.dao.runner.DaoGenRunner ^
--dbpassword=SHAREDUSER ^
--dbuser=SHAREDUSER ^
--deffile=/workspaces/daogen/wb-web-security/src/main/resources/daodefs.xml ^
--deftpl=/daodefs.vm ^
--dtopkg=com.fcci.wb.web.security ^
--dtotpl=/dto.vm ^
--driverclass=oracle.jdbc.driver.OracleDriver ^
--jdbcurl=jdbc:oracle:thin:@ldap://oradsqa.fcci-group.com:10389/dv01,cn=oraclecontext,dc=fcci-group,dc=com ^
--junittpl=dbutils/junit.vm ^
--outputdir=/workspaces/daogen/wb-web-security/src/main/java ^
--schema=SHAREDUSER ^
--names=APPLICATION%% ^
--types=TABLE ^
--testoutputdir=/workspaces/daogen/wb-web-security/src/test/java ^
--testpkg=com.fcci.wb.web.security ^
--dbupropstpl=dbutils/propsfile.vm ^
--propsfile=classpath:unitils.properties ^
--jndiname=java:/comp/env/jdbc/dv01 ^
--dbupropsdir=/workspaces/daogen/wb-web-security/src/main/resources ^
--sqlstmts="SELECT AO.APPLICATION_OBJECT_NAME,OT.OBJECT_TYPE,G.GROUPDESC,P.PRIVILEGE_DESC,PT.PRIVILEGE_TYPE FROM GROUP_OBJECTS GO,APPLICATION_OBJECTS AO, GROUP_OBJECT_PRIVILEGES GOP, PRIVILEGE P, PRIVILEGE_TYPE PT, OBJECT_TYPE OT, APPLICATION A,GROUPS G,shareduser.groupusers grpusers WHERE GO.APPLICATION_OBJECTID = AO.APPLICATION_OBJECTID AND GOP.GROUP_OBJECTID = GO.GROUP_OBJECTID AND GOP.PRIVILEGEID = P.PRIVILEGEID AND P.PRIVILEGE_TYPEID = PT.PRIVILEGE_TYPEID AND AO.OBJECT_TYPEID = OT.OBJECT_TYPEID AND AO.APPLICATIONID = A.APPLICATIONID AND GO.GROUPID = G.GROUPID and grpusers.GROUPID=G.GROUPID and grpusers.USERIDCODE=8599 order by application_object_name"  
 
endlocal
