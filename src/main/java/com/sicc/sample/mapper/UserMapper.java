package com.sicc.sample.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.sicc.sample.vo.LoginVO;
import com.sicc.sample.vo.UserVO;

@Repository
@Mapper
public interface UserMapper {
	
	UserVO getUserOne(LoginVO loginVO);
	UserVO getUserDetail(String userId);
	int insertUser(UserVO user);
}
