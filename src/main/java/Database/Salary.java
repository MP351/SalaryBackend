package Database;

public class Salary {
    private int _id;
    private EnrollPeriod beginPeriod;
    private int amount;

    public Salary(int _id, EnrollPeriod begin_ts, int amount) {
        this._id = _id;
        this.beginPeriod = begin_ts;
        this.amount = amount;
    }

    public int get_id() {
        return _id;
    }

    public EnrollPeriod getBegin_ts() {
        return beginPeriod;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "ID: " + _id + " Begin: " + beginPeriod + " Amount: " + amount;
    }
}
