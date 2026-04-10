package com.donaway.dao.user;
import com.donaway.model.user.PersonalMedico;
import java.sql.SQLException;
public interface IPersonalMedico {
	void registrarPersonalMedico (PersonalMedico personalmedico) throws SQLException;
}
