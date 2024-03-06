package visual;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import service.Gramatica;
import service.LeitorArquivo;

import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
//import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

/*
 * JFrame extende uma interface chamada Serializable, responsável por serializar uma classe(Transformala em dados, pra salvar em disco por exemplo)
 * Pra eventualmente deserializala, ele comparará o serialVersionUID da classe com o do objeto salvo, por isso é recomendado definir serialVersionUID.

*/
public class JanelaPrincipal extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private static File arquivo = null;
	private static Gramatica gramatica = null;
	private static final OlaMundo olaMundo = new OlaMundo();
	private static JanelaExibicaoGramatica janelaExibicaoGramatica = new JanelaExibicaoGramatica(arquivo);
	
	private JPanel contentPane;
	private JTextField txtArquivoSelecionado;
	private JButton btnExibirGramatica;
	private JPanel panelPalavra;
	private JTextField txtFieldPalavra;
	private JButton btnChecarPalavra;
	private JPanel panelGramatica;
	private JCheckBox chckbxFormatado;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
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
		JanelaPrincipal.setDefaultLookAndFeelDecorated(true);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JanelaPrincipal frame = new JanelaPrincipal();
					frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public JanelaPrincipal() {
		// Informações Janela
		setTitle("Leitor de Gram\u00E1tica");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 497, 279);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		
		
		
		//Informações Olá Mundo		
		JButton btnOlaMundo = new JButton("Ol\u00E1 Mundo");
		/*Através da função anônima definimos uma implementação da função actionperformed
		 * que é uma função abstrata da interface ActionListener(), dessa forma não precisamos criar
		 * uma classe para criar um evento.*/
		
		btnOlaMundo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(olaMundo.isVisible() == false)
					olaMundo.setVisible(true);
				else olaMundo.toFront();
			}
		});
		
		//BotãoSelecionar Gramática
		JButton btnSelecionarGramatica = new JButton("Selecionar Gram\u00E1tica");
		btnSelecionarGramatica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SelecionarArquivo();
			}
		});
		
		//Campo que mostra o arquivo selecionado
		txtArquivoSelecionado = new JTextField();
		txtArquivoSelecionado.setBackground(new Color(255, 255, 255));
		txtArquivoSelecionado.setText("...");
		txtArquivoSelecionado.setEditable(false);
		txtArquivoSelecionado.setColumns(10);
		
		panelGramatica = new JPanel();
		FlowLayout flowLayoutPanelGramatica = (FlowLayout) panelGramatica.getLayout();
		flowLayoutPanelGramatica.setHgap(1);
		panelGramatica.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		
		//Botão Exibir Gramática
		btnExibirGramatica = new JButton("Exibir Gram\u00E1tica");
		panelGramatica.add(btnExibirGramatica);
		
		chckbxFormatado = new JCheckBox("Formatar");
		chckbxFormatado.setEnabled(false);
		panelGramatica.add(chckbxFormatado);
		btnExibirGramatica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exibirGramatica();
			}
		});
		
		panelPalavra = new JPanel();
		panelPalavra.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		
		txtFieldPalavra = new JTextField();
		txtFieldPalavra.setHorizontalAlignment(SwingConstants.CENTER);
		txtFieldPalavra.setText("Escreva a Palavra");
		txtFieldPalavra.setColumns(12);
		
		//Botão checar palavra
		JList<String> listRegrasUsadas = new JList<String>();
		btnChecarPalavra = new JButton("Checar Palavra");
		btnChecarPalavra.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Gramatica.clearRegrasUsadas();
				long start1 = System.currentTimeMillis();
				boolean hasPalavra = gramatica.verificarPalavra(txtFieldPalavra.getText(), "S");
				long end1 = System.currentTimeMillis();
				System.out.print(end1-start1);
				if(hasPalavra) {
					JOptionPane.showMessageDialog(null, "Essa palavra existe na gramática dada.", getTitle(), JOptionPane.INFORMATION_MESSAGE);
					listRegrasUsadas.setListData(new String[0]);
					listRegrasUsadas.setListData(Gramatica.getRegrasUsadas().toArray(new String[0]));
			
				}
				else {
					JOptionPane.showMessageDialog(null, "Essa palavra não existe na gramática dada.", getTitle(), JOptionPane.ERROR_MESSAGE);
					listRegrasUsadas.setListData(Gramatica.getRegrasUsadas().toArray(new String[0]));
				}
			}
		});
		btnChecarPalavra.setEnabled(false);
		
		JScrollPane panelRegrasUsadas = new JScrollPane();
		panelRegrasUsadas.setViewportView(listRegrasUsadas);
		
		JLabel lblRegrasUsadas = new JLabel("Regras de Produ\u00E7\u00E3o Usadas");
		lblRegrasUsadas.setVerticalAlignment(SwingConstants.BOTTOM);
		lblRegrasUsadas.setLabelFor(listRegrasUsadas);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
					.addGap(79)
					.addComponent(btnOlaMundo)
					.addGap(5)
					.addComponent(btnSelecionarGramatica)
					.addGap(5)
					.addComponent(txtArquivoSelecionado)
					.addGap(80))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(5)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(28)
							.addComponent(panelGramatica, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(5)
							.addComponent(panelPalavra, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblRegrasUsadas, GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE)
							.addGap(185)))
					.addGap(34))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(5)
					.addComponent(panelRegrasUsadas, GroupLayout.DEFAULT_SIZE, 461, Short.MAX_VALUE)
					.addGap(5))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(5)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(btnOlaMundo)
						.addComponent(btnSelecionarGramatica)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(1)
							.addComponent(txtArquivoSelecionado, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGap(5)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(panelGramatica, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(36)
							.addComponent(lblRegrasUsadas))
						.addComponent(panelPalavra, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
					.addGap(1)
					.addComponent(panelRegrasUsadas, GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
					.addGap(6))
		);
		GroupLayout gl_panelPalavra = new GroupLayout(panelPalavra);
		gl_panelPalavra.setHorizontalGroup(
			gl_panelPalavra.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelPalavra.createSequentialGroup()
					.addGap(1)
					.addComponent(txtFieldPalavra)
					.addGap(1)
					.addComponent(btnChecarPalavra)
					.addGap(1))
		);
		gl_panelPalavra.setVerticalGroup(
			gl_panelPalavra.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelPalavra.createSequentialGroup()
					.addGap(6)
					.addComponent(txtFieldPalavra, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_panelPalavra.createSequentialGroup()
					.addGap(5)
					.addComponent(btnChecarPalavra))
		);
		panelPalavra.setLayout(gl_panelPalavra);
		contentPane.setLayout(gl_contentPane);
	}
	
	private String lastPath = "";
	
	private void SelecionarArquivo() {
		JFileChooser janelaEscolherArquivo = new JFileChooser();

		{
			janelaEscolherArquivo.setAcceptAllFileFilterUsed(false); //Não pode escolher qualquer extensão
			janelaEscolherArquivo.addChoosableFileFilter(new FileNameExtensionFilter("txt", "TXT")); //Só pode escolher essas.
			janelaEscolherArquivo.setDialogTitle("Selecione a Gramática");
			janelaEscolherArquivo.setCurrentDirectory(new File(lastPath));

			if(janelaEscolherArquivo.showOpenDialog(this) == JFileChooser.APPROVE_OPTION ) {
				JanelaPrincipal.arquivo = janelaEscolherArquivo.getSelectedFile();
				lastPath = JanelaPrincipal.arquivo.getParent();
				try {
					JanelaPrincipal.gramatica = new Gramatica(new LeitorArquivo(JanelaPrincipal.arquivo.getAbsolutePath()).toHashMap());
					chckbxFormatado.setEnabled(true);
					btnChecarPalavra.setEnabled(true);
					txtArquivoSelecionado.setForeground(new Color( 0, 0, 0));
				} catch (Exception e) {
					chckbxFormatado.setSelected(false);
					chckbxFormatado.setEnabled(false);
					JOptionPane.showMessageDialog(janelaEscolherArquivo, e.getMessage(), getTitle(), JOptionPane.INFORMATION_MESSAGE);
					e.printStackTrace();
					txtArquivoSelecionado.setForeground(new Color( 212, 36, 36));
					btnChecarPalavra.setEnabled(false);
				}
			}
			
			
			
			if (JanelaPrincipal.arquivo != null) {
				txtArquivoSelecionado.setText(JanelaPrincipal.arquivo.getName());
				try {
					janelaExibicaoGramatica.setTexto(new LeitorArquivo(JanelaPrincipal.arquivo.getAbsolutePath()).getTexto());
				} catch (Exception e) {
					e.printStackTrace();
				}
				chckbxFormatado.addItemListener(new ItemListener() {
					@Override
					public void itemStateChanged(ItemEvent e) {
						if (chckbxFormatado.isSelected())
							janelaExibicaoGramatica.setTexto(JanelaPrincipal.gramatica.regrasToString());
						else
							try {
								janelaExibicaoGramatica.setTexto(new LeitorArquivo(JanelaPrincipal.arquivo.getAbsolutePath()).getTexto());
							} catch (Exception e1) {
								e1.printStackTrace();
							}
					}
				});

			}
		}
	
	}
	
	private void exibirGramatica() {
		if(janelaExibicaoGramatica.isVisible() == false) {
			janelaExibicaoGramatica.setVisible(true);
		}
		else janelaExibicaoGramatica.toFront();
		
	}
}
