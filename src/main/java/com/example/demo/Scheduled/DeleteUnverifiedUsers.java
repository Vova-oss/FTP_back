//package com.example.demo.Scheduled;
//
//import com.example.demo.Entity.UserEntity;
//import com.example.demo.Service.UserService;
//import com.example.demo.Singleton.SingletonOne;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class DeleteUnverifiedUsers {
//
//    @Autowired
//    UserService userService;
//
//    public static final Object object = new Object();
//
//    /**
//     * Метод, который очищает всех пользователей, которые неверифицированы в течение 10 минут.
//     *  - synchronized необходим для предотвращения конкурентсности с методом codeConfirmation в UserService
//     *  - для того, чтобы @Scheduled работал, необходимо поставить аннотацию @EnableScheduling в главном классе
//     *  - запуск происхоодит каждые 10 минут
//     */
//    @Scheduled(cron = "0 */10 * * * *")
//    private void deleteUnverifiedUsers(){
//
//        synchronized (SingletonOne.getSingleton()){
//            List<UserEntity> list = userService.findAll();
//            for(UserEntity userEntity: list){
//                if(!userEntity.getVerification()
//                        && (userEntity.getTimeOfCreation() != null)
//                        && (System.currentTimeMillis() - userEntity.getTimeOfCreation() > 600_000)){
//                    userService.delete(userEntity);
//                }
//            }
//        }
//
//    }
//
//}
