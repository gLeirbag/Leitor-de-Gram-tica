package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


import javax.swing.UIManager.LookAndFeelInfo;



public class JanelaExibicaoGramatica extends JDialog {

	private static final long serialVersionUID = 1L;
	private JTextArea txtGramatica = new JTextArea();
	private JScrollPane scrollPane = new JScrollPane(txtGramatica);
	

	public static void main(String[] args) {
		try {
			JanelaExibicaoGramatica dialog = new JanelaExibicaoGramatica(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public JanelaExibicaoGramatica(File arquivo) {
		try {//Tenta alterar a aparência
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) { //procura o tema Windows no Array
		        if ("Windows".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		txtGramatica.setEditable(false);
		txtGramatica.setText("Nenhuma Gram\u00E1tica selecionada");
		setBounds(100, 100, 383, 192);
		getContentPane().setLayout(new BorderLayout());
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		JButton btnFechar = new JButton("Fechar");
		btnFechar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnFechar.setActionCommand("OK");
		buttonPane.add(btnFechar);
		getRootPane().setDefaultButton(btnFechar);
		pack();
		
	}
	
	public void setTexto(String texto) {
		txtGramatica.setText(texto);		
		pack();
	}

}