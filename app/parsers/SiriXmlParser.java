package parsers;

import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import uk.org.siri.siri.MonitoredVehicleJourneyStructure;
import uk.org.siri.siri.Siri;

import java.io.StringReader;

import org.w3c.dom.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;


public class SiriXmlParser extends XmlParser<Siri> {

    public SiriXmlParser() throws JAXBException {
        super(Siri.class);
    }

    //TODO: Currently, XmlParser stops at ExtensionStructure under MonitoredCall, which should be replaced
    //with SiriDistanceExtension.
    @Override
    public Siri process(String xml) throws JAXBException {
        return super.process(xml);
    }

    private static String stripOffNamespaces(String xml) {
        return xml.replaceAll(" xmlns(?:.*?)?=\".*?\"", "");
    }

    public static SiriDistanceExtension getDistanceExtension(MonitoredVehicleJourneyStructure j) throws JAXBException {
        Element e = (Element) j.getMonitoredCall().getExtensions().getAny();
        DOMImplementationLS impl = (DOMImplementationLS) e.getOwnerDocument().getImplementation();

        LSSerializer lsSerializer = impl.createLSSerializer();
        String content = lsSerializer.writeToString(e);
        content = stripOffNamespaces(content);

        Unmarshaller unmarshaller = JAXBContext.newInstance(SiriDistanceExtension.class).createUnmarshaller();
        return (SiriDistanceExtension) unmarshaller.unmarshal(new StringReader(content));
    }
}
