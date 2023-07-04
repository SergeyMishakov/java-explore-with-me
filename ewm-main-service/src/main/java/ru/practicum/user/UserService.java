package ru.practicum.user;

import java.util.List;

public interface UserService {

    User createUser(User user);

    List<UserDto> getUsers(List<Integer> ids, Integer from, Integer size);

    void delete(Integer id);
}
