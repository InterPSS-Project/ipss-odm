package org.ieee.odm.psse.sample;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.ieee.odm.adapter.psse.bean.PSSESchema;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PSSERawxReader {

	public static void main(String[] args) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        
        try (FileReader reader = new FileReader("testdata/psse/json/sample.rawx")) {
            PSSESchema root = gson.fromJson(reader, PSSESchema.class);

            // Print parsed data
            System.out.println(root);
            System.out.println(root.getNetwork().getInterface_());
            System.out.println(root.getNetwork().getInterface_().getFieldAry()[0]);
            
            List<Object> list = root.getNetwork().getInterface_().getData();
            List<Object> strList = (List<Object>)list.get(0);
            Object[] objAry = strList.toArray();
            System.out.println(objAry[0]);
            System.out.println(objAry[1].getClass().getSimpleName());
        } catch (IOException e) {
            e.printStackTrace();
        }

	}

}
