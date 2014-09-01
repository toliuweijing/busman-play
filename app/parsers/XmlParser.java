package parsers;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

/**
 * Created by weijingliu on 9/1/14.
 */
public class XmlParser<T> {
    private JAXBContext mContext;

    public XmlParser(Class<T> kclass) throws JAXBException {
        mContext = JAXBContext.newInstance(kclass);
    }

    T process(String xml) throws JAXBException {
        Unmarshaller u = mContext.createUnmarshaller();
        T t = (T) u.unmarshal(new StringReader(xml));
        return t;
    }
}
