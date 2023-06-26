package com.medicalip.login.domains.users.repo.impl;

import com.medicalip.login.domains.commons.config.QuerydslConfig;
import com.medicalip.login.domains.users.dto.QUsers;
import com.medicalip.login.domains.users.dto.UserRequest;
import com.medicalip.login.domains.users.dto.Users;
import com.medicalip.login.domains.users.repo.UsersRepositoryCustom;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class UsersRepositoryImpl implements UsersRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Users> findUser(UserRequest userRequest) {
        QUsers qUsers = QUsers.users;

        if(userRequest.getUserEmail() == null && userRequest.getUserName() == null){
            return queryFactory
                    .select(qUsers)
                    .from(qUsers)
                    .fetch();
        }else{
            Predicate p = null;
            if(userRequest.getUserEmail() != null){
                p = qUsers.userEmail.contains(userRequest.getUserEmail());
                if(userRequest.getUserName() != null){
                    p = qUsers.userEmail.contains(userRequest.getUserEmail()).and(qUsers.userName.contains(userRequest.getUserName()));
                }
            }else{
                if(userRequest.getUserName() != null){
                    p = qUsers.userName.contains(userRequest.getUserName());
                }
            }
            return queryFactory
                    .select(qUsers)
                    .from(qUsers)
                    .where(p)
                    .fetch();
        }


    }

    @Override
    public Page<Users> findAll(Pageable pageable) {
        QUsers qUsers = QUsers.users;
        List<Users> result =  queryFactory
                .select(qUsers)
                .from(qUsers)
                .fetch();
        int start = (int)pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), result.size());
        return new PageImpl<>(result.subList(start, end), pageable, result.size());
    }
}
