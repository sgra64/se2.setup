package application;

import java.util.stream.Stream;


/**
 * Class that implements the {@link Runnable} interface with the {@code run()}-method
 * that is called as the main entry point of the application.
 * 
 * @version <code style=color:green>{@value application.package_info#Version}</code>
 * @author <code style=color:blue>{@value application.package_info#Author}</code>
 */
@RunPriority(priority = 1)
public class Application implements Runnable {

    /*
     * arguments passed from command line
     */
    private final String[] args;

    /**
     * Public {@code String[] args} constructor.
     * @param args arguments passed from command line
     */
    public Application(String[] args) { this.args = args; }


    /**
     * Method of {@link Runnable} interface called on {@code Application}
     * instance. Actual program execution starts here.
     */
    @Override
    public void run() {
        // say Hello and print args[]
        System.out.println(String.format("Hello, %s", this.getClass().getSimpleName()));
        Stream.of(args)
            .map(arg -> String.format("arg: %s", arg))
            .forEach(System.out::println);
    }


    /**
     * JavaVM runtime system primary entry method that creates and runs an application
     * instance that implements the {@link Runnable} interface.
     * @param args arguments passed from command line 
     */
    public static void main(String[] args) {
        Runnable runnable =
            createApplicationInstance(args);
        //
        runnable.run();
    }


    /**
     * Support method to create {@link Runnable} application instance.
     * @param args arguments passed from command line
     * @return {@link Runnable} application instance
     */
    public static Runnable createApplicationInstance(String[] args) {
        return new Factorizer(args);
    }
}