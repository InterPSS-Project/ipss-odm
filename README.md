ipss-odm
========

InterPSS ODM development

## Setup the maven-based project in VS Code
First, you need to install maven on your computer. For macos users, you can simply do this by `brew install maven` 

Then you can run the following command to install the dependent JAR `lib/ipss_core.jar`  into your local Maven repository (~/.m2/repository):

```sh
mvn install:install-file -Dfile=lib/ipss_core.jar \
                         -DgroupId=com.interpss \
                         -DartifactId=ipss_core \
                         -Dversion=1.0 \
                         -Dpackaging=jar

```
Lastly, you need to verify if the maven-based project setup is done correctly by running `cd <path-to-ipss-odm>`, followed by `mvn clean test` in the terminal
You should see the testcases are run and most of them passed, except for two test cases shown below (as of Feb 19, 2025):

[ERROR] Failures: 
[ERROR]   BPA_500kv_NoDC_Test.bpaTestCase:18
[ERROR]   IEEE9cn_DStabTest.testCaseNew:39






