package service;

import DAO.DAOProyecto;
import DAO.DAOException;
import model.Proyecto;

import java.util.ArrayList;

public class ServiceProyecto {
    private DAOProyecto daoProyecto;

    public ServiceProyecto() {
        daoProyecto = new DAOProyecto();
    }

    public void guardar(Proyecto proyecto) throws ServiceException {
        try {
            daoProyecto.guardar(proyecto);
        }
        catch (DAOException e) {
            throw new ServiceException("Error");
        }
    }

    public int obtenerID(String nombreProyecto) throws ServiceException {
        try {
            return daoProyecto.obtenerID(nombreProyecto);
        }
        catch(DAOException d) {
            throw new ServiceException("Error");
        }
    }

    public ArrayList<Proyecto> buscarTodos() throws ServiceException {
        try {
            return daoProyecto.buscarTodos();
        }
        catch (DAOException d) {
            throw new ServiceException("Error");
        }
    }

    public int obtenerUltimoID() throws ServiceException {
        try {
            return daoProyecto.obtenerUltimoID();
        }
        catch (DAOException d) {
            throw new ServiceException("Error");
        }
    }

    public Proyecto buscar(int id) throws ServiceException {
        try {
            return daoProyecto.buscar(id);
        }
        catch (DAOException d) {
            throw new ServiceException("Error");
        }
    }
}
