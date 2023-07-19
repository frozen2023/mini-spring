package chen.springframework.beans.factory.xml;

import cn.hutool.core.util.XmlUtil;
import org.w3c.dom.Document;

import java.io.InputStream;

public class DefaultDocumentLoader implements DocumentLoader {

    @Override
    public Document loadDocument(InputStream inputStream) {
        return XmlUtil.readXML(inputStream);
    }

}
