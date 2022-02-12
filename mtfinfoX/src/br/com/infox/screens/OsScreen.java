/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package br.com.infox.screens;

import java.sql.*;
import br.com.infox.dal.ConnectionModule;
import java.awt.HeadlessException;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author Marlon
 */
public class OsScreen extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    //a linha abaixo cria uma variável para armazenar o texto de acordo com o
    //radiobutton selecionado
    private String tipo = null;
    
    public OsScreen() {
        initComponents();
        conexao = ConnectionModule.conector();
    }
    
    public boolean isEmptyTxtField(JTextField textField){
        if(textField.getText().isBlank()){
            textField.requestFocus();
            return true;
        }else{
            return false;
        }
    }
    
    public void limpar_campos(){
        tblClientes.setModel(new DefaultTableModel());
        btnAdicionar.setEnabled(true);
        txtNumeroOs.setText(null);
        txtData.setText(null);
        buttonGroup1.clearSelection();
        tipo = null;
        cbxSituacao.setSelectedIndex(-1);
        txtEquipamento.setText(null);
        txtDefeito.setText(null);
        txtServico.setText(null);
        txtTecnico.setText(null);
        txtValorTotal.setText(null);
    }
    
    private void pesquisar_cliente(){
        if(!txtPesquisar.getText().isBlank()){
            String sql = "Select idcli Id, nomecli Nome, fonecli Fone from "
                    + "tbclientes where nomecli like ? order by nomecli "
                    + "limit 20";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtPesquisar.getText()+"%");
                rs = pst.executeQuery();
                tblClientes.setModel(DbUtils.resultSetToTableModel(rs));
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, e);
            }
        }else{
            limpar_campos();
        }
    }
    
    private void setar_campos(){
        if(!txtPesquisar.getText().isBlank()){
            int setar = tblClientes.getSelectedRow();
            txtId.setText(tblClientes.getModel().getValueAt(setar, 0)
                .toString());
        }
    }
    
    //método para cadastrar uma OS
    private void emitir_os(){
        if(!isEmptyTxtField(txtId) && !isEmptyTxtField(txtEquipamento) 
                && !isEmptyTxtField(txtDefeito) 
                && cbxSituacao.getSelectedIndex() != -1 && tipo != null){
            
            String sql = "insert into tbos (tipo, situacao, equipamento, "
                    + "defeito, servico, tecnico, valor, idcli) "
                    + "values(?,?,?,?,?,?,?,?)";
            try {
                pst=conexao.prepareStatement(sql);
                pst.setString(1, tipo);
                pst.setString(2, cbxSituacao.getSelectedItem().toString());
                pst.setString(3, txtEquipamento.getText());
                pst.setString(4, txtDefeito.getText());
                pst.setString(5, txtServico.getText());
                pst.setString(6, txtTecnico.getText());
                pst.setString(7, txtValorTotal.getText().replace(",", "."));
                pst.setInt(8, Integer.parseInt(txtId.getText()));
                if(pst.executeUpdate() >0){
                    JOptionPane.showMessageDialog(null, "OS emitida com sucesso");
                    limpar_campos();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e);
            }
        }else{
            JOptionPane.showMessageDialog(null, "Preencha todos os campos requeridos");
        }
            
    }

    private void pesquisar_os(){
        //a linha abaixo cria uma caixa de entrada do tipo JOption Pane
        String num_os = JOptionPane.showInputDialog("Número da OS");
        String sql = "select * from tbos where os = "+num_os;
        
        try {
            pst=conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            if(rs.next()){
                txtNumeroOs.setText(rs.getString("os"));
                txtData.setText(rs.getString("data_os"));
                //setando os radio buttons
                String rdbTipo = rs.getString("tipo");
                if(rdbTipo.equals("OS")){
                    rdbOrdemServico.setSelected(true);
                }else if(rdbTipo.equals("Orçamento")){
                    rdbOrcamento.setSelected(true);
                }
                cbxSituacao.setSelectedItem(rs.getString("situacao"));
                txtEquipamento.setText(rs.getString("equipamento"));
                txtDefeito.setText(rs.getString("defeito"));
                txtServico.setText(rs.getString("servico"));
                txtTecnico.setText(rs.getString("tecnico"));
                txtValorTotal.setText(rs.getString("valor"));
                txtId.setText(rs.getString("idcli"));
                //evitando problemas
                btnAdicionar.setEnabled(false);
                txtPesquisar.setEnabled(false);
                tblClientes.setVisible(false);
            }else{
                JOptionPane.showMessageDialog(null, "OS não cadastrada");
            }
        } catch (java.sql.SQLSyntaxErrorException e) {
            JOptionPane.showMessageDialog(null, "OS Inválida");
        } catch (Exception e2){
            JOptionPane.showMessageDialog(null, e2);
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

        txtEquipamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEquipamentoActionPerformed(evt);
            }
        });

        lblDefeito.setText("Defeito*");

        lblServico.setText("Serviço");

        lblTecnico.setText("Técnico");

        lblValorTotal.setText("Valor Total:");

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

        btnExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/images/delete.png"))); // NOI18N
        btnExcluir.setToolTipText("Remover");
        btnExcluir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/images/print.png"))); // NOI18N
        btnImprimir.setToolTipText("Imprimir");
        btnImprimir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
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

    private void txtEquipamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEquipamentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEquipamentoActionPerformed

    private void txtPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisarKeyReleased
        //Chamando o método pesquisar cliente
        pesquisar_cliente();
    }//GEN-LAST:event_txtPesquisarKeyReleased

    private void tblClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblClientesMouseClicked
        //Chamando o método setar campos
        setar_campos();
    }//GEN-LAST:event_tblClientesMouseClicked

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed
        // TODO add your handling code here:
        System.out.println(buttonGroup1.getSelection());
    }//GEN-LAST:event_btnImprimirActionPerformed

    private void rdbOrcamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdbOrcamentoActionPerformed
        // TODO add your handling code here:
        tipo = "Orçamento";
    }//GEN-LAST:event_rdbOrcamentoActionPerformed

    private void rdbOrdemServicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdbOrdemServicoActionPerformed
        // TODO add your handling code here:
        tipo = "OS";
    }//GEN-LAST:event_rdbOrdemServicoActionPerformed

    private void btnAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarActionPerformed
        // Chamando o método para emitir uma OS
        emitir_os();
    }//GEN-LAST:event_btnAdicionarActionPerformed

    private void btnConsultarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConsultarActionPerformed
        //Chama o método pesquisar_os
        pesquisar_os();
    }//GEN-LAST:event_btnConsultarActionPerformed


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
