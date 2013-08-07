package org.ieee.odm.pwd;

import static org.junit.Assert.assertTrue;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.ieee.odm.adapter.IODMAdapter;
import org.ieee.odm.adapter.pwd.PowerWorldAdapter;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.schema.XformerZTableXmlType.XformerZTableItem;
import org.junit.Test;

public class sixBus_XFCorrection_Test {
	
	@Test
	public void testXFCorrection(){
	final LogManager logMgr = LogManager.getLogManager();
	Logger logger = Logger.getLogger("IEEE ODM Logger");
	logger.setLevel(Level.INFO);
	logMgr.addLogger(logger);
	
	IODMAdapter adapter = new PowerWorldAdapter();
	assertTrue(adapter.parseInputFile("testdata/pwd/SixBusTestCase_v2_xfmCorrTable.aux"));
	
	AclfModelParser parser = (AclfModelParser)adapter.getModel();
	System.out.println(parser.toXmlDoc());
	
	assertTrue(parser.getNet().getXfrZTable().getXformerZTableItem().size()==1);
	XformerZTableItem item= parser.getNet().getXfrZTable().getXformerZTableItem().get(0);
	assertTrue(item.getLookup().size()==3);
	assertTrue(item.getLookup().get(0).getTurnRatioShiftAngle()==-25.0);
	assertTrue(item.getLookup().get(1).getTurnRatioShiftAngle()==0.0);
	assertTrue(item.getLookup().get(2).getTurnRatioShiftAngle()==25.0);
	
	assertTrue(item.getLookup().get(0).getScaleFactor()==1.978);
	assertTrue(item.getLookup().get(1).getScaleFactor()==1.0);
	assertTrue(item.getLookup().get(2).getScaleFactor()==1.978);
	}
}
