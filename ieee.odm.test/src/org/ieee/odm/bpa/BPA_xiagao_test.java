package org.ieee.odm.bpa;

import static org.interpss.CorePluginFunction.AclfParser2AclfNet;
import static org.junit.Assert.*;

import org.ieee.odm.adapter.IODMAdapter;
import org.ieee.odm.adapter.bpa.BPAAdapter;
import org.ieee.odm.adapter.bpa.BPAResultAdapter;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.model.dstab.DStabModelParser;
import org.interpss.IpssCorePlugin;
import org.interpss.display.AclfOutFunc;
import org.interpss.mapper.odm.ODMAclfNetMapper;
import org.junit.Test;

import com.interpss.CoreObjectFactory;
import com.interpss.core.aclf.AclfBranch;
import com.interpss.core.aclf.AclfBus;
import com.interpss.core.aclf.AclfNetwork;
import com.interpss.core.algo.LoadflowAlgorithm;

public class BPA_xiagao_test {
//	@Test
//	public void testDStab() throws Exception {
//
//		IODMAdapter adapter = new BPAAdapter();
//		assertTrue(adapter.parseInputFile(IODMAdapter.NetType.DStabNet,
//				new String[] { "testdata/bpa/2013huadongxiagao.dat", "testdata/bpa/2013ÄêÎÈ¶¨_ÏÄ¸ß.swi" }));
//
//		DStabModelParser parser = (DStabModelParser) adapter.getModel();
//		parser.stdout();
//	}

	@Test
	public void testAclf() throws Exception {
		IpssCorePlugin.init();
		IODMAdapter adapter = new BPAAdapter();
		adapter.parseInputFile("testdata/bpa/2013huadongxiagao.dat");

		AclfModelParser parser = (AclfModelParser) adapter.getModel();

		AclfNetwork aclfNet = AclfParser2AclfNet.fx(parser, ODMAclfNetMapper.XfrBranchModel.InterPSS);
		BPAResultAdapter resultAdapter=new BPAResultAdapter(aclfNet);
		resultAdapter.parseInputFile("testdata/bpa/2013XiaGaoResult.txt");
//		LoadflowAlgorithm algo = CoreObjectFactory.createLoadflowAlgorithm(aclfNet);
//		//algo.setMaxIterations(2);
//		// use the loadflow algorithm to perform loadflow calculation
//		algo.loadflow();
//		for(AclfBranch branch:aclfNet.getBranchList()){
//			System.out.println(branch.toString());
//		}
//		for(AclfBus bus :aclfNet.getBusList()){
//			System.out.println(bus.toString());
//		}
		System.out.println(AclfOutFunc.loadFlowSummary(aclfNet));
//		parser.stdout();
	}
}
