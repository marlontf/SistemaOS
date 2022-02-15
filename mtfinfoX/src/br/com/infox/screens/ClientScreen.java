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
import java.awt.HeadlessException;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;

/**
 * Tela de gerenciamento de clientes
 *
 * @author Marlon Tavares
 * @version 1.0
 */
public class ClientScreen extends javax.swing.JInternalFrame {

    Connection conexao;
    PreparedStatement pst;
    ResultSet rs;

    /**
     * Cria uma nova tela para gerenciamento de clientes
     */
    public ClientScreen() {
        initComponents();
        conexao = ConnectionModule.conector();
    }
    
    /**
     * Verifica se o campo de texto indicado por parâmetro está vazio
     * @param textField
     * @return boolean
     */
    private boolean isEmpty(JTextField textField) {
        if (textField.getText().isBlank()) {
            textField.setText("");
            textField.requestFocus();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Realiza a insersão de um novo cliente no banco de dados
     */
    private void adicionar() {
        if (!isEmpty(txtNome) && !isEmpty(txtTelefone)) {
            String sql = "Insert into tbclientes (nomecli, endcli, fonecli,"
                    + " emailcli) values (?,?,?,?)";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtNome.getText());
                pst.setString(2, txtEndereco.getText());
                pst.setString(3, txtTelefone.getText());
                pst.setString(4, txtEmail.getText());
                if (pst.executeUpdate() != 0) {
                    JOptionPane.showMessageDialog(null, "Cliente cadastrado com"
                            + " sucesso");
                } else {
                    JOptionPane.showMessageDialog(null, "Houve um erro com o"
                            + " cadastro");
                }
                limpar_campos();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos"
                    + "obrigatórios");
        }
    }

    /**
     * realiza a pesquisa de clientes no banco de dados com base no nome
     */
    private void pesquisar_cliente() {
        if(!txtPesquisar.getText().isBlank()){
            try {
                String sql = "select idcli Id, nomecli Nome, endcli Endereço,"
                        + "fonecli Telefone, emailcli Email from tbclientes "
                        + "where nomecli like ? limit 20";
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtPesquisar.getText() + "%");
                rs = pst.executeQuery();
                tblClientes.setModel(DbUtils.resultSetToTableModel(rs));
                tblClientes.setDefaultEditor(Object.class, null);
                tblClientes.getTableHeader().setReorderingAllowed(false);
            } catch (SQLException e) {
                JOptionPane.showConfirmDialog(this, e);
            }
        }else{
            tblClientes.setModel(new DefaultTableModel());
            limpar_campos();
        }
        
        
    }

    /**
     * Preenche campos "Para edição ou exclusão" ao clicar em um cliente
     * selecionado na tabela de pesquisa
     */
    public void setar_campos() {
        int setar = tblClientes.getSelectedRow();
        txtNome.setText(tblClientes.getModel().getValueAt(setar, 1).toString());
        txtEndereco.setText(tblClientes.getModel().getValueAt(setar, 2).toString());
        txtTelefone.setText(tblClientes.getModel().getValueAt(setar, 3).toString());
        txtEmail.setText(tblClientes.getModel().getValueAt(setar, 4).toString());
        btnCadastrar.setEnabled(false);
    }

    /**
     * Realiza a alteração no banco de dados à partir do ID do cliente
     * selecinado na tabela de pesquisa
     */
    private void alterar() {
        int setar = 0;
        if(tblClientes.getSelectedRow() != -1){
            setar = (int)tblClientes.getModel().getValueAt(tblClientes.
                    getSelectedRow(), 0);
        }
        
        if (!isEmpty(txtNome) && !isEmpty(txtTelefone)) {
            String sql = "update tbclientes set nomecli=?, endcli=?, fonecli=?,"
                    + " emailcli=? where idcli = ?";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtNome.getText());
                pst.setString(2, txtEndereco.getText());
                pst.setString(3, txtTelefone.getText());
                pst.setString(4, txtEmail.getText());
                pst.setInt(5, setar);
                if (pst.executeUpdate() != 0) {
                    
                    JOptionPane.showMessageDialog(null, "Dados do cliente "
                            + "alterados com sucesso");
                    limpar_campos();
                } else {
                    JOptionPane.showMessageDialog(null, "Houve um erro com "
                            + "alteração");
                }
            } catch (HeadlessException | SQLException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos "
                    + "obrigatórios");
        }

    }

    /**
     * Realiza a remoção de uma linha no banco de dados à partir do ID 
     * do cliente selecinado na tabela de pesquisa
     */
    private void remover() {
        if(tblClientes.getSelectedRow() != -1){
            int setar = (int)tblClientes.getModel().getValueAt(tblClientes.
                    getSelectedRow(), 0);
            int confirmar = JOptionPane.showConfirmDialog(null, "Tem certeza "
                    + "que deseja remover este usuário?", "Atenção", 
                    JOptionPane.YES_NO_OPTION);
            if (confirmar == JOptionPane.YES_OPTION) {
                try {
                    String sql = "delete from tbclientes where idcli=?";
                    pst = conexao.prepareStatement(sql);
                    pst.setInt(1, setar);
                    if (pst.executeUpdate() != 0) {
                        JOptionPane.showMessageDialog(null, "Exclusão realizada"
                                + " com sucesso");
                        limpar_campos();
                    }
                } catch (SQLException e) {
                    JOptionPane.showConfirmDialog(this, e);
                }
            }
        }else{
            JOptionPane.showMessageDialog(null, "Pesquise e selecione um "
                    + "cliente antes da exclusão");
        }
        
    }
    
    /**
     * Limpa os campos JTextField, reseta o TableModel para Default e ativa
     * o botão de cadastro
     */
    private void limpar_campos(){
        txtNome.setText(null);
        txtEndereco.setText(null);
        txtTelefone.setText(null);
        txtEmail.setText(null);
        txtPesquisar.setText(null);
        tblClientes.setModel(new DefaultTableModel());
        btnCadastrar.setEnabled(true);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtPesquisar = new javax.swing.JTextField();
        lblIconPesquisa = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblClientes = new javax.swing.JTable();
        lblNome = new javax.swing.JLabel();
        txtNome = new javax.swing.JTextField();
        lblEndereco = new javax.swing.JLabel();
        txtEndereco = new javax.swing.JTextField();
        lblTelefone = new javax.swing.JLabel();
        txtTelefone = new javax.swing.JTextField();
        lblEmail = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        lblCampObr = new javax.swing.JLabel();
        btnCadastrar = new javax.swing.JButton();
        btnAlterar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Clientes");
        setPreferredSize(new java.awt.Dimension(640, 480));
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });

        txtPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesquisarKeyReleased(evt);
            }
        });

        lblIconPesquisa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/images/pesquisar.png"))); // NOI18N

        tblClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tblClientes.setFocusable(false);
        tblClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblClientesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblClientes);

        lblNome.setText("Nome*");

        lblEndereco.setText("Endereço");

        lblTelefone.setText("Telefone*");

        lblEmail.setText("Email");

        lblCampObr.setText("* Campos Obrigatórios");

        btnCadastrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/images/create.png"))); // NOI18N
        btnCadastrar.setToolTipText("Adicionar");
        btnCadastrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCadastrar.setPreferredSize(new java.awt.Dimension(70, 70));
        btnCadastrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadastrarActionPerformed(evt);
            }
        });

        btnAlterar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/images/update.png"))); // NOI18N
        btnAlterar.setToolTipText("Alterar");
        btnAlterar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAlterar.setPreferredSize(new java.awt.Dimension(70, 70));
        btnAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlterarActionPerformed(evt);
            }
        });

        btnExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/images/delete.png"))); // NOI18N
        btnExcluir.setToolTipText("Remover");
        btnExcluir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExcluir.setPreferredSize(new java.awt.Dimension(70, 70));
        btnExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 476, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblIconPesquisa)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTelefone)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblEndereco, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(lblNome))
                            .addComponent(lblEmail))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txtEmail)
                                    .addComponent(txtEndereco, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtNome, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                                .addComponent(lblCampObr)))))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCadastrar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64)
                .addComponent(btnAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64)
                .addComponent(btnExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(130, 130, 130))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(txtPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblIconPesquisa))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNome)
                    .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCampObr))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblEndereco))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTelefone))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblEmail))
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCadastrar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Chama o método adicionar()
     * @param evt 
     */
    private void btnCadastrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadastrarActionPerformed
        adicionar();
    }//GEN-LAST:event_btnCadastrarActionPerformed

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
     * Chama o método alterar()
     * @param evt 
     */
    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        alterar();
    }//GEN-LAST:event_btnAlterarActionPerformed

    /**
     * Chama o método remover()
     * @param evt 
     */
    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        remover();
    }//GEN-LAST:event_btnExcluirActionPerformed

    /**
     * Seta o foco no campo txtPesquisar quando o formulário receber o foco
     * @param evt 
     */
    private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained
        txtPesquisar.requestFocus();
    }//GEN-LAST:event_formFocusGained


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnCadastrar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCampObr;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblEndereco;
    private javax.swing.JLabel lblIconPesquisa;
    private javax.swing.JLabel lblNome;
    private javax.swing.JLabel lblTelefone;
    private javax.swing.JTable tblClientes;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtEndereco;
    private javax.swing.JTextField txtNome;
    private javax.swing.JTextField txtPesquisar;
    private javax.swing.JTextField txtTelefone;
    // End of variables declaration//GEN-END:variables
}
