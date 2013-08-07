package org.ieee.odm.pwd;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses({
	CustomStringTest.class,
	PWD_Contingency_test.class,
	PWD_IEEE14Bus_ODMTest.class,
	PWD_XfrControl_Test.class,
	LineStringParserTest.class,
	PwdAdapterTest.class,
})
public class PWDTestSuite {
	
}
