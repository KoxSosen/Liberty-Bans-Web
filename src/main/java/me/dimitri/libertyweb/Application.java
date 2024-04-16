package me.dimitri.libertyweb;

import io.micronaut.runtime.Micronaut;
import me.dimitri.libertyweb.utils.PlatformChecker;
import me.dimitri.libertyweb.utils.StartupFiles;
import me.dimitri.libertyweb.utils.exception.FileWorkerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        StartupFiles startupFiles;

        PlatformChecker.checkIfIsAMinecraftServer();

        if (PlatformChecker.isAMinecraftServer()) {
            Path resourcesPath = PlatformChecker.checkForResourcesFolder();
            if (resourcesPath != null) {
                PlatformChecker.setRootPath(resourcesPath);
                startupFiles = new StartupFiles(resourcesPath);
                try {
                    if (startupFiles.createPlatformFolder()) {
                        log.info(" Platform specific folders are created.");
                    }
                } catch (FileWorkerException e) {
                    log.error(e.getMessage());
                }
            } else {
                log.info(" Detected a server platform, but unable to determine resources folder.");
                log.info(" Starting normally, but the LibertyWeb resources may not be in the correct folder.");
                startupFiles = new StartupFiles();
            }
        } else {
            startupFiles = new StartupFiles();
        }

        try {
            if (startupFiles.createConfig()) {
                log.info(" config.yml created, please configure your Liberty Web application there");
                log.info(" Make sure to copy your LibertyBans plugin folder to the same directory as Liberty Web");
                if (!PlatformChecker.isAMinecraftServer()) {
                    System.exit(0);
                    return;
                }
            }
            if (startupFiles.createFrontend()) {
                log.info(" Frontend files for the website have been created, if you wish to edit");
                log.info(" the look of your website you can do so in the \"frontend\" folder located");
                log.info(" in the same directory as your application jar file.");
                log.info(" It is recommended to create a new user that has read-only access to your punishments");
                log.info(" database, this will improve overall security and make logging database connections easier.");
            }
        } catch (FileWorkerException e) {
            log.error(e.getMessage());
            System.exit(0);
            return;
        }

        if (PlatformChecker.isAMinecraftServer()) {
            System.setProperty("micronaut.config.files", PlatformChecker.getRootPath().toString() + "/config.yml");
            Micronaut.build(args).banner(false).start();
        } else {
            System.setProperty("micronaut.config.files", "config.yml");
            Micronaut.build(args).banner(false).start();
        }
    }
}