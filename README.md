# Csapat4 - Empire strategy game
***
## Requirements:
* Java >=8
* Maven >=3.6.3
* Windows >=10

## Maven commands:
Build:
>mvn clean install

Test:
>mvn test

Test coverage riport: (target/site/jacoco/index.html)
>mvn test jacoco:report

Release:
>mvn clean compile assembly:single

##Game start after build command:
Double-click on the jar or in the target/executable folder run: java -jar empire-[VERSION].jar


##TODO:
* junit
* pár kép cserélése
* felesleges actionök 

##Last updates: JAVÍTÁSOK
* help-ben szöveg
* rejtélyes üres kocka
* termelés míért nem megy?
* game over után ne lehessen semmit csinálni (kattintás esemény)
* sárkány ne daráljon hanem csak átrepüljön



