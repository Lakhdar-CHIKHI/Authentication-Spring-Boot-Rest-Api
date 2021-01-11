package Code.security.services.roleService;


import Code.models.EnumRole;
import Code.models.Role;
import Code.repository.RoleRepository;
import Code.security.Error.Role_NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImplRoleService implements IRoleService {

    @Autowired
    RoleRepository roleRepository;

    @Override
    public Role findByName(String roleName) throws Role_NotFoundException {

        switch (roleName) {
            case "admin":
                return roleRepository.findByName(EnumRole.ROLE_ADMIN)
                        .orElseThrow(() -> new Role_NotFoundException());

            case "new":
                return roleRepository.findByName(EnumRole.ROLE_NEW)
                        .orElseThrow(() -> new Role_NotFoundException());

            case "user":
                return roleRepository.findByName(EnumRole.ROLE_USER)
                        .orElseThrow(() -> new Role_NotFoundException());

            case "superuser":
                return roleRepository.findByName(EnumRole.ROLE_SUPERUSER)
                        .orElseThrow(() -> new Role_NotFoundException());
            default:
                throw new Role_NotFoundException();
        }
    }

    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }
}
