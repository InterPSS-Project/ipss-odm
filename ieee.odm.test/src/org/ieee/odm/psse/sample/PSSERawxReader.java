package org.ieee.odm.psse.sample;

import java.io.FileReader;
import java.io.IOException;

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
        } catch (IOException e) {
            e.printStackTrace();
        }

	}

}
