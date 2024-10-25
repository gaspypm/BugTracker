package service;

import DAO.DAOAdministrador;
import DAO.DAOException;

import java.util.ArrayList;

public class ServiceAdministrador {
    private DAOAdministrador daoAdministrador;

    public ServiceAdministrador() {
        daoAdministrador = new DAOAdministrador();
    }

    public boolean crearUsuario(String usuario, String contrasena, String tipo) throws ServiceException {
        try {
            return daoAdministrador.crearUsuario(usuario, contrasena, tipo);
        }
        catch(DAOException d) {
            throw new ServiceException("Error");
        }
    }

    public ArrayList<Integer> obtenerPermisos(int idUsuario) throws ServiceException {
        try {
            return daoAdministrador.obtenerPermisos(idUsuario);
        }
        catch(DAOException d) {
            throw new ServiceException("Error");
        }
    }

    public void editarPermisos(int idUsuario, ArrayList<Integer> permisosSeleccionados) throws ServiceException {
        try {
            daoAdministrador.editarPermisos(idUsuario, permisosSeleccionados);
        }
        catch(DAOException d) {
            throw new ServiceException("Error");
        }
    }
}
