document.

-----------------------------------------------
H2 database
-----------------------------------------------
dump:

script
# SCRIPT TO './qifu3_h2.sql'

# init database file
java -classpath h2-1.4.200.jar org.h2.tools.RunScript -url jdbc:h2:file:~/qifu3;AUTO_SERVER=TRUE -script qifu3_h2.sql


# dump database file
# java org.h2.tools.Script -url <url> -user <user> -password <password>
java -classpath h2-1.4.200.jar org.h2.tools.RunScript -url jdbc:h2:file:~/qifu3;AUTO_SERVER=TRUE -user sa -script h2_dump_script.sql


