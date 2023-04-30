package repo;

import model.LoanRecord;
import model.LoanType;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.HashMap;

public class LoanRecordRepo extends AbstractRepo<LoanRecord>{
    public LoanRecordRepo() throws IOException {
        super("loan_record");
    }

    public LoanRecord getLoanRecordByBookIdAndType(int bookId, LoanType loanType) throws SQLException {
        try(Connection con = dataSource.getConnection();
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM loan_record WHERE book_id = " + bookId + " AND loan_type = '" + loanType + "'")) {
            if(resultSet.next()) {
                return resultMapper(resultSet);
            }
        }
        return null;
    }


    @Override
    public LoanRecord resultMapper(ResultSet resultSet) throws SQLException {
        return new LoanRecord(
                resultSet.getInt("id"),
                resultSet.getString("user_name"),
                resultSet.getInt("book_id"),
                LoanType.valueOf(resultSet.getString("loan_type"))
        );
    }

    @Override
    public HashMap<String, Object> modelValues(LoanRecord loanRecord) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("user_name", loanRecord.getUserName());
        map.put("book_id", loanRecord.getBookId());
        map.put("loan_type", loanRecord.getLoanType().toString());
        return map;
    }

    @Override
    public int getId(LoanRecord loanRecord) {
        return loanRecord.getId();
    }

}
