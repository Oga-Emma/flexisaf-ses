package app.seven.flexisafses.services.auth_user;

import app.seven.flexisafses.models.pojo.AppUser;

import java.util.List;

public interface AuthUserService {
    AppUser saveUser(AppUser user);
    AppUser addRoleToUser(String id, String roleName);
    AppUser getUser(String username);
    List<AppUser> getUsers();
}
