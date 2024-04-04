/**
 * Module of ...
 * <p>
 * The {@link application} package contains runnable application classes including
 * the main class {@link application.Application}.java.
 * </p>
 *  
 * @version <code style=color:green>{@value application.package_info#Version}</code>
 * @author <code style=color:blue>{@value application.package_info#Author}</code>
 */
module se2_setup {
    opens application;
    exports application;                // export to enable Javadoc

    requires org.junit.jupiter.api;     // JUnit-5 test modules
    requires java.logging;
}