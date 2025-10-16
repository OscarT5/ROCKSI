/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.avanti.desarollo.integration;

import jakarta.persistence.EntityManager;
import mx.avanti.desarollo.dao.*;
import mx.avanti.desarollo.persistence.HibernateUtil;
import mx.desarollo.entity.Clase;


/**
 *
 * @author total
 */
public class ServiceLocator {

    private static ClienteDAO ClienteDAO;
    private static ClaseDAO ClaseDAO;
    private static ProductoDAO ProductoDAO;
    //private static UsuarioDAO usuarioDAO;

    private static EntityManager getEntityManager(){
        return HibernateUtil.getEntityManager();
    }

    /**
     * se crea la instancia para Cliente DAO si esta no existe
     */
    public static ClienteDAO getInstanceClienteDAO(){
        if(ClienteDAO == null){
            ClienteDAO = new ClienteDAO(getEntityManager());
            return ClienteDAO;
        } else{
            return ClienteDAO;
        }
    }

    public static ClaseDAO getInstanceClaseDAO(){
        if(ClaseDAO == null){
            ClaseDAO = new ClaseDAO(getEntityManager());
            return ClaseDAO;
        } else{
            return ClaseDAO;
        }
    }

    public static ProductoDAO getInstanceProductoDAO(){
        if(ProductoDAO == null){
            ProductoDAO = new ProductoDAO(getEntityManager());
            return ProductoDAO;
        } else{
            return ProductoDAO;
        }
    }
    /**
     * se crea la instancia de usuarioDAO si esta no existe
     */
    /*
    public static UsuarioDAO getInstanceUsuarioDAO(){
        if(usuarioDAO == null){
            usuarioDAO = new UsuarioDAO(getEntityManager());
            return usuarioDAO;
        } else{
            return usuarioDAO;
        }
    }

     */

}
