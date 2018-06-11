package com.resultant.task.connector;

import com.sun.org.apache.xerces.internal.dom.ElementNSImpl;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.cbr.code.generate.ws.*;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class CBRClient  extends WebServiceGatewaySupport {

    private DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();

    public CBRClient() throws DatatypeConfigurationException {
    }

    public List<Map<String, String>> getEnum(){
        EnumValutes request = new EnumValutes();
        request.setSeld(false);

        EnumValutesResponse response = (EnumValutesResponse)getWebServiceTemplate().marshalSendAndReceive(
                "http://www.cbr.ru/DailyInfoWebServ/DailyInfo.asmx",
                request);

        EnumValutesResponse.EnumValutesResult result = response.getEnumValutesResult();

        NodeList list = ((ElementNSImpl)result.getAny()).getElementsByTagName("EnumValutes");

        return parse(list);
    }

    public List<Map<String, String>> getCursOnDate(LocalDate date){

        GetCursOnDate request = new GetCursOnDate();

        GregorianCalendar gc = GregorianCalendar.from(date.atStartOfDay(ZoneId.systemDefault()));

        XMLGregorianCalendar dateGR = datatypeFactory.newXMLGregorianCalendar(gc);

        request.setOnDate(dateGR);

        GetCursOnDateResponse response = (GetCursOnDateResponse)getWebServiceTemplate().marshalSendAndReceive(
                "http://www.cbr.ru/DailyInfoWebServ/DailyInfo.asmx",
                request);
        GetCursOnDateResponse.GetCursOnDateResult result = response.getGetCursOnDateResult();

        NodeList list = ((ElementNSImpl)result.getAny()).getElementsByTagName("ValuteCursOnDate");

        return parse(list);
    }

    public List<Map<String, String>> getCursDynamic(LocalDate from, LocalDate to, String VcharCode){

        GetCursDynamic request = new GetCursDynamic();

        GregorianCalendar gFrom = GregorianCalendar.from(from.atStartOfDay(ZoneId.systemDefault()));
        GregorianCalendar gTo = GregorianCalendar.from(to.atStartOfDay(ZoneId.systemDefault()));

        request.setFromDate(datatypeFactory.newXMLGregorianCalendar(gFrom));
        request.setToDate(datatypeFactory.newXMLGregorianCalendar(gTo));
        request.setValutaCode(VcharCode);

        GetCursDynamicResponse response = (GetCursDynamicResponse)getWebServiceTemplate().marshalSendAndReceive(
                "http://www.cbr.ru/DailyInfoWebServ/DailyInfo.asmx",
                request);
        GetCursDynamicResponse.GetCursDynamicResult result = response.getGetCursDynamicResult();

        NodeList list = ((ElementNSImpl)result.getAny()).getElementsByTagName("ValuteCursDynamic");

        return parse(list);
    }




    private List<Map<String, String>> parse(NodeList list){
        List<Map<String, String>> resultList = new ArrayList<>();

        for(int i = 0 ; i < list.getLength(); i++) {
            Map<String, String> map = new HashMap<>();
            Node node = list.item(i);
            parse(node.getFirstChild(), map);
            resultList.add(map);
        }
        return resultList;
    }

    private void parse(Node node, Map<String, String> map){
        if(node == null) return;
        map.put(node.getNodeName().trim() ,node.getFirstChild().getNodeValue().trim());
        parse(node.getNextSibling(), map);
    }


}
