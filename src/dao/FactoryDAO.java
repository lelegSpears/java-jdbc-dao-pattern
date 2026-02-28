package dao;

import db.DB;
import interfaces.DepartmentDAO;
import interfaces.SellerDAO;

public class FactoryDAO {

	public static SellerDAO createSellerDAO() {
		return new SellerDAOJDBC(DB.getConnection());
	}
	public static DepartmentDAO createDepartmentDAO() {
		return new DepartmentDAOJDBC(DB.getConnection());
	}
}
