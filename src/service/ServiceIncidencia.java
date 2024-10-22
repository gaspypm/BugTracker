package service;

import DAO.DAOIncidencia;
import DAO.DAOException;
import model.Incidencia;

import java.util.ArrayList;

public class ServiceIncidencia {
    private DAOIncidencia daoIncidencia;

    public ServiceIncidencia() {
        daoIncidencia = new DAOIncidencia();
    }
    public void guardar(Incidencia elemento) throws ServiceException {
        try {
            daoIncidencia.guardar(elemento);
        }
        catch(DAOException d) {
            throw new ServiceException("Error");
        }
    }

    public ArrayList<Incidencia> buscarTodos() throws DAOException {
        return daoIncidencia.buscarTodos();
    }

    public Incidencia buscar(int id) throws DAOException {
        return daoIncidencia.buscar(id);
    }

    /*public void modificar(Incidencia elemento) {
        daoIncidencia.modificar(elemento);
    }
    public void eliminar(int id) {
        daoIncidencia.eliminar(id);
    }*/
}
