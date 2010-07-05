package marten.age.core;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class SelectionDialog extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private static final int DEFAULT_WIDTH = 300;
	private static final int DEFAULT_HEIGHT = 200;
	
	private String options[];
	
	private boolean cancelWasPressed = false;
	
	private Object monitor = new Object();
	private int height = 0;
	private volatile boolean done = false;
	private volatile int selectedIndex = -1;
	
	public SelectionDialog(String title, String[] options, int height) {
		this.height = height;
		this.options = options;

		setTitle(title);
		final JList modeList = new JList(options);
		modeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		modeList.setSelectedIndex(0);
		JScrollPane scroller = new JScrollPane(modeList);
		getContentPane().setLayout(new BorderLayout());
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(new JLabel("Make your choice: "), BorderLayout.NORTH);
		getContentPane().add(BorderLayout.NORTH, panel);
		getContentPane().add(BorderLayout.CENTER, scroller);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.add(Box.createGlue());
		JButton button = new JButton("OK");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedIndex = modeList.getSelectedIndex();
				done = true;
				synchronized(monitor) {
					monitor.notify();
				}
				setVisible(false);
				dispose();
			}
		});
		buttonPanel.add(button);
		buttonPanel.add(Box.createHorizontalStrut(10));
		button = new JButton("Cancel");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelWasPressed = true;
				done = true;
				synchronized(monitor) {
					monitor.notify();
				}
				setVisible(false);
				dispose();
			}
		});
		buttonPanel.add(button);
		buttonPanel.add(Box.createGlue());
		getContentPane().add(BorderLayout.SOUTH, buttonPanel);
		setSize(DEFAULT_WIDTH, this.height);
		center(this, Toolkit.getDefaultToolkit().getScreenSize());
	}

	public SelectionDialog(String title, String[] options) {
		this(title, options, DEFAULT_HEIGHT);
	}

	public void waitFor() {
		synchronized(monitor) {
			while (!done) {
				try {
					monitor.wait();
				} catch (InterruptedException e) {
				}
			}
		}
	}

	public int selectedIndex() {
		if (cancelWasPressed) throw new RuntimeException(
				"Cancel was pressed so selected index can not be returned");
		return selectedIndex;
	}
	
	public String selectedString() {
		if (cancelWasPressed) throw new RuntimeException(
			"Cancel was pressed so selected index can not be returned");
		else return options[selectedIndex];
	}
	
	public boolean cancelWasPressed() {
		return cancelWasPressed;
	}

	private void center(Component component,
			Dimension containerDimension) {
		Dimension sz = component.getSize();
		int x = ((containerDimension.width - sz.width) / 2);
		int y = ((containerDimension.height - sz.height) / 2);
		component.setLocation(x, y);
	}
}