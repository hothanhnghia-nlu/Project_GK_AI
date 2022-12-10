package view;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import detection.*;

public class GUI extends JFrame {
	JLabel lblInput, lblOrigin, lblRestored, lblNotif;
	JTextField tfInput;
	JButton btnUpload, btnStart, btnSave, btnDel;
	
	public GUI() {
		init();
		createComponents();
		createAction();
	}

	// Create JFrame
	private void init() {
		setTitle("Restore old photos");
		setSize(900, 680);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	// Create components
	private void createComponents() {
		Container pane = getContentPane();
		pane.setLayout(null);
		
		lblInput = new JLabel("Input file:");
		lblInput.setBounds(20, 10, 80, 30);
		lblInput.setFont(new Font("Arial", Font.BOLD, 15));
		pane.add(lblInput);
		
		tfInput = new JTextField();
		tfInput.setBounds(100, 10, 300, 30);
		tfInput.setEditable(false);
		tfInput.setBackground(Color.WHITE);
		pane.add(tfInput);
		
		btnUpload = new JButton("Upload image");
		btnUpload.setBounds(420, 10, 120, 30);
		pane.add(btnUpload);
		
		lblNotif = new JLabel();
		lblNotif.setBounds(600, 10, 200, 30);
		lblNotif.setFont(new Font("Arial", Font.BOLD, 15));
		lblNotif.setForeground(Color.RED);
		pane.add(lblNotif);
		
		btnStart = new JButton("Restore");
		btnStart.setBounds(100, 50, 80, 30);
		pane.add(btnStart);
		
		btnSave = new JButton("Save Image");
		btnSave.setBounds(200, 50, 100, 30);
		pane.add(btnSave);
		
		btnDel = new JButton("Delete");
		btnDel.setBounds(320, 50, 80, 30);
		pane.add(btnDel);
		
		lblOrigin = new JLabel("Original photo", SwingConstants.CENTER);
		lblOrigin.setBounds(20, 100, 400, 520);
		lblOrigin.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		pane.add(lblOrigin);
		
		lblRestored = new JLabel("Restored photo", SwingConstants.CENTER);
		lblRestored.setBounds(460, 100, 400, 520);
		lblRestored.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		pane.add(lblRestored);
		
		setVisible(true);
	}

	// Implement action for each button
	private void createAction() {
		
		// Browse button action
		btnUpload.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser openFile = new JFileChooser();
				openFile.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int browse = openFile.showOpenDialog(null);
				
				if (browse == JFileChooser.APPROVE_OPTION) {
					File file = openFile.getSelectedFile();
					String filename = file.getName();
					if (filename.endsWith("jpg")) {
						String pathFile = file.getAbsolutePath();
						BufferedImage b;
						try {
							b = ImageIO.read(file);
							lblOrigin.setIcon(ResizeImage(String.valueOf(pathFile)));
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						tfInput.setText(pathFile);
					} else {
						JOptionPane.showMessageDialog(btnUpload, "Path invalid! Please choose again!");
						tfInput.setText("");
					}
				}
			}
		});
		
		// Start button action
		btnStart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String path = tfInput.getText();
				File file = new File(path);
				Detection detec = new Detection(file);
				try {
					detec.run();
					lblRestored.setIcon(ResizeImage(String.valueOf(detec.getDestFile())));
					lblRestored.setText(detec.getDestFile());
				} catch (Exception e1) {
					lblNotif.setText("Failed to restore!");
				}
//				ImageFilter imgFilter = new ImageFilter(file);
//				imgFilter.bilateralFilter();
//				lblRestored.setIcon(ResizeImage(String.valueOf(imgFilter.getDestFile())));
//				lblRestored.setText(imgFilter.getDestFile());
			}
		});
		
		// Save button action
		btnSave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser saveFile = new JFileChooser();
				saveFile.setFileFilter(new FileNameExtensionFilter("*.jpg", "jpg"));
				int save = saveFile.showSaveDialog(null);
			    
			    if (save == JFileChooser.APPROVE_OPTION) {
			        File file = saveFile.getSelectedFile();
			        try {
			        	File f = new File(lblRestored.getText());
			            BufferedImage image = ImageIO.read(f);
						ImageIO.write((BufferedImage) image , "jpg", new File(file.getAbsolutePath()));
						lblNotif.setText("Image is saved successfully!");
			        } catch (IOException ex) {
			        	lblNotif.setText("Failed to save image!");
			        }
			    } else {
			    	JOptionPane.showMessageDialog(btnSave, "No file choosen! Please choose again!");
			    }
			}
		});
		
		// Delete button action
		btnDel.addActionListener(new ActionListener() {
					
			@Override
			public void actionPerformed(ActionEvent e) {
				tfInput.setText(null);
				lblOrigin.setIcon(null);
				lblRestored.setIcon(null);
				lblRestored.setText("Restored photo");
				lblNotif.setText(null);
			}
		});
	}

	// Fixed image size
	private ImageIcon ResizeImage(String path) {
		ImageIcon res = new ImageIcon(path);
		Image img = res.getImage();
		Image newImg = img.getScaledInstance(lblOrigin.getWidth(), lblOrigin.getHeight(), Image.SCALE_SMOOTH);
		ImageIcon image = new ImageIcon(newImg);
		return image;
	}
	
	public static void main(String[] args) {
		new GUI();
	}
}
