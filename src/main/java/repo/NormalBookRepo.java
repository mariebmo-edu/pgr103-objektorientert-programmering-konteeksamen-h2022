package repo;

import model.NormalBook;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class NormalBookRepo extends AbstractRepo<NormalBook>{

    public NormalBookRepo() throws IOException {
        super("normal_book");
    }

    @Override
    public NormalBook resultMapper(ResultSet resultSet) throws SQLException {
        return new NormalBook(
                resultSet.getInt("id"),
                resultSet.getString("title"),
                resultSet.getString("author"),
                resultSet.getInt("number_of_hard_copies"),
                resultSet.getInt("loan_period")
        );
    }

    @Override
    public HashMap<String, Object> modelValues(NormalBook normalBook) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("title", normalBook.getTitle());
        map.put("author", normalBook.getAuthor());
        map.put("number_of_hard_copies", normalBook.getNumberOfHardCopies());
        map.put("loan_period", normalBook.getLoanPeriod());
        return map;
    }

    @Override
    public int getId(NormalBook normalBook) {
        return normalBook.getId();
    }
}
