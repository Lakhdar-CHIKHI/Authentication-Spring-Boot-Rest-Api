package Code.security.services.roleService;

import Code.models.Role;

public interface IRoleService {
    Role findByName(String RoleName);
    Role save(Role role);
}
