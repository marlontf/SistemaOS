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

/**
 * Tela de gerenciamento de usuários
 *
 * @author Marlon Tavares
 * @version 1.0
 */
public class UserScreen extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    /**
     * Cria uma nova tela para gerenciamento de usuários
     */
    public UserScreen() {
        initComponents();
        conexao = ConnectionModule.conector();
    }

    /**
     * Realiza a consulta de um usuário baseado no ID
     */
    private void consultar() {
        String sql = "select * from tbusuarios where iduser = ?";

        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtId.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                txtNome.setText(rs.getString("usuario"));
                txtTelefone.setText(rs.getString("fone"));
                txtLogin.setText(rs.getString("login"));
                txtSenha.setText(rs.getString("senha"));
                cbPerfil.setSelectedItem(rs.getString("perfil"));
            } else {
                JOptionPane.showMessageDialog(null, "Usuário não cadastrado");
                txtNome.setText(null);
                txtTelefone.setText(null);
                txtLogin.setText(null);
                txtSenha.setText(null);
                cbPerfil.setSelectedIndex(-1);
            }
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(this, e);
        }

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
     * Realiza a inserção de um novo usuário
     */
    private void adicionar() {
        if (!isEmpty(txtId) 
                && !isEmpty(txtNome) 
                && !isEmpty(txtLogin) 
                && !isEmpty(txtSenha) 
                && cbPerfil.getSelectedIndex() != -1) {
            String sql = "Insert into tbusuarios (iduser, usuario, fone, "
                    + "login, senha, perfil) values (?,?,?,?,?,?)";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtId.getText());
                pst.setString(2, txtNome.getText());
                pst.setString(3, txtTelefone.getText());
                pst.setString(4, txtLogin.getText());
                pst.setString(5, new String(txtSenha.getPassword()));
                pst.setString(6, (String) cbPerfil.getSelectedItem());
                if (pst.executeUpdate() != 0) {
                    JOptionPane.showMessageDialog(null, "Usuário cadastrado "
                            + "com sucesso");
                } else {
                    JOptionPane.showMessageDialog(null, "Houve um erro com o "
                            + "cadastro");
                }
                txtId.setText(null);
                txtId.requestFocus();
                txtNome.setText(null);
                txtTelefone.setText(null);
                txtLogin.setText(null);
                txtSenha.setText(null);
                cbPerfil.setSelectedIndex(-1);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos "
                    + "obrigatórios");
        }
    }

    /**
     * Realiza a alteração (Update) de um usuário com base no ID
     */
    private void alterar() {
        if (!isEmpty(txtId) 
                && !isEmpty(txtNome) 
                && !isEmpty(txtLogin) 
                && !isEmpty(txtSenha) 
                && cbPerfil.getSelectedIndex() != -1) {
            String sql = "update tbusuarios set usuario=?, fone=?, login=?, "
                    + "senha=?, perfil=? where iduser=?";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtNome.getText());
                pst.setString(2, txtTelefone.getText());
                pst.setString(3, txtLogin.getText());
                pst.setString(4, new String(txtSenha.getPassword()));
                pst.setString(5, (String) cbPerfil.getSelectedItem());
                pst.setString(6, txtId.getText());
                if (pst.executeUpdate() != 0) {
                    txtId.setText(null);
                    txtId.requestFocus();
                    txtNome.setText(null);
                    txtTelefone.setText(null);
                    txtLogin.setText(null);
                    txtSenha.setText(null);
                    cbPerfil.setSelectedIndex(-1);
                    JOptionPane.showMessageDialog(null, "Dados do usuário "
                            + "alterados com sucesso");
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
     * Realiza a remoção de um usuário com base no ID
     */
    private void remover() {
        int confirmar = JOptionPane.showConfirmDialog(null, "Tem certeza que "
                + "deseja remover este usuário?", "Atenção", 
                JOptionPane.YES_NO_OPTION);
        if (confirmar == JOptionPane.YES_OPTION) {
            try {
                String sql = "delete from tbusuarios where iduser=?";
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtId.getText());
                if (pst.executeUpdate() != 0) {
                    txtId.setText(null);
                    txtId.requestFocus();
                    txtNome.setText(null);
                    txtTelefone.setText(null);
                    txtLogin.setText(null);
                    txtSenha.setText(null);
                    cbPerfil.setSelectedIndex(-1);
                    JOptionPane.showMessageDialog(null, "Exclusão realizada "
                            + "com sucesso");
                }
            } catch (SQLException e) {
                JOptionPane.showConfirmDialog(this, e);
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

        lblId = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        lblNome = new javax.swing.JLabel();
        txtNome = new javax.swing.JTextField();
        lblTelefone = new javax.swing.JLabel();
        txtTelefone = new javax.swing.JTextField();
        lblLogin = new javax.swing.JLabel();
        txtLogin = new javax.swing.JTextField();
        lblSenha = new javax.swing.JLabel();
        txtSenha = new javax.swing.JPasswordField();
        lblPerfil = new javax.swing.JLabel();
        cbPerfil = new javax.swing.JComboBox<>();
        btnCreate = new javax.swing.JButton();
        btnRead = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        lblCampObr = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Usuários");
        setPreferredSize(new java.awt.Dimension(640, 480));
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });

        lblId.setText("Id*");

        lblNome.setText("Nome*");

        lblTelefone.setText("Telefone");

        lblLogin.setText("Login*");

        lblSenha.setText("Senha*");

        lblPerfil.setText("Perfil*");

        cbPerfil.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "admin", "user" }));
        cbPerfil.setSelectedIndex(-1);
        cbPerfil.setMinimumSize(new java.awt.Dimension(80, 22));

        btnCreate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/images/create.png"))); // NOI18N
        btnCreate.setToolTipText("Adicionar");
        btnCreate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCreate.setPreferredSize(new java.awt.Dimension(80, 80));
        btnCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateActionPerformed(evt);
            }
        });

        btnRead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/images/read.png"))); // NOI18N
        btnRead.setToolTipText("Consultar");
        btnRead.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRead.setPreferredSize(new java.awt.Dimension(80, 80));
        btnRead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReadActionPerformed(evt);
            }
        });

        btnUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/images/update.png"))); // NOI18N
        btnUpdate.setToolTipText("Alterar");
        btnUpdate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUpdate.setPreferredSize(new java.awt.Dimension(80, 80));
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/images/delete.png"))); // NOI18N
        btnDelete.setToolTipText("Remover");
        btnDelete.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDelete.setPreferredSize(new java.awt.Dimension(80, 80));
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        lblCampObr.setText("* Campos Obrigatórios");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnCreate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addComponent(btnRead, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 168, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblId)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblLogin)
                                    .addComponent(lblSenha)
                                    .addComponent(lblPerfil)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblTelefone, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(lblNome)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtNome, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                                    .addComponent(txtTelefone)
                                    .addComponent(txtLogin, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                                    .addComponent(txtSenha)
                                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblCampObr)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblId)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCampObr))
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNome)
                    .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTelefone))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblLogin)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(102, 102, 102)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblSenha)
                            .addComponent(txtSenha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblPerfil)
                            .addComponent(cbPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 80, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCreate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRead, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUpdate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(67, 67, 67))
        );

        setBounds(0, 0, 640, 480);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Chama o evento consultar()
     * @param evt 
     */
    private void btnReadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReadActionPerformed
        consultar();
    }//GEN-LAST:event_btnReadActionPerformed

    /**
     * Chama o evento adicioanr()
     * @param evt 
     */
    private void btnCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateActionPerformed
        adicionar();
    }//GEN-LAST:event_btnCreateActionPerformed

    /**
     * Direciona o cursor para o campo de ID quando o formulário ganha foco
     * @param evt 
     */
    private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained
        txtId.requestFocus();
    }//GEN-LAST:event_formFocusGained

    /**
     * Chama o método alterar()
     * @param evt 
     */
    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        alterar();
    }//GEN-LAST:event_btnUpdateActionPerformed

    /**
     * Chama o método remover()
     * @param evt 
     */
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        remover();
    }//GEN-LAST:event_btnDeleteActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCreate;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnRead;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox<String> cbPerfil;
    private javax.swing.JLabel lblCampObr;
    private javax.swing.JLabel lblId;
    private javax.swing.JLabel lblLogin;
    private javax.swing.JLabel lblNome;
    private javax.swing.JLabel lblPerfil;
    private javax.swing.JLabel lblSenha;
    private javax.swing.JLabel lblTelefone;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtLogin;
    private javax.swing.JTextField txtNome;
    private javax.swing.JPasswordField txtSenha;
    private javax.swing.JTextField txtTelefone;
    // End of variables declaration//GEN-END:variables
}
