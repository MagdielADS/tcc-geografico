/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpb.tcc.persistencia;

import br.edu.ifpb.tcc.beans.Tabela;
import br.edu.ifpb.tcc.persistencia.util.Conexao;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Magdiel Ildefonso
 */
public class TabelaDAO {

    private static Connection connection;

    public static void createTable(Tabela t) {
        String sql = "create table " + t.getName() + "(gid serial,";
        sql = sql + criaColunas(t.getColumns());
        sql = sql + "the_geom geometry, primary key(gid))";

        connection = Conexao.getInstance().createConnection();
        PreparedStatement pst;
        try {
            pst = connection.prepareStatement(sql);
            pst.execute();
            pst.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(TabelaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static String criaColunas(List<String> colunas) {
        String sql = " ";

        for (String col : colunas) {
            sql += col + " VARCHAR, ";
        }

        return sql;
    }

    public static void persisteArquivo(Tabela t, List<String> dados) {
        createTable(t);
        String sql = geraQueryInsercao(t);

        connection = Conexao.getInstance().createConnection();
        PreparedStatement pst;
        dados.remove(0);
        try {
            pst = connection.prepareStatement(sql);
            Calendar data = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH_mm_ss");
            String dataFormat = df.format(data.getTime());
            BufferedWriter writer = new BufferedWriter(new FileWriter(t.getName() + "-gerado-" + dataFormat + ".txt"));
            String novaLinha = "";
            int qtde=0;

            for (String dado : dados) {
                String[] d = dado.split(";");
                if (d.length == t.getColumns().size()) {
                    for (int i = 0; i < d.length; i++) {
                        pst.setString(i + 1, d[i]);
                    }
                    pst.execute();
                } else {
                    qtde++;
                    novaLinha += dado;
                    novaLinha += System.getProperty("line.separator");
                    writer.write(novaLinha);
                }
            }
            
            writer.close();
            pst.close();
            connection.close();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(TabelaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static String geraQueryInsercao(Tabela t) {
        String sql = "insert into " + t.getName() + "(";
        for (String atributo : t.getColumns()) {
            sql = sql + " " + atributo + ",";
        }
        sql = sql.substring(0, sql.length() - 1) + ") ";
        sql = sql + "values (";

        for (Object object : t.getColumns()) {
            sql = sql + "?,";
        }
        sql = sql.substring(0, sql.length() - 1) + ")";
        return sql;
    }
}
