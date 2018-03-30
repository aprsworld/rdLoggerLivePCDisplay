/*
 * ConfigSelect.java
 *
 * Created on October 17, 2006, 9:24 PM
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author  davarus
 */
public class ConfigSelect extends javax.swing.JFrame 
implements ListSelectionListener, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Creates new form ConfigSelect */
	public ConfigSelect() {
		WindowUtilities.setNativeLookAndFeel();
		initComponents();
		initList();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	private void initComponents() {//GEN-BEGIN:initComponents
		panelSide = new javax.swing.JPanel();
		panelButtons = new javax.swing.JPanel();
		buttonLaunch = new javax.swing.JButton();
		buttonEdit = new javax.swing.JButton();
		buttonDelete = new javax.swing.JButton();
		buttonNew = new javax.swing.JButton();
		panelConfig = new javax.swing.JPanel();
		listConfig = new javax.swing.JList();
		scrollConfig = new javax.swing.JScrollPane(listConfig);
		labelConfig = new javax.swing.JLabel();
		listModel = new javax.swing.DefaultListModel();

		setTitle("Select Configuration");
		setResizable(false);
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				exitForm(evt);
			}
		});

		panelSide.setLayout(new java.awt.BorderLayout());

		panelSide.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(5, 5, 5, 5)));
		panelButtons.setLayout(new java.awt.GridLayout(4, 0));

		buttonLaunch.setText("Launch");
		buttonLaunch.setActionCommand("launch");
		buttonLaunch.addActionListener(this);
		buttonLaunch.setEnabled(false);
		panelButtons.add(buttonLaunch);

		buttonEdit.setText("Edit");
		buttonEdit.setActionCommand("edit");
		buttonEdit.addActionListener(this);
		buttonEdit.setEnabled(false);
		panelButtons.add(buttonEdit);

		buttonDelete.setText("Delete");
		buttonDelete.setActionCommand("delete");
		buttonDelete.addActionListener(this);
		buttonDelete.setEnabled(false);
		panelButtons.add(buttonDelete);

		buttonNew.setText("New");
		buttonNew.setActionCommand("new");
		buttonNew.addActionListener(this);
		panelButtons.add(buttonNew);

		panelSide.add(panelButtons, java.awt.BorderLayout.NORTH);

		getContentPane().add(panelSide, java.awt.BorderLayout.EAST);

		panelConfig.setLayout(new java.awt.BorderLayout());

		panelConfig.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(5, 5, 5, 5)));
		scrollConfig.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
		listConfig.setModel(listModel);
		listConfig.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listConfig.addListSelectionListener(this);

		/*new javax.swing.AbstractListModel() {
            String[] strings = { "Wind2db", "Test" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });*/

		listConfig.setVisibleRowCount(20);
		panelConfig.add(scrollConfig, java.awt.BorderLayout.CENTER);

		labelConfig.setText("Configuration:                  ");
		panelConfig.add(labelConfig, java.awt.BorderLayout.NORTH);

		getContentPane().add(panelConfig, java.awt.BorderLayout.CENTER);

		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((screenSize.width-400)/2, (screenSize.height-300)/2, 400, 300);
	}//GEN-END:initComponents

	private void initList () {
		File dir = new File(".");

		FilenameFilter filter = new FilenameFilter() {
			public boolean accept (File dir, String name) {
				return name.endsWith(".ini");
			}
		};

		String[] files = dir.list(filter);
		for (int i = 0; i < files.length; i++) {
			listModel.addElement(files[i].substring(0, files[i].length() - 4));
		}
	}

	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting() == false) {
			if (listConfig.getSelectedIndex() == -1) {
				buttonDelete.setEnabled(false);
				buttonEdit.setEnabled(false);
				buttonLaunch.setEnabled(false);
			} else {
				buttonDelete.setEnabled(true);
				buttonEdit.setEnabled(true);
				buttonLaunch.setEnabled(true);
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		if ("delete".equals(e.getActionCommand())) {
			File file = new File(((String)listConfig.getSelectedValue()) + ".ini");
			try {
				file.delete();
				listModel.remove(listConfig.getSelectedIndex());
			} catch (SecurityException se) {
			}
		} else if ("launch".equals(e.getActionCommand())) {
			new RDLoggerLivePCDisplay(((String)listConfig.getSelectedValue()) + ".ini").start();
			dispose();
		}
	}

	/** Exit the Application */
	private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
		//System.exit(0);
	}//GEN-LAST:event_exitForm

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		new ConfigSelect().setVisible(true);
	}


	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton buttonDelete;
	private javax.swing.JButton buttonEdit;
	private javax.swing.JButton buttonLaunch;
	private javax.swing.JButton buttonNew;
	private javax.swing.JLabel labelConfig;
	private javax.swing.JList listConfig;
	private javax.swing.DefaultListModel listModel;
	private javax.swing.JScrollPane scrollConfig;
	private javax.swing.JPanel panelButtons;
	private javax.swing.JPanel panelConfig;
	private javax.swing.JPanel panelSide;
	// End of variables declaration//GEN-END:variables

}
