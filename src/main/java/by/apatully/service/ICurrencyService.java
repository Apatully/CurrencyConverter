package by.apatully.service;

import by.apatully.entity.Currency;
import java.util.Date;
import java.util.List;

public interface ICurrencyService {


    List<Currency> getAllCurrencies(Date date);


    Float getExchangeRateByDate(String currency, Date date);

}
