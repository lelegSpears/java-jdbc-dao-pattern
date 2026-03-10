package application;

import java.time.LocalDate;
import java.util.List;

import dao.FactoryDAO;
import entities.Department;
import entities.Seller;
import interfaces.SellerDAO;

public class ProgramSeller {

    public static void main(String[] args) {

        SellerDAO sellDao = FactoryDAO.createSellerDAO();

        // INSERT
        Seller newSeller = new Seller(
                null,
                "Leandro",
                "leandro@gmail.com",
                LocalDate.of(2000, 5, 10),
                3500.0,
                new Department(1, null)
        );

        sellDao.insert(newSeller);
        System.out.println("Inserido! Novo id = " + newSeller.getId());

        // UPDATE
        newSeller.setName("Leandro Atualizado");
        newSeller.setBaseSalary(4200.0);
        sellDao.update(newSeller);
        System.out.println("Atualizado!");

        // FIND BY ID
        Seller sell = sellDao.findById(newSeller.getId());
        System.out.println("\nEncontrado:");
        System.out.println(sell);

        // FIND ALL
        List<Seller> list = sellDao.findAll();

        System.out.println("\nLista de vendedores:");
        for (Seller s : list) {
            System.out.printf(
                    "id:%d nome:%s email:%s nascimento:%s salario:%.2f departamento:%s\n",
                    s.getId(),
                    s.getName(),
                    s.getEmail(),
                    s.getBirthDate(),
                    s.getBaseSalary(),
                    s.getDepartment().getName()
            );
        }

        // DELETE 
        sellDao.deleteById(newSeller.getId());
        System.out.println("\nSeller deletado!");
    }
}
