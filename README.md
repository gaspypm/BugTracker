# BugTracker (Java Desktop Application)

BugTracker is a Java desktop application designed to register and manage software incidents (bugs) across multiple projects, supporting different users/roles and a graphical user interface.
This repository is primarily intended as a portfolio project to demonstrate proficiency in Java, object-oriented design, layered architecture, and CRUD-style operations in a realistic domain.

## Main features
- User authentication (login) and role/permission management.
- CRUD operations for:
    - Users
    - Projects
    - Incidents
    - Incident lifecycle management, including creation, assignment, status updates, and closure.
    - Change tracking through “movements” (an activity/history log for traceability).
    - Local persistence through a DAO layer (database files are included in the repository).

## Architecture (layered structure)
The codebase is organized by responsibility to keep concerns separated:
- `model/`
  Domain entities such as Usuario, Administrador, Proyecto, Incidencia, and Movimiento.
- `DAO/`
  Data access and persistence logic (e.g., DAOUsuario, DAOAdministrador, DAOIncidencia), including error handling via DAOException.
- `service/`
  Business logic and validations before persistence (e.g., ServiceUsuario, ServiceProyecto, ServiceIncidencia), including ServiceException for service-layer errors.
- `gui/`
  Graphical user interface components (screens/forms) such as InicioSesion, FormularioUsuario, FormularioProyecto, and FormularioIncidencia.
- `iconos/`
  UI assets used by the application.

## How to run

### Requirements
- Java (JDK) 8+ (JDK 11/17 recommended).
- An IDE such as IntelliJ IDEA or Eclipse.

### Steps
1. Clone the repository:
```bash
git clone https://github.com/gaspypm/BugTracker.git
cd BugTracker
```
2. Open the project in your IDE.
3. Run the Main class.

Note: The repository includes local database files as an example (baseDeDatos.mv.db, baseDeDatos.trace.db). If you want to reset the stored data, rename or remove these files (this will delete existing data).

## What this project demonstrates
- Object-oriented modeling and domain-driven design basics.
- Clear separation of concerns across UI, service, DAO, and model layers.
- CRUD workflows with validation and error handling per layer.
- Practical incident tracking with audit/history capabilities.
