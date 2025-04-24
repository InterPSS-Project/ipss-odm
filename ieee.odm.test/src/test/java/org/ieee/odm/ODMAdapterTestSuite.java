package org.ieee.odm;

import org.ieee.odm.ge_pslf.PSLFCMPLDWDataParserTest;
import org.ieee.odm.ge_pslf.PSLF_CMPLDW_Mapper_Test;
import org.ieee.odm.ieeecdf.IEEECDF_ODMTest;
import org.ieee.odm.odm_xml.OdmXml_ODMTest;
import org.ieee.odm.opf.OpfSample_3Bus_ODMTest;
import org.ieee.odm.opf.OpfSample_3Bus_ODMTest_stephen;
import org.ieee.odm.opf.ProcessOPFData_ODMTest;
import org.ieee.odm.opf.matpower.OPF_Matpower_ODMTest;
import org.ieee.odm.psse.raw.PSSE_HearderVer_ODMTest;
import org.ieee.odm.psse.raw.v30.PSSEV30_Dyn_Load_Test;
import org.ieee.odm.psse.raw.v30.PSSEV30_GuideSampleTest;
import org.ieee.odm.psse.raw.v30.PSSEV30_IEEE39_Acsc_Test;
import org.ieee.odm.psse.raw.v30.PSSEV30_IEEE9_Acsc_Test;
import org.ieee.odm.psse.raw.v30.PSSEV30_IEEE9_Dstab_Test;
import org.ieee.odm.psse.raw.v30.PSSEV30_ODMTest;
import org.ieee.odm.psse.raw.v30.PSSEV30_RealCase_ODMTest;
import org.ieee.odm.psse.raw.v30.PSSEV30_SegmentTest;
import org.ieee.odm.psse.raw.v30.PSSE_Dyn_Exciter_Test;
import org.ieee.odm.psse.raw.v30.PSSE_V30_3WindingXfrTest;
import org.ieee.odm.psse.raw.v31.PSSEV31_AESOTest;
import org.ieee.odm.psse.raw.v33.PSSEV33_IEEE9_Test;
import org.ieee.odm.psse.raw.v33.PSSEV33_Kundur2Area_LCCHVDC_Test;
import org.ieee.odm.psse.raw.v33.PSSEV33_Sample_savnw_Test;
import org.ieee.odm.psse.raw.v35.PSSEV35_Kundur2Area_VSCHVDC_Test;
import org.ieee.odm.psse.raw.v36.PSSEV31_v36_IEEE9_Test;
import org.ieee.odm.psse.raw.v36.PSSEV31_v36_Sample_Test;
import org.ieee.odm.pwd.LineStringParserTest;
import org.ieee.odm.pwd.PWD_IEEE14Bus_ODMTest;
import org.ieee.odm.pwd.PWD_XfrControl_Test;
import org.ieee.odm.pwd.PwdAdapterTest;
import org.ieee.odm.ucte.UCTE_ODMTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	FuncTest_ODMTest.class,
	
	IEEECDF_ODMTest.class,
	
	UCTE_ODMTest.class,
	//XBeanUCTE_ODMTest.class,
	
//	org.ieee.odm.psse.old.XBeanPSSEV30_ODMTest.class,
//	org.ieee.odm.psse.old.XBeanPSSEV30_NEISO_ODMTest.class,
//	org.ieee.odm.psse.old.XBeanPSSEV26_ODMTest.class,
//	org.ieee.odm.psse.old.XBeanPSSEV30_GuideSampleTest.class,
//	org.ieee.odm.psse.old.XBeanPSSEV30_SegmentTest.class,

	PSSE_HearderVer_ODMTest.class,
	PSSEV30_ODMTest.class,
	PSSEV30_RealCase_ODMTest.class,
	PSSEV30_GuideSampleTest.class,
	PSSEV30_SegmentTest.class,
	PSSEV30_IEEE39_Acsc_Test.class,
	PSSEV30_IEEE9_Acsc_Test.class,
	PSSEV30_IEEE9_Dstab_Test.class,
	PSSEV30_Dyn_Load_Test.class,
	PSSE_V30_3WindingXfrTest.class,
	PSSE_Dyn_Exciter_Test.class,
	
	PSSEV31_AESOTest.class,

	PSSEV33_IEEE9_Test.class,
	PSSEV33_Sample_savnw_Test.class,
	PSSEV33_Kundur2Area_LCCHVDC_Test.class,

	PSSEV35_Kundur2Area_VSCHVDC_Test.class,

	PSSEV31_v36_IEEE9_Test.class,
	PSSEV31_v36_Sample_Test.class,

	
	PSLF_CMPLDW_Mapper_Test.class,
	PSLFCMPLDWDataParserTest.class,
	
	PWD_IEEE14Bus_ODMTest.class,
	LineStringParserTest.class,
	PwdAdapterTest.class,
	PWD_XfrControl_Test.class,
	
	OpfSample_3Bus_ODMTest.class,
	OPF_Matpower_ODMTest.class,
	ProcessOPFData_ODMTest.class,
	OpfSample_3Bus_ODMTest_stephen.class,

	OdmXml_ODMTest.class,
})
public class ODMAdapterTestSuite {
}
