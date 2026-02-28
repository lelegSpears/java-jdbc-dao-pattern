package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DBException;
import entities.Department;
import entities.Seller;
import interfaces.SellerDAO;

public class SellerDAOJDBC implements SellerDAO {

	private Connection conn;

	public SellerDAOJDBC(Connection conn) {
		this.conn = conn;
	}

	private Seller instantiateSeller(ResultSet rs, Department dep) {
		try {
			Seller sell = new Seller();
			sell.setId(rs.getInt("id"));
			sell.setName(rs.getString("name"));
			sell.setEmail(rs.getString("email"));
			sell.setBirthDate(rs.getDate("birthDate").toLocalDate());
			sell.setBaseSalary(rs.getDouble("baseSalary"));
			sell.setDepartment(dep);

			return sell;
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}

	private Department instantiateDepartment(ResultSet rs) {
		try {
			Department dep = new Department();
			dep.setId(rs.getInt("Department_id"));
			dep.setName(rs.getString("DepName"));

			return dep;
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}

	@Override
	public void insert(Seller obj) {
		try (PreparedStatement ps = conn.prepareStatement(
				"insert into Seller(name, email, birthDate, BaseSalary, department_id) values (?, ?, ?, ?, ?)",
				Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, obj.getName());
			ps.setString(2, obj.getEmail());
			ps.setDate(3, java.sql.Date.valueOf(obj.getBirthDate()));
			ps.setDouble(4, obj.getBaseSalary());
			ps.setInt(5, obj.getDepartment().getId());
			int la = ps.executeUpdate();
			if (la > 0) {
				try (ResultSet rs = ps.getGeneratedKeys()) {
					if (rs.next()) {
						obj.setId(rs.getInt(1));
					}
				}
			}
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}

	@Override
	public void update(Seller obj) {
		try (PreparedStatement ps = conn.prepareStatement(
				"UPDATE seller SET name = ?, email = ?, birthDate = ?, BaseSalary = ?, department_id = ? WHERE id = ?")) {
			ps.setString(1, obj.getName());
			ps.setString(2, obj.getEmail());
			ps.setDate(3, java.sql.Date.valueOf(obj.getBirthDate()));
			ps.setDouble(4, obj.getBaseSalary());
			ps.setInt(5, obj.getDepartment().getId());
			ps.setInt(6, obj.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}

	}

	@Override
	public void deleteById(Integer id) {
		try (PreparedStatement ps = conn.prepareStatement("Delete from seller where id = ?")) {
			ps.setInt(1, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}

	}

	@Override
	public Seller findById(Integer id) {
		try (PreparedStatement ps = conn
				.prepareStatement("SELECT Seller.*, Department.name as DepName" + " from Seller "
						+ "INNER JOIN Department on Department.id = Seller.department_Id " + "WHERE Seller.id = ?")) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					Department dep = instantiateDepartment(rs);
					Seller sell = instantiateSeller(rs, dep);
					return sell;
				}

				return null;
			}
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}

	@Override
	public List<Seller> findAll() {
		List<Seller> lista = new ArrayList<>();
		try (PreparedStatement ps = conn.prepareStatement("SELECT Seller.*, Department.name as DepName"
				+ " from Seller " + "INNER JOIN Department on Department.id = Seller.department_Id");
				ResultSet rs = ps.executeQuery()) {
			Map<Integer, Department> deps = new HashMap<>();
			while (rs.next()) {
				Integer idDep = rs.getInt("Department_id");
				Department dp = deps.get(idDep);
				if (dp == null) {
					dp = new Department(idDep, rs.getString("DepName"));
					deps.put(idDep, dp);
				}

				lista.add(instantiateSeller(rs, dp));
			}
			return lista;
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}
}
