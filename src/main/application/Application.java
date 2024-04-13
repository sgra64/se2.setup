package application;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;


/**
 * Class that implements the {@link Runnable} interface with the
 * {@code run()}-method called as entry point.
 * 
 * @version <code style=color:green>{@value application.package_info#Version}</code>
 * @author <code style=color:blue>{@value application.package_info#Author}</code>
 */
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
     * Method of {@link Runnable} interface called on application instance.
     * Actual program execution starts here.
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
     * JavaVM entry method that creates and runs an application instance
     * that implements the {@link Runnable} interface.
     * @param args arguments passed from command line 
     */
    public static void main(String[] args) {
        Runnable runnable = createFactorizer(args);
        runnable.run();
    }


    /**
     * Static factory method that creates a {@link Factorizer} instance from a list
     * of class names returning the instance of first class name found in the list
     * that can be instantiated or returns instance of {@link Factorizer} class.
     * 
     * @param args command line arguments passed to created instance when it has a {@code (String[] args)} constructor
     * @return instance that is assignable from the {@link Factorizer} class.
     */
    public static Factorizer createFactorizer(String[] args) {
        return create(new String[] { "application.Factorizer_2", "application.Factorizer" },
            cls -> Runnable.class.isAssignableFrom(cls), args, () -> new Factorizer(args));
    }


    /**
     * Static factory method that creates an instance of type {@code T} from
     * a list of class names. Returns instance of first class name found in the
     * list that can be instantiated or uses alternative supplier.
     * 
     * @param <T> type of created instance
     * @param classNames list of fully-qualified class names from which instances are attempted to be created
     * @param args command line arguments passed to created instance when it has a {@code (String[] args)} constructor
     * @param alt alternative instance supplier when creation from {@code classNames} yields no result
     * @return instance that is assignable from type {@code T}
     */
    @SuppressWarnings("unchecked")
    public static <T> T create(String[] classNames, Function<Class<?>, Boolean> assignable, String[] args, Supplier<T> alt) {
        if(classNames==null || alt==null)
            throw new IllegalArgumentException("classNames[] null or no alternative instance supplier provided (null)");
        //
        return Arrays.asList(classNames).stream()
            .map(cn -> {
                try { // load class from class name and verify class implements the Runnable interface
                    var cls = Application.class.getClassLoader().loadClass(cn);
                    if(assignable != null && assignable.apply(cls))
                        return Optional.of(cls);
                } catch (ClassNotFoundException | NoClassDefFoundError e) { }
                return Optional.ofNullable((Class<T>)null);
            })
            .filter(opt -> opt.isPresent()).map(opt -> opt.get())
            .findFirst()
            .map(cls -> {
                try { // attempt to instantiate class using (String[] args) constructor
                    var ctor = cls.getConstructor(String[].class);
                    return Optional.of((T)ctor.newInstance(new Object[] {args}));
                //
                } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    try { // attempt to instantiate class using default () constructor
                        var ctor = cls.getConstructor();
                        return Optional.of((T)ctor.newInstance());
                    } catch (Exception e1) { }
                }
                return Optional.ofNullable((T)null);
            }).filter(opt -> opt.isPresent()).map(opt -> opt.get())
            .orElse(alt.get());
    }
}