ipss-odm
========

## Maven-based Development

Starting from 2025/03, InterPSS development is based on Maven. ipss-odm repo has the following active projects:

```
    ieee.odm_pass
    ieee.odm.schema
    ipss.odm.test
```

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


## Generate xml java classes from schema xsd file
1. In Eclipse, you will need to first check the `pom.xml` file under `ieee.odm.schema` and make sure it includes these [jaxb packages](/ieee.odm.schema/fig/1_pom.png).
2. Right click the `pom.xml` file, then maven-->update project to get the necessary packages to install or update
2. Install the JAXB packages in Eclipse-->Help-->Install new software--> [search "JAXB"](/ieee.odm.schema/fig/2_JAXB%20%20packages.png)
3. After successfully installing the JAXB plugins, you will restart. You find the `ODMSchema.xsd` file under the  `schema` folder. Right click-->Generate-->[to start the configuration](/ieee.odm.schema/fig/3_Generate.png) 
4. Configure the generation using the [recommended configuration](/ieee.odm.schema/fig/4_configuration.png) 



