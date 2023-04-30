package repo;

import model.AudioBook;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class AudioBookRepo extends AbstractRepo<AudioBook>{

    public AudioBookRepo() throws IOException {
        super("audio_book");
    }

    @Override
    public AudioBook resultMapper(ResultSet resultSet) throws SQLException {
        return new AudioBook(
                resultSet.getInt("id"),
                resultSet.getString("title"),
                resultSet.getString("author"),
                resultSet.getInt("free_trial_period")
        );
    }

    @Override
    public HashMap<String, Object> modelValues(AudioBook audioBook) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("title", audioBook.getTitle());
        map.put("author", audioBook.getAuthor());
        map.put("free_trial_period", audioBook.getFreeTrialPeriod());
        return map;
    }

    @Override
    public int getId(AudioBook audioBook) {
        return audioBook.getId();
    }
}
