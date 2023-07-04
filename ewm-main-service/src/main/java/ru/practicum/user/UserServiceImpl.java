package ru.practicum.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.exceptions.ConflictException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        Optional<User> optUser = userRepository.getUserByName(user.getName());
        if (optUser.isPresent()) {
            throw new ConflictException("Пользователь с таким именем уже существует");
        }
        return userRepository.save(user);
    }

    @Override
    public List<UserDto> getUsers(List<Integer> ids, Integer from, Integer size) {
        List<User> userList;
        if (ids != null) {
            userList = userRepository.findAllById(ids);
        } else {
            userList = userRepository.findUsers(size, from);
        }
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : userList) {
            UserDto userDto = MappingUser.mapToUserDto(user);
            userDtoList.add(userDto);
        }
        return userDtoList;
    }

    @Override
    public void delete(Integer id) {
        userRepository.deleteById(id);
    }
}
