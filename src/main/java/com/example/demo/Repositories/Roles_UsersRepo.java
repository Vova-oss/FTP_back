//package com.example.demo.Repositories;
//
//import com.example.demo.Entity.Roles_Users;
//import org.springframework.data.r2dbc.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.data.repository.reactive.ReactiveCrudRepository;
//import reactor.core.publisher.Mono;
//
//public interface Roles_UsersRepo extends ReactiveCrudRepository<Roles_Users, Long> {
//
//    @Query(value = "insert into os_roles_user_entities(user_id, role_id) VALUES(:user_id, :role_id)")
//    Mono<Roles_Users> addRelation(@Param("user_id") Long user_id, @Param("role_id") Long role_id);
//
//}
