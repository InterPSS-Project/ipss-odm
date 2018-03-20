package org.ieee.odm.psse;

import static org.junit.Assert.assertTrue;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.ieee.odm.adapter.IODMAdapter.NetType;
import org.ieee.odm.adapter.psse.PSSEAdapter;
import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.dstab.DStabModelParser;
import org.ieee.odm.schema.DStabBusXmlType;
import org.junit.Test;

public class PSSEV30_Dyn_Load_Test {
	
	@Test
	public void ieee9_ODM_Dstab_1981Exc_test() throws ODMException{
		final LogManager logMgr = LogManager.getLogManager();
		Logger logger = Logger.getLogger("IEEE ODM Logger");
		logger.setLevel(Level.INFO);
		logMgr.addLogger(logger);
		
		PSSEAdapter adapter = new PSSEAdapter(PsseVersion.PSSE_30);
		assertTrue(adapter.parseInputFile(NetType.DStabNet, new String[]{
				"testData/psse/IEEE9Bus/ieee9.raw",
				"testData/psse/IEEE9Bus/ieee9_dyn_Load_ACMotor.dyr"
		}));
		DStabModelParser dstabParser =(DStabModelParser) adapter.getModel();
		dstabParser.stdout();
		
		DStabBusXmlType bus5 = dstabParser.getDStabBus("Bus1");
		
		
	
	
	}
	
	

}
