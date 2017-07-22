package tools;

import java.io.File;
import java.net.URL;

import org.jsonschema2pojo.DefaultGenerationConfig;
import org.jsonschema2pojo.GenerationConfig;
import org.jsonschema2pojo.Jackson2Annotator;
import org.jsonschema2pojo.SchemaGenerator;
import org.jsonschema2pojo.SchemaMapper;
import org.jsonschema2pojo.SchemaStore;
import org.jsonschema2pojo.rules.RuleFactory;

import com.sun.codemodel.JCodeModel;

public class CodeGenerator {

	public static void main(String[] args) throws Exception {
		JCodeModel codeModel = new JCodeModel();

		URL source = new URL("file:///ipss.schema/GRGv1.0_schema.json");

		GenerationConfig config = new DefaultGenerationConfig() {
			@Override
			public boolean isGenerateBuilders() { // set config option by overriding method
				return true;
			}
		};

		SchemaMapper mapper = new SchemaMapper(
				new RuleFactory(config, 
							    new Jackson2Annotator(config), 
							    new SchemaStore()), 
				new SchemaGenerator());
		mapper.generate(codeModel, "ClassName", "com.example", source);

		codeModel.build(new File("tmp/output.txt"));
	}

}
