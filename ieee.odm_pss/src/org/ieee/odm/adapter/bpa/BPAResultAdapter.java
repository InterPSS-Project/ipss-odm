package org.ieee.odm.adapter.bpa;

import java.util.StringTokenizer;

import org.ieee.odm.adapter.AbstractODMAdapter;
import org.ieee.odm.adapter.bpa.lf.BPABusRecord;
import org.ieee.odm.common.IFileReader;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.base.ODMModelStringUtil;

import com.interpss.core.aclf.AclfBus;
import com.interpss.core.aclf.AclfNetwork;

public class BPAResultAdapter extends AbstractODMAdapter{
	private AclfNetwork aclfNet;
	private long n;
	public BPAResultAdapter(AclfNetwork aclfNet){
		this.aclfNet=aclfNet;
	}
	
	@Override
	protected IODMModelParser parseInputFile(IFileReader din, String encoding) throws ODMException {
		String str;
		do {
			str = din.readLine(); 
			if (str!=null)
				mapInputLine(str);
		} while (str!=null);	
		return null;
	}

	private void mapInputLine(String str) {
		// TODO Auto-generated method stub
		String[] strAry=getBusVoltageData(str);
		if (strAry[0] != null) {
			try {
				n++;
				String id=BPABusRecord.getBusId(strAry[0]);
				AclfBus bus = aclfNet.getBus(id);
				bus.setVoltageMag(Double.valueOf(strAry[1])/bus.getBaseVoltage()*1000);
				bus.setVoltageAng(Double.valueOf(strAry[2])/180 * 3.1415926);
			} catch (ODMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	protected IODMModelParser parseInputFile(NetType type, IFileReader[] dins, String encoding) throws ODMException {
		// TODO Auto-generated method stub
		return null;
	}
	
	private String[] getBusVoltageData(String str){
		String[] strAry = new String[3];
		String name;
		String voltage;
		//TODO get bus voltage mag and ang
		String str1=ODMModelStringUtil.getStringReturnEmptyString(str,1, 1); 
		if(str1.length()<1){
			return strAry;
		}
		else{
			StringTokenizer itr = new StringTokenizer(str);
			//name
			name=itr.nextToken();
			voltage=itr.nextToken();
			strAry[0]=name+voltage;
			
			//angle
			String str2=itr.nextToken();
			if (str2.length() > 10) {
				strAry[1]=str2.substring(0,str2.length()-9);
				strAry[2] = str2.substring(str2.length() - 6, str2.length() - 1);
			} else {
				strAry[1]=str2.substring(0,str2.length()-3);
				String angle = itr.nextToken();
				strAry[2] = angle.substring(0, angle.length() - 1);
			}
			
			//mag
//			int count = itr.countTokens()-2;
//			for (int i = 0; i < count; i++){
//				itr.nextToken();
//			}
//				
//			strAry[1]=itr.nextToken().substring(0,5);
		}
		//System.out.println(strAry[0]);
		return strAry;
	}
}
