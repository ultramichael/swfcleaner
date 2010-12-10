package fr.epsi.csii3.secu.business.dump;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import flash.swf.TagDecoder;
import flash.swf.TagHandler;
import flash.swf.tags.DoABC;
import fr.epsi.csii3.secu.business.dump.AbcLoader.MethodInfo;

public class DeobfuscationTagHandler extends TagHandler {
	
	private Map<String, String> methods = new HashMap<String, String>();
	
	public Map<String, String> parseSwf(URL fileUrl) {
		try {
			InputStream input = fileUrl.openStream();
			TagDecoder t = new TagDecoder(input, fileUrl);
			t.parse(this);
			return this.methods;
		} catch (Exception e) {
			//e.printStackTrace();
			return this.methods;
		}
	}
	
	

	@Override
	public void doABC(DoABC tag) {
		AbcLoader l = new AbcLoader(tag.abc);
		l.process();
	
		if(l.methods != null) {
			for(MethodInfo m : l.methods) {
				methods.put('<'+m.getClassName()+">"+m.getName(), l.methodBodiesStrings.get(m));
			}
		}

		super.doABC(tag);
	}
}
