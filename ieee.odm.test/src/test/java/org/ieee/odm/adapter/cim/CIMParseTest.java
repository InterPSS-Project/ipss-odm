package org.ieee.odm.adapter.cim;

import org.ieee.odm.adapter.cim.parser.CIMRdfParser;
import org.apache.jena.rdf.model.Model;
import org.junit.Test;

public class CIMParseTest {
    private static final String TD = "testdata/cim/";
    
    @Test
    public void testMiniGridTPOnly() throws Exception {
        String content = new String(java.nio.file.Files.readAllBytes(
            java.nio.file.Paths.get(TD + "MiniGrid_NB_TP_V3.xml")));
        
        CIMRdfParser parser = new CIMRdfParser();
        Model model = parser.parseString(content);
        
        System.err.println("Model size: " + model.size());
        
        // List all types
        var typeProp = model.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
        var iter = model.listStatements(null, typeProp, (org.apache.jena.rdf.model.RDFNode)null);
        java.util.Set<String> types = new java.util.TreeSet<>();
        while (iter.hasNext()) {
            var obj = iter.next().getObject();
            if (obj.isURIResource()) types.add(obj.asResource().getURI());
        }
        System.err.println("Types: " + types);
        
        // List some subjects
        var subjects = model.listSubjects();
        int count = 0;
        while (subjects.hasNext() && count < 5) {
            var s = subjects.next();
            if (s.isURIResource()) {
                System.err.println("  Subject: " + s.getURI());
                count++;
            }
        }
    }
}
