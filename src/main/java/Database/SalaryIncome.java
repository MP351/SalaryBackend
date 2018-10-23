package Database;

public class SalaryIncome {
    private int _id;
    private Income income;
    private Salary salary;
    private EnrollPeriod month_enroll;
    private int amount_of_enroll;

    public SalaryIncome(int _id, Income income, Salary salary, EnrollPeriod month_enroll, int amount_of_enroll) {
        this._id = _id;
        this.income = income;
        this.salary = salary;
        this.month_enroll = month_enroll;
        this.amount_of_enroll = amount_of_enroll;
    }

    public int get_id() {
        return _id;
    }

    public Income getIncome() {
        return income;
    }

    public Salary getSalary() {
        return salary;
    }

    public EnrollPeriod getMonth_enroll() {
        return month_enroll;
    }

    public int getAmount_of_enroll() {
        return amount_of_enroll;
    }
}
