package net.vakror.farmer.renderEngine.util;

import net.vakror.farmer.renderEngine.mod.DynamicClassLoader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

public class JVMUtil {
    public static final String DYNAMIC_CLASS_LOADER_LOCATION = "net.vakror.farmer.renderEngine.mod.DynamicClassLoader";

    public static boolean restartJVM() {


        String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
        if (!doesNeedToRestartBecauseOfMac(pid) && !doesNeedToRestartBecauseOfClassLoader()) {
            return false;
        } else {

            // restart jvm with -XstartOnFirstThread
            String separator = System.getProperty("file.separator");
            String classpath = System.getProperty("java.class.path");
            String mainClass = System.getenv("JAVA_MAIN_CLASS_" + pid);
            String jvmPath = System.getProperty("java.home") + separator + "bin" + separator + "java";

            List<String> inputArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();

            ArrayList<String> jvmArgs = new ArrayList<>();

            jvmArgs.add(jvmPath);
            jvmArgs.add("-XstartOnFirstThread");
            jvmArgs.add("-Djava.system.class.loader=" + DYNAMIC_CLASS_LOADER_LOCATION);
            jvmArgs.addAll(inputArguments);
            jvmArgs.add("-cp");
            jvmArgs.add(classpath);
            jvmArgs.add(mainClass);

            // if you don't need console output, just enable these two lines
            // and delete bits after it. This JVM will then terminate.
            //ProcessBuilder processBuilder = new ProcessBuilder(jvmArgs);
            //processBuilder.start();

            try {
                ProcessBuilder processBuilder = new ProcessBuilder(jvmArgs);
                processBuilder.redirectErrorStream(true);
                Process process = processBuilder.start();

                InputStream is = process.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line;

                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }

                process.waitFor();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return true;
        }
    }

    private static boolean doesNeedToRestartBecauseOfClassLoader() {
        return !(ClassLoader.getSystemClassLoader() instanceof DynamicClassLoader);
    }

    private static boolean doesNeedToRestartBecauseOfMac(String pid) {
        String osName = System.getProperty("os.name");
        // return false if not on mac
        if (!osName.startsWith("Mac") && !osName.startsWith("Darwin")) {
            return false;
        }

        // get current jvm process pid
        // get environment variable on whether XstartOnFirstThread is enabled
        String env = System.getenv("JAVA_STARTED_ON_FIRST_THREAD_" + pid);

        // if environment variable is "1", then XstartOnFirstThread is enabled
        return env == null || !env.equals("1");
    }
}
