/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifpb.tcc.beans;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Magdiel Ildefonso
 */
public class Tabela {
    private String nome;
    private int gid;
    private List<String> colunas;

    public Tabela(){
        colunas = new ArrayList<>();
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }
    
    public String getName() {
        return nome;
    }

    public void setName(String name) {
        this.nome = name;
    }

    public List<String> getColumns() {
        return colunas;
    }

    public void setColumns(List<String> columns) {
        this.colunas = columns;
    }
    
    public void addColunas(String c){
        this.colunas.add(c);
    }

    @Override
    public String toString() {
        return "Tabela{" + "nome=" + nome + ", colunas=" + colunas + '}';
    }
}
