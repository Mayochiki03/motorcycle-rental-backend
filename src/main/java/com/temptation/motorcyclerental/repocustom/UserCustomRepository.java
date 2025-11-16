package com.temptation.motorcyclerental.repocustom;

import com.temptation.motorcyclerental.domain.Customers;
import com.temptation.motorcyclerental.domain.Employees;
import com.temptation.motorcyclerental.objc.UserSearchCriteria;

import java.util.List;

public interface UserCustomRepository {
    List<Customers> searchCustomers(UserSearchCriteria criteria);
    List<Employees> searchEmployees(UserSearchCriteria criteria);
    List<Object[]> getUserRegistrationStats(String period);
}