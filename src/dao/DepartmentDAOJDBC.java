package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DBException;
import entities.Department;
import interfaces.DepartmentDAO;

public class DepartmentDAOJDBC implements DepartmentDAO {

	private Connection conn;

	public DepartmentDAOJDBC(Connection conn) {
		this.conn = conn;
	}

	private Department instantiateDepartment(ResultSet rs) {
		try {
			Department dep = new Department();
			dep.setId(rs.getInt("id"));
			dep.setName(rs.getString("name"));

			return dep;
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}

	@Override
	public void insert(Department obj) {
		try (PreparedStatement ps = conn.prepareStatement("Insert into Department(name) values(?)",
				Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, obj.getName());
			int rowsAffected = ps.executeUpdate();

			if (rowsAffected > 0) {
				try (ResultSet rs = ps.getGeneratedKeys()) {
					if (rs.next()) {
						obj.setId(rs.getInt(1));
					}
				}
			} else {
				throw new DBException("Algo deu errado, nenhuma linha afetada. ");
			}
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}

	@Override
	public void update(Department obj) {
		try (PreparedStatement ps = conn.prepareStatement("Update Department set name = ? WHERE id = ?")) {
			ps.setString(1, obj.getName());
			ps.setInt(2, obj.getId());
			int rowsAffected = ps.executeUpdate();

			if (rowsAffected == 0) {
				throw new DBException("Nenhuma linha foi alterada.");
			}
			System.out.printf("Mudança finalizada! Linhas alteradas: %d", rowsAffected);
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}

	@Override
	public void deleteById(Integer id) {
		try (PreparedStatement ps = conn.prepareStatement("Delete from Department WHERE id = ?")) {
			ps.setInt(1, id);
			int rowsAffected = ps.executeUpdate();
			if (rowsAffected == 0) {
				throw new DBException("Nenhuma linha foi alterada.");
			}
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}

	@Override
	public Department findById(Integer id) {
		try (PreparedStatement ps = conn.prepareStatement("SELECT * from Department WHERE id = ?")) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					Department dep = instantiateDepartment(rs);
					return dep;
				}

				return null;
			}
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}

	@Override
	public List<Department> findAll() {
		List<Department> list = new ArrayList<>();
		try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM Department ORDER BY id");
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				list.add(instantiateDepartment(rs));
			}

			return list;
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}

}
