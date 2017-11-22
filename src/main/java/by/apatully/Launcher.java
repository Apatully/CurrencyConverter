package by.apatully;

import by.apatully.forms.CurrencyWindow;
import by.apatully.service.ICurrencyService;
import by.apatully.service.IInputStreamService;
import by.apatully.service.NBRBCurrencyService;
import by.apatully.service.UrlConnect;

public class Launcher {

    public static void main(String[] args) {

        IInputStreamService inputStreamService = new UrlConnect();
        final ICurrencyService currencyService = new NBRBCurrencyService(inputStreamService);

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new CurrencyWindow(currencyService);
            }
        });

    }


}
