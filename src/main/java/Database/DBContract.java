package Database;

public final class DBContract {

    public static class IncomeEntry {
        public static final String TABLE_NAME           = "income";
        public static final String _ID                  = "_id";
        public static final String COLUMN_NAME_DATE     = "date";
        public static final String COLUMN_NAME_AMOUNT   = "amount";

        public static final String SQL_CREATE_ENTRIES   = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY, "
                + COLUMN_NAME_DATE + " INTEGER NOT NULL, "
                + COLUMN_NAME_AMOUNT + " INTEGER NOT NULL)";

        public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class SalaryEntry {
        public static final String TABLE_NAME                       = "salary";
        public static final String _ID                              = "_id";
        public static final String COLUMN_NAME_DATE_OF_BEGIN        = "date_of_begin";
        public static final String COLUMN_NAME_AMOUNT               = "amount";

        public static final String SQL_CREATE_ENTRIES   = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY, "
                + COLUMN_NAME_AMOUNT + " INTEGER NOT NULL, "
                + COLUMN_NAME_DATE_OF_BEGIN + " TEXT NOT NULL)";

        public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class SalaryIncomeEntry {
        public static final String TABLE_NAME                       = "salary_income";
        public static final String _ID                              = "_id";
        public static final String COLUMN_NAME_MONTH_ENROLL         = "month_enroll";
        public static final String COLUMN_NAME_ID_OF_INCOME         = "_id_of_income";
        public static final String COLUMN_NAME_ID_OF_SALARY         = "_id_of_salary";
        public static final String COLUMN_NAME_AMOUNT               = "amount";

        public static final String SQL_CREATE_ENTRIES   = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY, "
                + COLUMN_NAME_MONTH_ENROLL + " TEXT NOT NULL, "
                + COLUMN_NAME_ID_OF_INCOME + " INTEGER NOT NULL, "
                + COLUMN_NAME_ID_OF_SALARY + " INTEGER NOT NULL, "
                + COLUMN_NAME_AMOUNT + " INTEGER NOT NULL)";

        public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
