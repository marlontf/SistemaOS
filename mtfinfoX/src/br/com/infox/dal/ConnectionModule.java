/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infox.dal;

import java.sql.*;

/**
 *
 * @author Marlon
 */
public class ConnectionModule {

    //Método responsável por estabelecer a conexão com o banco
    public static Connection conector() {
        Connection conexao = null;
        //A linha abaixo chama o driver importado
        String driver = "org.mariadb.jdbc.Driver";
        //Armazenando informações referentes ao banco
        String url = "jdbc:mariadb://localhost:3306/dbinfox";
        String user = "root";
        String password = "123456";
        //Estabelecendo conexão com o banco
        try {
            Class.forName(driver);
            conexao = DriverManager.getConnection(url, user, password);
            return conexao;
        } catch (Exception e){
            //a linha abaixo serve de apoio pra esclarecer o erro de conexao
            //System.out.println(e);
            return null;
        }
    }
}
