package repositories;

import config.DatabaseConfig;
import entities.FeeRule;
import enums.Modal;
import enums.TransactionType;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

public class FeeRuleRepositoryImpl implements FeeRuleRepository {

    private final Connection conn;

    public FeeRuleRepositoryImpl() {
        try {
            conn = DatabaseConfig.getInstance().getConnection();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean insert(FeeRule rule) {
        String sql = "insert into fee_rules(modal, rule_value, operation_type, is_active) values (?, ?, ?, ?) RETURNING id";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, rule.getModal().name());
            ps.setBigDecimal(2, rule.getRuleValue());
            ps.setString(3, rule.getOperationType().name());
            ps.setBoolean(4, rule.isActive());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                rule.setId((UUID) rs.getObject("id"));
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error inserting fee rule: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean update(FeeRule rule) {
        String sql = "update fee_rules set modal = ?, rule_value = ?, operation_type = ?, is_active = ? where id = ? ";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, rule.getModal().name());
            ps.setBigDecimal(2, rule.getRuleValue());
            ps.setString(3, rule.getOperationType().name());
            ps.setBoolean(4, rule.isActive());
            ps.setObject(5, rule.getId());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("Error updating fee rule: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean toggleStatus(UUID id, boolean active) {
        String sql = "update fee_rules set is_active = ? where id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, active);
            ps.setObject(2, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("Error toggling fee rule: " + e.getMessage());
        }
        return false;
    }

    @Override
    public List<FeeRule> findAll() {
        String sql = "select * from fee_rules order by operation_type, modal";
        List<FeeRule> list = new ArrayList<>();
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            System.out.println("Error listing fee rules: " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<FeeRule> findActiveByType(TransactionType type) {
        String sql = " select * from fee_rules where operation_type = ? and is_active = TRUE ";
        List<FeeRule> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, type.name());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            System.out.println("Error finding active rules: " + e.getMessage());
        }
        return list;
    }

    @Override
    public Optional<FeeRule> findById(UUID id) {
        String sql = "SELECT * FROM fee_rules WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(map(rs));
        } catch (SQLException e) {
            System.out.println("Error finding fee rule by id: " + e.getMessage());
        }
        return Optional.empty();
    }

    private FeeRule map(ResultSet rs) throws SQLException {
        FeeRule f = new FeeRule(
                Modal.valueOf(rs.getString("modal")),
                rs.getBigDecimal("rule_value"),
                TransactionType.valueOf(rs.getString("operation_type")),
                rs.getBoolean("is_active")
        );
        f.setId((UUID) rs.getObject("id"));
        return f;
    }
}