package ua.pochernin;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ua.pochernin.exception.CowServiceException;
import ua.pochernin.service.CowService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@SpringBootApplication
@RequiredArgsConstructor
public class Application implements CommandLineRunner {

    private final CowService cowService;

    public static void main(String[] args) {
        final SpringApplication app = new SpringApplication(Application.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    @Override
    public void run(String... args) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                System.out.println();
                System.out.println("Enter the command number to run the command or type 'exit' to close the application");
                System.out.println("1. GiveBirth (parentCowId, childCowId, childNickName)");
                System.out.println("2. EndLifeSpan (cowId)");
                System.out.println("3. Print farm data");
                System.out.println();

                final String line = reader.readLine();

                if (line.equals("1")) {
                    System.out.println("Enter parentCowId");
                    final Integer parentCowId = Integer.valueOf(reader.readLine());
                    System.out.println("Enter childCowId");
                    final Integer childCowId = Integer.valueOf(reader.readLine());
                    System.out.println("Enter childNickName");
                    final String childNickName = reader.readLine();

                    try {
                        cowService.giveBirth(parentCowId, childCowId, childNickName);
                    } catch (CowServiceException e) {
                        System.out.println(e.getMessage());
                        System.out.println("Please try again");
                        continue;
                    }
                }

                if (line.equals("2")) {
                    System.out.println("Enter cowId");
                    final Integer cowId = Integer.valueOf(reader.readLine());

                    try {
                        cowService.endLifeSpan(cowId);
                    } catch (CowServiceException e) {
                        System.out.println(e.getMessage());
                        System.out.println("Please try again");
                        continue;
                    }
                    System.out.println();
                }

                if (line.equals("3")) {
                    System.out.println();
                    System.out.println(cowService.getFarmDataAsString());
                }

                if (line.equals("exit")) {
                    break;
                }
            }
        }
    }
}
