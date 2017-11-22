package by.apatully.forms;

import by.apatully.entity.Currency;
import by.apatully.service.ICurrencyService;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;


public class CurrencyWindow extends JFrame {

    ICurrencyService iCurrencyService;

    Currency currency;

    public CurrencyWindow(final ICurrencyService iCurrencyService) {
        super("Currency");
        this.iCurrencyService = iCurrencyService;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final Container content = getContentPane();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        final JTextField currencyRB = new JTextField();
        final Container panel = new Container();
        final JLabel currencyLabel = new JLabel("BYR");
        panel.add(currencyLabel);
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(currencyRB);
        content.add(panel);

        final JTextField currencyResult = new JTextField();
        Container panel2 = new Container();
        final JLabel currencyLabel2 = new JLabel("BYR2");
        panel2.add(currencyLabel2);
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.X_AXIS));
        panel2.add(currencyResult);
        content.add(panel2);

        final JLabel currentCurrencyName = new JLabel();
        currentCurrencyName.setAlignmentX(LEFT_ALIGNMENT);
        content.add(currentCurrencyName);

        List<Currency> currencies = iCurrencyService.getAllCurrencies(new Date());
        Currency[] array = currencies.toArray(new Currency[currencies.size()]);
        final JComboBox listOfCurrencies = new JComboBox(array);
        listOfCurrencies.setAlignmentX(LEFT_ALIGNMENT);
        content.add(listOfCurrencies);

        new TextField();

        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");

        UtilDateModel model = new UtilDateModel();
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

        content.add(datePicker);


        datePicker.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JDatePanelImpl datePanel = (JDatePanelImpl) e.getSource();
                Date date = (Date) datePanel.getModel().getValue();
                List<Currency> currencies = iCurrencyService.getAllCurrencies(date);
                String currentSelected=  listOfCurrencies.getSelectedItem().toString();
                listOfCurrencies.removeAllItems();
                Currency[] array = currencies.toArray(new Currency[currencies.size()]);
                listOfCurrencies.setModel(new DefaultComboBoxModel(array));
                for (int i = 0; i < array.length; i++) {
                    if(array[i].getName().equals(currentSelected)){
                        listOfCurrencies.setSelectedIndex(i);
                        break;
                    }
                }
            }
        });

        currency = (Currency) listOfCurrencies.getSelectedItem();
        currentCurrencyName.setText(" Course: " + currency.getRate());
        currencyLabel2.setText(currency.getCharCode());


        currencyRB.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '.')
                    e.consume();
                if (currencyRB.getText().contains(".") && c == '.')
                    e.consume();
                if (currencyRB.getText().length() == 0 && c == '.')
                    currencyRB.setText("0");
            }

            public void keyPressed(KeyEvent e) {

            }

            public void keyReleased(KeyEvent e) {

            }
        });

        currencyRB.getDocument().addDocumentListener(new DocumentListener() {

            public void onChange() {
                if (currencyRB.hasFocus()) {
                    if (currencyRB.getText().length() > 0) {
                        Double currencyRateValue = Double.valueOf(currency.getRate());
                        Double currencyRBValue = Double.valueOf(currencyRB.getText());
                        currencyResult.setText(String.valueOf(currencyRBValue * currencyRateValue));
                    } else {
                        currencyResult.setText("");
                    }
                }
            }

            public void changedUpdate(DocumentEvent e) {
                onChange();
            }

            public void removeUpdate(DocumentEvent e) {
                onChange();
            }

            public void insertUpdate(DocumentEvent e) {
                onChange();
            }


        });

        currencyResult.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '.')
                    e.consume();
                if (currencyResult.getText().contains(".") && c == '.')
                    e.consume();
                if (currencyResult.getText().length() == 0 && c == '.')
                    currencyResult.setText("0");
            }

            public void keyPressed(KeyEvent e) {

            }

            public void keyReleased(KeyEvent e) {

            }
        });


        currencyResult.getDocument().addDocumentListener(new DocumentListener() {

            public void onChange() {
                if (currencyResult.hasFocus()) {
                    if (currencyResult.getText().length() > 0) {
                        Double currencyRateValue = Double.valueOf(currency.getRate());
                        Double currencyResultValue = Double.valueOf(currencyResult.getText());
                        currencyRB.setText(String.valueOf(currencyResultValue / currencyRateValue));
                    } else {
                        currencyRB.setText("");
                    }
                }
            }


            public void insertUpdate(DocumentEvent e) {
                onChange();
            }

            public void removeUpdate(DocumentEvent e) {
                onChange();
            }

            public void changedUpdate(DocumentEvent e) {
                onChange();
            }
        });

        listOfCurrencies.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent event) {
                if (event.getStateChange() == ItemEvent.SELECTED) {
                    currency = (Currency) event.getItem();
                    currentCurrencyName.setText(" Course: " + currency.getRate());
                    currencyLabel2.setText(currency.getCharCode());
                    if (currencyRB.getText().length() == 0)
                        return;
                    Double currencyRateValue = Double.valueOf(currency.getRate());
                    Double currencyRBValue = Double.valueOf(currencyRB.getText());
                    currencyResult.setText(String.valueOf(currencyRBValue / currencyRateValue));


                }
            }
        });
        pack();
        setLocation(500, 300);
        setVisible(true);
    }


    public class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {

        private String datePattern = "MM/dd/YYYY";
        private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

        @Override
        public Object stringToValue(String text) throws ParseException {
            return dateFormatter.parseObject(text);
        }

        @Override
        public String valueToString(Object value) throws ParseException {
            if (value != null) {
                Calendar cal = (Calendar) value;
                return dateFormatter.format(cal.getTime());
            }

            return "";
        }

    }
}






