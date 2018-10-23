package JsonSerialization;

import Database.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.InputStreamReader;

public class Serializer {
    public static void serializeIncome(JsonWriter writer, Income income) throws IOException {
        writer.beginObject();
        writer.name("_id").value(income.get_id());
        writer.name("timestamp").value(income.getTimestamp());
        writer.name("amount").value(income.getAmount());
        writer.endObject();
    }

    private static Income deserializeIncome(JsonReader reader) throws IOException {
        int _id = 0;
        long ts = 0;
        int amount = 0;

        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("_id")) {
                _id = reader.nextInt();
            } else if (name.equals("timestamp")) {
                ts = reader.nextLong();
            } else if (name.equals("amount")) {
                amount = reader.nextInt();
            }
        }

        return new Income(_id, ts, amount);
    }

    public static void serializeSalary(JsonWriter writer, Salary salary) throws IOException {
        writer.beginObject();
        writer.name("_id").value(salary.get_id());
        writer.name("beginPeriod").value(salary.getBegin_ts().toString());
        writer.name("amount").value(salary.getAmount());
        writer.endObject();
    }

    public static Salary deserializeSalary(JsonReader reader) throws IOException {
        int _id = 0;
        String beginPeriod = "";
        int amount = 0;

        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("_id")) {
                _id = reader.nextInt();
            } else if (name.equals("beginPeriod")) {
                beginPeriod = reader.nextString();
            } else if (name.equals("amount")) {
                amount = reader.nextInt();
            } else reader.skipValue();
        }

        return new Salary(_id, new EnrollPeriod(beginPeriod), amount);
    }

    public static String deserializeSalaryIncome(SalaryIncome salaryIncome) {
        Gson gson = new Gson();
        return gson.toJson(salaryIncome);
    }

    public static void deserialize(InputStreamReader inputStreamReader) throws IOException {
        JsonReader reader = new JsonReader(inputStreamReader);
        DBManager manager = DBManager.getInstance();
        reader.beginObject();

            String name = reader.nextName();
            if (name.equals("type")) {
                String identifier = reader.nextString();
                if (identifier.equals("income")) {
                    Income income = deserializeIncome(reader);
                    manager.proceed(income.getAmount(), income.getTimestamp());
                } else if (identifier.equals("salary")) {
                    Salary salary = deserializeSalary(reader);
                    manager.writeIntoSalary(salary.getBegin_ts(), salary.getAmount());
                }
            }

        reader.endObject();
    }
}
