
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JScrollPane;

public class TextAreaFrame extends JFrame {
	private final JTextArea textArea1;
	private final JTextArea textArea2;
	private final JButton loadTextJButton;
	private final JButton convertDateJButton;
	private final JButton selected;
	private final JButton letter;
	private final JButton word;
	private final JButton writeObjectsButton;
	private final JButton LoadText;

	public TextAreaFrame() {
		super("TextArea Demo");
		Box box = Box.createHorizontalBox();

		Box boxV = Box.createVerticalBox();

		textArea1 = new JTextArea(10, 15);
		box.add(new JScrollPane(textArea1));

		JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayout(7, 8, 8, 8));
		box.add(panel1);

		// LOAD TEXT FROM FILE
		String n = "Load Text\nfrom file\n\"inputText.txt\"";
		loadTextJButton = new JButton("<html>" + n.replaceAll("\\n", "<br>") + "</html>");
		panel1.add(loadTextJButton);
		loadTextJButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					BufferedReader br = new BufferedReader(new FileReader("inputText.txt"));
					String line;
					StringBuffer sb = new StringBuffer();
					while ((line = br.readLine()) != null) {  //read from file until null returned
						sb.append(line + "\n");
					}
					textArea1.setText(sb.toString());
					textArea2.setText("");
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		// SLECTED TEXT INTO FILE
		String x = "Write the\nSelected text\nto file\n\"outputText.txt\"";
		selected = new JButton("<html>" + x.replaceAll("\\n", "<br>") + "</html>");
		panel1.add(selected);
		selected.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				String selectedText = textArea1.getSelectedText();

				try (BufferedWriter bw = new BufferedWriter(new FileWriter("outputText.txt"))) {
					bw.write(selectedText); //writes selected text string into file
				} catch (IOException ex) {
					ex.printStackTrace();
				}

				textArea2.setText("The following text is written to\nthe file\"outputText.txt\":\n\n" + selectedText);
			}
		});

		// CONVERT DATE
		String f = "Convert\nDate Format";
		convertDateJButton = new JButton("<html>" + f.replaceAll("\\n", "<br>") + "</html>");
		panel1.add(convertDateJButton);
		convertDateJButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String input = textArea1.getSelectedText(); //get text
				String[] lines = input.split("\n"); //split and put in array
				StringBuilder output = new StringBuilder();
				for (String line : lines) {
					try {
						DateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy");
						Date date = inputFormat.parse(line);    //loops over and tries parsing selected text into date
						DateFormat outputFormat = new SimpleDateFormat("MMMM dd, yyyy"); //converts to month,day,year
						output.append(outputFormat.format(date)).append("\n");
					} catch (ParseException ex) {
						output.append("Invalid date ");
					}
				}
				textArea2.setText(output.toString());
			}
		});

		// COUNT LETTERS
		letter = new JButton("Letter Count");
		panel1.add(letter);
		letter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String text = textArea1.getText();
				int[] letterCounts = new int[26];
				for (int i = 0; i < text.length(); i++) { // loops over characters in text string
					char c = text.charAt(i); // gets char at current index and assigns 'c' variable
					if (Character.isLetter(c)) {
						letterCounts[Character.toLowerCase(c) - 'a']++; // increments if its lower case also if the
																		// variable 'c'
					}
				}

				StringBuilder sb = new StringBuilder();
				sb.append("\nResult:\nchar               count\n");
				for (int i = 0; i < 26; i++) {
					sb.append((char) ('A' + i)).append("                    ")
					.append(letterCounts[i]).append("\n");
				}
				textArea2.setText(sb.toString());
			}
		});

		// COUNT THE AMOUNT OF WORDS
		word = new JButton("Word Count");
		panel1.add(word);
		word.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String selectedText = textArea1.getText();
				String[] words = selectedText.split("\\s+"); //split selected text into array
															// \s+, splits into words and any whitespace treated as 1
				int[] wordLengthCounts = new int[21];
				for (String word : words) {
					
					if (word.length() <= 21) {			//if word has length of 5, wordLengthCounts[5], will increment
						wordLengthCounts[word.length()]++;
					}
				}
				StringBuilder sb = new StringBuilder();
				sb.append("Result:\nNo. of letter           Count\n");
				for (int i = 0; i <= 20; i++) {
					sb.append(String.format("%d", i)).append("                            ")
					.append(wordLengthCounts[i]).append("\n");
				}
				textArea2.setText(sb.toString());
			}
		});

		// WRITE .ser FILE
		String c = "Write Objects\nto file\n\"outputObj.ser\"";
		writeObjectsButton = new JButton("<html>" + c.replaceAll("\\n", "<br>") + "</html>");
		panel1.add(writeObjectsButton);

		writeObjectsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//creating objects
				SalariedEmployee employee1 = new SalariedEmployee("Alice", "\nSmith", "\n222-22-2222", 800);
				HourlyEmployee employee2 = new HourlyEmployee("Bob", "Smith", "\n111-11-1111", 16.75, 40);
				CommissionEmployee employee3 = new CommissionEmployee("Jane", "\nDoe", "\n987-65-4321", 10000, 0.06);
				BasePlusCommissionEmployee employee4 = new BasePlusCommissionEmployee("John", "Doe", "\n123-45-6789",
						5000, 0.04, 300);

				//writing to the file
				try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("outputObj.ser"))) {
					out.writeObject(employee1);
					out.writeObject(employee2);
					out.writeObject(employee3);
					out.writeObject(employee4);
				} catch (IOException ex) {
					ex.printStackTrace();
					return;
				}

				//printing to textArea2
				try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("outputObj.ser"))) {

					StringBuilder sb = new StringBuilder();
					sb.append("The objects for the following\ninformation is written to file\n\"outputObj.ser\":\n\n");
					while (true) {
						try {
							Object obj = in.readObject();
							sb.append(obj.toString()).append("\n\n");
						} catch (EOFException ex) {
							break;
						}
					}
					textArea1.setText("");
					textArea2.setText(sb.toString());
				} catch (IOException | ClassNotFoundException ex) {
					ex.printStackTrace();
				}
			}
		});

		// LOAD OBJECTS FROM .ser
		String e = "Load Objects\nfrom file\n\"outputObj.ser\"";
		LoadText = new JButton("<html>" + e.replaceAll("\\n", "<br>") + "</html>");

		panel1.add(LoadText);
		LoadText.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("outputObj.ser"))) {
					
					StringBuilder sb = new StringBuilder();
					sb.append("The objects read from file\n\"outputObj.ser\":\n\n");
					
					//continue reading file until "EOF" Exception to end reading
					while (true) {
						try {
							Employee obj = (Employee) ois.readObject(); //reading emp objects
							sb.append(obj.toString()).append("\n\n"); //calling the objects toStrings
						} catch (EOFException ex) {
							break;
						} catch (ClassNotFoundException e1) {
							
							e1.printStackTrace();
						}
					}
					textArea2.setText("");
					textArea1.setText(sb.toString());
				} catch (FileNotFoundException e1) {
				
					e1.printStackTrace();
				} catch (IOException e1) {
				
					e1.printStackTrace();
				}

			}
		});

		textArea2 = new JTextArea(10, 15);
		textArea2.setEditable(false);
		box.add(new JScrollPane(textArea2));

		add(box);
	}
}
