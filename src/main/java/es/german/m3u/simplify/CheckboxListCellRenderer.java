package es.german.m3u.simplify;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class CheckboxListCellRenderer extends JCheckBox implements ListCellRenderer<JCheckBox> {

    public Component getListCellRendererComponent(JList list, JCheckBox value, int index, 
            boolean isSelected, boolean cellHasFocus) {

        setComponentOrientation(list.getComponentOrientation());
        setFont(list.getFont());
        setBackground(list.getBackground());
        setForeground(list.getForeground());
        setSelected(isSelected);
        value.setSelected(isSelected);
        setEnabled(list.isEnabled());

        setText(value == null ? "" : value.getText());  

        return this;
    }
}
