package com.example.mapper;

import com.example.entity.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    //通过id或者邮箱查询用户
    @Select("select * from db_account where username = #{text} or email = #{text}")
    Account findAccountByIdOrEmail(String text);
}
