package es.german.m3u.simplify;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class SelectGroupFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4460723544825437001L;
	
	private JList<JCheckBox> listCheckbox;
	
	public SelectGroupFrame(List<String> groups, Consumer<List<String>> checkedOptionsConsumer) {
		super("JList Example");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(new Dimension(600, 300));
		
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	
		List<JCheckBox> auxCheckboxList = new ArrayList<>();
		for (String group : groups) {
			auxCheckboxList.add(new JCheckBox(group));			
		}
		
		listCheckbox = new JList<>(auxCheckboxList.toArray(new JCheckBox[auxCheckboxList.size()]));
		listCheckbox.setCellRenderer(new CheckboxListCellRenderer());
		listCheckbox.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		JScrollPane pane = new JScrollPane(listCheckbox);
		
		
		JPanel container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

		container.add(pane);
		container.add(createButton(checkedOptionsConsumer));
		
		setVisible(true);
		this.add(container);
	}
	
	private JButton createButton(Consumer<List<String>> checkedOptionsConsumer) {
		JButton button = new JButton("Convert!");
		
		SelectGroupFrame frame = this;
		button.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  List<String> checkedOptions = new ArrayList<>();
			  for (int i = 0; i < listCheckbox.getModel().getSize(); i++) {
				  JCheckBox c = listCheckbox.getModel().getElementAt(i);
				  if (c.isSelected()) { 
					  System.out.println(c.getText() + ": " + c.isSelected());
					  checkedOptions.add(c.getText());
				  }
			  }
			  checkedOptionsConsumer.accept(checkedOptions);
			  frame.dispose();
		  }
		});
		
		return button;
	}

}
