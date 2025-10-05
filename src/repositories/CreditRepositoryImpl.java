package repositories;

import config.DatabaseConfig;
import entities.Credit;
import enums.CreditStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.*;

public class CreditRepositoryImpl implements CreditRepository {
    private Connection conn;

    public CreditRepositoryImpl() {
        try {
            this.conn = DatabaseConfig.getInstance().getConnection();
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error connecting to database: " + e.getMessage());
        }
    }

    @Override
    public boolean createCredit(Credit credit) {
        String sql = "INSERT INTO credits(account_id, amount, interest_rate, duration_months, status) " +
                "VALUES (?, ?, ?, ?, ?::credit_status) RETURNING id";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, credit.getAccountId());
            ps.setBigDecimal(2, credit.getAmount());
            ps.setBigDecimal(3, credit.getInterestRate());
            ps.setInt(4, credit.getDurationMonths());
            ps.setString(5, credit.getStatus().name());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                credit.setId((UUID) rs.getObject("id"));
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error creating credit: " + e.getMessage());
        }
        return false;
    }

    @Override
    public Optional<Credit> findById(UUID creditId) {
        String sql = "SELECT * FROM credits WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, creditId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapCredit(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error finding credit: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Credit> findByAccountId(String accountNumber) {
        String sql = "SELECT * FROM credits WHERE account_id = ? ORDER BY created_at DESC";
        List<Credit> credits = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                credits.add(mapCredit(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error finding credits by account: " + e.getMessage());
        }
        return credits;
    }

    @Override
    public List<Credit> findAll() {
        String sql = "SELECT * FROM credits ORDER BY created_at DESC";
        List<Credit> credits = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                credits.add(mapCredit(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error finding all credits: " + e.getMessage());
        }
        return credits;
    }

    @Override
    public List<Credit> findByStatus(CreditStatus status) {
        String sql = "SELECT * FROM credits WHERE status = ?::credit_status ORDER BY created_at DESC";
        List<Credit> credits = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                credits.add(mapCredit(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error finding credits by status: " + e.getMessage());
        }
        return credits;
    }

    @Override
    public boolean updateStatus(UUID creditId, CreditStatus status) {
        String sql = "UPDATE credits SET status = ?::credit_status WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setObject(2, creditId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("Error updating credit status: " + e.getMessage());
        }
        return false;
    }

    private Credit mapCredit(ResultSet rs) throws SQLException {
        Credit c = new Credit();
        c.setId((UUID) rs.getObject("id"));
        c.setAccountId(rs.getString("account_id"));
        c.setAmount(rs.getBigDecimal("amount"));
        c.setInterestRate(rs.getBigDecimal("interest_rate"));
        c.setDurationMonths(rs.getInt("duration_months"));
        c.setStatus(CreditStatus.valueOf(rs.getString("status")));
        c.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return c;
    }
}