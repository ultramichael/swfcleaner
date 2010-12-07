package fr.epsi.csii3.secu.business.dump;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import macromedia.abc.BytecodeBuffer;
import macromedia.abc.Decoder;
import macromedia.abc.DecoderException;
import flash.swf.Header;
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
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void header(Header h) {
		// data here
		/*System.out.println("C : "+h.compressed);
		System.out.println("S : "+h.size);
		System.out.println("V : "+h.version);*/
		super.header(h);
	}
	
	

	
	@Override
	public void doABC(DoABC tag) {
		//System.out.println("TagABC : "+tag.name);
		AbcLoader l = new AbcLoader(tag.abc);
		l.process();
	
		if(l.instanceNames != null) {
			for(String s : l.instanceNames) {
				//System.out.println('['+s+']');
			}
		}
		if(l.methods != null) {
			for(MethodInfo m : l.methods) {
				methods.put('<'+m.getClassName()+">"+m.getName(), l.methodBodiesStrings.get(m));
				/*String body = "";
				for(byte b : l.methodBodies.get(m))
					body += Integer.toHexString(Integer.parseInt(Byte.toString(b)) + 128) + " ";
				System.out.println("    "+body);*/
				//System.out.println(m.getAbcCode());
			}
		}

		super.doABC(tag);
	}
}
