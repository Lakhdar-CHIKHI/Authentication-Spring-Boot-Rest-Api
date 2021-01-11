package Code.DB_Initializer_;


import Code.models.EnumRole;
import Code.models.Role;
import Code.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;


@Component
@ConditionalOnProperty(name = "app.init-db",havingValue = "true")
public class DB_initializer implements CommandLineRunner {
    String line = null;
    List<String> lines = new ArrayList<String>();
    @Autowired
    private RoleRepository roleRepository;
    @Override
    public void run(String... args) throws Exception {
        Role role_user = Role.builder().name(EnumRole.ROLE_USER).build();
        Role role_new = Role.builder().name(EnumRole.ROLE_NEW).build();
        Role role_superuser = Role.builder().name(EnumRole.ROLE_SUPERUSER).build();
        Role role_admin = Role.builder().name(EnumRole.ROLE_ADMIN).build();

        roleRepository.save(role_user);
        roleRepository.save(role_admin);
        roleRepository.save(role_new);
        roleRepository.save(role_superuser);
        Change_prop();
//        System.out.println(app_init_db);
    }

    public void  Change_prop() {
        try {
            File f1 = new File("src/main/resources/application.properties");
            FileReader fr = new FileReader(f1);
            BufferedReader br = new BufferedReader(fr);
            while ((line = br.readLine()) != null) {
                if (line.contains("app.init-db=true"))
                    line = line.replace("app.init-db=true", "app.init-db=false");
                lines.add(line);
            }
            fr.close();
            br.close();

            FileWriter fw = new FileWriter(f1);
            BufferedWriter out = new BufferedWriter(fw);
            for(String s : lines) {
                out.write(s);
                out.newLine();
            }
            out.flush();
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

