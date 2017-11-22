package by.apatully.service;

import by.apatully.entity.Currency;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NBRBCurrencyService implements ICurrencyService {

    IInputStreamService inputStreamService;


    public NBRBCurrencyService(IInputStreamService inputStreamService) {
        this.inputStreamService = inputStreamService;
    }

    public List<Currency> getAllCurrencies(Date date) {

        List<Currency> resultList = new ArrayList<Currency>();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/YYYY");
            String stringDate = sdf.format(date);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            String address = "http://www.nbrb.by/Services/XmlExRates.aspx?ondate=" + stringDate;
            InputStream inputStream = inputStreamService.getInputStream(address);
            Document doc = dBuilder.parse(inputStream);
            doc.getDocumentElement().normalize();
            NodeList currencyList = doc.getElementsByTagName("Currency");
            for (int i = 0; i < currencyList.getLength(); i++) {
                Node currencyNode = currencyList.item(i);
                if (currencyNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element currencyElement = (Element) currencyNode;
                    Currency currency = new Currency();
                    currency.setId(Integer.valueOf(currencyElement.getAttribute("Id")));
                    currency.setNumCode(Integer.valueOf(currencyElement.getElementsByTagName("NumCode").item(0).getTextContent()));
                    currency.setCharCode(currencyElement.getElementsByTagName("CharCode").item(0).getTextContent());
                    currency.setScale(Integer.valueOf(currencyElement.getElementsByTagName("Scale").item(0).getTextContent()));
                    currency.setName(currencyElement.getElementsByTagName("Name").item(0).getTextContent());
                    currency.setRate(Float.valueOf(currencyElement.getElementsByTagName("Rate").item(0).getTextContent()));
                    resultList.add(currency);
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    public Float getExchangeRateByDate(String currency, Date date) {
        return null;
    }
}
