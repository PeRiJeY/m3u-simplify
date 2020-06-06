package es.german.m3u.simplify;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileSystemView;

import com.iheartradio.m3u8.ParseException;
import com.iheartradio.m3u8.PlaylistException;

public class App {

	/**
	 * Create the GUI and show it. For thread safety, this method should be invoked
	 * from the event-dispatching thread.
	 * 
	 * @throws Exception
	 * @throws PlaylistException
	 * @throws ParseException
	 */
	private static void createAndShowGUI() throws ParseException, PlaylistException, Exception {

		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

		int returnValue = jfc.showOpenDialog(null);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = jfc.getSelectedFile();
			
			Simplify simplify = new Simplify(selectedFile);
			List<String> groups = simplify.readGroups();

			SelectGroupFrame selectGroup = new SelectGroupFrame(groups, (checkedOptions) -> {
				try {
					simplify.run(checkedOptions);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} );

		}
		System.out.println("End!");
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					createAndShowGUI();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
