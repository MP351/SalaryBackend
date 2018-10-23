package Database;

public class Income {
    private int _id;
    private long timestamp;
    private int amount;

    public Income(int _id, long timestamp, int amount) {
        this._id = _id;
        this.timestamp = timestamp;
        this.amount = amount;
    }

    public int get_id() {
        return _id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "ID: " + _id + " TS: " + timestamp + " Amount: " + amount;
    }
}
