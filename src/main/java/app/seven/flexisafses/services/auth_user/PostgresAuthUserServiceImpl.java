package app.seven.flexisafses.services.auth_user;

import app.seven.flexisafses.models.exception.NotFoundException;
import app.seven.flexisafses.models.pojo.AppUser;
import app.seven.flexisafses.repositories.UserRepo;
import app.seven.flexisafses.util.Constants;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service @Transactional
@Slf4j
public class PostgresAuthUserServiceImpl implements AuthUserService, UserDetailsService {
    private final UserRepo userRepo;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public PostgresAuthUserServiceImpl(@Autowired  BCryptPasswordEncoder bCryptPasswordEncoder,
                                       @Autowired  UserRepo userRepo) {
//        AppUser appUser = getUser(username);
        log.debug("Load user by username const");

        this.userRepo = userRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public AppUser saveUser(AppUser user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    @SneakyThrows
    @Override
    public AppUser addRoleToUser(String id, String role) {
        AppUser user = getUserById(id);

        user.setRole(role);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return saveUser(user);
    }

    private AppUser getUserByUserName(String username) throws NotFoundException {
        Optional<AppUser> user = userRepo.findByUsername(username);

        if(user.isEmpty()){
            throw new NotFoundException("No user with name " + username + " found");
        }

        return user.get();
    }

    private AppUser getUserById(String id) throws NotFoundException {
        Optional<AppUser> user = userRepo.findById(id);

        if(user.isEmpty()){
            throw new NotFoundException("No user with id " + id + " found");
        }

        return user.get();
    }

    @SneakyThrows
    @Override
    public AppUser getUser(String username) {
        return getUserByUserName(username);
    }

    @Override
    public List<AppUser> getUsers() {
        return userRepo.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = getUser(username);

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(appUser.getRole()));

        log.debug("Authentication  : " + appUser);
//        log.debug(appUser.toString());
        return appUser;
    }
}
