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
		setResizable(false);
		// Informações Janela
		setTitle("Leitor de Gram\u00E1tica");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 497, 279);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		
		
		
		//Informações Olá Mundo		
		JButton btnOlaMundo = new JButton("Ol\u00E1 Mundo");
		btnOlaMundo.setBounds(84, 10, 83, 23);
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
		contentPane.setLayout(null);
		contentPane.add(btnOlaMundo);
		
		//BotãoSelecionar Gramática
		JButton btnSelecionarGramatica = new JButton("Selecionar Gram\u00E1tica");
		btnSelecionarGramatica.setBounds(172, 10, 133, 23);
		btnSelecionarGramatica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SelecionarArquivo();
			}
		});
		contentPane.add(btnSelecionarGramatica);
		
		//Campo que mostra o arquivo selecionado
		txtArquivoSelecionado = new JTextField();
		txtArquivoSelecionado.setBounds(310, 11, 86, 20);
		txtArquivoSelecionado.setBackground(new Color(255, 255, 255));
		txtArquivoSelecionado.setText("...");
		txtArquivoSelecionado.setEditable(false);
		contentPane.add(txtArquivoSelecionado);
		txtArquivoSelecionado.setColumns(10);
		
		panelGramatica = new JPanel();
		panelGramatica.setBounds(38, 38, 185, 37);
		FlowLayout flowLayoutPanelGramatica = (FlowLayout) panelGramatica.getLayout();
		flowLayoutPanelGramatica.setHgap(1);
		panelGramatica.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		contentPane.add(panelGramatica);
		
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
		panelPalavra.setBounds(228, 38, 214, 37);
		FlowLayout flowLayoutPanelPalavra = (FlowLayout) panelPalavra.getLayout();
		flowLayoutPanelPalavra.setHgap(1);
		panelPalavra.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		contentPane.add(panelPalavra);
		
		txtFieldPalavra = new JTextField();
		txtFieldPalavra.setHorizontalAlignment(SwingConstants.CENTER);
		txtFieldPalavra.setText("Escreva a Palavra");
		panelPalavra.add(txtFieldPalavra);
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
		panelPalavra.add(btnChecarPalavra);
		
		JScrollPane panelRegrasUsadas = new JScrollPane();
		panelRegrasUsadas.setBounds(10, 89, 461, 140);
		contentPane.add(panelRegrasUsadas);
		panelRegrasUsadas.setViewportView(listRegrasUsadas);
	}
	
	private void SelecionarArquivo() {
		JFileChooser janelaEscolherArquivo = new JFileChooser();

		{
			janelaEscolherArquivo.setAcceptAllFileFilterUsed(false); //Não pode escolher qualquer extensão
			janelaEscolherArquivo.addChoosableFileFilter(new FileNameExtensionFilter("txt", "TXT")); //Só pode escolher essas.
			janelaEscolherArquivo.setDialogTitle("Selecione a Gramática");
			janelaEscolherArquivo.setCurrentDirectory(new File("D:\\Gabriel\\Downloads"));

			if(janelaEscolherArquivo.showOpenDialog(this) == JFileChooser.APPROVE_OPTION ) {
				JanelaPrincipal.arquivo = janelaEscolherArquivo.getSelectedFile();
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
