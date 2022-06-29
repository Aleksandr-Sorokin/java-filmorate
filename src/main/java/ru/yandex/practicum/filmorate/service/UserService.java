package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

// для работы с друзьями пользователя
@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage")UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    //Добавляет друзей в список
    public void addFriends(int idUser, int idFriends){
        User user = userStorage.findUserById(idUser);
        User friend = userStorage.findUserById(idFriends);
        Set<Integer> friendsUser = user.getFriends();
        friendsUser.add(idFriends);
        userStorage.changeUser(user);
    }

    //Удаляет друзей из списка
    public void deleteFriends(int idUser, int idFriends){
        User user = userStorage.findUserById(idUser);
        user.getFriends().remove(idFriends);
        userStorage.changeUser(user);
        userStorage.changeUser(userStorage.findUserById(idFriends));
    }

    public List<User> findAllFriends(Integer idUser){
        List<User> friends = new ArrayList<>();
        User user = userStorage.findUserById(idUser);
        Set<Integer> allFriend = user.getFriends();
        if (!allFriend.isEmpty()){
            for (Integer id : allFriend){
                friends.add(userStorage.findUserById(id));
            }
        }
        return friends;
    }

    //ищет общих друзей
    public List<User> findCommonFriends(int idUser, int idOther){
        List<User> commonFriends = new ArrayList<>();
        User user = userStorage.findUserById(idUser);
        User otherUser = userStorage.findUserById(idOther);
        for (Integer friend : user.getFriends()) {
            if (otherUser.getFriends().contains(friend)) {
                commonFriends.add(userStorage.findUserById(friend));
            }
        }
        return commonFriends;
    }
}
