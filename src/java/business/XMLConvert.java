/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business;

import java.lang.Exception;
import components.data.*;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Tushar
 */

//Class Used to serialize and deserialize the xml to object and from object to xml
public class XMLConvert {

    String contextPath;

    //Initialize the contextPath for the service
    public XMLConvert(String contextPath) {
        this.contextPath = contextPath;
    }

    //Used to create an XML String from the Document Object
    private static String createXML(Document doc) {
        try {

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);

            // Output to console for testing
            // StreamResult result = new StreamResult(System.out);
            transformer.transform(source, result);

            return sw.toString();
        } catch (TransformerException ex) {
            Logger.getLogger(XMLConvert.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    //Used to convert XML from the list of Appointment Object
    public String createAppointmentListXML(List<Appointment> objs) {

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element AppointmentList = doc.createElement("AppointmentList");
            doc.appendChild(AppointmentList);

            for (Appointment obj : objs) {
                AppointmentList.appendChild(createAppointmentElement(doc, obj));
            }
            return createXML(doc);

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XMLConvert.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    //Used to create XML format of the Appointment node
    public Element createAppointmentElement(Document doc, Appointment a) {
        Element appointment = doc.createElement("appointment");

        appointment.setAttribute("id", a.getId());
        appointment.setAttribute("date", a.getApptdate().toString());
        appointment.setAttribute("time", a.getAppttime().toString());

        Element uri = doc.createElement("uri");
        uri.setTextContent(contextPath + a.getId());
        appointment.appendChild(uri);

        appointment.appendChild(createPatientElement(doc, a.getPatientid()));

        appointment.appendChild(createPhlebotomistElement(doc, a.getPhlebid()));

        appointment.appendChild(createPSCElement(doc, a.getPscid()));

        appointment.appendChild(createAllLabTestsElement(doc, a.getAppointmentLabTestCollection()));

        return appointment;

    }

    //Used to create XML format of the Patient node
    public Element createPatientElement(Document doc, Patient p) {
        Element rootElement = doc.createElement("patient");
        rootElement.setAttribute("id", p.getId());

        Element uri = doc.createElement("uri");
        rootElement.appendChild(uri);

        // name elements
        Element name = doc.createElement("name");
        name.setTextContent(p.getName());
        rootElement.appendChild(name);

        //address  elements
        Element address = doc.createElement("address");
        address.setTextContent(p.getAddress());
        rootElement.appendChild(address);

        //insurance elements
        Element insurance = doc.createElement("insurance");
        insurance.setTextContent(String.valueOf(p.getInsurance()));
        rootElement.appendChild(insurance);

        //address  elements
        Element dob = doc.createElement("dob");
        dob.setTextContent(p.getDateofbirth().toString());
        rootElement.appendChild(dob);

        return rootElement;
    }

    //Used to create XML format of the Phlebotomist node
    public Element createPhlebotomistElement(Document doc, Phlebotomist p) {
        Element rootElement = doc.createElement("phlebotomist");
        rootElement.setAttribute("id", p.getId());

        Element uri = doc.createElement("uri");
        rootElement.appendChild(uri);

        Element name = doc.createElement("name");
        name.setTextContent(p.getName());
        rootElement.appendChild(name);

        return rootElement;
    }

    //Used to create XML format of the PSC node
    public Element createPSCElement(Document doc, PSC p) {
        Element rootElement = doc.createElement("psc");
        rootElement.setAttribute("id", p.getId());

        Element uri = doc.createElement("uri");
        rootElement.appendChild(uri);

        Element name = doc.createElement("name");
        name.setTextContent(p.getName());
        rootElement.appendChild(name);

        return rootElement;
    }

    //Used to create XML format of the AllLabTest node
    public Element createAllLabTestsElement(Document doc, List<AppointmentLabTest> objs) {
        Element rootElement = doc.createElement("allLabTests");

        for (AppointmentLabTest obj : objs) {
            rootElement.appendChild(createLabTestsElement(doc, obj));
        }

        return rootElement;
    }

    //Used to create XML format of the LabTest node
    public Element createLabTestsElement(Document doc, AppointmentLabTest l) {

        Element rootElement = doc.createElement("appointmentLabTest");
        rootElement.setAttribute("apptointmentId", l.getAppointmentLabTestPK().getApptid());
        rootElement.setAttribute("dxcode", l.getAppointmentLabTestPK().getDxcode());
        rootElement.setAttribute("labTestId", l.getAppointmentLabTestPK().getLabtestid());

        Element uri = doc.createElement("uri");
        rootElement.appendChild(uri);
        return rootElement;
    }

    //Used to create List of Appointment Object from the String xml
    public List<Appointment> createAppointmentList(String xml) {
        /*
         <appointment date="2004-02-01" id="700" time="11:00:00">
         <uri>
         http://localhost:8080/AppointmentsAPI/ws/Services/Appointments/700
         </uri>
         <patient id="210">
         <uri/>
         <name>Tom Thumb</name>
         <address>31 Westbrook Drive</address>
         <insurance>Y</insurance>
         <dob>1959-09-22</dob>
         </patient>
         <phlebotomist id="100">
         <uri/>
         <name>Dorothea Dix</name>
         </phlebotomist>
         <psc id="500">
         <uri/>
         <name>North Hampton</name>
         </psc>
         <allLabTests>
         <appointmentLabTest apptointmentId="700" dxcode="290.0" labTestId="82088">
         <uri/>
         </appointmentLabTest>
         </allLabTests>
         </appointment>
         */

        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));

            Document doc = db.parse(is);
            NodeList nodes = doc.getElementsByTagName("appointment");

            List<Appointment> as = new ArrayList<>();

            for (int i = 0; i < nodes.getLength(); i++) {
                Element e = (Element) nodes.item(0);
                String id = e.getAttribute("id");
                Date apptdate = Date.valueOf(e.getAttribute("date"));
                Time appttime = Time.valueOf(e.getAttribute("time"));
                Appointment a = new Appointment(id, apptdate, appttime);

                Element patient = (Element) e.getElementsByTagName("patient").item(0);
                //System.err.println("goat123"+patient.getTextContent());
                Patient p = createPatient(patient);
                a.setPatientid(p);

                Element phlebotomist = (Element) e.getElementsByTagName("phlebotomist").item(0);
                Phlebotomist pl = createPhlebotomist(phlebotomist);
                a.setPhlebid(pl);

                Element psc = (Element) e.getElementsByTagName("psc").item(0);
                PSC ps = createPSC(psc);
                a.setPscid(ps);

                Element allLabTests = (Element) e.getElementsByTagName("allLabTests").item(0);
                List<AppointmentLabTest> lsa = createAllLabTests(allLabTests);
                a.setAppointmentLabTestCollection(lsa);

                as.add(a);
            }

            //System.out.print(nodes.item(0).getTextContent());
            return as;

        } catch (SAXException ex) {
            Logger.getLogger(XMLConvert.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XMLConvert.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XMLConvert.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(XMLConvert.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    //Used to create Patient Object from the String xml
    public Patient createPatient(Element patient) throws Exception {
        /*
         <patient id="210">
         <uri/>
         <name>Tom Thumb</name>
         <address>31 Westbrook Drive</address>
         <insurance>Y</insurance>
         <dob>1959-09-22</dob>
         </patient>
         */
        String id = patient.getAttribute("id");
        String name = f(patient, "name");
        String address = f(patient, "address");
        char insurance = f(patient, "insurance").charAt(0);
        Date dob = Date.valueOf(f(patient, "dob"));

        Patient p = new Patient(id, name, address, insurance, dob);

        // Patient p = new Patient(contextPath, contextPath, contextPath, insurance, null);
        return p;
    }

    //Used to create Phlebotomist Object from the String xml
    public Phlebotomist createPhlebotomist(Element phlebotomist) throws Exception {
        /*
         <phlebotomist id="100">
         <uri/>
         <name>Dorothea Dix</name>
         </phlebotomist>
         */
        String id = phlebotomist.getAttribute("id");
        String name = f(phlebotomist, "name");

        Phlebotomist p = new Phlebotomist(id, name);
        return p;
    }

    //Used to create PSC Object from the String xml
    public PSC createPSC(Element psc) throws Exception {
        /*
         <psc id="500">
         <uri/>
         <name>North Hampton</name>
         </psc>
         */
        String id = psc.getAttribute("id");
        String name = f(psc, "name");
        PSC p = new PSC(id, name);
        return p;
    }

    //Used to create List of AppointmentLabTest Object from the String xml
    public List<AppointmentLabTest> createAllLabTests(Element allLabTests) throws Exception {
        /*
         <allLabTests>
         <appointmentLabTest apptointmentId="700" dxcode="290.0" labTestId="82088">
         <uri/>
         </appointmentLabTest>
         </allLabTests>
         */
        List<AppointmentLabTest> s = new ArrayList<>();

        NodeList nodes = allLabTests.getElementsByTagName("appointmentLabTest");

        for (int i = 0; i < nodes.getLength(); i++) {
            Element e = (Element) nodes.item(i);
            s.add(createLabTest(e));
        }
        return s;
    }

    //Used to create List of LabTest Object from the String xml
    public AppointmentLabTest createLabTest(Element labTest) throws Exception {
        /*
         <appointmentLabTest apptointmentId="700" dxcode="290.0" labTestId="82088">
         <uri/>
         </appointmentLabTest>
         */

        String apptointmentId = labTest.getAttribute("apptointmentId");
        String dxcode = labTest.getAttribute("dxcode");
        String labTestId = labTest.getAttribute("labTestId");

        AppointmentLabTestPK ap = new AppointmentLabTestPK(apptointmentId, labTestId, dxcode);

        AppointmentLabTest a = new AppointmentLabTest(ap);

        return a;
    }

    //Used to create Appointment Object from the String xml
    public Appointment createAppointment(String xml) throws Exception {
        /*
         <?xml version="1.0" encoding="utf-8" standalone="no"?>
         <appointment>
         <date>2016-12-30</date>
         <time>10:00</time>
         <patientId>220</patientId>
         <physicianId>20</physicianId>
         <pscId>520</pscId>
         <phlebotomistId>110</phlebotomistId>
         <labTests>
         <test id="86900" dxcode="292.9" />
         <test id="86609" dxcode="307.3" />
         </labTests>
         </appointment>
         */

        

            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));

            Document doc = db.parse(is);
            NodeList nodes = doc.getElementsByTagName("appointment");
            Element e = (Element) nodes.item(0);

            IComponentsData dbq = new DB();
            List<Object> obs = dbq.getData("Appointment", "");

            int aid = Integer.valueOf(((Appointment) obs.get(obs.size() - 1)).getId()) + 1;
            String apptid = String.valueOf(aid);

            Appointment a = new Appointment(apptid);
            try{
                a.setApptdate(Date.valueOf(f(e, "date")));
            }catch(Exception exception){
                throw new Exception("Please select a valid date.");
            }
            a.setAppttime(Time.valueOf(f(e, "time")));

            obs = dbq.getData("Physician", "id='" + f(e, "physicianId") + "'");
            if(obs.size()==0){
                throw new Exception("This Physician is not Available.");
            }
            
            obs = dbq.getData("Patient", "id='" + f(e, "patientId") + "'");
            if(obs.size()==0){
                throw new Exception("This Patient is not Available.");
            }
            Patient p = (Patient) obs.get(0);
            a.setPatientid(p);

            obs = dbq.getData("Phlebotomist", "id='" + f(e, "phlebotomistId") + "'");
            if(obs.size()==0){
                throw new Exception("This Phlebotomist is not Available.");
            }
            Phlebotomist pl = (Phlebotomist) obs.get(0);
            a.setPhlebid(pl);

            obs = dbq.getData("PSC", "id='" + f(e, "pscId") + "'");
            if(obs.size()==0){
                throw new Exception("This PSC is not Available.");
            }
            PSC psc = (PSC) obs.get(0);
            a.setPscid(psc);

            List<AppointmentLabTest> ls = new ArrayList<AppointmentLabTest>();

            NodeList lNodes = e.getElementsByTagName("test");
            for (int i = 0; i < lNodes.getLength(); i++) {
                Element el = (Element) lNodes.item(i);
                String testid = el.getAttribute("id");
                String dxcode = el.getAttribute("dxcode");
                System.out.println(testid);
                AppointmentLabTest app = new AppointmentLabTest(apptid, testid, dxcode);
                
                obs = dbq.getData("Diagnosis", "code='" + dxcode + "'");
                if(obs.size()==0){
                    throw new Exception("The Diagnosis code that you have selected is not Available.");
                }
                app.setDiagnosis((Diagnosis) obs.get(0));
                obs = dbq.getData("LabTest", "id='" + testid + "'");
                if(obs.size()==0){
                    throw new Exception("The LabTest Id that you have selected is not Available.");
                }
                app.setLabTest((LabTest) obs.get(0));
                ls.add(app);
            }
            a.setAppointmentLabTestCollection(ls);

            return a;
       
    }

    //Used to create Appointment Object from the String xml with specific AppointmentId
    public Appointment createAppointment(String xml, String apptid) {
        /*
         <?xml version="1.0" encoding="utf-8" standalone="no"?>
         <appointment>
         <date>2016-12-30</date>
         <time>10:00</time>
         <patientId>220</patientId>
         <physicianId>20</physicianId>
         <pscId>520</pscId>
         <phlebotomistId>110</phlebotomistId>
         <labTests>
         <test id="86900" dxcode="292.9" />
         <test id="86609" dxcode="307.3" />
         </labTests>
         </appointment>
         */

        try {

            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));

            Document doc = db.parse(is);
            NodeList nodes = doc.getElementsByTagName("appointment");
            Element e = (Element) nodes.item(0);

            IComponentsData dbq = new DB();
            List<Object> obs = dbq.getData("Appointment", "");

            System.out.println("size:" + obs.size());

            Appointment a = new Appointment(apptid);

            a.setApptdate(Date.valueOf(f(e, "date")));
            a.setAppttime(Time.valueOf(f(e, "time")));

            obs = dbq.getData("Patient", "id='" + f(e, "patientId") + "'");
            Patient p = (Patient) obs.get(0);
            a.setPatientid(p);

            obs = dbq.getData("Phlebotomist", "id='" + f(e, "phlebotomistId") + "'");
            Phlebotomist pl = (Phlebotomist) obs.get(0);
            a.setPhlebid(pl);

            obs = dbq.getData("PSC", "id='" + f(e, "pscId") + "'");
            PSC psc = (PSC) obs.get(0);
            a.setPscid(psc);

            List<AppointmentLabTest> ls = new ArrayList<AppointmentLabTest>();

            NodeList lNodes = e.getElementsByTagName("test");
            for (int i = 0; i < lNodes.getLength(); i++) {
                Element el = (Element) lNodes.item(i);
                String testid = el.getAttribute("id");
                String dxcode = el.getAttribute("dxcode");
                System.out.println(testid);
                AppointmentLabTest app = new AppointmentLabTest(apptid, testid, dxcode);
                app.setDiagnosis((Diagnosis) dbq.getData("Diagnosis", "code='" + dxcode + "'").get(0));
                app.setLabTest((LabTest) dbq.getData("LabTest", "id='" + testid + "'").get(0));
                ls.add(app);
            }
            a.setAppointmentLabTestCollection(ls);

            return a;
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XMLConvert.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(XMLConvert.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XMLConvert.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    //Used to create String Object of the value present inside the nodes
    public String f(Element e, String cTag) {
        return e.getElementsByTagName(cTag).item(0).getTextContent();
    }

    //Used to create XML String of the error message 
    public String errorXml(String msg) {
        /*
         <?xml version="1.0" encoding="utf-8" standalone="no"?>
         <AppointmentList>
         <error>ERROR:Appointment is not available</error>
         </AppointmentList>
         */
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element AppointmentList = doc.createElement("AppointmentList");
            Element error = doc.createElement("error");
            error.setTextContent("ERROR:Appointment is not available");

            Element message = doc.createElement("message");
            message.setTextContent(msg);

            Element code = doc.createElement("code");
            code.setTextContent("-1");

            AppointmentList.appendChild(error);
            AppointmentList.appendChild(message);
            AppointmentList.appendChild(code);

            doc.appendChild(AppointmentList);

            return createXML(doc);

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XMLConvert.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    //Used to create XML String of the success message
    public String successXml(String id) {
        /*
         <?xml version="1.0" encoding="UTF-8"?>
         <AppointmentList>
         <uri>http://localhost:8080/LAMSAppointment/webresources/Services/Appointments/791</uri>
         </AppointmentList>
         */
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element AppointmentList = doc.createElement("AppointmentList");

            Element uri = doc.createElement("uri");
            if(id.length()>0){
                uri.setTextContent(contextPath + "/" + id);
            }else{
                uri.setTextContent(contextPath);
            }

            Element message = doc.createElement("message");
            message.setTextContent("Operation Successful");

            Element code = doc.createElement("code");
            code.setTextContent("1");

            AppointmentList.appendChild(uri);
            AppointmentList.appendChild(message);
            AppointmentList.appendChild(code);

            doc.appendChild(AppointmentList);

            return createXML(doc);

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XMLConvert.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    //Used to create XML String of the Service that has been created
    public String introXml() {
        /*
         <?xml version="1.0" encoding="UTF-8"?>
         <AppointmentList>
         <intro>Welcome to the LAMS Appointment Service</intro>
         <wadl>http://localhost:8080/LAMSAppointment/webresources/application.wadl</wadl>
         </AppointmentList>
         */
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element AppointmentList = doc.createElement("AppointmentList");

            Element intro = doc.createElement("intro");
            intro.setTextContent("Welcome to the LAMS Appointment Service");

            Element wadl = doc.createElement("wadl");
            wadl.setTextContent(contextPath + "application.wadl");

            AppointmentList.appendChild(intro);
            AppointmentList.appendChild(wadl);

            doc.appendChild(AppointmentList);

            return createXML(doc);

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XMLConvert.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
