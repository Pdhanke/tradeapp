package com.prashant.util;

import java.io.File;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;

import com.prashant.strategy.StrategyData;
import com.zerodhatech.kiteconnect.utils.Constants;
import com.zerodhatech.models.OrderParams;

public class XMLWrite {
	public static void main(String[] args) {

		OrderParams orderParams = new OrderParams();

        orderParams.disclosedQuantity = 0;
        orderParams.exchange = Constants.EXCHANGE_NFO;
        orderParams.orderType = Constants.ORDER_TYPE_LIMIT;
        orderParams.parentOrderId = "";
		orderParams.price = 0.0;
        orderParams.product = Constants.PRODUCT_MIS;
        orderParams.quantity = 0;
        orderParams.squareoff= 0.0;
        orderParams.tag="";
        orderParams.tradingsymbol = "BANKNIFTYDDMMM30000CE";
        orderParams.trailingStoploss = 0.0;
        orderParams.transactionType = Constants.TRANSACTION_TYPE_BUY;
        orderParams.triggerPrice = 0.0;
        orderParams.validity = Constants.VALIDITY_DAY;
        try {
			marshal(orderParams);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        List<OrderParams> orderParamList = new LinkedList<OrderParams>();
        orderParamList.add(orderParams);
        OrderParams o2 = new OrderParams();
        o2 = orderParams;
        orderParamList.add(o2);
        StrategyData sdata = new StrategyData();
        sdata.setOrderParamList(orderParamList);
        try {

		File file = new File("C:\\Users\\Prashant\\eclipse-workspace\\project2\\resources\\file.xml");
		/*JAXBContext jaxbContext = JAXBContext.newInstance(OrderParams.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

		// output pretty printed
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		jaxbMarshaller.marshal(orderParams, file);
		jaxbMarshaller.marshal(orderParams, System.out);
		
		JAXBContext jc = JAXBContext.newInstance(OrderParams.class);
        JAXBElement<OrderParams> je2 = new JAXBElement<OrderParams>(new QName("orderparams"), OrderParams.class, orderParams);
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(je2, System.out);*/
        
//		JAXBContext jc = JAXBContext.newInstance(StrategyData.class);
//        StreamSource xml = new StreamSource("C:\\Users\\Prashant\\eclipse-workspace\\project2\\resources\\file.xml");
//        Unmarshaller unmarshaller = jc.createUnmarshaller();
//        JAXBElement<StrategyData> je1 = unmarshaller.unmarshal(xml, StrategyData.class);
//       StrategyData sdata = je1.getValue();
		JAXBContext jc = JAXBContext.newInstance(StrategyData.class);
        JAXBElement<StrategyData> je2 = new JAXBElement<StrategyData>(new QName("stradata"), StrategyData.class, sdata);
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(je2, System.out);
          //marshaller.marshal(je2, file);
        
        
        

	      } catch (JAXBException e) {
		e.printStackTrace();
	      }

	}
	
	public static StrategyData getStrategyDatefromXMLFile (File strategyDateFile) {
		
		try {
			JAXBContext jc = JAXBContext.newInstance(StrategyData.class);
			StreamSource xml = new StreamSource("C:\\Users\\Prashant\\eclipse-workspace\\project2\\resources\\file.xml");
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			JAXBElement<StrategyData> je1 = unmarshaller.unmarshal(xml, StrategyData.class);
			StrategyData sdata = je1.getValue();
			return sdata;
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public static String marshal(OrderParams orderParams) throws JAXBException {
	    StringWriter stringWriter = new StringWriter();

	    JAXBContext jaxbContext = JAXBContext.newInstance(OrderParams.class);
	    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

	    // format the XML output
	    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
	        true);

	    QName qName = new QName("com.zerodhatech.models", "OrderParams");
	    JAXBElement<OrderParams> root = new JAXBElement<>(qName, OrderParams.class, orderParams);

	    jaxbMarshaller.marshal(root, stringWriter);

	    String result = stringWriter.toString();
	   System.out.println("XMLWrite.marshal() " + result);
	    return result;
	  }
}




