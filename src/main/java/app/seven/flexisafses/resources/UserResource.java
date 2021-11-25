package app.seven.flexisafses.resources;

import app.seven.flexisafses.models.pojo.AppUser;
import app.seven.flexisafses.services.auth_user.AuthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class UserResource {
    private final AuthUserService authUserService;

    @GetMapping("/users")
    public ResponseEntity<List<AppUser>> fetchUser(){
        return new ResponseEntity<List<AppUser>>( authUserService.getUsers(), HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<AppUser> createUser(@RequestBody AppUser user){
        return new ResponseEntity<AppUser>( authUserService.saveUser(user), HttpStatus.OK);
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<AppUser> updateUserAccount(@PathVariable String id, @RequestBody AppUser user){
        return new ResponseEntity<AppUser>( authUserService.getUser("user"), HttpStatus.OK);
    }

    @PutMapping("/users/{id}/roles")
    public ResponseEntity<AppUser> updateUserRole(@PathVariable String id, @RequestBody String role){
        return new ResponseEntity<AppUser>( authUserService.addRoleToUser(id, role), HttpStatus.OK);
    }
}
