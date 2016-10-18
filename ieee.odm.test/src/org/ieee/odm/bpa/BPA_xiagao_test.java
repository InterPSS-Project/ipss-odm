package org.ieee.odm.bpa;

import static org.interpss.CorePluginFunction.AclfParser2AclfNet;
import static org.junit.Assert.*;

import org.apache.commons.math3.complex.Complex;
import org.ieee.odm.adapter.IODMAdapter;
import org.ieee.odm.adapter.bpa.BPAAdapter;
import org.ieee.odm.adapter.bpa.BPAResultAdapter;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.model.dstab.DStabModelParser;
import org.interpss.IpssCorePlugin;
import org.interpss.display.AclfOutFunc;
import org.interpss.mapper.odm.ODMAclfNetMapper;
import org.interpss.numeric.datatype.Unit.UnitType;
import org.junit.Test;

import com.interpss.CoreObjectFactory;
import com.interpss.core.aclf.AclfBranch;
import com.interpss.core.aclf.AclfBus;
import com.interpss.core.aclf.AclfNetwork;
import com.interpss.core.algo.AclfMethod;
import com.interpss.core.algo.LoadflowAlgorithm;
import com.interpss.core.net.Branch;

public class BPA_xiagao_test {
	@Test
	public void testDStab() throws Exception {

		IODMAdapter adapter = new BPAAdapter();
		assertTrue(adapter.parseInputFile(IODMAdapter.NetType.DStabNet,
				new String[] { "testdata/bpa/2013huadongxiagao.dat", "testdata/bpa/2013年稳定_夏高.swi" }));

		DStabModelParser parser = (DStabModelParser) adapter.getModel();
		parser.stdout();
	}

	//@Test
//	public void testAclf() throws Exception {
	//	IpssCorePlugin.init();
	//	IODMAdapter adapter = new BPAAdapter();
	//	adapter.parseInputFile("testdata/bpa/2013huadongxiagao.dat");

	//	AclfModelParser parser = (AclfModelParser) adapter.getModel();

	//	AclfNetwork aclfNet = AclfParser2AclfNet.fx(parser, ODMAclfNetMapper.XfrBranchModel.InterPSS);
		//BPAResultAdapter resultAdapter=new BPAResultAdapter(aclfNet);
		
		//swing bus
		//aclfNet.getBus("Bus2527").setGenP(1.441);
		//aclfNet.getBus("Bus2527").setGenQ(1.9582);
		//resultAdapter.parseInputFile("testdata/bpa/2013XiaGaoResult.txt");
		//LoadflowAlgorithm algo = CoreObjectFactory.createLoadflowAlgorithm(aclfNet);
		 //use the loadflow algorithm to perform loadflow calculation
		//algo.setLfMethod(AclfMethod.PQ);
		//algo.setMaxIterations(4);
		//algo.loadflow();
		
		//algo.setLfMethod(AclfMethod.NR);
		//algo.setMaxIterations(30);
		//algo.loadflow();
		
//		for(AclfBranch branch:aclfNet.getBranchList()){
//			System.out.println(branch.toString());
//		}
//		double genP = 0;
//		for(AclfBus bus :aclfNet.getBusList()){
//			//System.out.println(bus.toString());
//			genP=genP+bus.getGenP();
			
//		}
//		System.out.println(genP);
//		System.out.println(aclfNet.totalLoad(UnitType.PU));
		//System.out.println(aclfNet.getYMatrix());
		
		//System.out.println(AclfOutFunc.loadFlowSummary(aclfNet));
		
//		for(Branch branch:aclfNet.getBus("Bus6119").getToBranchList()){
//			AclfBranch bra=(AclfBranch) branch;
//			System.out.println(((AclfBus)bra.getFromBus()).getName()+"    "+bra.current(UnitType.PU));
//		}
//		parser.stdout();
//		int count=0;
//		for (AclfBus bus:aclfNet.getBusList()){
//			count++;
//			System.out.println(bus.getId()+bus.getName()+"----导纳"+bus.yii()+";有功P"+(-bus.getLoadP()+bus.getGenP())+";无功Q"+(-bus.getLoadQ()+bus.getGenQ()));
//			System.out.println(bus.getJii(null));
//	}
		
//		
	//}
}
