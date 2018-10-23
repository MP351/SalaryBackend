package Database;

import JsonSerialization.Serializer;

import java.sql.*;
import java.util.*;

public class DBManager {
    private static DBManager manager = new DBManager();
    private Connection connection;
    private Statement statement;
    private ArrayList<SalaryIncome> sl;
    private HashMap<Integer, Income> incomeHM;
    private HashMap<Integer, Salary> salaryHM;

    private DBManager() {
        connect();
    }

    public static DBManager getInstance() {
        return manager;
    }


    private void connect() {
        String url = "jdbc:sqlite:src/main/resources/db.db";

        try  {
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();

            statement.execute(DBContract.IncomeEntry.SQL_CREATE_ENTRIES);
            statement.execute(DBContract.SalaryEntry.SQL_CREATE_ENTRIES);
            statement.execute(DBContract.SalaryIncomeEntry.SQL_CREATE_ENTRIES);
            cacheTables();

            System.out.println(Serializer.deserializeSalaryIncome(sl.get(0)));


            System.out.println("Connection to DB has been established");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void cacheTables() throws SQLException{
        sl = new ArrayList<>();
        salaryHM = new HashMap<>();
        incomeHM = new HashMap<>();
        String getSalariesSql = "SELECT * FROM salary";
        String getIncomesSql = "SELECT * FROM income";
        String getSalaryIncomeSql = "SELECT * FROM salary_income";

        ResultSet incomeRs = statement.executeQuery(getIncomesSql);
        while (incomeRs.next()) {
            Integer id = incomeRs.getInt(DBContract.IncomeEntry._ID);
            Long ts = incomeRs.getLong(DBContract.IncomeEntry.COLUMN_NAME_DATE);
            Integer amount = incomeRs.getInt(DBContract.IncomeEntry.COLUMN_NAME_AMOUNT);

            incomeHM.put(id, new Income(id, ts, amount));
        }

        ResultSet salaryRs = statement.executeQuery(getSalariesSql);
        while (salaryRs.next()) {
            Integer id = salaryRs.getInt(DBContract.SalaryEntry._ID);
            Integer amount = salaryRs.getInt(DBContract.SalaryEntry.COLUMN_NAME_AMOUNT);
            String date = salaryRs.getString(DBContract.SalaryEntry.COLUMN_NAME_DATE_OF_BEGIN);

            salaryHM.put(id, new Salary(id, new EnrollPeriod(date), amount));
        }

        ResultSet salaryIncomeRs = statement.executeQuery(getSalaryIncomeSql);
        while (salaryIncomeRs.next()) {
            Integer id = salaryIncomeRs.getInt(DBContract.SalaryIncomeEntry._ID);
            String  enroll = salaryIncomeRs.getString(DBContract.SalaryIncomeEntry.COLUMN_NAME_MONTH_ENROLL);
            Integer id_income = salaryIncomeRs.getInt(DBContract.SalaryIncomeEntry.COLUMN_NAME_ID_OF_INCOME);
            Integer id_salary = salaryIncomeRs.getInt(DBContract.SalaryIncomeEntry.COLUMN_NAME_ID_OF_SALARY);
            Integer amount = salaryIncomeRs.getInt(DBContract.SalaryIncomeEntry.COLUMN_NAME_AMOUNT);

            sl.add(new SalaryIncome(id, incomeHM.get(id_income),
                    salaryHM.get(id_salary), new EnrollPeriod(enroll), amount));
        }
    }

    private int writeIntoIncome(Long date, int amount) {
        int id = 0;
        String sql = "INSERT INTO " + DBContract.IncomeEntry.TABLE_NAME
                + "(" + DBContract.IncomeEntry.COLUMN_NAME_DATE + ", "
                + DBContract.IncomeEntry.COLUMN_NAME_AMOUNT + ") VALUES(?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, date);
            ps.setInt(2, amount);
            ps.executeUpdate();

            id = ps.getGeneratedKeys().getInt(1);
            cacheTables();
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public int writeIntoSalary(EnrollPeriod date, int amount) {
        int id = 0;
        String sql = "INSERT INTO " +DBContract.SalaryEntry.TABLE_NAME
                + "(" + DBContract.SalaryEntry.COLUMN_NAME_DATE_OF_BEGIN + ", "
                + DBContract.SalaryEntry.COLUMN_NAME_AMOUNT + ") VALUES(?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1, date.toString());
            ps.setInt(2, amount);
            id = ps.executeUpdate();

            cacheTables();
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int recordSalaryIncome(EnrollPeriod monthOfEnroll, int _id_of_income, int _id_of_salary, int amount) {
        String sql = "INSERT INTO " + DBContract.SalaryIncomeEntry.TABLE_NAME + "("
                + DBContract.SalaryIncomeEntry.COLUMN_NAME_MONTH_ENROLL + ", "
                + DBContract.SalaryIncomeEntry.COLUMN_NAME_ID_OF_INCOME + ", "
                + DBContract.SalaryIncomeEntry.COLUMN_NAME_ID_OF_SALARY + ", "
                + DBContract.SalaryIncomeEntry.COLUMN_NAME_AMOUNT + ") VALUES(?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, monthOfEnroll.toString());
            ps.setInt(2, _id_of_income);
            ps.setInt(3, _id_of_salary);
            ps.setInt(4, amount);
            ps.executeUpdate();

            cacheTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private EnrollPeriod getLastMonthEnroll() {
        return sl.get(sl.size()-1).getMonth_enroll();
    }

    public int getSumForEnroll(EnrollPeriod period) {
        String sql = "SELECT SUM(amount) as sum FROM salary_income where month_enroll = \"" + period.toString() + "\"";
        Integer sum = 0;
        try (ResultSet ps = statement.executeQuery(sql)) {
            sum = ps.getInt("sum");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sum;
        //return sl.stream().filter(s -> s.getMonth_enroll()
          //      .compareTo(period) == 0).mapToInt(s -> s.getAmount_of_enroll()).sum();
    }

    public Salary getSalaryForEnroll(EnrollPeriod period) {
        Salary sal = null;
        for (Integer id : salaryHM.keySet()) {
            if (period.compareTo(salaryHM.get(id).getBegin_ts()) == 0) {
                return salaryHM.get(id);
            } else if (period.compareTo(salaryHM.get(id).getBegin_ts()) > 0) {
                sal = salaryHM.get(id);
            } else return sal;
        }
        return sal;
    }

    private Salary getFirstSalary() {
        return salaryHM.entrySet().stream().min(
                (s, v) -> s.getValue().getBegin_ts().compareTo(v.getValue().getBegin_ts())).get().getValue();
    }

    public void proceed(int incomeAmount, Long date) {
        int incomeId = writeIntoIncome(date, incomeAmount);
        if (sl.size() > 0) {
            addRecord(getSalaryForEnroll(getLastMonthEnroll()), incomeId, incomeAmount, getLastMonthEnroll());
        } else if (salaryHM.size() > 0) {
            addRecord(getFirstSalary(), incomeId, incomeAmount, getFirstSalary().getBegin_ts());
        }
    }

    private void addRecord(Salary salary, int income_id, int amount, EnrollPeriod period) {
        Integer remainings = getSalaryForEnroll(period).getAmount() - getSumForEnroll(period);
        if (remainings > 0) {
            if (amount <= salary.getAmount()) {
                recordSalaryIncome(period, income_id, salary.get_id(), amount);
            } else {
                recordSalaryIncome(period, income_id, salary.get_id(), remainings);
                period.add(1);
                addRecord(getSalaryForEnroll(period), income_id, amount-remainings, period);
            }
        } else if (remainings == 0) {
            period.add(1);
            addRecord(getSalaryForEnroll(period), income_id, amount, period);
        }
    }
}
