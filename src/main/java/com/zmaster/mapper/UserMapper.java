package com.zmaster.mapper;


import com.zmaster.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

import java.util.List;


@Mapper
public interface UserMapper {

    User selectByNameAndPwd(User user);
    @Options(useGeneratedKeys=true, keyProperty="id",keyColumn = "id")
    int insert(User user);

    int update(User user);

    int selectIsName(User user);

    String selectPasswordByName(User user);

    int count(User user);

    List<User> list(User user);

    void deleteUser(User user);

    User getUserById(User user);
}
