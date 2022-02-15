/*
 * The MIT License
 *
 * Copyright 2022 Marlon Tavares.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package br.com.infox.screens;

import java.sql.*;
import br.com.infox.dal.ConnectionModule;
import java.util.HashMap;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 * Tela de gerenciamentod e OS
 *
 * @author Marlon Tavares
 * @version 1.0
 */
public class OsScreen extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    private String tipo = null;

    /**
     * Cria uma nova tela para gerenciamento de OS
     */
    public OsScreen() {
        initComponents();
        conexao = ConnectionModule.conector();
    }

    /**
     * Verifica se o campo de texto indicado por parâmetro está vazio
     * @param textField
     * @return boolean
     */
    public boolean isEmptyTxtField(JTextField textField) {
        if (textField.getText().isBlank()) {
            textField.requestFocus();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Desabilita os botões de Editar, Excluir e Imprimir
     * Habilita o botões de Adicionar e o campo de Pesquisa de cliente
     * Limpa os campos do formulário
     * Ativa a tabela de pesquisa
     */
    public void limpar_campos() {
        btnEditar.setEnabled(false);
        btnExcluir.setEnabled(false);
        btnImprimir.setEnabled(false);
        btnAdicionar.setEnabled(true);
        txtPesquisar.setEnabled(true);
        tblClientes.setModel(new DefaultTableModel());
        txtPesquisar.setText(null);
        txtId.setText(null);
        tblClientes.setVisible(true);
        txtNumeroOs.setText(null);
        txtData.setText(null);
        buttonGroup1.clearSelection();
        tipo = null;
        cbxSituacao.setSelectedIndex(-1);
        txtEquipamento.setText(null);
        txtDefeito.setText(null);
        txtServico.setText(null);
        txtTecnico.setText(null);
        txtValorTotal.setText("0.00");
    }

    /**
     * Realiza pesquisa de cliente para iserção na OS com base no nome
     * informado, limitando a 20 linhas de resultado
     */
    private void pesquisar_cliente() {
        if (!txtPesquisar.getText().isBlank()) {
            String sql = "Select idcli Id, nomecli Nome, fonecli Fone from "
                    + "tbclientes where nomecli like ? order by nomecli "
                    + "limit 20";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtPesquisar.getText() + "%");
                rs = pst.executeQuery();
                tblClientes.setModel(DbUtils.resultSetToTableModel(rs));
                tblClientes.setDefaultEditor(Object.class, null);
                tblClientes.getTableHeader().setReorderingAllowed(false);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, e);
            }
        }
    }

    /**
     * Realiza a seleção do ID do cliente para a OS
     */
    private void setar_campos() {
        int setar = tblClientes.getSelectedRow();
        txtId.setText(tblClientes.getModel().getValueAt(setar, 0)
                .toString());
    }

    /**
     * Realiza a inserção da OS informada no banco de dados
     */
    private void emitir_os() {
        if (!isEmptyTxtField(txtId) 
                && !isEmptyTxtField(txtEquipamento)
                && !isEmptyTxtField(txtDefeito) 
                && !isEmptyTxtField(txtValorTotal) 
                && cbxSituacao.getSelectedIndex() != -1 
                && tipo != null) {

            String sql = "insert into tbos (tipo, situacao, equipamento, "
                    + "defeito, servico, tecnico, valor, idcli) "
                    + "values(?,?,?,?,?,?,?,?)";
            try {
                pst = conexao.prepareStatement(sql, 
                        Statement.RETURN_GENERATED_KEYS);
                pst.setString(1, tipo);
                pst.setString(2, cbxSituacao.getSelectedItem().toString());
                pst.setString(3, txtEquipamento.getText());
                pst.setString(4, txtDefeito.getText());
                pst.setString(5, txtServico.getText());
                pst.setString(6, txtTecnico.getText());
                pst.setString(7, txtValorTotal.getText().replace(",", "."));
                pst.setInt(8, Integer.parseInt(txtId.getText()));
                if (pst.executeUpdate() > 0) {
                    ResultSet rs2 = pst.getGeneratedKeys();
                    if (rs2.next()) {
                        String os = rs2.getString(1);                        
                        JOptionPane.showMessageDialog(null, "<html>OS nº: "
                                + "<b style='color:red'>" + os 
                                + "</b> emitida com sucesso</html>");
                        pesquisar_os(os);
                    }
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, e);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Preencha todos os "
                    + "campos requeridos");
        }

    }

    /**
     * Cria uma sobrecarga da função pesquisar_os() caso o parâmetro não
     * tenha sido informado na chamada do método
     */
    private void pesquisar_os(){
        this.pesquisar_os(null);
    }
    
    /**
     * Realiza a pesquisa de OS no banco de dados com base no número da OS
     * @param os 
     */
    private void pesquisar_os(String os) {
        String num_os = null;
        if (os != null){
            num_os = os;
        }else{
            num_os = JOptionPane.showInputDialog("Número da OS");
        }
        
        String sql = "select * from vw_tbos where os = " + num_os;
        if (num_os != null) {
            try {
                pst = conexao.prepareStatement(sql);
                rs = pst.executeQuery();
                if (rs.next()) {
                    txtNumeroOs.setText(rs.getString("os"));
                    txtData.setText(rs.getString("data_os"));
                    String rdbTipo = rs.getString("tipo");
                    if (rdbTipo.equals("OS")) {
                        rdbOrdemServico.setSelected(true);
                        tipo = "OS";
                    } else if (rdbTipo.equals("Orçamento")) {
                        rdbOrcamento.setSelected(true);
                        tipo = "Orçamento";
                    }
                    cbxSituacao.setSelectedItem(rs.getString("situacao"));
                    txtEquipamento.setText(rs.getString("equipamento"));
                    txtDefeito.setText(rs.getString("defeito"));
                    txtServico.setText(rs.getString("servico"));
                    txtTecnico.setText(rs.getString("tecnico"));
                    txtValorTotal.setText(rs.getString("valor"));
                    txtId.setText(rs.getString("idcli"));
                    btnAdicionar.setEnabled(false);
                    tblClientes.setVisible(false);
                    btnEditar.setEnabled(true);
                    btnExcluir.setEnabled(true);
                    btnImprimir.setEnabled(true);
                    txtPesquisar.setEnabled(false);
                } else {
                    JOptionPane.showMessageDialog(null, "OS não cadastrada");
                }
            } catch (java.sql.SQLSyntaxErrorException e) {
                JOptionPane.showMessageDialog(null, "OS Inválida");
            } catch (SQLException e2) {
                JOptionPane.showMessageDialog(null, e2);
            }
        }

    }

    /**
     * Realiza a alteração (Update) de uma OS no banco de dados com base no
     * número da OS
     */
    private void alterar_os() {
        if (!isEmptyTxtField(txtId) 
                && !isEmptyTxtField(txtEquipamento)
                && !isEmptyTxtField(txtDefeito) 
                && !isEmptyTxtField(txtValorTotal)
                && cbxSituacao.getSelectedIndex() != -1 
                && tipo != null) {
            String sql = "update tbos set tipo=?, situacao=?, equipamento=?"
                    + ",defeito=?, servico=?, tecnico=?, valor=? where os=?";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, tipo);
                pst.setString(2, cbxSituacao.getSelectedItem().toString());
                pst.setString(3, txtEquipamento.getText());
                pst.setString(4, txtDefeito.getText());
                pst.setString(5, txtServico.getText());
                pst.setString(6, txtTecnico.getText());
                pst.setString(7, txtValorTotal.getText());
                pst.setString(8, txtNumeroOs.getText());
                if (pst.executeUpdate() > 0) {
                    JOptionPane.showMessageDialog(null, "Os alterada com "
                            + "sucesso");
                    limpar_campos();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, e);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos "
                    + "obrigatórios");
        }
    }

    /**
     * Realiza a exclusão de uma OS com base no número da OS
     */
    private void excluir_os() {
        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que "
                + "deseja excluir esta OS?", "Atenção", 
                JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            try {
                String sql = "delete from tbos where os=?";
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtNumeroOs.getText());
                if (pst.executeUpdate() > 0) {
                    JOptionPane.showMessageDialog(null, "OS Excluída "
                            + "com sucesso");
                    limpar_campos();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, e);
            }

        }

    }

    /**
     * Realiza a impressão de uma OS com base no número da OS utilizando-se do
     * framework JasperSoft "iReport"
     */
    private void imprimir_os(){
        int confirma = JOptionPane.showConfirmDialog(null, "Confirma a "
                + "impressão desta OS?", "Atenção", JOptionPane.YES_NO_OPTION);
        if(confirma == JOptionPane.YES_OPTION){
            try {
                HashMap filtro = new HashMap();
                filtro.put("os",Integer.parseInt(txtNumeroOs.getText()));
                JasperPrint print = JasperFillManager.fillReport("C:\\Users"
                        + "\\marlon\\Documents\\development\\Java\\sistemaos"
                        + "\\reports\\os.jasper",filtro,conexao);
                JasperViewer.viewReport(print, false);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e);
            }
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        lblNumeroOs = new javax.swing.JLabel();
        txtNumeroOs = new javax.swing.JTextField();
        lblData = new javax.swing.JLabel();
        txtData = new javax.swing.JTextField();
        rdbOrcamento = new javax.swing.JRadioButton();
        rdbOrdemServico = new javax.swing.JRadioButton();
        lblSituacao = new javax.swing.JLabel();
        cbxSituacao = new javax.swing.JComboBox<>();
        pnlCliente = new javax.swing.JPanel();
        txtPesquisar = new javax.swing.JTextField();
        lblPesquisar = new javax.swing.JLabel();
        lblId = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        JScroolPanel = new javax.swing.JScrollPane();
        tblClientes = new javax.swing.JTable();
        lblEquipamento = new javax.swing.JLabel();
        txtEquipamento = new javax.swing.JTextField();
        lblDefeito = new javax.swing.JLabel();
        txtDefeito = new javax.swing.JTextField();
        lblServico = new javax.swing.JLabel();
        txtServico = new javax.swing.JTextField();
        lblTecnico = new javax.swing.JLabel();
        txtTecnico = new javax.swing.JTextField();
        lblValorTotal = new javax.swing.JLabel();
        txtValorTotal = new javax.swing.JTextField();
        btnAdicionar = new javax.swing.JButton();
        btnConsultar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnImprimir = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("OS");
        setPreferredSize(new java.awt.Dimension(640, 480));

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setPreferredSize(new java.awt.Dimension(255, 120));

        lblNumeroOs.setText("Numero OS");

        txtNumeroOs.setEnabled(false);

        lblData.setText("Data");

        txtData.setEnabled(false);

        buttonGroup1.add(rdbOrcamento);
        rdbOrcamento.setText("Orçamento");
        rdbOrcamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdbOrcamentoActionPerformed(evt);
            }
        });

        buttonGroup1.add(rdbOrdemServico);
        rdbOrdemServico.setText("Ordem de Serviço");
        rdbOrdemServico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdbOrdemServicoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(rdbOrcamento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblNumeroOs)
                    .addComponent(txtNumeroOs))
                .addGap(8, 8, 8)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rdbOrdemServico)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblData)
                            .addComponent(txtData, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNumeroOs)
                    .addComponent(lblData))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNumeroOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rdbOrcamento)
                    .addComponent(rdbOrdemServico))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        lblSituacao.setText("Situação*");

        cbxSituacao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Aguardando aprovação", "Aguardando peças", "Abandonado pelo cliente", "Entraga OK", "Na bancada", "Orçamento REPROVADO", "Retornou" }));
        cbxSituacao.setSelectedIndex(-1);

        pnlCliente.setBorder(javax.swing.BorderFactory.createTitledBorder("Cliente"));

        txtPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesquisarKeyReleased(evt);
            }
        });

        lblPesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/images/pesquisar.png"))); // NOI18N

        lblId.setText("Id*");

        txtId.setEnabled(false);

        tblClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Title 1"
            }
        ));
        tblClientes.setFocusable(false);
        tblClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblClientesMouseClicked(evt);
            }
        });
        JScroolPanel.setViewportView(tblClientes);

        javax.swing.GroupLayout pnlClienteLayout = new javax.swing.GroupLayout(pnlCliente);
        pnlCliente.setLayout(pnlClienteLayout);
        pnlClienteLayout.setHorizontalGroup(
            pnlClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlClienteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlClienteLayout.createSequentialGroup()
                        .addComponent(txtPesquisar, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblPesquisar)
                        .addGap(12, 12, 12)
                        .addComponent(lblId)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(JScroolPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlClienteLayout.setVerticalGroup(
            pnlClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlClienteLayout.createSequentialGroup()
                .addGroup(pnlClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblPesquisar)
                    .addComponent(txtPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblId)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JScroolPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
        );

        lblEquipamento.setText("Equipamento*");

        lblDefeito.setText("Defeito*");

        lblServico.setText("Serviço");

        lblTecnico.setText("Técnico");

        lblValorTotal.setText("Valor Total*");

        txtValorTotal.setText("0");

        btnAdicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/images/create.png"))); // NOI18N
        btnAdicionar.setToolTipText("Adicionar");
        btnAdicionar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarActionPerformed(evt);
            }
        });

        btnConsultar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/images/read.png"))); // NOI18N
        btnConsultar.setToolTipText("Pesquisar");
        btnConsultar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnConsultar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConsultarActionPerformed(evt);
            }
        });

        btnEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/images/update.png"))); // NOI18N
        btnEditar.setToolTipText("Alterar");
        btnEditar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEditar.setEnabled(false);
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        btnExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/images/delete.png"))); // NOI18N
        btnExcluir.setToolTipText("Remover");
        btnExcluir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExcluir.setEnabled(false);
        btnExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirActionPerformed(evt);
            }
        });

        btnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/images/print.png"))); // NOI18N
        btnImprimir.setToolTipText("Imprimir");
        btnImprimir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnImprimir.setEnabled(false);
        btnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblEquipamento)
                            .addComponent(lblDefeito)
                            .addComponent(lblServico)
                            .addComponent(lblTecnico))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtTecnico)
                                .addGap(18, 18, 18)
                                .addComponent(lblValorTotal)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtValorTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtServico)
                            .addComponent(txtDefeito)
                            .addComponent(txtEquipamento)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblSituacao)
                                .addGap(32, 32, 32)
                                .addComponent(cbxSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(btnAdicionar)
                .addGap(23, 23, 23)
                .addComponent(btnConsultar)
                .addGap(23, 23, 23)
                .addComponent(btnEditar)
                .addGap(23, 23, 23)
                .addComponent(btnExcluir)
                .addGap(23, 23, 23)
                .addComponent(btnImprimir)
                .addContainerGap(11, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblSituacao)
                            .addComponent(cbxSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(6, 6, 6))
                    .addComponent(pnlCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEquipamento)
                    .addComponent(txtEquipamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDefeito)
                    .addComponent(txtDefeito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblServico)
                    .addComponent(txtServico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTecnico)
                    .addComponent(txtTecnico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblValorTotal)
                    .addComponent(txtValorTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAdicionar, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnConsultar, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnEditar, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnExcluir, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnImprimir, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap(46, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Chama o método pesquisar_cliente()
     * @param evt 
     */
    private void txtPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisarKeyReleased
        pesquisar_cliente();
    }//GEN-LAST:event_txtPesquisarKeyReleased

    /**
     * Chama o método setar_campos()
     * @param evt 
     */
    private void tblClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblClientesMouseClicked
        setar_campos();
    }//GEN-LAST:event_tblClientesMouseClicked

    /**
     * Chama o método imprimir_os()
     * @param evt 
     */
    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed
        imprimir_os();
    }//GEN-LAST:event_btnImprimirActionPerformed

    /**
     * Seta o campo tipo como "Orçamento" ao clicar no respectivo radiobutton
     * @param evt 
     */
    private void rdbOrcamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdbOrcamentoActionPerformed
        tipo = "Orçamento";
    }//GEN-LAST:event_rdbOrcamentoActionPerformed

    /**
     * Seta o campo tipo como "OS" ao clicar no respectivo radiobutton
     * @param evt 
     */
    private void rdbOrdemServicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdbOrdemServicoActionPerformed
        tipo = "OS";
    }//GEN-LAST:event_rdbOrdemServicoActionPerformed

    /**
     * Chama o método emitir_os()
     * @param evt 
     */
    private void btnAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarActionPerformed
        emitir_os();
    }//GEN-LAST:event_btnAdicionarActionPerformed

    /**
     * Chama o método pesquisar_os()
     * @param evt 
     */
    private void btnConsultarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConsultarActionPerformed
        pesquisar_os();
    }//GEN-LAST:event_btnConsultarActionPerformed

    /**
     * Chama o método alterar_os()
     * @param evt 
     */
    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        alterar_os();
    }//GEN-LAST:event_btnEditarActionPerformed

    /**
     * Chama o método excluir_os()
     * @param evt 
     */
    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        excluir_os();
    }//GEN-LAST:event_btnExcluirActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane JScroolPanel;
    private javax.swing.JButton btnAdicionar;
    private javax.swing.JButton btnConsultar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnImprimir;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cbxSituacao;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblData;
    private javax.swing.JLabel lblDefeito;
    private javax.swing.JLabel lblEquipamento;
    private javax.swing.JLabel lblId;
    private javax.swing.JLabel lblNumeroOs;
    private javax.swing.JLabel lblPesquisar;
    private javax.swing.JLabel lblServico;
    private javax.swing.JLabel lblSituacao;
    private javax.swing.JLabel lblTecnico;
    private javax.swing.JLabel lblValorTotal;
    private javax.swing.JPanel pnlCliente;
    private javax.swing.JRadioButton rdbOrcamento;
    private javax.swing.JRadioButton rdbOrdemServico;
    private javax.swing.JTable tblClientes;
    private javax.swing.JTextField txtData;
    private javax.swing.JTextField txtDefeito;
    private javax.swing.JTextField txtEquipamento;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtNumeroOs;
    private javax.swing.JTextField txtPesquisar;
    private javax.swing.JTextField txtServico;
    private javax.swing.JTextField txtTecnico;
    private javax.swing.JTextField txtValorTotal;
    // End of variables declaration//GEN-END:variables
}
