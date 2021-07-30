package fudan.se.lab2.service;

import fudan.se.lab2.controller.request.CreateAdminRequest;
import fudan.se.lab2.domain.User;
import fudan.se.lab2.enums.UserPermissionEnum;
import fudan.se.lab2.store.MyToken;
import fudan.se.lab2.repository.UserRepository;
import fudan.se.lab2.util.AccountChecker;
import fudan.se.lab2.util.JsonMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author WangHaiWei
 */
@Service
public class AdministratorService {
    private final UserRepository userRepository;
    private final AccountChecker accountChecker;

    @Autowired
    public AdministratorService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.accountChecker = new AccountChecker(userRepository);
    }

    public JsonMap creatAdministrator(CreateAdminRequest request) {
        JsonMap resultMap = new JsonMap();
        int rtn = 0;
        String message = "";
        //检查是否存在超级管理员
        if (superAdminExist() == null) {
            message = "当前不存在超级管理员，请联系网站维护人员。";
            resultMap.put("rtn", rtn);
            resultMap.put("message", message);
            return resultMap;
        }

        //检查当前用户是否为超级管理员
        User superAdmin = MyToken.getUserByTokenId(request.getToken());
        if (superAdmin == null || superAdmin.getPermission() != UserPermissionEnum.superAdministrator.getPermission()) {
            message = "只有超级管理员可以创建管理员";
            resultMap.put("rtn", rtn);
            resultMap.put("message", message);
            return resultMap;
        }
        String username = request.getUsername();
        String password = request.getPassword();
        String email = request.getEmail();
        message += accountChecker.checkValidUsername(username);
        message += accountChecker.checkValidPassword(password);
        message += accountChecker.checkValidEmail(email);
        //验证通过，创建新用户
        if (message.equals("")) {
            User newUser = new User(request.getUsername(), request.getPassword(), request.getEmail(),
                    UserPermissionEnum.normalAdministrator.getPermission(), new Date(), new Date(), ""
            );
            userRepository.save(newUser);
            rtn = 1;
            message = "创建管理员成功";
        }
        resultMap.put("rtn", rtn);
        resultMap.put("message", message);
        return resultMap;
    }

    public User superAdminExist() {
        return userRepository.findByPermission(UserPermissionEnum.superAdministrator.getPermission());
    }
}
