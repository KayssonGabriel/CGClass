package org.gear.framework.core.log;

import org.gear.framework.core.log.annotation.GenerateCriticalFile;
import org.gear.framework.core.log.annotation.LogAlias;
import org.gear.framework.core.log.annotation.LogInfo;
import org.gear.framework.core.log.annotation.NotDebugLog;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import static org.gear.framework.core.log.LogColor.*;
import static org.gear.framework.core.log.LogLevel.*;

public abstract class Logger {

    public static boolean globalDebugDefinition = true;
    public static boolean generateCriticalFiles = true;
    public static boolean enableFileTracking = false;
    private final String name;
    private final DateFormat dateFormat;
    private LogLevel logLevel;
    private boolean isVerboseException;
    private boolean isDebugging;
    private boolean generateCriticalFile;

    protected Logger() {
        if (getClass().isAnnotationPresent(LogAlias.class)) {
            String alias = getClass().getDeclaredAnnotation(LogAlias.class).value();
            this.name = alias.isEmpty() ? getClass().getSimpleName() : alias;
        } else {
            this.name = getClass().getSimpleName();
        }
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        defineProperties(getClass());
    }

    private <T> void defineProperties(Class<T> klass) {
        if (klass.isAnnotationPresent(LogInfo.class)) {
            LogInfo info = klass.getDeclaredAnnotation(LogInfo.class);
            this.logLevel = info.level();
            this.isVerboseException = info.verbose();
        } else {
            this.logLevel = LogLevel.INFO;
            this.isVerboseException = true;
        }

        this.generateCriticalFile = klass.isAnnotationPresent(GenerateCriticalFile.class);

        this.isDebugging = !klass.isAnnotationPresent(NotDebugLog.class) && globalDebugDefinition;
    }

    private String getFormattedTimestamp() {
        return dateFormat.format(new Date());
    }

    private String formatLogMessage(String level, String message, String color) {
        int maxSpacesLength = Arrays.stream(values()).mapToInt(_level -> _level.name().length()).max().orElse(0);

        int currentSpacesLength = level.length();

        String formattedName = " ".repeat(1 + (maxSpacesLength - currentSpacesLength)) + BOLD + name + RESET;

        return String.format("[%s] %s %s: %s", getFormattedTimestamp(), color + level + RESET, formattedName, message);
    }

    private String formatExceptionMessage(Throwable throwable) {
        if (isVerboseException) {
            StringBuilder exceptionMessage = new StringBuilder();
            exceptionMessage.append(throwable.getClass().getName()).append(": ").append(throwable.getMessage()).append("\n");
            for (StackTraceElement element : throwable.getStackTrace()) {
                exceptionMessage.append("\t").append(element.toString()).append("\n");
            }
            return exceptionMessage.toString();
        } else {
            return throwable.getClass().getSimpleName() + (throwable.getMessage() != null ? ": " : "") + throwable.getMessage();
        }
    }

    protected final void printMessage(LogLevel level, String message, String color) {
        if (globalDebugDefinition) {
            if (level.ordinal() <= this.logLevel.ordinal()) {
                StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
                String line = stackTrace[3].toString();

                String tracking = " [Caller: " + getClass().getSimpleName() + " - line: " + line + "]";
                String logMessage = formatLogMessage(level.name(), message, color) + (enableFileTracking ? tracking : "");
                System.out.println(logMessage);
            }
        }
    }

    protected final void info(Object target) {
        if (isDebugging) {
            printMessage(INFO, target.toString(), COLOR_INFO);
        }
    }

    protected final void trace(Object target) {
        if (isDebugging) {
            printMessage(TRACE, target.toString(), COLOR_TRACE);
        }
    }

    protected final void warn(Object target) {
        if (isDebugging) {
            printMessage(WARN, target.toString(), COLOR_WARN);
        }
    }

    protected final void error(Object target) {
        printMessage(ERROR, target.toString(), COLOR_ERROR);
    }

    protected final void critical(Object target) {
        printMessage(CRITICAL, target.toString(), COLOR_CRITICAL);
        System.exit(-1);
    }

    protected final void error(Object target, Throwable throwable) {
        printMessage(ERROR, target.toString(), COLOR_ERROR);
        logException(throwable);
    }

    protected final void critical(Object target, RuntimeException exception) {
        printMessage(CRITICAL, target.toString(), COLOR_CRITICAL);

        if (this.generateCriticalFile && generateCriticalFiles) {
            generateCriticalFile(exception, target.toString());
        }
        throwException(exception);
    }

    private void throwException(RuntimeException exception) {
        throw exception;
    }

    protected final void breakLine() {
        System.out.println();
    }

    private void logException(Throwable throwable) {
        System.err.println(formatExceptionMessage(throwable));
    }

    protected final LogLevel getLogLevel() {
        return this.logLevel;
    }

    private void generateCriticalFile(RuntimeException exception, String message) {
        if (generateCriticalFile) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(getFormattedTimestamp().replaceAll(" ", "_").replaceAll(":", "-") + ".log"))) {
                writer.println("Critical Error:");
                writer.println("Timestamp: " + getFormattedTimestamp());
                writer.println("Message: " + message);
                writer.println("Stack Trace:");
                exception.printStackTrace(writer);
                writer.println("\nAdditional Details:");
                writer.println("Exception Type: " + exception.getClass().getName());
                writer.println("Exception Message: " + exception.getMessage());
                writer.println("Exception Cause: " + (exception.getCause() != null ? exception.getCause().toString() : "N/A"));
                writer.println("Exception Source: " + getExceptionSource(exception));
            } catch (IOException e) {
                error(message, e);
            }
        }
    }

    private String getExceptionSource(Exception exception) {
        StackTraceElement[] elements = exception.getStackTrace();
        if (elements.length > 0) {
            return elements[0].toString();
        }
        return "Unknown";
    }
}
