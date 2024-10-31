package service;

import DAO.DAOAdministrador;
import DAO.DAOException;

import java.util.ArrayList;

public class ServiceAdministrador {
    private DAOAdministrador daoAdministrador;

    public ServiceAdministrador() {
        daoAdministrador = new DAOAdministrador();
    }

    public void crearUsuario(int id, String usuario, char[] contrasena, String tipo) throws ServiceException {
        try {
            daoAdministrador.crearUsuario(id, usuario, contrasena, tipo);
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
