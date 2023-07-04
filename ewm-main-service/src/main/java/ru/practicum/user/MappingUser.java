package ru.practicum.user;

public class MappingUser {

    public static User mapToUser(NewUserRequest newUR) {
        User user = new User();
        user.setName(newUR.getName());
        user.setEmail(newUR.getEmail());
        return user;
    }

    public static UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    public static UserShortDto mapToUserShortDto(User user) {
        UserShortDto userShortDto = new UserShortDto();
        userShortDto.setId(user.getId());
        userShortDto.setName(user.getName());
        return userShortDto;
    }
}
