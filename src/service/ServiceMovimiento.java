package service;

import DAO.DAOException;
import DAO.DAOMovimiento;
import model.Movimiento;

import java.util.ArrayList;

public class ServiceMovimiento {
    private DAOMovimiento daoMovimiento;

    public ServiceMovimiento() {
        daoMovimiento = new DAOMovimiento();
    }

    public void guardar(Movimiento movimiento) throws ServiceException {
        try {
            daoMovimiento.guardar(movimiento);
        }
        catch(DAOException d) {
            throw new ServiceException("Error");
        }
    }

    public ArrayList<Movimiento> buscarTodos() throws ServiceException {
        try {
            return daoMovimiento.buscarTodos();
        }
        catch (DAOException d) {
            throw new ServiceException("Error");
        }
    }

    public ArrayList<Movimiento> buscarPorIncidencia(int idIncidencia) throws DAOException {
        return daoMovimiento.buscarPorIncidencia(idIncidencia);
    }


}
