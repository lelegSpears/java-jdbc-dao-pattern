package application;

import java.util.List;

import dao.FactoryDAO;
import entities.Department;
import interfaces.DepartmentDAO;

public class ProgramDepartment {

    public static void main(String[] args) {

        DepartmentDAO depDao = FactoryDAO.createDepartmentDAO();

        // INSERT 
        Department newDep = new Department(null, "Games");
        depDao.insert(newDep);
        System.out.println("Inserido! Novo id = " + newDep.getId());

        // UPDATE
        newDep.setName("Game Development");
        depDao.update(newDep);
        System.out.println("Atualizado!");

        // FIND BY ID
        Department dep = depDao.findById(newDep.getId());
        System.out.println("Encontrado: " + dep);

        // FIND ALL
        List<Department> list = depDao.findAll();
        System.out.println("\nLista de departamentos:");
        for (Department d : list) {
            System.out.println(d);
        }

        // DELETE
        depDao.deleteById(newDep.getId());
        System.out.println("Departamento deletado!");
    }
}
