/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifpb.tcc.loaders;

import br.edu.ifpb.tcc.beans.Tabela;
import br.edu.ifpb.tcc.beans.Tupla;
import br.edu.ifpb.tcc.persistencia.TabelaDAO;
import java.util.List;

/**
 *
 * @author Magdiel Ildefonso
 */
public class LoaderTabelaDAO {
    public static void main(String[] args) {
        Tabela t = new Tabela();
        t.setName("cadastro_das_ies_2011");
        List<Tupla> tuplas = TabelaDAO.getTuplas(t);
        
        for (Tupla tupla : tuplas) {
            System.out.println(tupla.getValores().get(1));
        }
    }
}
