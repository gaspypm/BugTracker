package service;

import DAO.DAOIncidencia;
import DAO.DAOException;
import model.Incidencia;
import model.Movimiento;

import java.util.ArrayList;

public class ServiceIncidencia {
    private DAOIncidencia daoIncidencia;

    public ServiceIncidencia() {
        daoIncidencia = new DAOIncidencia();
    }

    public void guardar(Incidencia incidencia) throws ServiceException {
        try {
            daoIncidencia.guardar(incidencia);
        }
        catch(DAOException d) {
            throw new ServiceException("Error");
        }
    }

    public Incidencia buscar(int id) throws DAOException {
        return daoIncidencia.buscar(id);
    }

    public ArrayList<Incidencia> buscarPorProyecto(int idProyecto) throws DAOException {
        return daoIncidencia.buscarPorProyecto(idProyecto);
    }

    public ArrayList<Incidencia> buscarTodos() throws DAOException {
        return daoIncidencia.buscarTodos();
    }

    public int obtenerUltimoID() throws DAOException {
        return daoIncidencia.obtenerUltimoID();
    }

    public void cerrar(Incidencia incidencia) throws DAOException {
        daoIncidencia.cerrar(incidencia);
    }

    public void modificar(Incidencia incidencia) throws DAOException {
        daoIncidencia.modificar(incidencia);
    }

    public void eliminar(int id) throws DAOException {
        daoIncidencia.eliminar(id);
    }
}
