package chen.springframework.beans.factory.xml;

import org.w3c.dom.Document;

import java.io.InputStream;

public interface DocumentLoader {

    Document loadDocument(InputStream inputStream);

}
