package com.donaway.dao.user;
import com.donaway.dto.login.UserSessionDto;
import com.donaway.dto.login.LoginRequestDto;
import java.sql.SQLException;
public interface IUserDao {
	UserSessionDto validarLogin(LoginRequestDto request) throws SQLException;
}
