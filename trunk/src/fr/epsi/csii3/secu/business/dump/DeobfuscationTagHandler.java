package fr.epsi.csii3.secu.business.dump;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import flash.swf.Header;
import flash.swf.TagDecoder;
import flash.swf.TagHandler;
import flash.swf.tags.DoABC;
import fr.epsi.csii3.secu.business.dump.AbcLoader.MethodInfo;

public class DeobfuscationTagHandler extends TagHandler {
	
	public void parseSwf(URL fileUrl) {
		try {
			InputStream input = fileUrl.openStream();
			TagDecoder t = new TagDecoder(input, fileUrl);
			t.parse(this);
		} catch (IOException e) {
			e.printStackTrace();
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
				System.out.println('['+s+']');
			}
		}
		if(l.methods != null) {
			for(MethodInfo m : l.methods) {
				System.out.println('<'+m.getClassName()+">"+m.getName());
				//System.out.println(m.getAbcCode());
			}
		}

		super.doABC(tag);
	}
}
