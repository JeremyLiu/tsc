package com.jec.plugin.debug.view;

import java.awt.BorderLayout;
import java.text.SimpleDateFormat;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.jec.plugin.debug.Debugger.Level;
import com.jec.plugin.debug.core.DebugEntry;
import com.jec.plugin.debug.core.DebugProcessor;
import java.awt.Font;

public class DebugViewer extends JFrame implements DebugProcessor {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JScrollPane jScrollPane = null;
	private JTextArea jTextArea = null;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  //  @jve:decl-index=0:
	
	/**
	 * This is the default constructor
	 */
	public DebugViewer() {
		super();
		initialize();
		this.setLocationRelativeTo(null);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(594, 390);
		this.setContentPane(getJContentPane());
		this.setTitle("Debug Viewer");
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJScrollPane(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJTextArea());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jTextArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getJTextArea() {
		if (jTextArea == null) {
			jTextArea = new JTextArea();
			jTextArea.setEditable(false);
			jTextArea.setFont(new Font("宋体", Font.PLAIN, 14));
			jTextArea.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					if(e.isShiftDown() && (e.getClickCount() == 2)) {
						jTextArea.selectAll();
						jTextArea.replaceSelection("");
					}
				}
			});
		}
		return jTextArea;
	}

	public static String makeLength(String source, int len) {
		
		if(len <= 0) {
			return "";
		}
		
		if(source == null) {
			source = "";
		}
		
		if(source.length() > len) {
			return source.substring(0, len);
		}
		
		String result = source;
		
		while(result.length() < len) {
			result += " ";
		}
		
		return result;
		
	}
	
	public String levelToText(Level level) {
		switch(level) {
		case FATAL: return "致命";
		case ERROR: return "错误";
		case WARNING: return "警告";
		case MESSAGE: return "信息";
		case DEBUG: return "调试";
		default: return "未知";
		}
	}
	
	@Override
	public void onEntry(DebugEntry entry) {
		StringBuilder sb = new StringBuilder();
		sb.append("【");
		sb.append(dateFormat.format(entry.getTime()));
		sb.append("】【");
		sb.append(levelToText(entry.getLevel()));
		sb.append("】【");
		sb.append(makeLength(entry.getModule(), 12));
		sb.append("】 ");
		sb.append(entry.getContent());
		sb.append("\r\n");
		if(this.isVisible()) {
			getJTextArea().append(sb.toString());
		} /*else {
			System.out.print(sb.toString());
		}*/
		
	}
	
	

}  //  @jve:decl-index=0:visual-constraint="10,10"
