package application;

import interfaces.SellerDAO;

import java.time.LocalDate;
import java.util.List;

import dao.FactoryDAO;
import entities.Department;
import entities.Seller;

public class Program {

	public static void main(String[] args) {
		SellerDAO selldao= FactoryDAO.createSellerDAO();
		Seller sell = new Seller(null, "jao", "jao@gmail", LocalDate.of(2003, 10, 2), 2000.30, new Department(1, "pc"));
		selldao.insert(sell);
		List<Seller> sl = selldao.findAll();
		for(Seller s : sl) {
			System.out.printf("id:%d nome:%s email:%s, Data de nascimento%s, Salario%.2f, Departamento%s \n",
			s.getId(),
			s.getName(),
		    s.getEmail(),
		    s.getBirthDate(),
		    s.getBaseSalary(),
		    s.getDepartment().getName());
			
		}
	}
}
