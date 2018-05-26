package br.edu.etec.lojainformatica;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.hibernate.Session;

import br.edu.etec.lojainformatica.model.Cliente;
import br.edu.etec.lojainformatica.persistence.ClienteHibernateDAO;
import br.edu.etec.lojainformatica.persistence.ClienteJdbcDAO;
import br.edu.etec.lojainformatica.persistence.Hibernateutil;
import br.edu.etec.lojainformatica.persistence.JdbcUtil;

public class TelaCadClientes extends TelaDeCadastro{

	Cliente cliente = new Cliente();
	
	JLabel lbNome = new JLabel("Nome");
	JTextField txtNome = new JTextField();
	
	JLabel lbEndereco = new JLabel("Endereço");
	JTextField txtEndereco = new JTextField();
	
	JLabel lbFone = new JLabel("Fone");
	JTextField txtFone = new JTextField();
	
	JLabel lbEmail = new JLabel("Email");
	JTextField txtEmail = new JTextField();
	
	public TelaCadClientes() {
		super(4, 2);
		
		this.painelParaCampos.add(lbNome);
		this.painelParaCampos.add(txtNome);
		
		this.painelParaCampos.add(lbEndereco);
		this.painelParaCampos.add(txtEndereco);
		
		this.painelParaCampos.add(lbFone);
		this.painelParaCampos.add(txtFone);
		
		this.painelParaCampos.add(lbEmail);
		this.painelParaCampos.add(txtEmail);
		System.out.println("terminando de adicionar os campos, add afora um actionlistener");

		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setPreferredSize(new Dimension(250, 80));
		this.painelListagem.add(list);
		
		this.btnLimpar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TelaCadClientes.this.limparFormulario();
			}
		});
	
		this.btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					TelaCadClientes.this.Salvar();
				} catch(Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		this.btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TelaCadClientes.this.Cancelar();
			}
		});
		
		this.btnAlterar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					TelaCadClientes.this.Alterar();
				} catch(Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		this.btnListar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					TelaCadClientes.this.Listar();
				} catch(SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		
	}	
	
	@Override
	void limparFormulario() {
		System.out.println("void limparFormulario() {...");
		this.txtNome.setText("");
		this.txtEndereco.setText("");
		this.txtFone.setText("");
		this.txtEmail.setText("");
	}
	
	@Override
	void Salvar() {
		String salvarOuAlterar = "salvar";
		
		// o botao salvar vai salvar ou alterar. Se tiver id ele alterar se nao ele
		// salva
		String id = this.txtId.getText();
		int idInt = -1;
		
		try {
			idInt = Integer.parseInt(id);
			salvarOuAlterar = "alterar"; // se deu pra convertar num int entao altera
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			/*this.cliente.setNome(this.txtNome.getText());
			this.cliente.setEndereco(this.txtEndereco.getText());
			this.cliente.setFone(this.txtFone.getText());
			this.cliente.setEmail(this.txtEmail.getText());
			Connection connection = JdbcUtil.getConnection();
			ClienteJdbcDAO clienteJdbcDAO = new ClienteJdbcDAO(connection);*/
			Cliente c1 = new Cliente();
			c1.setNome(txtNome.getText());
			c1.setEndereco(txtEndereco.getText());
			c1.setFone(txtFone.getText());
			c1.setEmail(txtEmail.getText());
			
			
			if(salvarOuAlterar.equals("salvar")) {
				//clienteJdbcDAO.salvar(this.cliente);
				Session session = Hibernateutil.getSessionFactory().openSession();
				ClienteHibernateDAO<Cliente> clienteDao = new ClienteHibernateDAO<Cliente>(session);
				clienteDao.persistir(c1);
				clienteDao.commit();
				clienteDao.closeSession();
			} else {
				this.cliente.setId_cliente(idInt);
				//clienteJdbcDAO.alterar(this.cliente);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	void Cancelar() {
		this.setVisible(false);
	}
	
	@Override
	void Alterar() {
		String id = this.txtId.getText();
		try {
			int idInt = Integer.parseInt(id);
			Connection conn = JdbcUtil.getConnection();
			ClienteJdbcDAO clienteJdbcDAO = new ClienteJdbcDAO(conn);
			Cliente cli = clienteJdbcDAO.findById(idInt);
			if (cli != null) {
				this.txtNome.setText(cli.getNome());
				this.txtEndereco.setText(cli.getEndereco());
				this.txtFone.setText(cli.getFone());
				this.txtEmail.setText(cli.getEmail());
			} else {
				JOptionPane.showMessageDialog(this, "Não há clientes com esse ID");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	void Excluir() throws SQLException{
		String id = this.txtId.getText();
		try {
			int idInt = Integer.parseInt(id);
			Connection conn = JdbcUtil.getConnection();
			ClienteJdbcDAO clienteJdbcDAO = new ClienteJdbcDAO(conn);
			clienteJdbcDAO.Excluir(idInt);
			this.limparFormulario();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	void Listar() throws SQLException{
		Connection conn;
		try {
			conn = JdbcUtil.getConnection();
			ClienteJdbcDAO clienteJdbcDAO = new ClienteJdbcDAO(conn);
			List<Cliente> list = clienteJdbcDAO.listar();
			String[] strArr = new String[list.size()];
			for(int i=0; i < list.size(); i++) {
				String id= list.get(i).getId_cliente().toString();
				String nome = list.get(i).getNome();
				strArr[i] = id + " - " + nome;
			}
			this.list.setListData(strArr);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
