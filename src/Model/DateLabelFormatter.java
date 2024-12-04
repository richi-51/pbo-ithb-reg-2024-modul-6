package Model;
import java.text.SimpleDateFormat;
import java.util.Calendar;

// Formatter untuk mengatur format tanggal
public class DateLabelFormatter extends javax.swing.JFormattedTextField.AbstractFormatter {
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Object stringToValue(String text) throws java.text.ParseException {
        return dateFormatter.parse(text);
    }

    @Override
    public String valueToString(Object value) {
        if (value instanceof Calendar) {
            Calendar calendar = (Calendar) value;
            return dateFormatter.format(calendar.getTime());
        }
        return "";
    }
}
