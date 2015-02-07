/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpb.tcc.loaders;

import br.edu.ifpb.tcc.beans.Local;
import br.edu.ifpb.tcc.beans.Mapeamento;
import br.edu.ifpb.tcc.beans.ObjectSearchXML;
import br.edu.ifpb.tcc.beans.Ponto;
import br.edu.ifpb.tcc.beans.Tabela;
import br.edu.ifpb.tcc.beans.Tupla;
import br.edu.ifpb.tcc.fachada.Fachada;
import br.edu.ifpb.tcc.persistencia.GazetteerDAO;
import br.edu.ifpb.tcc.persistencia.TabelaDAO;
import br.edu.ifpb.tcc.xml.ReadFileXML;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Magdiel Ildefonso
 */
public class LoaderBuscaGeometria {

    public static void main(String[] args) {
        Fachada fachada = new Fachada();

        //RECUPERANDO TUPLAS DA TABELA cadastro_das_ies_2011
        Tabela tabela = new Tabela();
        tabela.setName("cadastro_das_ies_2011");
        List<Tupla> tuplas = TabelaDAO.getTuplas(tabela);

        //VARRENDO AS TUPLAS
        for (Tupla tupla : tuplas) {
            //VARRENDO TUPLA EM BUSCA DE REFERÊNCIAS DE LUGARES
            List<Local> locais = new ArrayList<>();
            for (String valor : tupla.getValores()) {
                List<Local> locais2 = GazetteerDAO.recuperaLocais(valor);
                locais.addAll(locais2);
            }
            System.out.println("Achei todos os lugares");

            //CONSTRUINDO MAPEAMENTO COM OS LOCAIS ENCONTRADOS NA TUPLA
            Mapeamento mapeamento = new Mapeamento();
            mapeamento = fachada.construirMapeamento(locais);
            System.out.println("Já foi mapeado");

            //RECUPERANDO GEOMETRIA MAIS INTERNA
            Local local = fachada.geometriaMaisInterna(mapeamento);
            System.out.println("Local mais interno: "+local.getNome()+" Tipo: "+local.getTipo());

            //RECUPERANDO PONTO
            Ponto ponto = GazetteerDAO.recuperaPonto(local);
            System.out.println(ponto);

            //VARRENDO TUPLA E BUSCA DE POSSIVEIS VALORES PARA A QUERY
            for (String valor : tupla.getValores()) {
                //VERIFICANDO SE O VALOR É DIFERENTE DOS VALORES MAPEADOS, PARA NÃO PEGAR GEOMETRIA DO MACRO;
                System.out.println("Valor do segundo for "+valor);
                //SE O VALOR NÃO É UM LOCAL EXISTENTE NO GAZZETER
                if (GazetteerDAO.recuperaLocais(valor).size() <= 0) {
                    ReadFileXML read = new ReadFileXML(valor, ponto.getLatitude(), ponto.getLongitude());
                    ObjectSearchXML ob = read.readFileReturnXMLObject();
                    
                    System.out.println("Valor da Query: "+valor);
                    
                    //VERIFICA SE O RETORNO DO WIKI É DIFERENTE DE NULO
                    if (ob != null) {
                        //VERIFICANDO SE FOI ENCONTRADO O RESULTADO PROCURADO
                        if(ob.getTitle() != null){
                            System.out.println("Title vindo do wiki: "+ob.getTitle());
                            if (ob.getTitle().contains(valor)) {
                                for (String point : ob.getPoints()) {
                                    System.out.println(point);
                                }
                                //TabelaDAO.persisteTheGeom(tabela, valor, tupla.getId());
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
}
