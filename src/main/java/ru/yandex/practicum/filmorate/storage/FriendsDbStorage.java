package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class FriendsDbStorage {

    JdbcTemplate jdbcTemplate;

    public FriendsDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addFriendsWithDefaultStatus(Long idUser, Long idFriend) {
        String sql = "MERGE INTO \"friendship\" (sender_id, recipient_id)  VALUES (?, ?);";
        jdbcTemplate.update(sql, idUser, idFriend);
    }

    public void deleteFriend(Long idUser, Long idFriend) {
        String sql = "DELETE FROM \"friendship\" WHERE sender_id = ? AND recipient_id = ?;";
        jdbcTemplate.update(sql, idUser, idFriend);
    }

    public Map<Long, String> getFriends(Long idUser) {
        String sql = "SELECT recipient_id, status  FROM \"friendship\" WHERE sender_id = ?;";
        Map<Long, String> result = new HashMap<>();

        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, idUser);
        for (Map<String, Object> map : list) {
            result.put(Long.parseLong(map.get("recipient_id").toString()),
                    map.get("status").toString());
        }
        return result;
    }
}
