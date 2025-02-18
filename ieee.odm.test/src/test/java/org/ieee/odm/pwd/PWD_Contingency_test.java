package org.ieee.odm.pwd;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.ieee.odm.ODMFileFormatEnum;
import org.ieee.odm.ODMObjectFactory;
import org.ieee.odm.adapter.IODMAdapter;
import org.ieee.odm.adapter.pwd.PWDAdapterForContingency;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.schema.BranchChangeRecSetXmlType;
import org.ieee.odm.schema.BranchChangeRecXmlType;
import org.ieee.odm.schema.BranchOutageEnumType;
import org.ieee.odm.schema.NetModificationXmlType;
import org.junit.Test;

public class PWD_Contingency_test {
	
		@Test
		public void ContingencySample_test() throws Exception {
			IODMAdapter adapter = ODMObjectFactory.createODMAdapter(ODMFileFormatEnum.PWD_Contingency);
			assertTrue(adapter.parseInputFile("testdata/pwd/ctg_sample.AUX"));
			AclfModelParser parser=(AclfModelParser) adapter.getModel();
			//parser.stdout();
			
            
			//check network data
			NetModificationXmlType netModify=(NetModificationXmlType) parser.getStudyCase().getModificationList().getModification().get(0);
			List<BranchChangeRecSetXmlType> branchChangeRecSetList=netModify.getBranchChangeRecSet();
			//rec set
			BranchChangeRecSetXmlType braRecSet=branchChangeRecSetList.get(0);
			//System.out.println("RecSetList.size()="+branchChangeRecSetList.size());
			assertTrue(braRecSet.getBranchChangeRec().size()==5);
			
			//rec element
			BranchChangeRecXmlType braCtgElement=braRecSet.getBranchChangeRec().get(0);
			assertTrue(braCtgElement.getBranchId().equals("Bus514_to_Bus7512_cirId_1"));
			assertTrue(braCtgElement.getOutage()==BranchOutageEnumType.OPEN);
		}

}
